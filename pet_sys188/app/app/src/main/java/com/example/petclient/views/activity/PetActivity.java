package com.example.petclient.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.petclient.App;
import com.example.petclient.R;
import com.example.petclient.base.BaseActivity;
import com.example.petclient.base.adapter.VaccineListTagAdapter;
import com.example.petclient.base.entry.SysPet;
import com.example.petclient.base.entry.SysPetVaccineConfig;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.BaseResponseData;
import com.example.petclient.utils.http.entry.SysPetData;
import com.example.petclient.utils.http.entry.SysPetVaccineConfigListData;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import recycleview.huanglinqing.com.dialogutils.DialogUtils;

public class PetActivity extends BaseActivity {
    EditText txtPetName;
    EditText txtPetAge;
    EditText txtPetColor;
    EditText txtPetInfo;
    EditText txtPetDetail;

    EditText txtPetWeight;

    Spinner spPetType;
    Spinner spPetSterilization;
    private long petId;
    private SysPet sysPet;

    AlertDialog alertDialog;
    NetClient netClient;

    TagFlowLayout flVaccine;
    VaccineListTagAdapter vaccineListTagAdapter;
    List<SysPetVaccineConfig> sysPetVaccineConfigs;

    private String vaccine;

    Button btnSave;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_pet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtPetName = findViewById(R.id.txtPetName);
        txtPetAge = findViewById(R.id.txtPetAge);
        txtPetColor = findViewById(R.id.txtPetColor);
        txtPetInfo = findViewById(R.id.txtPetInfo);
        txtPetDetail = findViewById(R.id.txtPetDetail);
        spPetType = findViewById(R.id.spPetType);
        txtPetWeight = findViewById(R.id.txtPetWeight);
        spPetSterilization = findViewById(R.id.spPetSterilization);

        flVaccine = findViewById(R.id.flVaccine);

        btnSave = findViewById(R.id.btnSave);
        Intent intent = getIntent();
        petId = intent.getLongExtra("petId",-1);
        //查询信息
        if(petId > 0){
            netClient = new NetClient();
            alertDialog = DialogUtils.dialogloading(PetActivity.this, "请稍等...", false, false);
            NetRequest request = new NetRequest(ApiConfig.API_PET_INFO,"get").addParam("petId",petId+"").addHeader("userId", App.getMember(this).getMemberId()+"");
            netClient.Request(request).Callback(new NetJsonCallBack<SysPetData>(SysPetData.class) {
                @Override
                public void success(SysPetData data) {
                    if(data.getState() == 0){
                        Message message = new Message();
                        message.what = 0;
                        Bundle bundle = new Bundle();
                        bundle.putString("message",data.getContent());
                        message.setData(bundle);
                        PetActivity.this.sysPet = data.getData();
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
                        //显示
                        PetActivity.this.txtPetName.setText(PetActivity.this.sysPet.getPetName());
                        PetActivity.this.txtPetInfo.setText(PetActivity.this.sysPet.getPetInfo());
                        PetActivity.this.txtPetColor.setText(PetActivity.this.sysPet.getPetColor());
                        PetActivity.this.txtPetAge.setText(PetActivity.this.sysPet.getPetAge());
                        PetActivity.this.txtPetDetail.setText(PetActivity.this.sysPet.getPetDetail());
                        PetActivity.this.txtPetWeight.setText(PetActivity.this.sysPet.getWeight()+"");
                        if(PetActivity.this.sysPet.getPetType().equals("猫")){
                            spPetType.setSelection(0);
                        }else{
                            spPetType.setSelection(1);
                        }
                        if(PetActivity.this.sysPet.getIsSterilization() == 0){
                            spPetSterilization.setSelection(0);
                        }else {
                            spPetSterilization.setSelection(1);
                        }
                        getVaccineLog(sysPet.getPetType().equals("猫") ? 0 : 1);
                    }else if(msg.what == 1){
                        ToastUtil.show(PetActivity.this,bundle.getString("message"));
                    }else{
                        ToastUtil.show(PetActivity.this,bundle.getString("message"));
                    }
                    alertDialog.dismiss();
                }
            });
            netClient.Http();
        }else {
            getVaccineLog(0);
        }

        //查询宠物是否疫苗

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String petName = txtPetName.getText().toString();
                String petInfo = txtPetInfo.getText().toString();
                String petAge = txtPetAge.getText().toString();
                String petDetail = txtPetDetail.getText().toString();
                String petColor = txtPetColor.getText().toString();
                String petType = spPetType.getSelectedItemPosition() == 0 ? "猫" : "狗";
                int isSterilization = spPetSterilization.getSelectedItemPosition() == 0 ? 0 : 1;
                String weight = txtPetWeight.getText().toString();
                alertDialog = DialogUtils.dialogloading(view.getContext(), "请稍等...", false, false);
                NetRequest request = new NetRequest(ApiConfig.API_ADD_PET,"post").addHeader("userId", App.getMember(PetActivity.this).getMemberId()+"")
                        .addParam("petId",petId+"").addParam("petName",petName+"").addParam("petInfo",petInfo+"").addParam("petAge",petAge+"")
                        .addParam("petDetail",petDetail+"").addParam("petColor",petColor+"").addParam("petType",petType+"")
                        .addParam("isSterilization",isSterilization+"").addParam("weight",weight)
                        .addParam("vaccine",vaccine);
                netClient = new NetClient();
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
                            ToastUtil.show(PetActivity.this,bundle.getString("message"));
                        }else if(msg.what == 1){
                            ToastUtil.show(PetActivity.this,bundle.getString("message"));
                        }else{
                            ToastUtil.show(PetActivity.this,bundle.getString("message"));
                        }
                        alertDialog.dismiss();
                    }
                });
                netClient.Http();
            }
        });
        this.vaccineListTagAdapter = new VaccineListTagAdapter(this.flVaccine,this,new ArrayList<>());
        flVaccine.setAdapter(this.vaccineListTagAdapter);
        flVaccine.setOnSelectListener(new TagFlowLayout.OnSelectListener()
        {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
                vaccine = vaccineListTagAdapter.selectString(new ArrayList<>(selectPosSet));
            }
        });
        //点击事件
        spPetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getVaccineLog(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getVaccineLog(int petType){
        //查询
        vaccine = "";
        NetClient netClient = new NetClient();
        NetRequest request = new NetRequest(ApiConfig.API_PET_VACCINE_LOG_LIST,"get")
                .addParam("petType",petType+"").addParam("petId",petId+"").addHeader("userId", App.getMember(this).getMemberId()+"");
        netClient.Request(request).Callback(new NetJsonCallBack<SysPetVaccineConfigListData>(SysPetVaccineConfigListData.class) {
            @Override
            public void success(SysPetVaccineConfigListData data) {
                if(data.getState() == 0){
                    Message message = new Message();
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("message",data.getContent());
                    message.setData(bundle);
                    PetActivity.this.sysPetVaccineConfigs = data.getData();
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
                    //显示
                    vaccineListTagAdapter.Clean();
                    vaccineListTagAdapter.add(sysPetVaccineConfigs);
                    vaccineListTagAdapter.notifyDataChanged();
                    for (int i = 0;i<vaccineListTagAdapter.getSysPetVaccineConfigs().size();i++){
                        if(vaccineListTagAdapter.getSysPetVaccineConfigs().get(i).getIsSelect() == 1){
                            if(vaccine.equals("")){
                                vaccine += vaccineListTagAdapter.getSysPetVaccineConfigs().get(i).getPetVaccineConfigId();
                            }else {
                                vaccine += "," + vaccineListTagAdapter.getSysPetVaccineConfigs().get(i).getPetVaccineConfigId();
                            }
                            vaccineListTagAdapter.setSelectedList(i);
                        }
                    }
                }else if(msg.what == 1){
                    ToastUtil.show(PetActivity.this,bundle.getString("message"));
                }else{
                    ToastUtil.show(PetActivity.this,bundle.getString("message"));
                }
            }
        });
        netClient.Http();
    }
}
