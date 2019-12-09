package com.icls.offlinekyc.function.Models;

import com.google.gson.annotations.SerializedName;

public class DocList {

    // response of issued doclist in json
    @SerializedName("name")
    private String name;
    @SerializedName("date")
    private String date;

    @SerializedName("description")
    private String description;
    @SerializedName("issuer")
    private String issuer;

    public void GetIssuedDoc(String name, String date, String description, String issuer) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.issuer = issuer;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
