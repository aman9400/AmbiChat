package com.ambiguous.ambichat.api;



import com.ambiguous.ambichat.bean.SignInResponse;
import com.ambiguous.ambichat.bean.VerifyDataResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServices {
    @FormUrlEncoded
    @POST("api/"+APITags.login)
    Call<SignInResponse> signIn(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("api/"+APITags.verifydata)
    Call<VerifyDataResponse> verifyData(@FieldMap Map<String,String> map);
}
