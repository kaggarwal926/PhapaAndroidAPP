
package com.icls.offlinekyc.helper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

@Entity
public class MemberTypeMasterPOJO {

    @PrimaryKey
    @NotNull
    private String memberTypeID;

    @SerializedName("memberTypeCode")
    @Expose
    private String memberTypeCode;

    private String memberTypeGroup;

    public String getMemberTypeGroup() {
        return memberTypeGroup;
    }

    public void setMemberTypeGroup(String memberTypeGroup) {
        this.memberTypeGroup = memberTypeGroup;
    }

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

}
