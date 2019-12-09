package com.icls.offlinekyc.roomdb;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Entity
public class UserProfile implements Serializable {

    @PrimaryKey
    @NotNull
    private String member_id;

    private String member_occupation;
    private String member_local_add;
    private String village_name;
    private String area_name;
    private String reg_city;
    private String member_local_add_taluk;
    private String reg_state;
    private String member_local_add_pincode;
    private String alternative_Number;

    private String memTypeIDFK;
    private String memberTypeCode;
    private String Member_group_idfk ;
    private String member_group;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getMembership_status() {
        return membership_status;
    }

    public void setMembership_status(String membership_status) {
        this.membership_status = membership_status;
    }

    public String getDate_Of_joining() {
        return date_Of_joining;
    }

    public void setDate_Of_joining(String date_Of_joining) {
        this.date_Of_joining = date_Of_joining;
    }

    private String email;
    private String marital_status;
    private String membership_status;
    private String date_Of_joining;
    private String organization_id;
    private String organization_name;
    private String primary_org_id;
    private String organisation_group_type;

    private String agent_id;
    private String agent_name;
    private String member_dob;
    private String member_permanent_address;
    private String member_sex;
    private String member_fullname;
    private String member_mobile_no;
    private String memProfileSyncStatus;
    private String profilePicture;



    public String getPrimary_org_id() {
        return primary_org_id;
    }

    public void setPrimary_org_id(String primary_org_id) {
        this.primary_org_id = primary_org_id;
    }

    public String getOrganisation_group_type() {
        return organisation_group_type;
    }

    public void setOrganisation_group_type(String organisation_group_type) {
        this.organisation_group_type = organisation_group_type;
    }

    public String getMember_group_idfk() {
        return Member_group_idfk;
    }

    public void setMember_group_idfk(String member_group_idfk) {
        Member_group_idfk = member_group_idfk;
    }

    public String getMember_group() {
        return member_group;
    }

    public void setMember_group(String member_group) {
        this.member_group = member_group;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    @NotNull
    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(@NotNull String organization_id) {
        this.organization_id = organization_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_occupation() {
        return member_occupation;
    }

    public void setMember_occupation(String member_occupation) {
        this.member_occupation = member_occupation;
    }

    public String getMember_local_add() {
        return member_local_add;
    }

    public void setMember_local_add(String member_local_add) {
        this.member_local_add = member_local_add;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getReg_city() {
        return reg_city;
    }

    public void setReg_city(String reg_city) {
        this.reg_city = reg_city;
    }

    public String getMember_local_add_taluk() {
        return member_local_add_taluk;
    }

    public void setMember_local_add_taluk(String member_local_add_taluk) {
        this.member_local_add_taluk = member_local_add_taluk;
    }

    public String getReg_state() {
        return reg_state;
    }

    public void setReg_state(String reg_state) {
        this.reg_state = reg_state;
    }

    public String getMember_local_add_pincode() {
        return member_local_add_pincode;
    }

    public void setMember_local_add_pincode(String member_local_add_pincode) {
        this.member_local_add_pincode = member_local_add_pincode;
    }

    public String getAlternative_Number() {
        return alternative_Number;
    }

    public void setAlternative_Number(String alternative_Number) {
        this.alternative_Number = alternative_Number;
    }

    public String getMemTypeIDFK() {
        return memTypeIDFK;
    }

    public void setMemTypeIDFK(String memTypeIDFK) {
        this.memTypeIDFK = memTypeIDFK;
    }

    public String getMemberTypeCode() {
        return memberTypeCode;
    }

    public void setMemberTypeCode(String memberTypeCode) {
        this.memberTypeCode = memberTypeCode;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
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

    public String getMember_fullname() {
        return member_fullname;
    }

    public void setMember_fullname(String member_fullname) {
        this.member_fullname = member_fullname;
    }

    public String getMember_mobile_no() {
        return member_mobile_no;
    }

    public void setMember_mobile_no(String member_mobile_no) {
        this.member_mobile_no = member_mobile_no;
    }

    public String getMemProfileSyncStatus() {
        return memProfileSyncStatus;
    }

    public void setMemProfileSyncStatus(String memProfileSyncStatus) {
        this.memProfileSyncStatus = memProfileSyncStatus;
    }
}
