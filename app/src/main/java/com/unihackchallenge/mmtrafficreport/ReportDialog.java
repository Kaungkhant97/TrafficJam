package com.unihackchallenge.mmtrafficreport;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by kaungkhantthu on 2/10/17.
 */

public class ReportDialog extends AppCompatDialogFragment {
    public static final String ADDRESS="ADDRESS";
    private String maddress;

    public static ReportDialog newInstance(String address) {
        ReportDialog f = new ReportDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(ADDRESS, address);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        maddress = getArguments().getString(ADDRESS,"");
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.reportdialog, null);
        EditText edAddress = (EditText) v.findViewById(R.id.edtxt_address);
        edAddress.setText(maddress);
        return new AlertDialog.Builder(getActivity())

                .setTitle("Alert DialogFragment")
                // Set Dialog Message

                // Positive button
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                    }
                })

                // Negative Button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                    }
                }).setView(v).create();
    }
}
