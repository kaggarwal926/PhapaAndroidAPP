package com.icls.offlinekyc.Database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Calendar;

public class Repository {
    private String DB_NAME = "MyEIdDatabase";
    private mDatabase database;

    public Repository(Context context) {
        database = Room.databaseBuilder(context, mDatabase.class, DB_NAME).build();
    }

    public void insertTaskmembertypemaster(String memberTypeID,String memberTypeCode,String memberTypeDesc,
                           String memberTypeGroup,String Industry,String Occupation_Job_Type,String Description,
                           String createdBy,
                           int Delete_status) {

        insertTaskmembertypemaster(memberTypeID,memberTypeCode, memberTypeDesc,memberTypeGroup,Industry,
                Occupation_Job_Type,Description,createdBy,Delete_status,false);
    }

    private void insertTaskmembertypemaster(String memberTypeID, String memberTypeCode, String memberTypeDesc, String memberTypeGroup, String industry, String occupation_job_type, String description, String createdBy, int delete_status, boolean b) {

        Membertypemaster membertypemaster = new Membertypemaster();
        membertypemaster.setMemberTypeID(memberTypeID);
        membertypemaster.setMemberTypeCode(memberTypeCode);
        membertypemaster.setMemberTypeDesc(memberTypeDesc);
        membertypemaster.setMemberTypeGroup(memberTypeGroup);
        membertypemaster.setIndustry(industry);

        membertypemaster.setOccupation_Job_Type(occupation_job_type);
        membertypemaster.setDescription(description);
        membertypemaster.setCreatedBy(createdBy);
        membertypemaster.setCreatedDate(Calendar.getInstance().getTime());
        membertypemaster.setModifiedBy(Calendar.getInstance().getTime());
        membertypemaster.setModifiedDate(Calendar.getInstance().getTime());
        membertypemaster.setDelete_status(delete_status);
        insertTaskmembertypemaster(membertypemaster);
    }





    public void insertTaskmembertypemaster(final Membertypemaster membertypemaster) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.dao_queries().insertTaskmembertypemaster(membertypemaster);
                return null;
            }
        }.execute();
    }



    public void insertTask (String organizationId, String organization_name, String address, String website
            ,String organization_info,String  Phone,String email,String  logo){
        insertTask(organizationId, organization_name, address, website
                ,organization_info, Phone, email, logo, false, null);

    }
    private void insertTask(String organizationId, String organization_name, String address, String website
            ,String organization_info,String  Phone,String email,String  logo, boolean b, Object o){
        OrganizationTypeParam OrgTypeParam=new OrganizationTypeParam();
        //OrgTypeParam.setOrganizationId(organizationId);
        OrgTypeParam.setOrganization_name(organization_name);
        OrgTypeParam.setAddress(address);
        OrgTypeParam.setWebsite(website);
        OrgTypeParam.setOrganization_info(organization_info);
        OrgTypeParam.setPhone(Phone);
        OrgTypeParam.setEmail(email);
        OrgTypeParam.setLogo(logo);
        insertTask(OrgTypeParam);

    }

   public void insertTask(final OrganizationTypeParam org) {
       new AsyncTask<Void, Void, Void>() {
           @Override
           protected Void doInBackground(Void... voids) {
               database.dao_queries().insertTask(org);
               return null;
           }
       }.execute();
   }



}
