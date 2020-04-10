package com.example.acccidentdemo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;


/**
 * This class manages application dialogs.
 */
public class DialogUtility {

    /**
     * This method returns a simple dialog.
     *
     * @param context
     * @return
     */
    public static Dialog getDialog(Context context, int view) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Holo_Dialog_NoActionBar);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(view);
        //dialog.setContentView(view);

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (!((AppCompatActivity) context).isFinishing())
            dialog.show();

        return dialog;
    }

    public static ProgressDialog showLoadingDialog(Context context) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
//        if (progressDialog.getWindow() != null) {
//            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
//        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        return progressDialog;
    }




    public static void showToast(Context context, String message) {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }


    public static Snackbar showErrorAlert(View v, String sError, String colorCode) {
        Snackbar snackbar = Snackbar.make(v, sError, Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", view -> snackbar.dismiss());

        TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.getView().setBackgroundColor(Color.parseColor(colorCode));
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.show();
        return snackbar;
    }

}
