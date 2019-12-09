package com.icls.offlinekyc.roomdb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.icls.offlinekyc.Database.OrganizationTypeParam;
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

@Database(entities = {OrganizationTypeParam.class, UserProfile.class, MemberTypeMasterPOJO.class,
        CoordinatorsPOJO.class, PrimaryOrganisationPOJO.class,
        KYCAadhar.class, MasterNotification.class, MasterServiceList.class, AllNotificationHistoryHelper.class,
        OrganizationGroupMaster.class, AllServiceHistoryHelper.class, PrimaryOrgGroupMaster.class, SupportConversationHistory.class},
        version = 35, exportSchema = false)
public abstract class OrganizationDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();

    @Override
    public void beginTransaction() {
        super.beginTransaction();
    }

    @Override
    public void endTransaction() {
        super.endTransaction();
    }

    @Override
    public boolean isOpen() {
        return super.isOpen();
    }

    @Override
    public void close() {
        super.close();
    }

}
