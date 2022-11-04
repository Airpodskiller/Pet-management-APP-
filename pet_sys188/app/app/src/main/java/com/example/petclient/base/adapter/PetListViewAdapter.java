package com.example.petclient.base.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.petclient.base.entry.SysPet;
import com.example.petclient.utils.CallBack;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.BaseResponseData;
import com.example.petclient.views.activity.MapActivity;
import com.example.petclient.views.activity.PetActivity;
import com.example.petclient.views.activity.PetCenterActivity;
import com.example.petclient.views.fragment.PetFragment;
import com.example.petclient.views.widget.InputDialog;

import java.util.List;

import recycleview.huanglinqing.com.dialogutils.DialogUtils;

public class PetListViewAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    TextView txtPetName;
    TextView txtPetType;

    Button btnInfo;
    Button btnDelete;
    Button btnCenter;
    Button btnMap;

    AlertDialog alertDialog;
    NetClient netClient;
    Context context;
    PetFragment petFragment;

    private CallBack callBack;

    private List<SysPet> sysPets;

    public void Call(CallBack callBack){
        this.callBack = callBack;
    }

    public PetListViewAdapter(Context context,List<SysPet> sysPets,PetFragment petFragment){
        this.context = context;
        this.sysPets = sysPets;
        this.petFragment = petFragment;
        this.mInflater = LayoutInflater.from(context);
    }

    public void Clean(){
        this.sysPets.clear();
    }

    public void add(List<SysPet> sysPets){
        this.sysPets.addAll(sysPets);
    }

    public void add(SysPet sysPet){
        this.sysPets.add(sysPet);
    }

    @Override
    public int getCount() {
        return this.sysPets.size();
    }

    @Override
    public Object getItem(int i) {
        return this.sysPets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.sysPets.get(i).getPetId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_pet_view, null);
        }
        btnCenter = convertView.findViewById(R.id.btnCenter);
        txtPetName = convertView.findViewById(R.id.txtPetName);
        txtPetType = convertView.findViewById(R.id.txtPetType);
        btnInfo = convertView.findViewById(R.id.btnInfo);
        btnDelete = convertView.findViewById(R.id.btnDelete);
        btnMap = convertView.findViewById(R.id.btnMap);
        txtPetName.setText(sysPets.get(position).getPetName());
        txtPetType.setText(sysPets.get(position).getPetType());
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), PetActivity.class);
                intent.putExtra("petId",sysPets.get(position).getPetId());
                v.getContext().startActivity(intent);
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), MapActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder normalDialog = new AlertDialog.Builder(v.getContext());
                normalDialog.setTitle("删除宠物");
                normalDialog.setMessage("确定删除宠物?");
                normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog = DialogUtils.dialogloading(PetListViewAdapter.this.context, "请稍等...", false, false);
                        netClient = new NetClient();
                        NetRequest request = new NetRequest(ApiConfig.API_DELETE_PET,"post").addHeader("userId", App.getMember(PetListViewAdapter.this.context).getMemberId()+"").addParam("petId",sysPets.get(position).getPetId()+"");
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
                                    ToastUtil.show(PetListViewAdapter.this.context,bundle.getString("message"));
                                    if(callBack != null){
                                        callBack.call();
                                    }
                                }else if(msg.what == 1){
                                    ToastUtil.show(PetListViewAdapter.this.context,bundle.getString("message"));
                                }else{
                                    ToastUtil.show(PetListViewAdapter.this.context,bundle.getString("message"));
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
        btnCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到宠物中心
                Intent intent=new Intent(view.getContext(), PetCenterActivity.class);
                intent.putExtra("petId",sysPets.get(position).getPetId());
                view.getContext().startActivity(intent);
            }
        });
        return convertView;
    }
}
