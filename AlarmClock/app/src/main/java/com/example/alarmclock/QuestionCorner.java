package com.example.alarmclock;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuestionCorner extends AppCompatActivity {


    private Button showdialog;
    private Dialog dialog;
    private Dialog dialog2;
    private Dialog dialog3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_corner);


        showdialog = findViewById(R.id.dialog_btn);

        //Create the Dialog here
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Button Okay = dialog.findViewById(R.id.btn_okay);
        Button Cancel = dialog.findViewById(R.id.btn_cancel);







        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(QuestionCorner.this, "Okay", Toast.LENGTH_SHORT).show();


                dialog2 = new Dialog(getApplicationContext());
                dialog2.setContentView(R.layout.custom_dialog_layout2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog2.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
                }
                dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog2.setCancelable(false); //Optional
                dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

                Button Okay2 = dialog2.findViewById(R.id.btn_okay);


                dialog2.show();

                Okay2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(QuestionCorner.this, "Okay", Toast.LENGTH_SHORT).show();


                        dialog3 = new Dialog(getApplicationContext());
                        dialog3.setContentView(R.layout.custom_dialog_layout3);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            dialog3.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
                        }
                        dialog3.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog3.setCancelable(false); //Optional
                        dialog3.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

                        Button Okay3 = dialog3.findViewById(R.id.btn_okay);


                        dialog3.show();


                        Okay3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(QuestionCorner.this, "Okay", Toast.LENGTH_SHORT).show();
                                dialog3.dismiss();
                            }
                        });

                    }
                });
            }
        });


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(QuestionCorner.this, "Cancel", Toast.LENGTH_SHORT).show();


                dialog2 = new Dialog(getApplicationContext());
                dialog2.setContentView(R.layout.custom_dialog_layout2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog2.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
                }
                dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog2.setCancelable(false); //Optional
                dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

                Button Cancel2 = dialog2.findViewById(R.id.btn_okay);


                dialog2.show();

                Cancel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(QuestionCorner.this, "Okay", Toast.LENGTH_SHORT).show();


                        dialog3 = new Dialog(getApplicationContext());
                        dialog3.setContentView(R.layout.custom_dialog_layout3);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            dialog3.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
                        }
                        dialog3.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog3.setCancelable(false); //Optional
                        dialog3.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

                        Button Cancel3 = dialog3.findViewById(R.id.btn_okay);


                        dialog3.show();


                        Cancel3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(QuestionCorner.this, "Okay", Toast.LENGTH_SHORT).show();
                                dialog3.dismiss();
                            }
                        });

                    }
                });
            }
        });


        showdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show(); // Showing the dialog here
            }
        });







    }


}
