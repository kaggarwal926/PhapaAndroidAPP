package com.icls.offlinekyc.helper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class PrimaryOrgGroupMaster {

    @PrimaryKey
    @NotNull
    private String primaryOrgId;

    private String organisationGroupType;
    private String organisationGroupTypeCode;

    public String getPrimaryOrgId() {
        return primaryOrgId;
    }

    public void setPrimaryOrgId(String primaryOrgId) {
        this.primaryOrgId = primaryOrgId;
    }

    public String getOrganisationGroupType() {
        return organisationGroupType;
    }

    public void setOrganisationGroupType(String organisationGroupType) {
        this.organisationGroupType = organisationGroupType;
    }

    public String getOrganisationGroupTypeCode() {
        return organisationGroupTypeCode;
    }

    public void setOrganisationGroupTypeCode(String organisationGroupTypeCode) {
        this.organisationGroupTypeCode = organisationGroupTypeCode;
    }
}
