
package com.icls.offlinekyc.helper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

@Entity
public class PrimaryOrganisationPOJO {

    @PrimaryKey
    @NotNull
    private String organization_id;

    @SerializedName("organization_name")
    @Expose
    private String organization_name;

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
}
