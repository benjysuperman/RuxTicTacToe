package com.cybridz.ruxtictactoe.helpers.api;

public interface ApiResponseCallback {
    void onSuccess(String textResponse);
    void onFailure(Exception e);
}
