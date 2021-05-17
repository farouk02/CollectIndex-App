package com.example.ade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class RegisterActivity extends AppCompatActivity {

    TextView error;
    TextInputLayout client;
    TextInputLayout email;
    TextInputLayout username;
    TextInputLayout password;
    CircularProgressButton register;

    private boolean clientValidate = false;
    private boolean emailValidate = false;
    private boolean usernameValidate = false;
    private boolean passwordValidate = false;

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

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        error = findViewById(R.id.textViewErrorRegister);
        client = findViewById(R.id.textInputClientNum);
        email = findViewById(R.id.textInputEmail);
        username = findViewById(R.id.textInputUsername);
        password = findViewById(R.id.textInputPassword);
        register = findViewById(R.id.cirRegisterButton);


        Objects.requireNonNull(client.getEditText()).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                String str = client.getEditText().getText().toString();

                if (client.getEditText().getText().toString().isEmpty()) {
                    client.setError(getString(R.string.field_required, getString(R.string.client_code)));
                    clientValidate = false;
                } else if (isNotValid(Objects.requireNonNull(client.getEditText()).getText().toString().trim(), "^(?=\\S+$).{1,}$")) {
                    client.setError(getString(R.string.no_spaces, getString(R.string.client_code)));
                    clientValidate = false;
                } else if (str.length() != 12) {
                    client.setError(getString(R.string.field_equal, getString(R.string.client_code), 12));
                    clientValidate = false;
                } else {
                    clientValidate = true;
                }

                if (clientValidate) {
                    client.setError(null);
                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

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

        Objects.requireNonNull(username.getEditText()).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                int length = username.getEditText().getText().toString().length();

                if (username.getEditText().getText().toString().isEmpty()) {
                    username.setError(getString(R.string.field_required, getString(R.string.username_hint)));
                    usernameValidate = false;
                } else if (isNotValid(username.getEditText().getText().toString().trim(), "^(?=\\S+$).{1,}$")) {
                    username.setError(getString(R.string.no_spaces, getString(R.string.username_hint)));
                    usernameValidate = false;
                } else if (length <= 6) {
                    username.setError(getString(R.string.field_equal, getString(R.string.username_hint), 6));
                    usernameValidate = false;
                } else {
                    usernameValidate = true;
                }

                if (usernameValidate) {
                    username.setError(null);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        Objects.requireNonNull(password.getEditText()).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (password.getEditText().getText().toString().isEmpty()) {
                    password.setError(getString(R.string.field_required, getString(R.string.password_hint)));
                    passwordValidate = false;
                } else if (isNotValid(password.getEditText().getText().toString().trim(), "^(?=\\S+$).{1,}$")) {
                    password.setError(getString(R.string.no_spaces, getString(R.string.password_hint)));
                    passwordValidate = false;
                } else if (isNotValid(password.getEditText().getText().toString().trim(), "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{3,}$")) {
                    password.setError(getString(R.string.password_requirement));
                    passwordValidate = false;
                } else if (password.getEditText().getText().toString().length() < 8) {
                    password.setError(getString(R.string.field_at_least, getString(R.string.password_hint), 8));
                    passwordValidate = false;
                } else {
                    passwordValidate = true;
                }

                if (passwordValidate) {
                    password.setError(null);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        register.setOnClickListener(v -> {

            String client, email, username, password;
            client = this.client.getEditText().getText().toString().toUpperCase();
            email = this.email.getEditText().getText().toString().toLowerCase();
            username = this.username.getEditText().getText().toString().toLowerCase();
            password = this.password.getEditText().getText().toString();


            if (clientValidate && emailValidate && usernameValidate && passwordValidate) {
                //send request
                Toast.makeText(this, "send request", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[4];
                    field[0] = "code_client";
                    field[1] = "email";
                    field[2] = "username";
                    field[3] = "password";
                    //Creating array for data
                    String[] data = new String[4];
                    data[0] = client;
                    data[1] = email;
                    data[2] = username;
                    data[3] = password;
                    PutData putData = new PutData(Initialize.HOST_NAME + "/signup.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            //End ProgressBar (Set visibility to GONE)
                            switch (result) {
                                case "1":
                                    startActivity(new Intent(this, LoginActivity.class));
                                    finish();
                                    break;
                                case "0":
                                    error.setText(getString(R.string.something_wrong));
                                    error.setVisibility(View.VISIBLE);
                                    break;
                                case "2":
                                    error.setText(getString(R.string.field_already_token, getString(R.string.email_hint)));
                                    error.setVisibility(View.VISIBLE);
                                    break;
                                case "3":
                                    error.setText(getString(R.string.field_already_token, getString(R.string.username_hint)));
                                    error.setVisibility(View.VISIBLE);
                                    break;
                                case "4":
                                    error.setText(getString(R.string.already_signed_up));
                                    error.setVisibility(View.VISIBLE);
                                    break;
                                case "5":
                                    error.setText(getString(R.string.field_inccorect, getString(R.string.client_code)));
                                    error.setVisibility(View.VISIBLE);
                                    break;
                                case "6":
                                    error.setText(getString(R.string.fields_required));
                                    error.setVisibility(View.VISIBLE);
                                    break;
                                case "7":

                                    this.client.setEnabled(false);
                                    this.email.setEnabled(false);
                                    this.username.setEnabled(false);
                                    this.password.setEnabled(false);

                                    error.setText(getString(R.string.verify_email));
                                    error.setVisibility(View.VISIBLE);
                                    break;
                                case "8":

                                    this.client.setEnabled(true);
                                    this.username.setEnabled(true);
                                    this.password.setEnabled(true);

                                    error.setText(getString(R.string.field_inccorect, getString(R.string.email_hint)));
                                    error.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                    }
                    //End Write and Read data with URL
                });
            } else {
                error.setText(R.string.fields_invalid);
                error.setVisibility(View.VISIBLE);
            }

        });

    }


    public void gotoFb(View view) {
        String uri;

        try {
            int versionCode = this.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

            if (versionCode >= 3002850) uri = "fb://facewebmodal/f?href=" + Initialize.FACEBOOK_URL;
            else uri = "fb://page/?id=" + Initialize.FACEBOOK_PAGE_ID;

        } catch (PackageManager.NameNotFoundException e) {
            uri = Initialize.FACEBOOK_URL;
        }

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
    }

    public void gotoSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Initialize.WEBSITE_URL)));
    }

    public void onLoginClick(View view) {
        finish();
        //startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.stay);
    }

}