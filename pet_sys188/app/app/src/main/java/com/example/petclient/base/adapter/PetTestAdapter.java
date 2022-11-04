package com.example.petclient.base.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.petclient.App;
import com.example.petclient.R;
import com.example.petclient.base.entry.SysPetTest;
import com.example.petclient.utils.CallBack;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.BaseResponseData;

import java.text.SimpleDateFormat;
import java.util.List;

import recycleview.huanglinqing.com.dialogutils.DialogUtils;

public class PetTestAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    TextView txtPetName;
    TextView txtStatus;
    TextView lblTime;
    TextView txtCreateTime;

    LinearLayout lnInfo;
    TextView lblNote;


    Context context;

    LinearLayout lnButton;
    Button btnNoIll;
    Button btnIll;

    public PetTestAdapter(Context context, List<SysPetTest> sysPetTests){
        this.context = context;
        this.sysPetTests = sysPetTests;
        this.mInflater = LayoutInflater.from(context);
    }

    private List<SysPetTest> sysPetTests;

    public void Clean(){
        this.sysPetTests.clear();
    }

    public void add(List<SysPetTest> sysPetTests){
        this.sysPetTests.addAll(sysPetTests);
    }

    public void add(SysPetTest sysPetTests){
        this.sysPetTests.add(sysPetTests);
    }

    @Override
    public int getCount() {
        return sysPetTests.size();
    }

    @Override
    public Object getItem(int i) {
        return sysPetTests.get(i);
    }

    @Override
    public long getItemId(int i) {
        return sysPetTests.get(i).getPetTestId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_info_view, null);
        }
        txtPetName = convertView.findViewById(R.id.txtPetName);
        txtStatus = convertView.findViewById(R.id.txtStatus);
        txtCreateTime = convertView.findViewById(R.id.txtCreateTime);
        lblTime = convertView.findViewById(R.id.lblTime);

        lnInfo = convertView.findViewById(R.id.lnInfo);
        lblNote = convertView.findViewById(R.id.lblNote);

        lnButton = convertView.findViewById(R.id.lnButton);
        btnIll = convertView.findViewById(R.id.btnIll);
        btnNoIll = convertView.findViewById(R.id.btnNoIll);

        lblTime.setText("完成时间：");
        if(sysPetTests.get(position).getIsOver() == 1){
            String date =
                    new SimpleDateFormat("yyyy-MM-dd").format(sysPetTests.get(position).getCreateTime());
            txtCreateTime.setText(date);
            lnInfo.setVisibility(View.VISIBLE);
            if(sysPetTests.get(position).getIllName() == null || sysPetTests.get(position).getIllName().equals("")){
                lblNote.setText("您的宠物很健康");
            }else {
                lblNote.setText(sysPetTests.get(position).getIllName());
            }
        }else {
            txtCreateTime.setText("--");
        }
        txtPetName.setText(sysPetTests.get(position).getPetName());
        txtStatus.setText(sysPetTests.get(position).getIsOver() == 0 ? "等待体检" : "完成体检");
        if(sysPetTests.get(position).getStatus() == 0 && sysPetTests.get(position).getIsOver() == 1){
            lnButton.setVisibility(View.VISIBLE);
            btnIll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addIll(sysPetTests.get(position));
                }
            });
            btnNoIll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelIll(sysPetTests.get(position));
                }
            });
        }
        return convertView;
    }

    AlertDialog alertDialog;
    NetClient netClient;

    private void cancelIll(SysPetTest sysPetTest){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
        normalDialog.setTitle("取消宠物治疗申请");
        normalDialog.setMessage("确认取消接受治疗?");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog = DialogUtils.dialogloading(context, "请稍等...", false, false);
                netClient = new NetClient();
                NetRequest request = new NetRequest(ApiConfig.API_CANCEL_ILL,"post").addHeader("userId", App.getMember(context).getMemberId()+"")
                        .addParam("testId",sysPetTest.getPetTestId()+"");
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
                            if(callBack != null){
                                callBack.call();
                            }
                            ToastUtil.show(context,bundle.getString("message"));
                        }else if(msg.what == 1){
                            ToastUtil.show(context,bundle.getString("message"));
                        }else{
                            ToastUtil.show(context,bundle.getString("message"));
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

    private void addIll(SysPetTest sysPetTest){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
        normalDialog.setTitle("发布宠物治疗申请");
        normalDialog.setMessage("确认宠物接受治疗?");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog = DialogUtils.dialogloading(context, "请稍等...", false, false);
                netClient = new NetClient();
                NetRequest request = new NetRequest(ApiConfig.API_ADD_ILL,"post").addHeader("userId", App.getMember(context).getMemberId()+"")
                        .addParam("testId",sysPetTest.getPetTestId()+"");
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
                            if(callBack != null){
                                callBack.call();
                            }
                            ToastUtil.show(context,bundle.getString("message"));
                        }else if(msg.what == 1){
                            ToastUtil.show(context,bundle.getString("message"));
                        }else{
                            ToastUtil.show(context,bundle.getString("message"));
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

    public void Call(CallBack callBack){
        this.callBack = callBack;
    }

    private CallBack callBack;
}
