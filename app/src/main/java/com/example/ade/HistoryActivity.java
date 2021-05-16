package com.example.ade;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ade.model.History;
import com.google.gson.Gson;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class HistoryActivity extends AppCompatActivity {

    TextView code_client, full_name, counter_num;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        code_client = findViewById(R.id.codeClientBar);
        full_name = findViewById(R.id.fullNameBar);

        counter_num = findViewById(R.id.counterNumBar);
        linearLayout = findViewById(R.id.historyShow);

        Bundle bundle = getIntent().getExtras();

        full_name.setText(bundle.getString("name"));
        code_client.setText(bundle.getString("code_client"));
        counter_num.setText(bundle.getString("counter_num"));

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            //Starting Write and Read data with URL
            //Creating array for parameters
            String[] field = new String[1];
            field[0] = "counter_num";
            //Creating array for data
            String[] data = new String[1];
            data[0] = bundle.getString("counter_num");

            PutData putData = new PutData(Initialize.HOST_NAME + "/getHistory.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();

                    try {
                        Gson gson = new Gson();

                        History[] histories = gson.fromJson(singleQ(result), History[].class);


                        for (History value : histories) {

                            View v = getLayoutInflater().inflate(R.layout.history_details, null);

                            TextView historyNewIndex = v.findViewById(R.id.historyNewIndex);
                            TextView historyDate = v.findViewById(R.id.historyDate);

                            historyNewIndex.setText(String.valueOf(value.new_index));
                            historyDate.setText(value.date);


                            linearLayout.addView(v);
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    //End ProgressBar (Set visibility to GONE)

                }
            }
            //End Write and Read data with URL
        });
    }


    public String singleQ(String str) {
        char[] s = str.toCharArray();
        for (int i = 0; i < s.length; i++) {
            if (s[i] == '\"') {
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

    public void back(View view) {
        finish();
    }
}