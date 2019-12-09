package com.icls.offlinekyc.helper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class OrganizationGroupMaster {

    @PrimaryKey
    @NotNull
    private String member_group_id;

    private String member_group;

    public String getMember_group_id() {
        return member_group_id;
    }

    public void setMember_group_id(String member_group_id) {
        this.member_group_id = member_group_id;
    }

    public String getMember_group() {
        return member_group;
    }

    public void setMember_group(String member_group) {
        this.member_group = member_group;
    }
}
