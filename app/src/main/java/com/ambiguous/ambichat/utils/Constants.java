package com.ambiguous.ambichat.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class Constants {

    static ProgressDialog progressDialog;

    public static void showProgresDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setMessage("loading...");
    }

    public static void dismissProgressDialog(Context context) {

        progressDialog.dismiss();
    }

    public void setToast(Context context, String toastMessage) {
        Toast.makeText(context, "" + toastMessage, Toast.LENGTH_SHORT).show();
    }
}
