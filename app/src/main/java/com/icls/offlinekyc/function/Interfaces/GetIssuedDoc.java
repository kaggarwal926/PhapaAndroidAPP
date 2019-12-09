package com.icls.offlinekyc.function.Interfaces;

import com.icls.offlinekyc.function.Models.DocList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GetIssuedDoc {

    @GET("public/oauth2/1/files/issued")
    Call<List<DocList>> getIsuuedDocList(@Header("Authorization") String auth);

}

