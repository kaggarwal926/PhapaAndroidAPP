package com.icls.offlinekyc.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.icls.offlinekyc.helper.TimestampConverter;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;
/*
This is nothing but a model class annotated with
@Entity where all the variable will becomes column name
for the table and name of the model class becomes name of the table.
The name of the class is the table name and the variables are the columns of the table.
*/

@Entity
public class Membertypemaster  implements Serializable {

    @PrimaryKey
    @NotNull
    private String memberTypeID;

    private String memberTypeCode;
    private String memberTypeDesc;
    private String memberTypeGroup;
    private String Industry;
    private String Occupation_Job_Type;
    private String Description;
    private String createdBy;

    @ColumnInfo(name = "createdDate")
    @TypeConverters({TimestampConverter.class})
    private Date createdDate;

    @ColumnInfo(name = "modifiedBy")
    @TypeConverters({TimestampConverter.class})
    private Date modifiedBy;

    @ColumnInfo(name = "modifiedDate")
    @TypeConverters({TimestampConverter.class})
    private Date modifiedDate;

    private int Delete_status;

    public String getMemberTypeID() {
        return memberTypeID;
    }

    public void setMemberTypeID(String memberTypeID) {
        this.memberTypeID = memberTypeID;
    }

    public String getMemberTypeCode() {
        return memberTypeCode;
    }

    public void setMemberTypeCode(String memberTypeCode) {
        this.memberTypeCode = memberTypeCode;
    }

    public String getMemberTypeDesc() {
        return memberTypeDesc;
    }

    public void setMemberTypeDesc(String memberTypeDesc) {
        this.memberTypeDesc = memberTypeDesc;
    }

    public String getMemberTypeGroup() {
        return memberTypeGroup;
    }

    public void setMemberTypeGroup(String memberTypeGroup) {
        this.memberTypeGroup = memberTypeGroup;
    }

    public String getIndustry() {
        return Industry;
    }

    public void setIndustry(String industry) {
        Industry = industry;
    }

    public String getOccupation_Job_Type() {
        return Occupation_Job_Type;
    }

    public void setOccupation_Job_Type(String occupation_Job_Type) {
        Occupation_Job_Type = occupation_Job_Type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Date modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getDelete_status() {
        return Delete_status;
    }

    public void setDelete_status(int delete_status) {
        Delete_status = delete_status;
    }
}
