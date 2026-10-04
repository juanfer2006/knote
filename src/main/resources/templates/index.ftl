<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Knote</title>
    <link rel="stylesheet" href="/tachyons.min.css">
</head>
<body class="bg-washed-yellow pa4 sans-serif">
<div class="mw7 center">
    <h1 class="f1 lh-title navy">Knote</h1>

    <form action="/note" method="POST" enctype="multipart/form-data" class="mb4">
        <textarea name="description" rows="5" class="w-100 pa2 border-box br2 b--light-silver mb2" placeholder="Write your note here... (Markdown supported)">${description!""}</textarea>
        <div class="flex justify-between items-center">
            <input type="file" name="image" accept="image/*" class="pv2">
            <div>
                <input type="submit" name="upload" value="Upload" class="b ph3 pv2 input-reset ba b--dark-blue bg-blue white br2 pointer mr2">
                <input type="submit" name="publish" value="Publish" class="b ph3 pv2 input-reset ba b--dark-green bg-green white br2 pointer">
            </div>
        </div>
    </form>

    <div class="notes">
        <#if notes?? && (notes?size > 0)>
            <#list notes as note>
                <div class="bg-white pa3 br3 shadow-1 mb3">
                    <div>${note.description}</div>
                </div>
            </#list>
        <#else>
            <p class="gray">No notes yet. Start by writing one!</p>
        </#if>
    </div>
</div>
</body>
</html>
