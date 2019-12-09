package com.icls.offlinekyc.helper;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;


@Entity
public class MasterNotification implements Serializable {

    /*@PrimaryKey(autoGenerate = true)
    @NotNull
    private  int masternoti_id;
*/
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "notification_id")
    private String notification_id;
    @ColumnInfo(name = "message_to_id")
    private String message_to_id ;
    @ColumnInfo(name = "reply_allowed")
    private String reply_allowed;
    @ColumnInfo(name = "message_subject")
    private String message_subject;
    @ColumnInfo(name = "parent_messaage_id")
    private String parent_messaage_id;
    @ColumnInfo(name = "organization_idfk")
    private String organization_idfk;
    @ColumnInfo(name = "organization_name")
    private String organization_name;
    @ColumnInfo(name = "message_from")
    private String message_from;
    @ColumnInfo(name = "message_type_idfk")
    private String message_type_idfk;
    @ColumnInfo(name = "message")
    private String message;
    @ColumnInfo(name = "message_to")
    private String message_to;
    @ColumnInfo(name = "read")
    private String read;
    @ColumnInfo(name = "syn_status")
    private String syn_status;




    public String getMessage_to_id() {
        return message_to_id;
    }

    public void setMessage_to_id(String message_to_id) {
        this.message_to_id = message_to_id;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getReply_allowed() {
        return reply_allowed;
    }

    public void setReply_allowed(String reply_allowed) {
        this.reply_allowed = reply_allowed;
    }

    public String getMessage_subject() {
        return message_subject;
    }

    public void setMessage_subject(String message_subject) {
        this.message_subject = message_subject;
    }

    public String getParent_messaage_id() {
        return parent_messaage_id;
    }

    public void setParent_messaage_id(String parent_messaage_id) {
        this.parent_messaage_id = parent_messaage_id;
    }

    public String getOrganization_idfk() {
        return organization_idfk;
    }

    public void setOrganization_idfk(String organization_idfk) {
        this.organization_idfk = organization_idfk;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getMessage_from() {
        return message_from;
    }

    public void setMessage_from(String message_from) {
        this.message_from = message_from;
    }

    public String getMessage_type_idfk() {
        return message_type_idfk;
    }

    public void setMessage_type_idfk(String message_type_idfk) {
        this.message_type_idfk = message_type_idfk;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_to() {
        return message_to;
    }

    public void setMessage_to(String message_to) {
        this.message_to = message_to;
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
}
