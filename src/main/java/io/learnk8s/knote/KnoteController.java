package io.learnk8s.knote;

import io.minio.MinioClient;
import org.apache.commons.io.IOUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@EnableConfigurationProperties(KnoteProperties.class)
public class KnoteController {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private KnoteProperties properties;

    private Parser parser = Parser.builder().build();
    private HtmlRenderer renderer = HtmlRenderer.builder().build();
    private MinioClient minioClient;

    @PostConstruct
    public void init() throws InterruptedException {
        initMinio();
    }

    @GetMapping("/")
    public String index(Model model) {
        getAllNotes(model);
        return "index";
    }

    @PostMapping("/note")
    public String saveNotes(@RequestParam("image") MultipartFile file,
                            @RequestParam String description,
                            @RequestParam(required = false) String publish,
                            @RequestParam(required = false) String upload,
                            Model model) throws Exception {
        if (publish != null && publish.equals("Publish")) {
            saveNote(description, model);
            getAllNotes(model);
            return "redirect:/";
        }
        if (upload != null && upload.equals("Upload")) {
            if (file != null && file.getOriginalFilename() != null
                    && !file.getOriginalFilename().isEmpty()) {
                uploadImage(file, description, model);
            }
            getAllNotes(model);
            return "index";
        }
        return "index";
    }

    @GetMapping(value = "/img/{name}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImageByName(@PathVariable String name) throws Exception {
        InputStream imageStream = minioClient.getObject(properties.getMinioBucket(), name);
        return IOUtils.toByteArray(imageStream);
    }

    private void getAllNotes(Model model) {
        List<Note> notes = notesRepository.findAll();
        Collections.reverse(notes);
        model.addAttribute("notes", notes);
    }

    private void saveNote(String description, Model model) {
        if (description != null && !description.trim().isEmpty()) {
            Node document = parser.parse(description.trim());
            String html = renderer.render(document);
            notesRepository.save(new Note(null, html));
            model.addAttribute("description", "");
        }
    }

    private void uploadImage(MultipartFile file, String description, Model model) throws Exception {
        String fileId = UUID.randomUUID().toString() + "." + file.getOriginalFilename().split("\\.")[1];
        minioClient.putObject(properties.getMinioBucket(), fileId, file.getInputStream(),
                file.getSize(), null, null, file.getContentType());
        model.addAttribute("description",
                description + " ![](/img/" + fileId + ")");
    }

    private void initMinio() throws InterruptedException {
        boolean success = false;
        while (!success) {
            try {
                minioClient = new MinioClient("http://" + properties.getMinioHost() + ":9000",
                        properties.getMinioAccessKey(),
                        properties.getMinioSecretKey(),
                        false);
                boolean isExist = minioClient.bucketExists(properties.getMinioBucket());
                if (isExist) {
                    System.out.println("> Bucket already exists.");
                } else {
                    minioClient.makeBucket(properties.getMinioBucket());
                }
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("> Minio Reconnect: " + properties.isMinioReconnectEnabled());
                if (properties.isMinioReconnectEnabled()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    success = true;
                }
            }
        }
        System.out.println("> Minio initialized!");
    }
}
