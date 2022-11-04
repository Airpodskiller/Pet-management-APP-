package com.example.petclient.views.activity;

import android.os.Bundle;
import android.os.Message;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.petclient.R;
import com.example.petclient.base.BaseActivity;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.BaseResponseData;

import recycleview.huanglinqing.com.dialogutils.DialogUtils;

public class SignActivity extends BaseActivity {
    EditText txtAccount;
    EditText txtMemberName;
    EditText txtMemberPhone;
    EditText txtPassword;

    Button btnSignin;

    AlertDialog alertDialog;
    NetClient netClient;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtAccount = findViewById(R.id.txtAccount);
        txtMemberName = findViewById(R.id.txtMemberName);
        txtPassword = findViewById(R.id.txtPassword);
        txtMemberPhone = findViewById(R.id.txtMemberPhone);
        btnSignin = findViewById(R.id.btnSignin);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String account = txtAccount.getText().toString();
                final String password = txtPassword.getText().toString();
                final String memberName = txtMemberName.getText().toString();
                final String memberPhone = txtMemberPhone.getText().toString();
                if(account.equals("")){
                    ToastUtil.show(v.getContext(),"请输入账号");
                    return;
                }
                if(password.equals("")){
                    ToastUtil.show(v.getContext(),"请输入密码");
                    return;
                }

                if(memberName.equals("")){
                    ToastUtil.show(v.getContext(),"请输入姓名");
                    return;
                }

                alertDialog = DialogUtils.dialogloading(v.getContext(), "请稍等...", false, false);
                netClient = new NetClient();
                NetRequest request = new NetRequest(ApiConfig.API_SIGNIN,"post").addParam("account",account).addParam("password",password).addParam("userName",memberName)
                        .addParam("phone",memberPhone);
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
                            ToastUtil.show(SignActivity.this,bundle.getString("message"));
                        }else if(msg.what == 1){
                            ToastUtil.show(SignActivity.this,bundle.getString("message"));
                        }else{
                            ToastUtil.show(SignActivity.this,bundle.getString("message"));
                        }
                        alertDialog.dismiss();
                    }
                });
                netClient.Http();
            }
        });
    }
}
