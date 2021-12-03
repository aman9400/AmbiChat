package com.ambiguous.ambichat.interfaces;

import retrofit2.Response;

public interface WebServiceResponse {
    void onSuccess(Object object);

    void onFailure();

    void onFailure(Response response);
}
