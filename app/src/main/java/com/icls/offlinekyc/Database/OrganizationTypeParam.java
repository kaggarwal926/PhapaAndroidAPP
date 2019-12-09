package com.icls.offlinekyc.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Entity
public class OrganizationTypeParam implements Serializable {



    @PrimaryKey/*(autoGenerate = true)*/
    @NotNull
    private String organization_id;

    private String organization_name;
    private String address;
    private String website;
    private String organization_info;
    private String Phone;
    private String email;
    private String logo;
    private String status;
    private String syn_status;
    private String org_mem_id;
    private String member_idfk;
    private String P_idfk;
    private String state;
    private String city;
    private String zip;
    private String country;
    private String orgTypeIDFK;
    private String Joining_Remarks;
    private String primary_org_idfk;
    private String is_memeber;

    public String getIs_memeber() {
        return is_memeber;
    }

    public void setIs_memeber(String is_memeber) {
        this.is_memeber = is_memeber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMember_Since() {
        return member_Since;
    }

    public void setMember_Since(String member_Since) {
        this.member_Since = member_Since;
    }

    public String getDate_of_initiation() {
        return date_of_initiation;
    }

    public void setDate_of_initiation(String date_of_initiation) {
        this.date_of_initiation = date_of_initiation;
    }

    public String getDate_of_joining() {
        return date_of_joining;
    }

    public void setDate_of_joining(String date_of_joining) {
        this.date_of_joining = date_of_joining;
    }

    private String location;
    private String member_Since;
    private String date_of_initiation ;
    private String date_of_joining;
    public String getPrimary_org_idfk() {
        return primary_org_idfk;
    }

    public void setPrimary_org_idfk(String primary_org_idfk) {
        this.primary_org_idfk = primary_org_idfk;
    }

    @NotNull
    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(@NotNull String organization_id) {
        this.organization_id = organization_id;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOrganization_info() {
        return organization_info;
    }

    public void setOrganization_info(String organization_info) {
        this.organization_info = organization_info;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSyn_status() {
        return syn_status;
    }

    public void setSyn_status(String syn_status) {
        this.syn_status = syn_status;
    }

    public String getOrg_mem_id() {
        return org_mem_id;
    }

    public void setOrg_mem_id(String org_mem_id) {
        this.org_mem_id = org_mem_id;
    }

    public String getMember_idfk() {
        return member_idfk;
    }

    public void setMember_idfk(String member_idfk) {
        this.member_idfk = member_idfk;
    }

    public String getP_idfk() {
        return P_idfk;
    }

    public void setP_idfk(String p_idfk) {
        P_idfk = p_idfk;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOrgTypeIDFK() {
        return orgTypeIDFK;
    }

    public void setOrgTypeIDFK(String orgTypeIDFK) {
        this.orgTypeIDFK = orgTypeIDFK;
    }

    public String getJoining_Remarks() {
        return Joining_Remarks;
    }

    public void setJoining_Remarks(String joining_Remarks) {
        Joining_Remarks = joining_Remarks;
    }
}
