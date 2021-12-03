package com.ambiguous.ambichat.api;




import com.ambiguous.ambichat.bean.SignInResponse;
import com.ambiguous.ambichat.bean.VerifyDataResponse;
import com.ambiguous.ambichat.interfaces.WebServiceResponse;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ServiceHandler {

    private static APIServices restInterface;

    public ServiceHandler() {
        if (restInterface == null)
            restInterface = RequestController.getClient().create(APIServices.class);
    }

    public void signIn(WebServiceResponse webServiceResponse, Map<String, String> request) {
        Call<SignInResponse> call = restInterface.signIn(request);
        call.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(@NotNull Call<SignInResponse> call, @NotNull Response<SignInResponse> response) {
                if (response.code() == 200) {
                    webServiceResponse.onSuccess(response.body());
                } else {
                    webServiceResponse.onFailure();
                }
            }
            @Override
            public void onFailure(@NotNull Call<SignInResponse> call, @NotNull Throwable t) {
                webServiceResponse.onFailure();
            }
        });
    }

    public void verifyData(final WebServiceResponse webServiceResponse, Map<String, String> request) {
        Call<VerifyDataResponse> call = restInterface.verifyData(request);
        call.enqueue(new Callback<VerifyDataResponse>() {
            @Override
            public void onResponse(@NotNull Call<VerifyDataResponse> call, @NotNull Response<VerifyDataResponse> response) {
                if (response.code() == 200) {
                    webServiceResponse.onSuccess(response.body());
                } else {
                    webServiceResponse.onFailure();
                }
            }
            @Override
            public void onFailure(@NotNull Call<VerifyDataResponse> call, @NotNull Throwable t) {
                webServiceResponse.onFailure();
            }
        });
    }

}
