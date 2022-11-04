package com.example.petclient.views.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.example.petclient.utils.http.entry.SysMemberData;

import recycleview.huanglinqing.com.dialogutils.DialogUtils;

public class InfoActivity extends BaseActivity {
    TextView txtUserName;
    TextView txtPhone;
    Button btnSave;
    NetClient netClient;
    AlertDialog alertDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txtUserName = findViewById(R.id.txtUserName);
        txtPhone = findViewById(R.id.txtPhone);
        btnSave = findViewById(R.id.btnSave);

        //
        alertDialog = DialogUtils.dialogloading(this, "请稍等...", false, false);
        netClient = new NetClient();
        NetRequest request = new NetRequest(ApiConfig.API_MIME_INFO,"post").addHeader("userId", App.getMember(InfoActivity.this).getMemberId()+"");
        netClient.Request(request).Callback(new NetJsonCallBack<SysMemberData>(SysMemberData.class) {
            @Override
            public void success(SysMemberData data) {
                if(data.getState() == 0){
                    Message message = new Message();
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("message",data.getContent());
                    bundle.putString("userName",data.getData().getUserName());
                    bundle.putString("phone",data.getData().getPhone());
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
                    txtUserName.setText(bundle.getString("userName"));
                    txtPhone.setText(bundle.getString("phone"));
                }else if(msg.what == 1){
                    ToastUtil.show(InfoActivity.this,bundle.getString("message"));
                }else{
                    ToastUtil.show(InfoActivity.this,bundle.getString("message"));
                }
                alertDialog.dismiss();
            }
        });
        netClient.Http();
        //设置用户信息
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = txtUserName.getText().toString();
                String phone = txtPhone.getText().toString();
                if(userName == null || userName.equals("")){
                    ToastUtil.show(view.getContext(),"请输入真实姓名");
                    return;
                }
                if(phone == null || phone.equals("")){
                    ToastUtil.show(view.getContext(),"请输入联系电话");
                    return;
                }
                alertDialog = DialogUtils.dialogloading(view.getContext(), "请稍等...", false, false);
                netClient = new NetClient();
                NetRequest request = new NetRequest(ApiConfig.API_MIME_UPDATE,"post").addHeader("userId", App.getMember(InfoActivity.this).getMemberId()+"")
                        .addParam("userName",userName).addParam("phone",phone);
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
                            ToastUtil.show(InfoActivity.this,"保存成功");
                        }else if(msg.what == 1){
                            ToastUtil.show(InfoActivity.this,bundle.getString("message"));
                        }else{
                            ToastUtil.show(InfoActivity.this,bundle.getString("message"));
                        }
                        alertDialog.dismiss();
                    }
                });
                netClient.Http();
            }
        });
    }
}
