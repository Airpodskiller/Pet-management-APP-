package com.example.petclient.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.petclient.R;
import com.example.petclient.base.entry.SysPetShower;

import java.text.SimpleDateFormat;
import java.util.List;

public class PetShowerListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    TextView txtPetName;
    TextView txtStatus;
    TextView lblTime;
    TextView txtCreateTime;

    LinearLayout lnInfo;
    TextView lblNote;
    Context context;
    List<SysPetShower> sysPetShowers;

    public PetShowerListAdapter(Context context,List<SysPetShower> sysPetShowers){
        this.context = context;
        this.sysPetShowers = sysPetShowers;
        this.mInflater = LayoutInflater.from(context);
    }

    public void Clean(){
        this.sysPetShowers.clear();
    }

    public void add(List<SysPetShower> sysPetShowers){
        this.sysPetShowers.addAll(sysPetShowers);
    }

    public void add(SysPetShower sysPetShowers){
        this.sysPetShowers.add(sysPetShowers);
    }

    @Override
    public int getCount() {
        return this.sysPetShowers.size();
    }

    @Override
    public Object getItem(int i) {
        return this.sysPetShowers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.sysPetShowers.get(i).getPetId();
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

        if(sysPetShowers.get(position).getIsOver() == 0){
            lblTime.setText("预计时间：");
            txtCreateTime.setText("--");
            txtStatus.setText("等待洗澡");
        }else if(sysPetShowers.get(position).getIsOver() == 1){
            lblTime.setText("开始时间：");
            String date =
                    new SimpleDateFormat("yyyy-MM-dd").format(sysPetShowers.get(position).getCreateTime());
            txtCreateTime.setText(date);
            lnInfo.setVisibility(View.VISIBLE);
            lblNote.setText("预计完成时间："+sysPetShowers.get(position).getTime());
            txtStatus.setText("正在洗澡");
        }else {
            lblTime.setText("完成时间：");
            String date =
                    new SimpleDateFormat("yyyy-MM-dd").format(sysPetShowers.get(position).getCreateTime());
            txtCreateTime.setText(date);
            txtStatus.setText("完成洗澡");
            lnInfo.setVisibility(View.VISIBLE);
            lblNote.setText("记得去领取宠物哦~");
        }

        txtPetName.setText(sysPetShowers.get(position).getPetName());
        return convertView;
    }
}
