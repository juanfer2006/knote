package io.learnk8s.knote;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "knote")
public class KnoteProperties {

    @Value("${knote.minio.host:localhost}")
    private String minioHost;

    @Value("${knote.minio.bucket:image-storage}")
    private String minioBucket;

    @Value("${knote.minio.access.key:}")
    private String minioAccessKey;

    @Value("${knote.minio.secret.key:}")
    private String minioSecretKey;

    @Value("${knote.minio.useSSL:false}")
    private boolean minioUseSSL;

    @Value("${knote.minio.reconnect.enabled:true}")
    private boolean minioReconnectEnabled;

    public String getMinioHost() {
        return minioHost;
    }

    public void setMinioHost(String minioHost) {
        this.minioHost = minioHost;
    }

    public String getMinioBucket() {
        return minioBucket;
    }

    public void setMinioBucket(String minioBucket) {
        this.minioBucket = minioBucket;
    }

    public String getMinioAccessKey() {
        return minioAccessKey;
    }

    public void setMinioAccessKey(String minioAccessKey) {
        this.minioAccessKey = minioAccessKey;
    }

    public String getMinioSecretKey() {
        return minioSecretKey;
    }

    public void setMinioSecretKey(String minioSecretKey) {
        this.minioSecretKey = minioSecretKey;
    }

    public boolean isMinioUseSSL() {
        return minioUseSSL;
    }

    public void setMinioUseSSL(boolean minioUseSSL) {
        this.minioUseSSL = minioUseSSL;
    }

    public boolean isMinioReconnectEnabled() {
        return minioReconnectEnabled;
    }

    public void setMinioReconnectEnabled(boolean minioReconnectEnabled) {
        this.minioReconnectEnabled = minioReconnectEnabled;
    }
}
