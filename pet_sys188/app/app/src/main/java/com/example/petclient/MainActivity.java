package com.example.petclient;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petclient.base.entry.SysMember;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.SysMemberData;
import com.example.petclient.views.activity.HomeActivity;
import com.example.petclient.views.activity.SignActivity;

import recycleview.huanglinqing.com.dialogutils.DialogUtils;

public class MainActivity extends AppCompatActivity {
    TextView txtAccount;
    TextView txtPassword;
    Button btnLogin;
    TextView linkSignin;
    AlertDialog alertDialog;
    NetClient netClient;

    private static final int authBaseRequestCode = 1;

    private static final String[] authBaseArr = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initPermission();
    }

    private void initPermission() {
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                requestPermissions(authBaseArr, authBaseRequestCode);
            }
        }
    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager
                    .PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void init(){
        txtAccount = findViewById(R.id.txtAccount);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        linkSignin = findViewById(R.id.linkSignin);
        SysMember member = App.getMember(this);
        if(member != null){
            txtAccount.setText(member.getAccount());
            txtPassword.setText(member.getPassword());
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断登录
                final String account = txtAccount.getText().toString();
                final String password = txtPassword.getText().toString();
                if(account.equals("")){
                    ToastUtil.show(v.getContext(),"请输入账号");
                    return;
                }
                if(password.equals("")){
                    ToastUtil.show(v.getContext(),"请输入密码");
                    return;
                }

                //登录操作
                alertDialog = DialogUtils.dialogloading(v.getContext(), "请稍等...", false, false);
                netClient = new NetClient();
                NetRequest request = new NetRequest(ApiConfig.API_LOGIN,"post").addParam("account",account).addParam("password",password);
                netClient.Request(request).Callback(new NetJsonCallBack<SysMemberData>(SysMemberData.class) {
                    @Override
                    public void success(SysMemberData data) {
                        if(data.getState() == 0){
                            Message message = new Message();
                            message.what = 0;
                            Bundle bundle = new Bundle();
                            bundle.putString("message",data.getContent());
                            bundle.putString("memberId",data.getData().getMemberId()+"");
                            bundle.putString("userName",data.getData().getUserName()+"");
                            message.setData(bundle);
                            this.handler.sendMessage(message);
                        }else {
                            Message message = new Message();
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("message",data.getContent());
                            message.setData(bundle);
                            this.handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void complete() {

                    }

                    @Override
                    public void fail(int code) {
                        Message message = new Message();
                        message.what = 2;
                        Bundle bundle = new Bundle();
                        bundle.putString("message","请求失败");
                        message.setData(bundle);
                        this.handler.sendMessage(message);
                    }


                    @Override
                    public void handler(Message msg) {
                        Bundle bundle = msg.getData();
                        if(msg.what == 0){
                            //保存token，账号和密码
                            SysMember member = new SysMember();
                            member.setAccount(account);
                            member.setPassword(password);
                            member.setUserName(bundle.getString("userName"));
                            member.setMemberId(Long.parseLong(bundle.getString("memberId")));
                            App.saveMember(member,MainActivity.this);
                            //启动到主界面
                            Intent intent=new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }else if(msg.what == 1){
                            ToastUtil.show(MainActivity.this,bundle.getString("message"));
                        }else{
                            ToastUtil.show(MainActivity.this,bundle.getString("message"));
                        }
                        alertDialog.dismiss();
                    }
                });
                netClient.Http();
            }
        });

        linkSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SignActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret != 0) {
                    Toast.makeText(MainActivity.this.getApplicationContext(),
                            "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }
}