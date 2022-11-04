package com.example.petclient.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.petclient.R;
import com.example.petclient.base.entry.SysPetSterilization;

import java.text.SimpleDateFormat;
import java.util.List;

public class PetSterilizationListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    TextView txtPetName;
    TextView txtStatus;
    TextView lblTime;
    TextView txtCreateTime;

    LinearLayout lnInfo;
    TextView lblNote;
    Context context;
    List<SysPetSterilization> sysPetSterilizations;

    public PetSterilizationListAdapter(Context context, List<SysPetSterilization> sysPetSterilizations){
        this.context = context;
        this.sysPetSterilizations = sysPetSterilizations;
        this.mInflater = LayoutInflater.from(context);
    }

    public void Clean(){
        this.sysPetSterilizations.clear();
    }

    public void add(List<SysPetSterilization> sysPetSterilizations){
        this.sysPetSterilizations.addAll(sysPetSterilizations);
    }

    public void add(SysPetSterilization sysPetSterilizations){
        this.sysPetSterilizations.add(sysPetSterilizations);
    }

    @Override
    public int getCount() {
        return this.sysPetSterilizations.size();
    }

    @Override
    public Object getItem(int i) {
        return this.sysPetSterilizations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.sysPetSterilizations.get(i).getPetId();
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

        if(sysPetSterilizations.get(position).getIsOver() == 0){
            lblTime.setText("预计时间：");
            txtCreateTime.setText("--");
            txtStatus.setText("等待绝育");
        }else if(sysPetSterilizations.get(position).getIsOver() == 1){
            lblTime.setText("开始时间：");
            String date =
                    new SimpleDateFormat("yyyy-MM-dd").format(sysPetSterilizations.get(position).getCreateTime());
            txtCreateTime.setText(date);
            lnInfo.setVisibility(View.VISIBLE);
            lblNote.setText("预计完成时间："+sysPetSterilizations.get(position).getTime());
            txtStatus.setText("正在绝育");
        }else {
            lblTime.setText("完成时间：");
            String date =
                    new SimpleDateFormat("yyyy-MM-dd").format(sysPetSterilizations.get(position).getCreateTime());
            txtCreateTime.setText(date);
            txtStatus.setText("完成绝育");
            lnInfo.setVisibility(View.VISIBLE);
            lblNote.setText("记得去领取宠物哦~");
        }
        txtPetName.setText(sysPetSterilizations.get(position).getPetName());
        return convertView;
    }
}
