package com.example.ade;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class RegisterActivity extends AppCompatActivity {

    private boolean counterValidate;
    private boolean clientValidate;
    private boolean emailValidate = false;
    private boolean usernameValidate;
    private boolean passwordValidate;


    TextInputLayout counter;
    TextInputLayout client;
    TextInputLayout email;
    TextInputLayout username;
    TextInputLayout password;

    CircularProgressButton register;

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        counter = findViewById(R.id.textInputCounterNum);
        client = findViewById(R.id.textInputClientNum);
        email = findViewById(R.id.textInputEmail);
        username = findViewById(R.id.textInputUsername);
        password = findViewById(R.id.textInputPassword);
        register = findViewById(R.id.cirRegisterButton);


        Objects.requireNonNull(counter.getEditText()).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                int length = counter.getEditText().getText().toString().length();

                if (counter.getEditText().getText().toString().isEmpty()) {
                    counter.setError(getString(R.string.field_required, getString(R.string.counter_num)));
                    counterValidate = false;
                } else if (length != 6) {
                    counter.setError(getString(R.string.field_equal, getString(R.string.counter_num),6));
                    counterValidate = false;
                } else {
                    counter.setError(null);
                    counterValidate = true;
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        Objects.requireNonNull(client.getEditText()).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                int length = client.getEditText().getText().toString().length();

                if (client.getEditText().getText().toString().isEmpty()) {
                    client.setError(getString(R.string.field_required, getString(R.string.client_code)));
                    clientValidate = false;
                } else if (length <= 6) {
                    client.setError(getString(R.string.field_equal, getString(R.string.client_code),6));
                    clientValidate = false;
                } else {
                    client.setError(null);
                    clientValidate = true;
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        Objects.requireNonNull(email.getEditText()).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                boolean matched = isValidEmail(email.getEditText().getText().toString().trim());

                if (email.getEditText().getText().toString().isEmpty()) {
                    email.setError(getString(R.string.field_required, getString(R.string.email_hint)));
                    emailValidate = false;
                } else if (!matched) {
                    email.setError(getString(R.string.email_invalid));
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
                } else if (length <= 6) {
                    username.setError(getString(R.string.field_equal, getString(R.string.username_hint),6));
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

                boolean matched = isValidPassword(password.getEditText().getText().toString());

                if (password.getEditText().getText().toString().isEmpty()) {
                    password.setError(getString(R.string.field_required, getString(R.string.password_hint)));
                    passwordValidate = false;
                } else if (password.getEditText().getText().toString().length() < 8) {
                    password.setError(getString(R.string.field_at_least, getString(R.string.password_hint),8));
                    passwordValidate = false;
                }  else if (!matched) {
                    password.setError(getString(R.string.password_requirement));
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

        /*register.setOnClickListener(v -> {
            if (counterValidate) {
                if (clientValidate) {
                    if (phoneValidate) {
                        if (usernameValidate) {
                            if (passwordValidate) {
                                //send request
                            } else {

                            }
                        }
                    }
                }
            }
        });
        */

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