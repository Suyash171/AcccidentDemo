package com.example.acccidentdemo.recoderdb;



/**
 * Creted by User on 19-May-17
 * use to hold information of any kind of attachment
 */

public class Attachment implements IAttachment {
    private String sName;
    private int nType;
    private String sFileName;
    private String sUrl;

    @Override
    public String getName() {
        return sName;
    }

    @Override
    public void setName(String sAttachmentName) {
        sName = sAttachmentName;
    }

    @Override
    public int getType() {
        return nType;
    }

    @Override
    public void setType(int nType) {
        this.nType = nType;
    }

    @Override
    public String getFileName() {
        return sFileName;
    }

    @Override
    public void setFileName(String sFileName) {
        this.sFileName = sFileName;
    }

    @Override
    public String getUrl() {
        return sUrl;
    }


    @Override
    public void setUrl(String sUrl) {
        this.sUrl = sUrl;
    }

    public static class TYPE {
        public static int AUDIO = 1;
        public static int VIDEO = 2;
        public static int IMAGE = 3;
    }
}
