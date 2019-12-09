package com.icls.offlinekyc.helper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class KYCAadhar {

    @PrimaryKey
    @NotNull
    private String member_mobile_no;

    private String mem_profile_pic;
    private String member_dob;
    private String member_permanent_address;
    private String member_sex;
    private String member_fullname;
    private String member_synstatus;

    public String getMember_synstatus() {
        return member_synstatus;
    }

    public void setMember_synstatus(String member_synstatus) {
        this.member_synstatus = member_synstatus;
    }

    public String getMem_profile_pic() {
        return mem_profile_pic;
    }

    public void setMem_profile_pic(String mem_profile_pic) {
        this.mem_profile_pic = mem_profile_pic;
    }

    public String getMember_dob() {
        return member_dob;
    }

    public void setMember_dob(String member_dob) {
        this.member_dob = member_dob;
    }

    public String getMember_permanent_address() {
        return member_permanent_address;
    }

    public void setMember_permanent_address(String member_permanent_address) {
        this.member_permanent_address = member_permanent_address;
    }

    public String getMember_sex() {
        return member_sex;
    }

    public void setMember_sex(String member_sex) {
        this.member_sex = member_sex;
    }

    public String getMember_mobile_no() {
        return member_mobile_no;
    }

    public void setMember_mobile_no(String member_mobile_no) {
        this.member_mobile_no = member_mobile_no;
    }

    public String getMember_fullname() {
        return member_fullname;
    }

    public void setMember_fullname(String member_fullname) {
        this.member_fullname = member_fullname;
    }
}
