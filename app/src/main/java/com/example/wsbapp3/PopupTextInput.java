package com.example.wsbapp3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class PopupTextInput {

    public interface InputCallback {
        void onInput(String inputText);
    }

    public static void showPopupTextInput(Context context, String title, InputCallback callback) {
        // Create an EditText to get user input
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        // Create a LinearLayout to hold the EditText
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(input);

        // Build the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing here; we handle the result after the dialog is closed
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Override the onClickListener for the positive button to retrieve the input
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = input.getText().toString();
                if (!userInput.trim().isEmpty()) {
                    // Call the callback with the input and dismiss the dialog
                    callback.onInput(userInput);
                    alertDialog.dismiss();
                } else {
                    // Optionally, you can show an error message for empty input
                    input.setError("Please enter text");
                }
            }
        });
    }
}