package com.icls.offlinekyc.roomdb;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.icls.offlinekyc.Database.OrganizationTypeParam;
import com.icls.offlinekyc.commonshare.common;
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

import java.util.List;

@Dao
public interface DaoAccess {
    @Insert
    Long insertTask(OrganizationTypeParam note);

    @Insert
    Long insertMasterNotification(MasterNotification masterNotification);

    @Insert
    Long insertServiceChat(AllServiceHistoryHelper allServiceHistoryHelper);


    @Insert
    Long insertMasterServiceList(MasterServiceList masterServiceList);

    @Insert
    Long insertMasterNotification(AllNotificationHistoryHelper allNotificationHistoryHelper);

    @Insert
    Long insertProfile(UserProfile userProfile);

    @Insert
    Long insertMemType(MemberTypeMasterPOJO memTypeMaster);

    @Insert
    Long insertCoordinator(CoordinatorsPOJO coordinators);

    @Insert
    Long insertMasterService(AllServiceHistoryHelper allServiceHistoryHelper);

    @Insert
    Long insertOrganizationGroup(OrganizationGroupMaster organizationGroupMaster);

    @Insert
    Long insertSupportConversationHistory(SupportConversationHistory conversationHistory);

    @Insert
    Long insertPramaryOrgGroup(PrimaryOrgGroupMaster primaryOrgGroupMaster);

    @Insert
    Long insertOrganization(PrimaryOrganisationPOJO organisationlist);

    @Insert
    Long insertKycAadhar(KYCAadhar kycAadharData);


    @Query("SELECT * FROM OrganizationTypeParam where status=:STATUS_PAYMENT OR status=:STATUS_APPROVED OR status =:STATUS_WAITING_PAYMENT OR status =:STATUS_NOT_JOINED")
    LiveData<List<OrganizationTypeParam>> fetchAllTasks(String STATUS_PAYMENT, String STATUS_APPROVED, String STATUS_WAITING_PAYMENT, String STATUS_NOT_JOINED);

    @Query("SELECT * FROM OrganizationTypeParam where  status =:STATUS_JOINED_MEMBER")
    LiveData<List<OrganizationTypeParam>> fetMyOrganization( String STATUS_JOINED_MEMBER);

    @Query("SELECT * FROM KYCAadhar ")
    LiveData<List<KYCAadhar>> getProfileData();

    @Query("SELECT * FROM MasterNotification ")
    LiveData<List<MasterNotification>>  getMasterNotificationData();

    @Query("SELECT * FROM MasterServiceList ")
    LiveData<List<MasterServiceList>>  getMasterServiceRequestMasterListData();

    @Query("SELECT * FROM MasterNotification where  organization_idfk =:orgId")
    LiveData<List<MasterNotification>>  getMyOrgNotifications(String orgId )  ;


    @Query("SELECT * FROM AllNotificationHistoryHelper  where notification_idfk=:id order by id")
    LiveData<List<AllNotificationHistoryHelper>>  getAllNotificationHistoryHelperData(String id);

   @Query("SELECT * FROM SupportConversationHistory  where organization_id=:orgId order by organization_id")
    LiveData<List<SupportConversationHistory>>  SupportConversationHistory(String orgId);

    @Query("SELECT * FROM AllServiceHistoryHelper  where Service_id=:id and organization_id=:orgid order by id")
    LiveData<List<AllServiceHistoryHelper>>  getAllServiceHistoryHelperData(String id, String orgid);


    @Query("SELECT * FROM MasterServiceList  where organization_idfk=:orgid")
    LiveData<List<MasterServiceList>>  getSpecificServiceReqData(String orgid);

    @Query("SELECT * FROM UserProfile ")
    LiveData<List<UserProfile>> fetchUserProfile();

    @Query("SELECT * FROM MemberTypeMasterPOJO ")
    LiveData<List<MemberTypeMasterPOJO>> fetchMemberType();

    @Query("SELECT * FROM OrganizationGroupMaster ")
        LiveData<List<OrganizationGroupMaster>> fetchMemberGroupType();



    @Query("SELECT * FROM CoordinatorsPOJO ")
    LiveData<List<CoordinatorsPOJO>> fetchCoordinatorData();

    @Query("SELECT * FROM PrimaryOrganisationPOJO ")
    LiveData<List<PrimaryOrganisationPOJO>> fetchPrimaryOrg();

    @Query("SELECT * FROM PrimaryOrgGroupMaster ")
    LiveData<List<PrimaryOrgGroupMaster>> fetchPrimaryOrgGroup();

    @Query("SELECT * FROM OrganizationGroupMaster ")
    LiveData<List<OrganizationGroupMaster>> fetchOrgGroup();

    @Query("SELECT * FROM UserProfile ")
    List<UserProfile> fetchUserProf();

    @Query("SELECT * FROM OrganizationTypeParam where organization_name=:organizationName")
    LiveData<List<OrganizationTypeParam>> fetchorganization(String organizationName);

  @Query("SELECT * FROM OrganizationTypeParam where syn_status=:UPDATEDRECORD")
    LiveData<List<OrganizationTypeParam>> syncOrganization(String UPDATEDRECORD);

    @Query("SELECT * FROM OrganizationTypeParam where syn_status=:UPDATEDRECORD")
    List<OrganizationTypeParam> syncOrganizationNew(String UPDATEDRECORD);

    @Query("SELECT * FROM AllNotificationHistoryHelper  where syn_status=:NEWRECORD")
    List<AllNotificationHistoryHelper> syncAllNotificationHistory(String NEWRECORD);

    @Query("SELECT * FROM AllServiceHistoryHelper  where syn_status=:NEWRECORD")
    List<AllServiceHistoryHelper> syncAllServiceChat(String NEWRECORD);

    @Query("SELECT  * FROM SupportConversationHistory  where syn_status=:NEWRECORD LIMIT 1")
    List<SupportConversationHistory> syncSupportChat(String NEWRECORD);


    @Query("SELECT * FROM UserProfile where memProfileSyncStatus=:newRecord OR memProfileSyncStatus=:updateRecord")
    List<UserProfile> syncProfile(String newRecord, String updateRecord);



    @Query("SELECT COUNT(*) FROM OrganizationTypeParam ")
    int dbCount();


    @Update
    void updateTask(OrganizationTypeParam note);

    @Query("UPDATE organizationTypeParam SET syn_status = :UPDATEDRECORD , Joining_Remarks= :remark, status = :joinRequestSend where organization_id = :orgId")
    void updateOrganizationStatus(String UPDATEDRECORD, String remark, String joinRequestSend, String orgId);

    @Query("UPDATE UserProfile SET memProfileSyncStatus = :synStatus ")
    void updateUserProfileStatus(String synStatus);

    @Query("UPDATE OrganizationTypeParam SET status = :joinStatus , syn_status = :NOCHANGE where organization_id = :orgid")
    void updateOrganizationJoinStatus(String orgid,String joinStatus,String NOCHANGE);

    @Query("UPDATE AllNotificationHistoryHelper SET syn_status =:NOCHANGE where id = :id")
    void updateSycStatusOfAllChatHistory(String NOCHANGE , int id);

    @Query("UPDATE AllServiceHistoryHelper SET syn_status =:NOCHANGE where id = :id")
    void updateSycStatusOfAllChatService(String NOCHANGE , int id);

    @Query("UPDATE SupportConversationHistory SET syn_status =:NOCHANGE where Support_id = :supportId")
    void updateSycSupportConvChatService(String NOCHANGE , int supportId);

    @Query("UPDATE UserProfile SET member_dob=:dob, member_permanent_address =:address," +
            "member_sex=:gender, member_mobile_no=:mobile, member_fullname=:name, memProfileSyncStatus=:synStatus, profilePicture=:image")
    void updateAadharData(String image, String dob, String address, String gender,
                          String mobile, String name, String synStatus);



    @Query("UPDATE UserProfile SET member_fullname =:fullanme,member_permanent_address =:permanentAdd ," +
            "member_dob =:dob ,member_occupation = :Occupation , member_local_add= :localAddress, " +
            "member_sex = :gender, village_name = :village, area_name = :areaName, reg_city = :city, " +
            "member_local_add_taluk = :taluk, reg_state =:state, member_local_add_pincode =:zipcode, Member_group_idfk =:memberTypeGroupId," +
            "member_group =:memberTypeGroupName,organization_name =:primaryOrganization, organization_id=:primaryOrganizationId, " +
            "memTypeIDFK =:memberTypeId, alternative_Number =:altMobNumber, " +
            " memberTypeCode=:memberType,agent_name = :coordinator,agent_id=:coordinatorId, " +
            " profilePicture =:profileImage,primary_org_id =:primaryOrganGroupId, organisation_group_type =:primaryOrganGroupName, memProfileSyncStatus =:NEWRECORD")
    void updateProfiledata(String fullanme, String permanentAdd, String localAddress, String altMobNumber, String dob,
                           String Occupation, String gender, String village,
                           String areaName, String city, String taluk, String state, String zipcode, String memberTypeGroupId,
                           String memberTypeGroupName, String memberType, String memberTypeId, String coordinator, String coordinatorId,
                           String primaryOrganization, String primaryOrganizationId, String profileImage,String primaryOrganGroupName,
                           String primaryOrganGroupId, String NEWRECORD);

    @Query("DELETE FROM OrganizationTypeParam")
    public void deleteTask();

    @Query("DELETE FROM KYCAadhar")
    public void deleteKYCAadhar();

    @Query("DELETE FROM MasterNotification")
    public void deleteMasterNotification();

    @Query("DELETE FROM OrganizationGroupMaster")
    public void deleteOrganizationGroup();

    @Query("DELETE FROM MasterNotification")
    public void deleteMasterServiceList();

    @Query("DELETE FROM AllNotificationHistoryHelper")
    public void deleteAllChatHistory();

    @Query("DELETE FROM AllServiceHistoryHelper")
    public void deleteAllChatServiceHistory();

    @Query("DELETE FROM SupportConversationHistory")
    public void deleteSupportConvHistory();

   @Query("DELETE FROM PrimaryOrgGroupMaster")
    public void deletePrimaryOrgGroup();

    @Query("DELETE FROM UserProfile")
    public void deleteProfile();

    @Query("DELETE FROM UserProfile")
    public void deleteOrgList();

    @Query("DELETE FROM PrimaryOrganisationPOJO")
    public void removePrimaryOrganization();

    @Query("DELETE FROM CoordinatorsPOJO")
    public void removeCoordinatorName();

    @Query("DELETE FROM MemberTypeMasterPOJO")
    public void removeMemberTypeMaster();
}
