package com.example.petclient.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.petclient.R;
import com.example.petclient.base.entry.SysPetVaccineConfig;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

public class VaccineListTagAdapter extends TagAdapter<SysPetVaccineConfig> {
    private Context context;
    LayoutInflater mInflater;
    List<SysPetVaccineConfig> sysPetVaccineConfigs;
    TagFlowLayout tagFlowLayout;
    public VaccineListTagAdapter(TagFlowLayout tagFlowLayout, Context context, List<SysPetVaccineConfig> sysPetVaccineConfigs) {
        super(sysPetVaccineConfigs);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.sysPetVaccineConfigs = sysPetVaccineConfigs;
        this.tagFlowLayout = tagFlowLayout;
    }

    public void Clean(){
        this.sysPetVaccineConfigs.clear();
    }

    public void add(List<SysPetVaccineConfig> sysPetVaccineConfigs){
        this.sysPetVaccineConfigs.addAll(sysPetVaccineConfigs);
    }

    public List<SysPetVaccineConfig> getSysPetVaccineConfigs() {
        return sysPetVaccineConfigs;
    }

    public void add(SysPetVaccineConfig sysPetVaccineConfigs){
        this.sysPetVaccineConfigs.add(sysPetVaccineConfigs);
    }

    @Override
    public View getView(FlowLayout parent, int position, SysPetVaccineConfig sysPetVaccineConfig) {
        TextView tv = (TextView) mInflater.inflate(R.layout.item_label_view,
                tagFlowLayout, false);
        tv.setText(sysPetVaccineConfig.getName());
        return tv;
    }

    public String selectString(List<Integer> positions){
        String items = "";
        for (int i = 0;i<positions.size();i++){
            if(items.equals("")){
                items += this.sysPetVaccineConfigs.get(positions.get(i)).getPetVaccineConfigId();
            }else {
                items += "," + this.sysPetVaccineConfigs.get(positions.get(i)).getPetVaccineConfigId();
            }
        }
        return items;
    }
}
