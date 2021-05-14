package com.example.ade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ade.model.Counter;
import com.google.gson.Gson;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class MainActivity extends AppCompatActivity {

    TextView code_client, full_name;
    LinearLayout linearLayout;Counter[] counter = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_main);

        code_client = findViewById(R.id.codeClientBar);
        full_name = findViewById(R.id.fullNameBar);

        linearLayout = findViewById(R.id.countersShow);




        Bundle bundle = getIntent().getExtras();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            //Starting Write and Read data with URL
            //Creating array for parameters
            String[] field = new String[1];
            field[0] = "code_client";
            //Creating array for data
            String[] data = new String[1];
            data[0] = bundle.getString("code_client");
            //data[0] = "0202020D5151";
            PutData putData = new PutData(Initialize.HOST_NAME + "/getCounters.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

                    try {
                        Gson gson = new Gson();

                        counter = gson.fromJson(singleQ(result),Counter[].class);

                        for (int i = 0 ; i < counter.length ; i++) {
                            View v = getLayoutInflater().inflate(R.layout.counter_details, null);
                            TextView tv = v.findViewById(R.id.counterAddress);

                            tv.setText(counter[i].address);

                            TextView tvC = v.findViewById(R.id.counterNum);
                            tvC.setText(counter[i].counter_num);

                            linearLayout.addView(v);
                        }

                    }catch (Exception e){
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

//
//                    counters = jsonParse(jsonArray);
                    //End ProgressBar (Set visibility to GONE)

                }
            }
            //End Write and Read data with URL
        });

        full_name.setText(bundle.getString("name"));
        code_client.setText(bundle.getString("code_client"));
    }

    public String singleQ(String str) {
        char[] s = str.toCharArray();
        for (int i = 0 ; i < s.length ; i++){
            if (s[i] == '\"'){
                s[i] = '\'';
            }
        }
        return String.valueOf(s);
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

}