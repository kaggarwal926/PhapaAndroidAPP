package com.icls.offlinekyc.function.Models;

import com.google.gson.annotations.SerializedName;

public class RefreshToken {
    @SerializedName( "access_token" )
    public String accessToken;

    @SerializedName( "expires_in" )
    public String expiresIn;

    @SerializedName( "scope" )
    public String Scope;

    @SerializedName( "token_type" )
    public String tokenType;

    @SerializedName( "refresh_token" )
    public  String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getScope() {
        return Scope;
    }

    public String getTokenType() {
        return tokenType;
    }
}
