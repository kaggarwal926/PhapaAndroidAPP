package com.icls.offlinekyc.helper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class AllServiceHistoryHelper {


    @PrimaryKey(autoGenerate = true)
    @NotNull
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getService_Msg_id() {
        return Service_Msg_id;
    }

    public void setService_Msg_id(String service_Msg_id) {
        Service_Msg_id = service_Msg_id;
    }

    private String Service_Msg_id;
    private String organization_id;
    private String member_id;

    public String getLocal_filepath() {
        return local_filepath;
    }

    public void setLocal_filepath(String local_filepath) {
        this.local_filepath = local_filepath;
    }

    private String local_filepath;
    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    private String file_path;
    public String getService_id() {
        return Service_id;
    }

    public void setService_id(String service_id) {
        Service_id = service_id;
    }

    private String Service_id;
    private String Message_form;
    private String Message_to;
    private String Message;
    private String attachment_name;
    private String file_ext;
    private String mime_typ;
    private String Parent_id;
    private String read;
    private String syn_status;
    private String created_date;




    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMessage_form() {
        return Message_form;
    }

    public void setMessage_form(String message_form) {
        Message_form = message_form;
    }

    public String getMessage_to() {
        return Message_to;
    }

    public void setMessage_to(String message_to) {
        Message_to = message_to;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getAttachment_name() {
        return attachment_name;
    }

    public void setAttachment_name(String attachment_name) {
        this.attachment_name = attachment_name;
    }

    public String getFile_ext() {
        return file_ext;
    }

    public void setFile_ext(String file_ext) {
        this.file_ext = file_ext;
    }

    public String getMime_typ() {
        return mime_typ;
    }

    public void setMime_typ(String mime_typ) {
        this.mime_typ = mime_typ;
    }

    public String getParent_id() {
        return Parent_id;
    }

    public void setParent_id(String parent_id) {
        Parent_id = parent_id;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getSyn_status() {
        return syn_status;
    }

    public void setSyn_status(String syn_status) {
        this.syn_status = syn_status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
