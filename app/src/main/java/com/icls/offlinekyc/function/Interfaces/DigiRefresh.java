package com.icls.offlinekyc.function.Interfaces;

import com.icls.offlinekyc.function.Models.RefreshToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DigiRefresh {
    @Headers( "Authorization: Basic RTJGM0JEODU6ZTljNTcwZjc1YzY0N2M3NzYzNWI=" )
    @FormUrlEncoded
    @POST("public/oauth2/1/token")
    Call<RefreshToken> getRefreshToken(
            @Field( "refresh_token" )String refreshtoken,
            @Field( "grant_type" )String granttype

    );
}
