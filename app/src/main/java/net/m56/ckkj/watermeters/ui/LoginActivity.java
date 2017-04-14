package net.m56.ckkj.watermeters.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import net.m56.ckkj.watermeters.R;
import net.m56.ckkj.watermeters.entitys.Region;
import net.m56.ckkj.watermeters.fragment.FragmentMain;
import net.m56.ckkj.watermeters.utils.ActivityStack;
import net.m56.ckkj.watermeters.utils.CONSTS;
import net.m56.ckkj.watermeters.utils.JMD5Kit;
import net.m56.ckkj.watermeters.utils.OkHttpEngine;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressLint("ShowToast")
public class LoginActivity extends TitleActivity {


    EditText et_user, et_pass;
    Button bt_login;
    TextInputLayout til_name, til_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("请登陆..");
        setContentView(R.layout.activity_login);
        toLoad();

        monitirButton();
    }


    private void monitirButton() {
        // TODO 根据 et pass 焦点       显示软键盘


        bt_login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                verificationDatas();
            }


            private void verificationDatas() {
                //uuid
//                TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//                @SuppressLint("HardwareIds") String imei=tm.getDeviceId();
//                @SuppressLint("HardwareIds") String simSerialNumber=tm.getSimSerialNumber();
//                @SuppressLint("HardwareIds") String androidId = android.provider.Settings.Secure.getString(
//                        getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//                UUID deviceUuid =new UUID(androidId.hashCode(), ((long)imei.hashCode() << 32) | simSerialNumber.hashCode());
//                String uniqueIuniqueId  = deviceUuid.toString();
//                Log.d("uuid",uniqueIuniqueId);
//                Log.d("simSerialNumber",simSerialNumber);
//                Log.d("androidId",androidId);
//                Log.d("imei",imei);

                if (TextUtils.isEmpty(et_user.getText().toString()) && TextUtils.isEmpty(et_pass.getText().toString())) {
                    til_name.setError("请输入用户名..");
                    til_password.setError("请输入密码..");
                } else if (TextUtils.isEmpty(et_user.getText().toString())) {
                    til_name.setError("请输入用户名..");

                } else if (TextUtils.isEmpty(et_pass.getText().toString())) {
                    til_password.setError("请输入密码..");
                } else if (et_pass.getText().toString().trim().length() > 16) {
                    til_password.setError("密码长度超过限制.,");
                    Log.d("md5", JMD5Kit.MD5(et_pass.getText().toString().trim()));

                } else {
//                    dialog = DialogMaker.showCommenWaitDialog(LoginActivity.this, "正在登陆..", null, true, null);
                    //TODO 联网验证          toInternet();


//                    RequestBody formBody = new FormBody.Builder()
//                            .add("username", et_user.getText().toString()).add("password", et_pass.getText().toString())
//                            .build();

//                    OkHttpEngine.getInstance().postAsynHttp(CONSTS.HOST, formBody, LoginActivity.this);
                    toInternet();
//                    intoActivity(FragmentMain.class);
                }

            }


            private void toInternet() {
                // TODO 联网登陆,验证身份
                // HttpClient

                RequestBody formBody = new FormBody.Builder()
                        .add("userName", et_user.getText().toString().trim())
                        .add("passWord", JMD5Kit.MD5(et_pass.getText().toString().trim()))
                        .build();
                OkHttpEngine.getInstance().postAsynHttp(CONSTS.HOST, formBody).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String json = null;
                                Log.e("daatas", "re" + response.body().toString());
                                json = new String(String.valueOf(response.body()));
                                Region region = null;
                                region = new Gson().fromJson(json, Region.class);
                                Log.e("region", region.getUsername());
                                intoActivity(FragmentMain.class);
                            }
                        });

                    }
                });


            }
        });
    }

    private void toLoad() {
        et_user = (EditText) findViewById(R.id.login_et_username);
        et_pass = (EditText) findViewById(R.id.login_et_password);
        bt_login = (Button) findViewById(R.id.login_bt_sub);
        til_name = (TextInputLayout) findViewById(R.id.til_username2);
        til_password = (TextInputLayout) findViewById(R.id.til_username3);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ActivityStack.getInstance().removeActivity(this);
        finish();
    }
}
