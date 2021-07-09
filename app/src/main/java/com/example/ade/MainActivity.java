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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ade.model.Counter;
import com.google.gson.Gson;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class MainActivity extends AppCompatActivity {

    TextView code_client, full_name;
    LinearLayout linearLayout;
    Counter[] counter = null;

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

            PutData putData = new PutData(Initialize.HOST_NAME + "/getCounters.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();

                    try {
                        Gson gson = new Gson();

                        counter = gson.fromJson(singleQ(result), Counter[].class);


                        for (Counter value : counter) {
                            View v = getLayoutInflater().inflate(R.layout.counter_details, null);

                            TextView tvAddress = v.findViewById(R.id.counterAddress);
                            tvAddress.setText(value.address);

                            TextView tvStatus = v.findViewById(R.id.counterStatus);
                            if (value.status == 1)
                                tvStatus.setText(R.string.open);
                            else
                                tvStatus.setText(R.string.block);

                            TextView tvCNum = v.findViewById(R.id.counterNum);
                            tvCNum.setText(value.counter_num);

                            TextView tvOld = v.findViewById(R.id.textViewOldIndex);
                            tvOld.setText(String.valueOf(value.old_index));


                            TextView addIndexButton = v.findViewById(R.id.addIndexButton);
                            if (value.status == 0){
                                addIndexButton.setVisibility(View.GONE);
                            } else {
                                addIndexButton.setVisibility(View.VISIBLE);
                            }

                            LinearLayout addIndexLayout = v.findViewById(R.id.addIndexLayout);

                            EditText etNew = v.findViewById(R.id.editTextNewIndex);

                            TextView addButton = v.findViewById(R.id.addButton);

                            TextView historyButton = v.findViewById(R.id.historyButton);

                            historyButton.setOnClickListener(v1 -> {
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("name", bundle.getString("name"));
                                bundle1.putString("code_client", bundle.getString("code_client"));
                                bundle1.putString("counter_num", value.counter_num);

                                Intent intent = new Intent(this, HistoryActivity.class);
                                intent.putExtras(bundle1);
                                startActivity(intent);
                            });

                            addIndexButton.setOnClickListener(v1 -> {
                                if (value.status == 1) {
                                    //verify date permission
                                    Handler handler1 = new Handler(Looper.getMainLooper());
                                    handler1.post(() -> {
                                        //Starting Write and Read data with URL
                                        //Creating array for parameters
                                        String[] field1 = new String[1];
                                        field1[0] = "counter_num";
                                        //Creating array for data
                                        String[] data1 = new String[1];
                                        data1[0] = "true";
                                        PutData putData1 = new PutData(Initialize.HOST_NAME + "/isCollectDate.php", "POST", field1, data1);
                                        if (putData1.startPut()) {
                                            if (putData1.onComplete()) {
                                                String result1 = putData1.getResult();
                                                //End ProgressBar (Set visibility to GONE)
                                                if (result1.equals("1")) {
                                                    addIndexLayout.setVisibility(View.VISIBLE);
                                                    addIndexButton.setVisibility(View.INVISIBLE);
                                                } else if (result1.equals("0")) {
                                                    Toast.makeText(this, getString(R.string.cant_add_index_today), Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(this, result1, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }

                                        //End Write and Read data with URL
                                    });
                                }
                            });

                            addButton.setOnClickListener(v1 -> {
                                if (etNew.getText().toString().isEmpty()) {
                                    Toast.makeText(this, getString(R.string.field_required, getString(R.string.new_index)), Toast.LENGTH_LONG).show();
                                } else if (value.old_index >= Integer.parseInt(etNew.getText().toString())) {
                                    Toast.makeText(this, getString(R.string.field_greater, getString(R.string.new_index), value.old_index), Toast.LENGTH_LONG).show();

                                } else {
                                    Handler handler1 = new Handler(Looper.getMainLooper());
                                    handler1.post(() -> {
                                        //Starting Write and Read data with URL
                                        //Creating array for parameters
                                        String[] field1 = new String[1];
                                        field1[0] = "counter_num";
                                        //Creating array for data
                                        String[] data1 = new String[1];
                                        data1[0] = "true";
                                        PutData putData1 = new PutData(Initialize.HOST_NAME + "/isCollectDate.php", "POST", field1, data1);
                                        if (putData1.startPut()) {
                                            if (putData1.onComplete()) {
                                                String result1 = putData1.getResult();
                                                //End ProgressBar (Set visibility to GONE)
                                                if (result1.equals("1")) {
                                                    Handler handler2 = new Handler(Looper.getMainLooper());
                                                    handler2.post(() -> {
                                                        //Starting Write and Read data with URL
                                                        //Creating array for parameters
                                                        String[] field2 = new String[3];
                                                        field2[0] = "counter_num";
                                                        field2[1] = "old_index";
                                                        field2[2] = "new_index";
                                                        //Creating array for data
                                                        String[] data2 = new String[3];
                                                        data2[0] = value.counter_num;
                                                        data2[1] = tvOld.getText().toString();
                                                        data2[2] = etNew.getText().toString();


                                                        PutData putData2 = new PutData(Initialize.HOST_NAME + "/addIndex.php", "POST", field2, data2);
                                                        if (putData2.startPut()) {
                                                            if (putData2.onComplete()) {
                                                                String result2 = putData2.getResult();
                                                                //End ProgressBar (Set visibility to GONE)
                                                                switch (result2) {
                                                                    case "1":

                                                                        tvOld.setText(etNew.getText().toString());
                                                                        addIndexLayout.setVisibility(View.GONE);
                                                                        addIndexButton.setVisibility(View.VISIBLE);
                                                                        break;
                                                                    case "-1":
                                                                        Toast.makeText(this, getString(R.string.error_update_counter), Toast.LENGTH_LONG).show();
                                                                        break;
                                                                    case "0":
                                                                        Toast.makeText(this, getString(R.string.error_add_index), Toast.LENGTH_LONG).show();
                                                                        break;
                                                                    case "2":
                                                                        Toast.makeText(this, getString(R.string.field_invalid, getString(R.string.counter_num)), Toast.LENGTH_LONG).show();
                                                                        break;
                                                                    case "3":
                                                                        Toast.makeText(this, getString(R.string.fields_required), Toast.LENGTH_LONG).show();
                                                                        break;
                                                                    default:
                                                                        Toast.makeText(this, result2, Toast.LENGTH_LONG).show();
                                                                        break;
                                                                }
                                                            }
                                                        }

                                                        //End Write and Read data with URL
                                                    });
                                                } else if (result1.equals("0")) {
                                                    Toast.makeText(this, getString(R.string.cant_add_index_today), Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(this, result1, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }

                                        //End Write and Read data with URL
                                    });

                                }
                            });

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

        full_name.setText(bundle.getString("name"));
        code_client.setText(bundle.getString("code_client"));

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

}