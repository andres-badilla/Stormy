package com.andresbadilla.stormy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Andres Badilla on 9/16/2015.
 */
public class AlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(getArguments().getInt("title")))
                .setMessage(context.getString(getArguments().getInt("message")))
                .setPositiveButton(context.getString(getArguments().getInt("button")), null);

        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialogFragment getInstance(int title, int message, int button){

        AlertDialogFragment dialog = new AlertDialogFragment();

        Bundle args = new Bundle();
        args.putInt("title",title);
        args.putInt("message",message);
        args.putInt("button",button);
        dialog.setArguments(args);

        return dialog;

    }
}
