package io.github.jartool.storage.entity;

/**
 * Linker
 *
 * @author jie.li
 */
public class Linker {

    /**
     * fileName
     */
    private String fileName;

    /**
     * downloadUrl
     */
    private String downloadUrl;

    /**
     * deleteUrl
     */
    private String deleteUrl;

    public Linker() {

    }

    public Linker(String fileName, String downloadUrl, String deleteUrl) {
        this.fileName = fileName;
        this.downloadUrl = downloadUrl;
        this.deleteUrl = deleteUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDeleteUrl() {
        return deleteUrl;
    }

    public void setDeleteUrl(String deleteUrl) {
        this.deleteUrl = deleteUrl;
    }
}
