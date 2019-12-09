package com.icls.offlinekyc.function.Interfaces;

import com.icls.offlinekyc.function.Models.AccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DigiClient {
    /// these are the parameter we are passing in post request
    @Headers( "Accept: application/json" )
    @POST("public/oauth2/1/token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field( "code" )String authorisationCode,
            @Field( "grant_type" )String granttype,
            @Field( "client_id" )String clientID,
            @Field( "client_secret" )String clientsecret,
            @Field( "redirect_uri" )String redirectUri
    );
}
