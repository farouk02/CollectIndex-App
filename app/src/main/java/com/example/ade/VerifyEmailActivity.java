package com.example.ade;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class VerifyEmailActivity extends AppCompatActivity {

    CircularProgressButton reset;
    TextInputLayout email;
    TextView error;


    private boolean emailValidate = false;

    public static boolean isEmailValid(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public boolean isNotValid(final String STR, final String PATTERN) {

        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile(PATTERN);
        matcher = pattern.matcher(STR);

        return !matcher.matches();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);


        email = findViewById(R.id.textInputVerifyEmail);
        reset = findViewById(R.id.cirVerifyButton);
        error = findViewById(R.id.textViewErrorVerify);

        Objects.requireNonNull(email.getEditText()).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                boolean matched = isEmailValid(email.getEditText().getText().toString().trim());

                if (email.getEditText().getText().toString().isEmpty()) {
                    email.setError(getString(R.string.field_required, getString(R.string.email_hint)));
                    emailValidate = false;
                } else if (isNotValid(email.getEditText().getText().toString().trim(), "^(?=\\S+$).{1,}$")) {
                    email.setError(getString(R.string.no_spaces, getString(R.string.email_hint)));
                    emailValidate = false;
                } else if (!matched) {
                    email.setError(getString(R.string.field_invalid, getString(R.string.email_hint)));
                    emailValidate = false;
                } else {
                    emailValidate = true;
                }


                if (emailValidate) {
                    email.setError(null);
                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        reset.setOnClickListener(v -> {

            String email;
            email = this.email.getEditText().getText().toString().toLowerCase();


            if (emailValidate) {
                //send request
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[1];
                    field[0] = "email";
                    //Creating array for data
                    String[] data = new String[1];
                    data[0] = email;
                    PutData putData = new PutData(Initialize.HOST_NAME + "/verify.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            //End ProgressBar (Set visibility to GONE)
                            switch (result) {
                                case "1":
                                    Intent intent = new Intent(this, ResetActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("email", email);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "5":
                                case "8":


                                    error.setText(getString(R.string.field_inccorect, getString(R.string.email_hint)));
                                    error.setVisibility(View.VISIBLE);
                                    break;
                                case "6":
                                    error.setText(getString(R.string.fields_required));
                                    error.setVisibility(View.VISIBLE);
                                    break;
                                case "7":

                                    this.email.setEnabled(false);

                                    error.setText(getString(R.string.verify_email));
                                    error.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                    }
                    //End Write and Read data with URL
                });
            } else {
                error.setText(getString(R.string.field_invalid, getString(R.string.email_hint)));
                error.setVisibility(View.VISIBLE);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String email;
        email = Objects.requireNonNull(this.email.getEditText()).getText().toString().toLowerCase();

        //send request
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            //Starting Write and Read data with URL
            //Creating array for parameters
            String[] field = new String[1];
            field[0] = "email";
            //Creating array for data
            String[] data = new String[1];
            data[0] = email;
            PutData putData = new PutData(Initialize.HOST_NAME + "/verify.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();
                    //End ProgressBar (Set visibility to GONE)
                    switch (result) {
                        case "1":
                            Intent intent = new Intent(this, ResetActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                            break;
                        case "8":
                            error.setText(getString(R.string.field_inccorect, getString(R.string.email_hint)));
                            error.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
            //End Write and Read data with URL
        });

    }
}