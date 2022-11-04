package com.example.petclient.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.petclient.R;
import com.example.petclient.base.entry.SysPetShowerType;
import com.example.petclient.base.entry.SysPetVaccineConfig;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

public class ShowerListTagAdapter extends TagAdapter<SysPetShowerType> {
    private Context context;
    LayoutInflater mInflater;
    List<SysPetShowerType> sysPetShowerTypes;
    TagFlowLayout tagFlowLayout;
    public ShowerListTagAdapter(TagFlowLayout tagFlowLayout, Context context, List<SysPetShowerType> sysPetShowerTypes) {
        super(sysPetShowerTypes);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.sysPetShowerTypes = sysPetShowerTypes;
        this.tagFlowLayout = tagFlowLayout;
    }

    public void Clean(){
        this.sysPetShowerTypes.clear();
    }

    public void add(List<SysPetShowerType> sysPetShowerTypes){
        this.sysPetShowerTypes.addAll(sysPetShowerTypes);
    }

    public void add(SysPetShowerType sysPetShowerTypes){
        this.sysPetShowerTypes.add(sysPetShowerTypes);
    }

    @Override
    public View getView(FlowLayout parent, int position, SysPetShowerType sysPetShowerType) {
        TextView tv = (TextView) mInflater.inflate(R.layout.item_label_view,
                tagFlowLayout, false);
        tv.setText(sysPetShowerType.getPetShowerType());
        return tv;
    }

    public String selectString(List<Integer> positions){
        String items = "";
        for (int i = 0;i<positions.size();i++){
            if(items.equals("")){
                items += this.sysPetShowerTypes.get(positions.get(i)).getPetShowerType();
            }else {
                items += "," + this.sysPetShowerTypes.get(positions.get(i)).getPetShowerType();
            }
        }
        return items;
    }

    /**
     * 查询金额
     * @param positions
     * @return
     */
    public double selectMoneys(List<Integer> positions){
        double moneys = 0;
        for (int i = 0;i<positions.size();i++){
            moneys += this.sysPetShowerTypes.get(positions.get(i)).getMoneys().doubleValue();
        }
        return moneys;
    }
}
