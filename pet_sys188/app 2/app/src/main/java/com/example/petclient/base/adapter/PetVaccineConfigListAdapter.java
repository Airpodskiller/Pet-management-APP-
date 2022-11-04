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
import com.example.petclient.base.entry.SysPetVaccineConfig;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.BaseResponseData;

import java.text.SimpleDateFormat;
import java.util.List;

import recycleview.huanglinqing.com.dialogutils.DialogUtils;

public class PetVaccineConfigListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    TextView txtStatus;
    TextView lblTime;
    TextView txtCreateTime;
    TextView txtName;

    LinearLayout lnInfo;
    TextView lblNote;

    LinearLayout lnButton;
    Button btnVaccine;
    Context context;
    long petId;
    public PetVaccineConfigListAdapter(Context context, List<SysPetVaccineConfig> sysPetVaccines,long petId){
        this.context = context;
        this.sysPetVaccines = sysPetVaccines;
        this.mInflater = LayoutInflater.from(context);
        this.petId = petId;
    }

    private List<SysPetVaccineConfig> sysPetVaccines;

    public void Clean(){
        this.sysPetVaccines.clear();
    }

    public void add(List<SysPetVaccineConfig> sysPetVaccines){
        this.sysPetVaccines.addAll(sysPetVaccines);
    }

    public void add(SysPetVaccineConfig sysPetVaccines){
        this.sysPetVaccines.add(sysPetVaccines);
    }

    @Override
    public int getCount() {
        return sysPetVaccines.size();
    }

    @Override
    public Object getItem(int i) {
        return sysPetVaccines.get(i);
    }

    @Override
    public long getItemId(int i) {
        return sysPetVaccines.get(i).getPetVaccineConfigId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_pet_vaccine, null);
        }
        txtStatus = convertView.findViewById(R.id.txtStatus);
        txtCreateTime = convertView.findViewById(R.id.txtCreateTime);
        lblNote = convertView.findViewById(R.id.lblNote);
        lblTime = convertView.findViewById(R.id.lblTime);
        txtName = convertView.findViewById(R.id.txtName);

        lnButton = convertView.findViewById(R.id.lnButton);
        btnVaccine = convertView.findViewById(R.id.btnVaccine);

        lnInfo = convertView.findViewById(R.id.lnInfo);
        if(sysPetVaccines.get(position).getIsSelect() == 1){
            //接种完成
            lblTime.setText("完成时间：");
            String date =
                    new SimpleDateFormat("yyyy-MM-dd").format(sysPetVaccines.get(position).getCreateTime());
            txtCreateTime.setText(date);
            txtStatus.setText("完成接种");
        }else if(sysPetVaccines.get(position).getIsOver() != null){
            if(sysPetVaccines.get(position).getIsOver() == 0){
                lblTime.setText("预计时间：");
                txtCreateTime.setText("--");
                txtStatus.setText("等待接种");
            }else if(sysPetVaccines.get(position).getIsOver() == 1){
                lblTime.setText("开始时间：");
                String date =
                        new SimpleDateFormat("yyyy-MM-dd").format(sysPetVaccines.get(position).getCreateTime());
                txtCreateTime.setText(date);
                lnInfo.setVisibility(View.VISIBLE);
                lblNote.setText("预计完成时间："+sysPetVaccines.get(position).getTime());
                txtStatus.setText("正在接种");
            }else if(sysPetVaccines.get(position).getIsOver() == 2){
                lblTime.setText("完成时间：");
                String date =
                        new SimpleDateFormat("yyyy-MM-dd").format(sysPetVaccines.get(position).getCreateTime());
                txtCreateTime.setText(date);
                txtStatus.setText("完成接种");
            }else {
                //未接种
                lnButton.setVisibility(View.VISIBLE);
                txtStatus.setText("未接种");
                btnVaccine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //点击接种
                        load(sysPetVaccines.get(position));
                    }
                });
            }
        }
        txtName.setText(sysPetVaccines.get(position).getName());
        return convertView;
    }

    AlertDialog alertDialog;
    NetClient netClient;
    private void load(SysPetVaccineConfig config){
        //弹窗
        //弹窗确认
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
        normalDialog.setTitle("发布宠物接种疫苗申请");
        normalDialog.setMessage("需要费用￥"+config.getMoneys().doubleValue()+",确认发布?");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog = DialogUtils.dialogloading(context, "请稍等...", false, false);
                netClient = new NetClient();
                NetRequest request = new NetRequest(ApiConfig.API_ADD_VACCINE,"post").addHeader("userId", App.getMember(context).getMemberId()+"")
                        .addParam("petId",petId+"").addParam("configId",config.getPetVaccineConfigId()+"");
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
}
