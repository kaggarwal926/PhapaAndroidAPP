package com.icls.offlinekyc.function.Models;

public class MytabServiceReqModal {

    private  String organization_idfk,Service_ID,Service_Name,Duration_To_Complete,
    Service_Description,serviceFee,organization_name,partner_id,
    partner_name;

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
