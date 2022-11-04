package com.example.petclient.views.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.petclient.App;
import com.example.petclient.R;
import com.example.petclient.base.BaseActivity;
import com.example.petclient.base.adapter.ShowerListTagAdapter;
import com.example.petclient.base.entry.SysPetShowerType;
import com.example.petclient.utils.ImageViewFilter;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.BaseResponseData;
import com.example.petclient.utils.http.entry.SysPetShowerTypeListData;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import recycleview.huanglinqing.com.dialogutils.DialogUtils;

public class PetShowerActivity extends BaseActivity {
    private TagFlowLayout tlShower;
    private TagFlowLayout tlShowerAfter;
    private ShowerListTagAdapter showerAdapter;
    private ShowerListTagAdapter showerAfterAdapter;

    //基础费用
    TextView txtBaseMoneys;
    double baseMoneys;

    private String shower;
    private double showerMoneys;
    private String showerAfter;
    private double showerAfterMoneys;

    ImageView imgShower;

    AlertDialog alertDialog;

    Button btnSave;

    List<SysPetShowerType> sysPetShowerTypes;
    long petId;
    int petType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        petId = intent.getLongExtra("petId",-1);
        petType = intent.getIntExtra("petType",0);
        tlShower = findViewById(R.id.tlShower);
        txtBaseMoneys = findViewById(R.id.txtBaseMoneys);
        tlShowerAfter = findViewById(R.id.tlShowerAfter);
        btnSave = findViewById(R.id.btnSave);
        imgShower = findViewById(R.id.imgShower);
        baseMoneys = 0;
        this.showerAdapter = new ShowerListTagAdapter(this.tlShower,this,new ArrayList<>());
        tlShower.setAdapter(this.showerAdapter);
        tlShower.setOnSelectListener(new TagFlowLayout.OnSelectListener()
        {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
                shower = showerAdapter.selectString(new ArrayList<>(selectPosSet));
                showerMoneys = showerAdapter.selectMoneys(new ArrayList<>(selectPosSet));
            }
        });

        this.showerAfterAdapter = new ShowerListTagAdapter(this.tlShowerAfter,this,new ArrayList<>());
        tlShowerAfter.setAdapter(this.showerAfterAdapter);
        tlShowerAfter.setOnSelectListener(new TagFlowLayout.OnSelectListener()
        {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
                showerAfter = showerAfterAdapter.selectString(new ArrayList<>(selectPosSet));
                showerAfterMoneys = showerAfterAdapter.selectMoneys(new ArrayList<>(selectPosSet));
                if(selectPosSet.size() > 0){
                    SysPetShowerType sysPetShowerType = (SysPetShowerType)showerAfterAdapter.getItem(new ArrayList<>(selectPosSet).get(0));
                    if(sysPetShowerType.getImage() != null){
                        new ImageViewFilter(imgShower,sysPetShowerType.getImage()).filter();
                    }else {
                        imgShower.setImageResource(R.mipmap.icon_none);
                    }
                }
            }
        });
        //动态查询数据

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });
        init();
        initBaseMoneys(petId);
    }

    /**
     * 查询数据
     */
    private void init(){
        //查询宠物信息
        alertDialog = DialogUtils.dialogloading(this, "请稍等...", false, false);
        NetClient netClient = new NetClient();
        NetRequest request = new NetRequest(ApiConfig.API_SHOWER_TYPE_LIST,"get")
                .addParam("petType",petType+"");
        netClient.Request(request).Callback(new NetJsonCallBack<SysPetShowerTypeListData>(SysPetShowerTypeListData.class) {
            @Override
            public void success(SysPetShowerTypeListData data) {
                if(data.getState() == 0){
                    Message message = new Message();
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("message",data.getContent());
                    sysPetShowerTypes = data.getData();
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
                    List<SysPetShowerType> showers = new ArrayList<>();
                    List<SysPetShowerType> showerAfters = new ArrayList<>();
                    for (int i = 0;i<sysPetShowerTypes.size();i++){
                        if(sysPetShowerTypes.get(i).getShowType() == 0){
                            showers.add(sysPetShowerTypes.get(i));
                        }else {
                            showerAfters.add(sysPetShowerTypes.get(i));
                        }
                    }
                    PetShowerActivity.this.shower = "";
                    PetShowerActivity.this.showerAfter = "";
                    PetShowerActivity.this.showerMoneys = 0;
                    PetShowerActivity.this.showerAfterMoneys = 0;

                    PetShowerActivity.this.showerAdapter.Clean();
                    PetShowerActivity.this.showerAfterAdapter.Clean();

                    PetShowerActivity.this.showerAdapter.add(showers);
                    PetShowerActivity.this.showerAfterAdapter.add(showerAfters);

                    PetShowerActivity.this.showerAdapter.notifyDataChanged();
                    PetShowerActivity.this.showerAfterAdapter.notifyDataChanged();
                }else if(msg.what == 1){
                    ToastUtil.show(PetShowerActivity.this,bundle.getString("message"));
                }else{
                    ToastUtil.show(PetShowerActivity.this,bundle.getString("message"));
                }

            }
        });
        netClient.Http();
    }

    /**
     * 提交数据
     */
    private void upload(){
        String shower = this.shower;
        if(shower.equals("")){
            ToastUtil.show(this,"请选择洗澡项目");
            return;
        }
        if(!this.showerAfter.equals("")){
            shower += ","+this.showerAfter;
        }
        //弹窗确认
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setTitle("发布宠物洗澡申请");
        normalDialog.setMessage("需要费用￥"+(this.showerMoneys + this.showerAfterMoneys+this.baseMoneys)+",确认发布?");
        String finalShower = shower;
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog = DialogUtils.dialogloading(PetShowerActivity.this, "请稍等...", false, false);
                NetClient netClient = new NetClient();
                NetRequest request = new NetRequest(ApiConfig.API_ADD_SHOWER,"post").addHeader("userId", App.getMember(PetShowerActivity.this).getMemberId()+"")
                        .addParam("petId",petId+"").addParam("moneys",((showerAfterMoneys+showerMoneys+baseMoneys)+""))
                        .addParam("shower", finalShower);
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
                            ToastUtil.show(PetShowerActivity.this,bundle.getString("message"));
                        }else if(msg.what == 1){
                            ToastUtil.show(PetShowerActivity.this,bundle.getString("message"));
                        }else{
                            ToastUtil.show(PetShowerActivity.this,bundle.getString("message"));
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

    private void initBaseMoneys(long petId){
        AlertDialog alertDialog = DialogUtils.dialogloading(this, "请稍等...", false, false);
        NetClient netClient = new NetClient();
        NetRequest request = new NetRequest(ApiConfig.API_PET_SHOWER_BASE_MONEYS,"post").addHeader("userId", App.getMember(this).getMemberId()+"")
                .addParam("petId",""+petId);
        netClient.Request(request).Callback(new NetJsonCallBack<BaseResponseData>(BaseResponseData.class) {
            @Override
            public void success(BaseResponseData data) {
                if(data.getState() == 0){
                    Message message = new Message();
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("message","查询成功");
                    bundle.putString("moneys",data.getContent()+"");
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
                    baseMoneys = Double.parseDouble(bundle.getString("moneys"));
                    txtBaseMoneys.setText("您的宠物基础洗澡费用："+bundle.getString("moneys"));
                }else if(msg.what == 1){
                    ToastUtil.show(PetShowerActivity.this,bundle.getString("message"));
                }else{
                    ToastUtil.show(PetShowerActivity.this,bundle.getString("message"));
                }
                alertDialog.dismiss();
            }
        });
        netClient.Http();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pet_shower;
    }
}
