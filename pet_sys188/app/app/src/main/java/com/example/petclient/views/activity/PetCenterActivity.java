package com.example.petclient.views.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.petclient.App;
import com.example.petclient.R;
import com.example.petclient.base.BaseActivity;
import com.example.petclient.base.entry.SysPet;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.BaseResponseData;
import com.example.petclient.utils.http.entry.SysPetData;
import com.example.petclient.utils.http.entry.SysPetSterilizationConfigData;

import recycleview.huanglinqing.com.dialogutils.DialogUtils;

public class PetCenterActivity extends BaseActivity {
    private long petId;

    LinearLayout btnSendTest;
    LinearLayout btnSendShower;
    LinearLayout btnSendSterilization;
    LinearLayout btnVaccine;

    LinearLayout btnTest;
    LinearLayout btnIll;
    LinearLayout btnShower;
    LinearLayout btnSterilization;

    AlertDialog alertDialog;
    NetClient netClient;

    SysPet sysPet;
    TextView txtPetName;
    TextView txtPetIsSterilization;

    private void init(){
        //查询宠物信息
        alertDialog = DialogUtils.dialogloading(this, "请稍等...", false, false);
        NetClient netClient = new NetClient();
        NetRequest request = new NetRequest(ApiConfig.API_PET_INFO,"post").addHeader("userId", App.getMember(this).getMemberId()+"")
                .addParam("petId",petId+"");
        netClient.Request(request).Callback(new NetJsonCallBack<SysPetData>(SysPetData.class) {
            @Override
            public void success(SysPetData data) {
                if(data.getState() == 0){
                    Message message = new Message();
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("message",data.getContent());
                    sysPet = data.getData();
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
                    PetCenterActivity.this.txtPetName.setText("宠物名字:"+sysPet.getPetName());
                    if(sysPet.getIsSterilization() == 0){
                        PetCenterActivity.this.txtPetIsSterilization.setText("是否绝育:否");
                    }else {
                        PetCenterActivity.this.txtPetIsSterilization.setText("是否绝育:是");
                    }
                }else if(msg.what == 1){
                    ToastUtil.show(PetCenterActivity.this,bundle.getString("message"));
                }else{
                    ToastUtil.show(PetCenterActivity.this,bundle.getString("message"));
                }
                alertDialog.dismiss();
            }
        });
        netClient.Http();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        petId = intent.getLongExtra("petId",-1);
        //绑定事件
        btnSendTest = findViewById(R.id.btnSendTest);
        btnSendShower = findViewById(R.id.btnSendShower);
        btnSendSterilization = findViewById(R.id.btnSendSterilization);
        btnVaccine = findViewById(R.id.btnVaccine);

        btnTest = findViewById(R.id.btnTest);
        btnIll = findViewById(R.id.btnIll);
        btnShower = findViewById(R.id.btnShower);
        btnSterilization = findViewById(R.id.btnSterilization);

        txtPetName = findViewById(R.id.txtPetName);
        txtPetIsSterilization = findViewById(R.id.txtPetIsSterilization);
        init();
        //事件
        //点击体检
        btnSendTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder normalDialog = new AlertDialog.Builder(view.getContext());
                normalDialog.setTitle("发布宠物体检申请");
                normalDialog.setMessage("确认发布一条申请?");
                normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog = DialogUtils.dialogloading(PetCenterActivity.this, "请稍等...", false, false);
                        netClient = new NetClient();
                        NetRequest request = new NetRequest(ApiConfig.API_ADD_PET_TEST,"post").addHeader("userId", App.getMember(PetCenterActivity.this).getMemberId()+"").addParam("petId",petId+"");
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
                                    //保存token，账号和密码
                                    ToastUtil.show(PetCenterActivity.this,bundle.getString("message"));
                                }else if(msg.what == 1){
                                    ToastUtil.show(PetCenterActivity.this,bundle.getString("message"));
                                }else{
                                    ToastUtil.show(PetCenterActivity.this,bundle.getString("message"));
                                }
                                alertDialog.dismiss();
                            }
                        });
                        netClient.Http();
                    }
                });
                normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                normalDialog.show();
            }
        });

        //跳转到创建洗澡的页面
        btnSendShower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转洗澡界面
                Intent intent=new Intent(view.getContext(), PetShowerActivity.class);
                intent.putExtra("petId",petId);
                intent.putExtra("petType",sysPet.getPetType().equals("猫") ? 0 : 1);
                view.getContext().startActivity(intent);
            }
        });
        //点击提交绝育
        btnSendSterilization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sysPet.getIsSterilization() == 1){
                    ToastUtil.show(view.getContext(),"您的宠物已绝育，请勿重复点击");
                    return;
                }
                //点击是否付款
                querySterilizationConfig();
            }
        });

        //点击到疫苗页面
        btnVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), PetVaccineActivity.class);
                intent.putExtra("petId",petId);
                view.getContext().startActivity(intent);
            }
        });

        //几个记录的页面
        //体检记录
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), PetTestActivity.class);
                intent.putExtra("petId",petId);
                view.getContext().startActivity(intent);
            }
        });

        //宠物症状
        btnIll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), PetIllActivity.class);
                intent.putExtra("petId",petId);
                view.getContext().startActivity(intent);
            }
        });

        //洗澡记录
        btnShower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), PetShowerRecordActivity.class);
                intent.putExtra("petId",petId);
                view.getContext().startActivity(intent);
            }
        });

        //绝育记录
        btnSterilization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), PetSterilizationActivity.class);
                intent.putExtra("petId",petId);
                view.getContext().startActivity(intent);
            }
        });
    }

    /**
     * 查询绝育的费用
     */
    private void querySterilizationConfig(){
        //查询宠物信息
        alertDialog = DialogUtils.dialogloading(this, "请稍等...", false, false);
        NetClient netClient = new NetClient();
        NetRequest request = new NetRequest(ApiConfig.API_STERILIZATION_CONFIG,"post").addHeader("userId", App.getMember(this).getMemberId()+"")
                .addParam("petType",(sysPet.getPetType().equals("猫") ? 0 : 1)+"");
        netClient.Request(request).Callback(new NetJsonCallBack<SysPetSterilizationConfigData>(SysPetSterilizationConfigData.class) {
            @Override
            public void success(SysPetSterilizationConfigData data) {
                if(data.getState() == 0){
                    Message message = new Message();
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("message",data.getContent());
                    bundle.putString("moneys",data.getData().getMoneys().toString());
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
                alertDialog.dismiss();
                if(msg.what == 0){
                    addSterilization(bundle.getString("moneys"));
                }else if(msg.what == 1){
                    ToastUtil.show(PetCenterActivity.this,bundle.getString("message"));
                }else{
                    ToastUtil.show(PetCenterActivity.this,bundle.getString("message"));
                }

            }
        });
        netClient.Http();
    }

    //提交绝育
    public void addSterilization(String moneys){
        //弹窗确认
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setTitle("发布宠物绝育申请");
        normalDialog.setMessage("需要费用￥"+moneys+",确认发布?");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog = DialogUtils.dialogloading(PetCenterActivity.this, "请稍等...", false, false);
                netClient = new NetClient();
                NetRequest request = new NetRequest(ApiConfig.API_ADD_STERILIZATION,"post").addHeader("userId", App.getMember(PetCenterActivity.this).getMemberId()+"")
                        .addParam("petId",petId+"");
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
                            ToastUtil.show(PetCenterActivity.this,bundle.getString("message"));
                        }else if(msg.what == 1){
                            ToastUtil.show(PetCenterActivity.this,bundle.getString("message"));
                        }else{
                            ToastUtil.show(PetCenterActivity.this,bundle.getString("message"));
                        }
                        alertDialog.dismiss();
                    }
                });
                netClient.Http();
            }
        });
        normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        normalDialog.show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pet_center;
    }
}
