package com.example.petclient.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RechargeActivity extends BaseActivity {
    private TextView txtRecharge;
    private EditText txtMoneys;
    NetClient netClient;
    Button btnRecharge;
    AlertDialog alertDialog;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtRecharge = findViewById(R.id.txtRecharge);
        txtMoneys = findViewById(R.id.txtMoneys);
        btnRecharge = findViewById(R.id.btnRecharge);
        load();
        //点击充值
        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = txtMoneys.getText().toString();
                if(text == null || text.equals("")){
                    ToastUtil.show(view.getContext(),"请输入充值金额");
                    return;
                }
                double moneys = Double.parseDouble(text);
                alertDialog = DialogUtils.dialogloading(view.getContext(), "请稍等...", false, false);
                netClient = new NetClient();
                NetRequest request = new NetRequest(ApiConfig.API_RECHARGE,"post").addHeader("userId", App.getMember(RechargeActivity.this).getMemberId()+"").addParam("moneys",moneys+"");
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
                            load();
                            ToastUtil.show(RechargeActivity.this,bundle.getString("message"));
                        }else if(msg.what == 1){
                            ToastUtil.show(RechargeActivity.this,bundle.getString("message"));
                        }else{
                            ToastUtil.show(RechargeActivity.this,bundle.getString("message"));
                        }
                        alertDialog.dismiss();
                    }
                });
                netClient.Http();
            }
        });
    }

    private void load(){
        //查询余额
        AlertDialog alertDialog = DialogUtils.dialogloading(this, "请稍等...", false, false);
        netClient = new NetClient();
        NetRequest request = new NetRequest(ApiConfig.API_MIME_INFO,"post").addHeader("userId", App.getMember(RechargeActivity.this).getMemberId()+"");
        netClient.Request(request).Callback(new NetJsonCallBack<SysMemberData>(SysMemberData.class) {
            @Override
            public void success(SysMemberData data) {
                if(data.getState() == 0){
                    Message message = new Message();
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("message",data.getContent());
                    bundle.putString("moneys",data.getData().getMoneys()+"");
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
                    txtRecharge.setText(bundle.getString("moneys"));
                }else if(msg.what == 1){
                    ToastUtil.show(RechargeActivity.this,bundle.getString("message"));
                }else{
                    ToastUtil.show(RechargeActivity.this,bundle.getString("message"));
                }
                alertDialog.dismiss();
            }
        });
        netClient.Http();
    }
}
