package com.example.acccidentdemo.recoderdb;

/**
 * Creted by User on 19-May-17
 */

public interface IAttachment {
    String getName();

    void setName(String sAttachmentName);

    int getType();

    void setType(int nType);

    String getFileName();

    void setFileName(String sFileName);

    String getUrl();

    void setUrl(String sUrl);

}
