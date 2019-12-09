package com.icls.offlinekyc.helper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class MasterServiceList {
    public int getAutoid() {
        return autoid;
    }

    public void setAutoid(int autoid) {
        this.autoid = autoid;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int autoid;


    private String organization_idfk;

    private String Service_ID;
    private String Service_Name;
    private String Duration_To_Complete;
    private String Service_Description;
    private String serviceFee;
    private String organization_name;
    private String partner_id;
    private String partner_name;
    private String org_service_id;
    private String eligibility;

    public String getOrg_service_id() {
        return org_service_id;
    }

    public void setOrg_service_id(String org_service_id) {
        this.org_service_id = org_service_id;
    }

    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getCo_ordionator() {
        return co_ordionator;
    }

    public void setCo_ordionator(String co_ordionator) {
        this.co_ordionator = co_ordionator;
    }

    private String documents;
    private String process;
    private String co_ordionator;


    public String getOrganization_idfk() {
        return organization_idfk;
    }

    public void setOrganization_idfk(String organization_idfk) {
        this.organization_idfk = organization_idfk;
    }

    public String getService_ID() {
        return Service_ID;
    }

    public void setService_ID(String service_ID) {
        Service_ID = service_ID;
    }

    public String getService_Name() {
        return Service_Name;
    }

    public void setService_Name(String service_Name) {
        Service_Name = service_Name;
    }

    public String getDuration_To_Complete() {
        return Duration_To_Complete;
    }

    public void setDuration_To_Complete(String duration_To_Complete) {
        Duration_To_Complete = duration_To_Complete;
    }

    public String getService_Description() {
        return Service_Description;
    }

    public void setService_Description(String service_Description) {
        Service_Description = service_Description;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }
}
