package com.example.petclient.views.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.petclient.App;
import com.example.petclient.R;
import com.example.petclient.base.BaseActivity;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.BaseResponseData;

import recycleview.huanglinqing.com.dialogutils.DialogUtils;

public class PasswordActivity extends BaseActivity {

    EditText txtPassword;
    EditText txtNPassword;
    Button btnSave;

    NetClient netClient;
    AlertDialog alertDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtPassword = findViewById(R.id.txtPassword);
        txtNPassword = findViewById(R.id.txtNPassword);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = txtPassword.getText().toString();
                String nPassword = txtNPassword.getText().toString();
                if(password == null || password.equals("")){
                    ToastUtil.show(view.getContext(),"请输入旧密码");
                    return;
                }
                if(nPassword == null || nPassword.equals("")){
                    ToastUtil.show(view.getContext(),"请输入新密码");
                    return;
                }
                alertDialog = DialogUtils.dialogloading(view.getContext(), "请稍等...", false, false);
                netClient = new NetClient();
                NetRequest request = new NetRequest(ApiConfig.API_MIME_UPDATE_PASSWORD,"post").addHeader("userId", App.getMember(PasswordActivity.this).getMemberId()+"")
                        .addParam("password",password).addParam("nPassword",nPassword);
                netClient.Request(request).Callback(new NetJsonCallBack<BaseResponseData>(BaseResponseData.class) {
                    @Override
                    public void success(BaseResponseData data) {
                        if(data.getState() == 0){
                            Message message = new Message();
                            message.what = 0;
                            Bundle bundle = new Bundle();
                            bundle.putString("message",data.getContent());
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
                            ToastUtil.show(PasswordActivity.this,bundle.getString("message"));
                        }else if(msg.what == 1){
                            ToastUtil.show(PasswordActivity.this,bundle.getString("message"));
                        }else{
                            ToastUtil.show(PasswordActivity.this,bundle.getString("message"));
                        }
                        alertDialog.dismiss();
                    }
                });
                netClient.Http();
            }
        });
    }
}
