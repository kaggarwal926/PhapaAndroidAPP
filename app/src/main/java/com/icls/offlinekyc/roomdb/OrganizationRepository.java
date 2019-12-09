package com.icls.offlinekyc.roomdb;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ImageView;

import com.icls.offlinekyc.Database.OrganizationTypeParam;
import com.icls.offlinekyc.commonshare.common;
import com.icls.offlinekyc.function.ServiceRequestingMaster;
import com.icls.offlinekyc.helper.AllNotificationHistoryHelper;
import com.icls.offlinekyc.helper.AllServiceHistoryHelper;
import com.icls.offlinekyc.helper.CoordinatorsPOJO;
import com.icls.offlinekyc.helper.KYCAadhar;
import com.icls.offlinekyc.helper.MasterNotification;
import com.icls.offlinekyc.helper.MasterServiceList;
import com.icls.offlinekyc.helper.MemberTypeMasterPOJO;
import com.icls.offlinekyc.helper.OrganizationGroupMaster;
import com.icls.offlinekyc.helper.PrimaryOrgGroupMaster;
import com.icls.offlinekyc.helper.PrimaryOrganisationPOJO;
import com.icls.offlinekyc.helper.SupportConversationHistory;

import org.w3c.dom.Element;

import java.lang.ref.WeakReference;
import java.util.List;

public class OrganizationRepository {

    public static Boolean openDb = false;
    private String DB_NAME = "db_task.db";
    private OrganizationDatabase OrganizationDB;
    private Context context;

    public OrganizationRepository(Context context) {
        this.context = context;
        OrganizationDB = Room.databaseBuilder(context, OrganizationDatabase.class, DB_NAME).build();
        openDb = OrganizationDB.isOpen();
    }
    public OrganizationDatabase getinstance(){
        return OrganizationDB;
    }
    public void insertTask(String organizationId, String organization_name, String address, String website
            , String organization_info, String Phone, String email, String logo, String joinedStatus, String synStatus,
                           String userOrgMemId, String usermemberId, String parentid, String state, String city, String zip,
                           String country, String orgTypeIDFK, String Joining_Remarks, String primaryOrgIdfk,
                           String is_memeber ,
                                   String location ,
                                   String member_Since ,
                                   String date_of_initiation ,
                                   String date_of_joining ) {

        insertTask(organizationId, organization_name, address, website
                , organization_info, Phone, email, logo, joinedStatus, false, null,
                synStatus, userOrgMemId, usermemberId, parentid, state, city, zip, country, orgTypeIDFK, Joining_Remarks,primaryOrgIdfk,
                is_memeber,
                location ,
                member_Since ,
                date_of_initiation ,
                date_of_joining);
    }

    public void insertTask(String organizationId, String organization_name, String address, String website
            , String organization_info, String Phone, String email, String logo,
                           String joinedStatus, boolean encrypt, String val, String synStatus, String userOrgMemId, String usermemberId, String parentid, String state, String city, String zip,
                           String country, String orgTypeIDFK, String Joining_Remarks, String primaryOrgIdfk,
                           String is_memeber ,
                           String location ,
                           String member_Since ,
                           String date_of_initiation ,
                           String date_of_joining ) {
        OrganizationTypeParam orgTypeParam = new OrganizationTypeParam();
        orgTypeParam.setOrganization_name(organization_name);
        orgTypeParam.setOrganization_id(organizationId);
        orgTypeParam.setAddress(address);
        orgTypeParam.setWebsite(website);
        orgTypeParam.setOrganization_info(organization_info);
        orgTypeParam.setPhone(Phone);
        orgTypeParam.setEmail(email);
        orgTypeParam.setLogo(logo);
        orgTypeParam.setStatus(joinedStatus);
        orgTypeParam.setSyn_status(synStatus);
        orgTypeParam.setOrg_mem_id(userOrgMemId);
        orgTypeParam.setMember_idfk(usermemberId);
        orgTypeParam.setP_idfk(parentid);
        orgTypeParam.setState(state);
        orgTypeParam.setCity(city);
        orgTypeParam.setZip(zip);
        orgTypeParam.setCountry(country);
        orgTypeParam.setOrgTypeIDFK(orgTypeIDFK);
        orgTypeParam.setJoining_Remarks(Joining_Remarks);
        orgTypeParam.setPrimary_org_idfk(primaryOrgIdfk);

        orgTypeParam.setIs_memeber( is_memeber );
        orgTypeParam.setLocation( location );
        orgTypeParam.setMember_Since( member_Since );
        orgTypeParam.setDate_of_initiation( date_of_initiation );
        orgTypeParam.setDate_of_joining( date_of_joining );
        insertTask(orgTypeParam);
    }

    public void insertTask(final OrganizationTypeParam orgTypeParam) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().insertTask(orgTypeParam);
                return null;
            }
        }.execute();
    }
    //========================== get master service list harish ============

    public void insertMasterServiceList(String organization_idfk,String Service_ID,String Service_Name,String Duration_To_Complete,
                                        String Service_Description,String serviceFee,String organization_name,String partner_id,
                                        String partner_name,
                                        String org_service_id ,
                                                String eligibility,
                                                String documents ,
                                                String process ,
                                                String co_ordionator ) {

        insertMasterServiceList( organization_idfk,Service_ID,Service_Name,Duration_To_Complete,
                Service_Description,serviceFee,organization_name,partner_id,
                partner_name,false, null,org_service_id ,
                eligibility ,documents , process ,co_ordionator);
    }

    public void insertMasterServiceList(String organization_idfk,String Service_ID,
                                        String Service_Name,
                                        String Duration_To_Complete,
                                        String Service_Description,
                                        String serviceFee,
                                        String organization_name,
                                        String partner_id,
                                        String partner_name,
                                        boolean encrypt, String val,String org_service_id ,
                                        String eligibility,
                                        String documents ,
                                        String process ,
                                        String co_ordionator) {

        MasterServiceList masterServiceList= new MasterServiceList();

        masterServiceList.setOrganization_idfk(organization_idfk  );
        masterServiceList.setService_ID( Service_ID);
        masterServiceList.setService_Name( Service_Name);
        masterServiceList.setDuration_To_Complete(Duration_To_Complete );
        masterServiceList.setServiceFee(serviceFee );
        masterServiceList.setOrganization_idfk(organization_idfk);
        masterServiceList.setOrganization_name( organization_name);
        masterServiceList.setService_Description( Service_Description );
        masterServiceList.setPartner_id(partner_id);
        masterServiceList.setPartner_name( partner_name );
        masterServiceList.setOrg_service_id( org_service_id );
        masterServiceList.setEligibility( eligibility );
        masterServiceList.setDocuments( documents );
        masterServiceList.setProcess( process );
        masterServiceList.setCo_ordionator( co_ordionator );

        insertMasterServiceList(masterServiceList);
    }
    public void insertMasterServiceList(final MasterServiceList masterServiceList) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().insertMasterServiceList(masterServiceList);
                return null;
            }
        }.execute();
    }

    //================get master service list harish ==============



    //========================== get master notification harish ============
    public void insertMasterNotification(String message_to_id,String notification_id,String reply_allowed,String message_subject,
                                         String parent_messaage_id,String organization_idfk,String organization_name,String message_from,String message_type_idfk,String message
            ,String message_to,String read,String syn_status) {

        insertMasterNotification( message_to_id,notification_id,reply_allowed,message_subject,
                parent_messaage_id,organization_idfk,organization_name,message_from,message_type_idfk,message ,message_to,read,syn_status,false, null);
    }

    public void insertMasterNotification(String message_to_id,String notification_id,String reply_allowed,
                                         String message_subject,
                                         String parent_messaage_id,String organization_idfk,
                                         String organization_name,String message_from,
                                         String message_type_idfk,String message
            ,String message_to,String read,
                                         String syn_status, boolean encrypt, String val) {

        MasterNotification masterNotification= new MasterNotification();

        masterNotification.setMessage_to_id(message_to_id  );
        masterNotification.setNotification_id( notification_id);
        masterNotification.setReply_allowed( reply_allowed);
        masterNotification.setMessage_subject(message_subject );
        masterNotification.setParent_messaage_id(parent_messaage_id );
        masterNotification.setOrganization_idfk(organization_idfk);
        masterNotification.setOrganization_name( organization_name);
        masterNotification.setMessage_from( message_from );
        masterNotification.setMessage_type_idfk(message_type_idfk);
        masterNotification.setMessage( message );
        masterNotification.setMessage_to( message_to );
        masterNotification.setRead( read);
        masterNotification.setSyn_status( syn_status );
        insertMasterNotification(masterNotification);
    }
    public void insertMasterNotification(final MasterNotification masterNotification) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().insertMasterNotification(masterNotification);
                return null;
            }
        }.execute();
    }

    //================get master notification harish ==============

    // =============== get all service chat history =========


    public void insertAllServiceChatHistory(String Service_Msg_id,String Service_id,
                                            String organization_id,
                                            String member_id,
                                            String Message_form,
                                            String Message_to,
                                            String Message,String attachment_name,
                                            String file_ext,String mime_typ,
                                            String Parent_id,
                                            String read,String syn_status,
                                            String created_date,String file_path, String local_filepath) {

        insertAllServiceChatHistory( Service_Msg_id,Service_id,
                organization_id, member_id,
                Message_form, Message_to, Message, attachment_name, file_ext,
                mime_typ,
                Parent_id, read, syn_status, created_date,file_path,local_filepath,
                false, null);
    }

    public void insertAllServiceChatHistory(
            String Service_Msg_id,
            String Service_id,
            String organization_id,
            String member_id,
            String Message_form,
            String Message_to,
            String Message,
            String attachment_name,
            String file_ext,
            String mime_typ,
            String Parent_id,
            String read,
            String syn_status,
            String created_date,
            String file_path,
            String local_filepath,
            boolean encrypt, String val) {

        AllServiceHistoryHelper allServiceHistoryHelper = new AllServiceHistoryHelper();
        allServiceHistoryHelper.setService_Msg_id( Service_Msg_id);
        allServiceHistoryHelper.setService_id( Service_id );
        allServiceHistoryHelper.setOrganization_id( organization_id);
        allServiceHistoryHelper.setMember_id(member_id );
        allServiceHistoryHelper.setMessage_form(Message_form);
        allServiceHistoryHelper.setMessage_to(Message_to);
        allServiceHistoryHelper.setMessage( Message);
        allServiceHistoryHelper.setAttachment_name( attachment_name);
        allServiceHistoryHelper.setFile_ext(file_ext);
        allServiceHistoryHelper.setMime_typ( mime_typ);
        allServiceHistoryHelper.setParent_id( Parent_id);
        allServiceHistoryHelper.setRead( read);
        allServiceHistoryHelper.setSyn_status( syn_status );
        allServiceHistoryHelper.setCreated_date( created_date );
        allServiceHistoryHelper.setFile_path( file_path );
        allServiceHistoryHelper.setLocal_filepath( local_filepath );
        insertAllServiceChatHistory(allServiceHistoryHelper);
    }
    public void insertAllServiceChatHistory(final AllServiceHistoryHelper allServiceHistoryHelper) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().insertMasterService(allServiceHistoryHelper);
                return null;
            }
        }.execute();
    }


    // =============== get all chat service history =========




    // =============== get all chat history =========


    public void insertAllChatHistory(String notification_idfk,
                                     String organization_id,
                                     String member_id,
                                     String Message_form,
                                     String Message_to,
                                     String Message,String attachment_name,
                                     String file_ext,String mime_typ,
                                     String Parent_id,
                                     String read,String syn_status,
                                     String created_date,String file_path,String local_filepath) {

        insertAllChatHistory(  notification_idfk,
                organization_id,member_id,
                Message_form,Message_to,
                Message,attachment_name,
                file_ext, mime_typ,
                Parent_id,read,syn_status,created_date,file_path,local_filepath,
                false, null);
    }

    public void insertAllChatHistory(
                                     String notification_idfk,
                                     String organization_id,
                                     String member_id,
                                     String Message_form,
                                     String Message_to,
                                     String Message,
                                     String attachment_name,
                                     String file_ext,
                                     String mime_typ,
                                     String Parent_id,
                                     String read,
                                     String syn_status,
                                     String created_date,
                                     String file_path,String local_filepath,
                                     boolean encrypt, String val) {

        AllNotificationHistoryHelper allchathistory = new AllNotificationHistoryHelper();


        allchathistory.setNotification_idfk( notification_idfk);
        allchathistory.setOrganization_id( organization_id);
        allchathistory.setMember_id(member_id );
        allchathistory.setMessage_form(Message_form);
        allchathistory.setMessage_to(Message_to);
        allchathistory.setMessage( Message);
        allchathistory.setAttachment_name( attachment_name);
        allchathistory.setFile_ext(file_ext);
        allchathistory.setMime_typ( mime_typ);
        allchathistory.setParent_id( Parent_id);
        allchathistory.setRead( read);
        allchathistory.setSyn_status( syn_status );
        allchathistory.setCreated_date( created_date );
        allchathistory.setFile_path( file_path );
        allchathistory.setLocal_filepath( local_filepath );
        insertAllChatHistory(allchathistory);
    }
    public void insertAllChatHistory(final AllNotificationHistoryHelper allNotificationHistoryHelper) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().insertMasterNotification(allNotificationHistoryHelper);
                return null;
            }
        }.execute();
    }
    public void deleteTask() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().deleteTask();
                return null;
            }
        }.execute();
    }

    // =============== get all chat history =========
    public LiveData<List<OrganizationTypeParam>> getTasks(String STATUS_PAYMENT, String STATUS_APPROVED, String STATUS_WAITING_PAYMENT, String STATUS_NOT_JOINED) {
        return OrganizationDB.daoAccess().fetchAllTasks(STATUS_PAYMENT, STATUS_APPROVED, STATUS_WAITING_PAYMENT, STATUS_NOT_JOINED);
    }
 	public LiveData<List<OrganizationTypeParam>> getMyOrganization( String STATUS_JOINED_MEMBER) {
        return OrganizationDB.daoAccess().fetMyOrganization( STATUS_JOINED_MEMBER);
    }
    public LiveData<List<MasterNotification>> getMasterNotification() {
        return OrganizationDB.daoAccess().getMasterNotificationData();
    }
    public LiveData<List<MasterServiceList>> getMasterServiceRequestMasterList() {
        return OrganizationDB.daoAccess().getMasterServiceRequestMasterListData();
    }
    public LiveData<List<MasterNotification>> getMyOrgNotifications(String orgId) {
        return OrganizationDB.daoAccess().getMyOrgNotifications(orgId);
    }

    public LiveData<List<AllNotificationHistoryHelper>> getAllNotificationHistoryHelper(String id) {
        return OrganizationDB.daoAccess().getAllNotificationHistoryHelperData(id);
    }

    public LiveData<List<SupportConversationHistory>> getSupportConversationHistoryHelper(String organizationId) {
        return OrganizationDB.daoAccess().SupportConversationHistory(organizationId);
    }

    public LiveData<List<AllServiceHistoryHelper>> getAllServiceHistoryHelper(String id, String orgid) {
        return OrganizationDB.daoAccess().getAllServiceHistoryHelperData(id,orgid);
    }
    public LiveData<List<MasterServiceList>> getSpecificServiceReq(String id) {
        return OrganizationDB.daoAccess().getSpecificServiceReqData(id);
    }
    public LiveData<List<KYCAadhar>> getProfile() {
        return OrganizationDB.daoAccess().getProfileData();
    }

    public LiveData<List<UserProfile>> getAdditionalProfile() {
        LiveData<List<UserProfile>> profile = OrganizationDB.daoAccess().fetchUserProfile();
        return profile;
    }

    public LiveData<List<MemberTypeMasterPOJO>> getMemberType() {
        LiveData<List<MemberTypeMasterPOJO>> profile = OrganizationDB.daoAccess().fetchMemberType();
        return profile;
    }
    public LiveData<List<OrganizationGroupMaster>> getMemberGroupType() {
        LiveData<List<OrganizationGroupMaster>> profile = OrganizationDB.daoAccess().fetchMemberGroupType();
        return profile;
    }

    public LiveData<List<CoordinatorsPOJO>> getCoordinator() {
        LiveData<List<CoordinatorsPOJO>> profile = OrganizationDB.daoAccess().fetchCoordinatorData();
        return profile;
    }

    public LiveData<List<PrimaryOrganisationPOJO>> getPrimaryOrg() {
        LiveData<List<PrimaryOrganisationPOJO>> profile = OrganizationDB.daoAccess().fetchPrimaryOrg();
        return profile;
    }

    public LiveData<List<PrimaryOrgGroupMaster>> getPrimaryOrgGroup() {
        LiveData<List<PrimaryOrgGroupMaster>> profile = OrganizationDB.daoAccess().fetchPrimaryOrgGroup();
        return profile;
    }

    public LiveData<List<OrganizationGroupMaster>> getOrgGroup() {
            LiveData<List<OrganizationGroupMaster>> profile = OrganizationDB.daoAccess().fetchOrgGroup();
            return profile;
        }

    public LiveData<List<OrganizationTypeParam>> getOrganization(String organizationName) {
        return OrganizationDB.daoAccess().fetchorganization(organizationName);
    }

    public LiveData<List<OrganizationTypeParam>> getSyncOrganization(String UPDATEDRECORD) {
        return OrganizationDB.daoAccess().syncOrganization(UPDATEDRECORD);
    }
    public void removeJoinedOrganization() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().deleteTask();
                return null;
            }
        }.execute();
    }
    public void removeMasterNotification() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().deleteMasterNotification();
                return null;
            }
        }.execute();
    }
    public void removeMasterServiceList() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().deleteMasterNotification();
                return null;
            }
        }.execute();
    }

    public void removeOrganizationGroup() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().deleteOrganizationGroup();
                return null;
            }
        }.execute();
    }

    public void removeAllChatHistory() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().deleteAllChatHistory();
                return null;
            }
        }.execute();
    }

    // remove service chat history table
    public void removeAllChatServiceHistory() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().deleteAllChatServiceHistory();
                return null;
            }
        }.execute();
    }

 public void removeSupportConvHistory() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().deleteSupportConvHistory();
                return null;
            }
        }.execute();
    }

public void removePrimaryOrgGroup() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().deletePrimaryOrgGroup();
                return null;
            }
        }.execute();
    }




    public void removeKYCAadhar() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().deleteKYCAadhar();
                return null;
            }
        }.execute();
    }

    public void removeCoordinatorName() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().removeCoordinatorName();
                return null;
            }
        }.execute();
    }

    public void removePrimaryOrganization() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().removePrimaryOrganization();
                return null;
            }
        }.execute();
    }

    public void removeMemberTypeMaster() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().removeMemberTypeMaster();
                return null;
            }
        }.execute();
    }

    public void removeAdditionalProfileDB() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().deleteProfile();
                return null;
            }
        }.execute();
    }

    public void deleteOrgList() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().deleteOrgList();
                return null;
            }
        }.execute();
    }

    public void deleteProfileData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().deleteProfile();
                return null;
            }
        }.execute();
    }

    public void closeDB() {
        if (openDb) {
            OrganizationDB.close();
        }
    }

    public void updateOrganization(OrganizationTypeParam updateOrganization) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().updateTask(updateOrganization);
                return null;
            }
        }.execute();
    }
	  public void updateOrganization(String UPDATEDRECORD, String remark, String joinRequestSend, String orgId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().updateOrganizationStatus(UPDATEDRECORD, remark, joinRequestSend, orgId);
                return null;
            }
        }.execute();
    }

    public void updateUserProfile(String synStatus) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().updateUserProfileStatus(synStatus);
                return null;
            }
        }.execute();
    }
    public void updateOrgJoinStatus(String orgId,String JoinStatus) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().updateOrganizationJoinStatus(orgId,JoinStatus,common.NOCHANGE);
                return null;
            }
        }.execute();
    }

    public void updateSycStatusOfAllChatHistory(String sycStatus, int id) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().updateSycStatusOfAllChatHistory(sycStatus,id);
                return null;
            }
        }.execute();
    }
    public void updateSycStatusOfAllChatService(String sycStatus, int id) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().updateSycStatusOfAllChatService(sycStatus,id);
                return null;
            }
        }.execute();
    }

    public void updateSycSupportConvChatService(String sycStatus, int supportId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().updateSycSupportConvChatService(sycStatus,supportId);
                return null;
            }
        }.execute();
    }

    public void updateProfile(String fullanme,String permanentAdd, String localAddress,String altMobNumber,
                              String dob, String Occupation, String gender, String village,
                              String areaName, String city, String taluk, String state, String zipcode, String memberTypeGroupId ,
                              String memberTypeGroupName, String memberType,String memberTypeId,String primaryOrganizationId,
                              String primaryOrganization, String coordinator,String coordinatorId, String profileImage,String primaryOrganGroupName,
                              String primaryOrganGroupId, String NEWRECORD) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().updateProfiledata(fullanme,permanentAdd,localAddress,altMobNumber,
                        dob,Occupation, gender, village,
                        areaName, city, taluk, state, zipcode,memberTypeGroupId ,
                        memberTypeGroupName,memberType,
                        memberTypeId,primaryOrganizationId, primaryOrganization,
                        coordinator,coordinatorId, profileImage, primaryOrganGroupName,
                        primaryOrganGroupId, NEWRECORD);
                return null;
            }
        }.execute();
    }


    public void insertAdditional(String member_id, String member_occupation, String member_local_add, String village_name, String area_name,String reg_city,
                                 String member_local_add_taluk, String reg_state, String member_local_add_pincode,
                                 String alternative_Number, String memTypeIDFK, String memberTypeCode, String organization_id,
                                 String organization_name, String agent_id, String agent_name, String member_dob,
                                 String member_permanent_address, String member_sex, String member_fullname, String member_mobile_no,
                                  String profilePicture,String Member_group_idfk, String member_groupName,  String memProfileSyncStatus,
                                 String primary_org_id, String organisation_group_type,String email,
                                         String marital_status,
                                         String membership_status,
                                         String date_Of_joining) {
        insertAdditional(member_id, member_occupation, member_local_add,  village_name, area_name, reg_city,
                member_local_add_taluk, reg_state, member_local_add_pincode,
                alternative_Number, memTypeIDFK, memberTypeCode, organization_id,
                organization_name, agent_id, agent_name, member_dob,
                member_permanent_address, member_sex, member_fullname, member_mobile_no, profilePicture, Member_group_idfk,
                member_groupName, memProfileSyncStatus,
                false, null, primary_org_id, organisation_group_type,email,
                marital_status,
                membership_status,
                date_Of_joining);
    }

    public void insertAdditional(String member_id, String member_occupation, String member_local_add, String village_name, String area_name,String reg_city,
                                 String member_local_add_taluk, String reg_state, String member_local_add_pincode,
                                 String alternative_Number, String memTypeIDFK, String memberTypeCode, String organization_id,
                                 String organization_name, String agent_id, String agent_name, String member_dob,
                                 String member_permanent_address, String member_sex, String member_fullname, String member_mobile_no,
                                 String profilePicture,String Member_group_idfk, String member_groupName, String memProfileSyncStatus, boolean statusval,
                                 String val, String primary_org_id, String organisation_group_type,
                                 String email,
                                 String marital_status,
                                 String membership_status,
                                 String date_Of_joining) {
        UserProfile userProfile = new UserProfile();
        userProfile.setMember_id(member_id);
        userProfile.setMember_occupation(member_occupation);
        userProfile.setMember_local_add(member_local_add);
        userProfile.setVillage_name(village_name);
        userProfile.setArea_name(area_name);
        userProfile.setReg_city(reg_city);
        userProfile.setMember_local_add_taluk(member_local_add_taluk);
        userProfile.setReg_state(reg_state);
        userProfile.setMember_local_add_pincode(member_local_add_pincode);
        userProfile.setAlternative_Number(alternative_Number);
        userProfile.setMemTypeIDFK(memTypeIDFK);
        userProfile.setMemberTypeCode(memberTypeCode);
        userProfile.setOrganization_id(organization_id);
        userProfile.setOrganization_name(organization_name);
        userProfile.setAgent_id(agent_id);
        userProfile.setAgent_name(agent_name);
        userProfile.setMember_dob(member_dob);
        userProfile.setMember_permanent_address(member_permanent_address);
        userProfile.setMember_sex(member_sex);
        userProfile.setMember_fullname(member_fullname);
        userProfile.setMember_mobile_no(member_mobile_no);
        userProfile.setProfilePicture(profilePicture);
        userProfile.setMemProfileSyncStatus(memProfileSyncStatus);
        userProfile.setMember_group_idfk(Member_group_idfk);
        userProfile.setMember_group(member_groupName);
        userProfile.setPrimary_org_id(primary_org_id);
        userProfile.setOrganisation_group_type(organisation_group_type);

        userProfile.setEmail( email );
        userProfile.setMarital_status( marital_status );
        userProfile.setMembership_status( membership_status );
        userProfile.setDate_Of_joining( date_Of_joining );

        insertAdditionalProfile(userProfile);
    }

    public void insertAdditionalProfile(UserProfile userProfile) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().insertProfile(userProfile);
                return null;
            }
        }.execute();
    }

    public void insertMemberType(String memberTypeCode, String memberTypeID,String memberTypeGroup) {
        insertMemberType(memberTypeCode, memberTypeID, memberTypeGroup, null);
    }

    public void insertMemberType(String memberTypeCode, String memberTypeID, String memberTypeGroup, String d) {
        MemberTypeMasterPOJO memTypeMaster = new MemberTypeMasterPOJO();
        memTypeMaster.setMemberTypeID(memberTypeID);
        memTypeMaster.setMemberTypeCode(memberTypeCode);
        memTypeMaster.setMemberTypeGroup(memberTypeGroup);
        insertAdditionalMemberType(memTypeMaster);
    }

    public void insertAdditionalMemberType(MemberTypeMasterPOJO memTypeMaster) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().insertMemType(memTypeMaster);
                return null;
            }
        }.execute();
    }

    public void setOrganizationGroupMaster(String memberGroupId, String memberGroup) {
        setOrganizationGroupMaster(memberGroupId, memberGroup, null);
    }

    public void setOrganizationGroupMaster(String memberGroupId, String memberGroup,String data) {
        OrganizationGroupMaster organizationGroupMaster = new OrganizationGroupMaster();
        organizationGroupMaster.setMember_group_id(memberGroupId);
        organizationGroupMaster.setMember_group(memberGroup);
        setOrganizationGroupMaster(organizationGroupMaster);
    }

    public void setOrganizationGroupMaster(OrganizationGroupMaster organizationGroupMaster) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().insertOrganizationGroup(organizationGroupMaster);
                return null;
            }
        }.execute();
    }

    public void setSupportConversationHistory( String organizationId, String memberId,
                                              String MessageForm, String MessageTo, String Message, String attachmentName, String fileExt, String filePath,
                                              String mimeTyp, String ParentId, String read, String synStatus, String createdDate, String local_filepath) {
        setSupportConversationHistory( organizationId, memberId,
                MessageForm, MessageTo, Message, attachmentName, fileExt, filePath,
                mimeTyp, ParentId, read, synStatus, createdDate,local_filepath, null);
    }

    public void setSupportConversationHistory( String organizationId, String memberId,
                                              String messageForm, String messageTo, String message,
                                              String attachmentName, String fileExt, String filePath,
                                              String mimeTyp, String parentId, String read, String synStatus,
                                              String createdDate,String local_filepath, String data) {
        SupportConversationHistory conversationHistory = new SupportConversationHistory();
        conversationHistory.setOrganization_id(organizationId);
        conversationHistory.setMessage_form(messageForm);
        conversationHistory.setMessage_to(messageTo);
        conversationHistory.setMessage(message);
        conversationHistory.setMember_id( memberId );
        conversationHistory.setAttachment_name(attachmentName);
        conversationHistory.setFile_ext(fileExt);
        conversationHistory.setFile_path(filePath);
        conversationHistory.setMime_typ(mimeTyp);
        conversationHistory.setParent_id(parentId);
        conversationHistory.setRead(read);
        conversationHistory.setSyn_status(synStatus);
        conversationHistory.setCreated_date(createdDate);
        conversationHistory.setLocal_filepath( local_filepath );
        setSupportConversationHistory(conversationHistory);
    }

    public void setSupportConversationHistory(SupportConversationHistory conversationHistory) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().insertSupportConversationHistory(conversationHistory);
                return null;
            }
        }.execute();
    }

    public void setprimaryOrgGroupMaster(String primaryOrgId, String organisationGroupType, String organisationGroupTypeCode) {
        setprimaryOrgGroupMaster(primaryOrgId, organisationGroupType, organisationGroupTypeCode, null);
        }
        public void setprimaryOrgGroupMaster(String primaryOrgId, String organisationGroupType, String organisationGroupTypeCode,
        String data) {
            PrimaryOrgGroupMaster primaryOrgGroupMaster = new PrimaryOrgGroupMaster();
            primaryOrgGroupMaster.setPrimaryOrgId(primaryOrgId);
            primaryOrgGroupMaster.setOrganisationGroupType(organisationGroupType);
            primaryOrgGroupMaster.setOrganisationGroupTypeCode(organisationGroupTypeCode);
            setprimaryOrgGroupMaster(primaryOrgGroupMaster);
        }

        public void setprimaryOrgGroupMaster(PrimaryOrgGroupMaster primaryOrgGroupMaster) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    OrganizationDB.daoAccess().insertPramaryOrgGroup(primaryOrgGroupMaster);
                    return null;
                }
            }.execute();
        }

    public void insertCoordinator(String login_id, String user_name) {
        insertCoordinator(login_id, user_name, null);
    }

    public void insertCoordinator(String login_id, String user_name, String d) {
        CoordinatorsPOJO coordinators = new CoordinatorsPOJO();
        coordinators.setLogin_id(login_id);
        coordinators.setUser_name(user_name);
        insertCoordinator(coordinators);
    }

    public void insertCoordinator(CoordinatorsPOJO coordinators) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().insertCoordinator(coordinators);
                return null;
            }
        }.execute();
    }

    public void insertOrganization(String organization_id, String organization_name) {
        insertOrganization(organization_id, organization_name, null);
    }

    public void insertOrganization(String organization_id, String organization_name, String d) {
        PrimaryOrganisationPOJO organisationlist = new PrimaryOrganisationPOJO();
        organisationlist.setOrganization_id(organization_id);
        organisationlist.setOrganization_name(organization_name);
        insertOrganizationList(organisationlist);
    }

    public void insertOrganizationList(PrimaryOrganisationPOJO organisationlist) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().insertOrganization(organisationlist);
                return null;
            }
        }.execute();
    }

    //Insert Aadhar data


    public void setAadharData(String image, String dob, String address, String gender,
                              String mobile, String name, String synStatus) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OrganizationDB.daoAccess().updateAadharData(image, dob, address, gender, mobile, name, synStatus);
                return null;
            }
        }.execute();
    }

    public static class getAdditionalProfile1 extends AsyncTask<Void, Element, List<UserProfile>> {

        ImageView additionalprofile_photo;
        EditText etmobile_number, etZipcode, etState, etCity, etTaluk, etArea, etVillage,
                etReferredBy, etReferredContact, etEmployeeName, etEmployeeContact, etLocalAddress;
        private WeakReference<Context> contextRef;


        public getAdditionalProfile1(Context context, ImageView additionalprofile_photo, EditText etState) {
            contextRef = new WeakReference<>(context);
            this.additionalprofile_photo = additionalprofile_photo;
            this.etState = etState;
            ;
        }

        @Override
        protected List<UserProfile> doInBackground(Void... voids) {

            return null;
        }


    }
}
