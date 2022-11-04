package com.example.petclient.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.petclient.R;
import com.example.petclient.base.entry.SysPetIll;

import java.text.SimpleDateFormat;
import java.util.List;

public class PetIllAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    TextView txtPetName;
    TextView txtStatus;
    TextView lblTime;
    TextView txtCreateTime;

    LinearLayout lnInfo;
    TextView lblNote;


    Context context;

    public PetIllAdapter(Context context, List<SysPetIll> sysPetIlls){
        this.context = context;
        this.sysPetIlls = sysPetIlls;
        this.mInflater = LayoutInflater.from(context);
    }

    private List<SysPetIll> sysPetIlls;

    public void Clean(){
        this.sysPetIlls.clear();
    }

    public void add(List<SysPetIll> sysPetIlls){
        this.sysPetIlls.addAll(sysPetIlls);
    }

    public void add(SysPetIll sysPetIlls){
        this.sysPetIlls.add(sysPetIlls);
    }

    @Override
    public int getCount() {
        return sysPetIlls.size();
    }

    @Override
    public Object getItem(int i) {
        return sysPetIlls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return sysPetIlls.get(i).getPetIllId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_info_view, null);
        }
        txtPetName = convertView.findViewById(R.id.txtPetName);
        txtStatus = convertView.findViewById(R.id.txtStatus);
        txtCreateTime = convertView.findViewById(R.id.txtCreateTime);
        lblNote = convertView.findViewById(R.id.lblNote);
        lblTime = convertView.findViewById(R.id.lblTime);

        lnInfo = convertView.findViewById(R.id.lnInfo);
        lnInfo.setVisibility(View.VISIBLE);
        lblTime.setText("完成时间：");
        if(sysPetIlls.get(position).getIsOver() == 1){
            String date =
                    new SimpleDateFormat("yyyy-MM-dd").format(sysPetIlls.get(position).getCreateTime());

            txtCreateTime.setText(date);
        }else {
            txtCreateTime.setText("--");
        }
        txtStatus.setText(sysPetIlls.get(position).getIsOver() == 0 ? "等待治疗" : "完成治疗");
        txtPetName.setText(sysPetIlls.get(position).getPetName());
        lblNote.setText(sysPetIlls.get(position).getIllDetail());
        return convertView;
    }
}
