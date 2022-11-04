package com.example.petclient.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.petclient.R;
import com.example.petclient.base.entry.SysPetRechargeOrder;

import java.util.List;

public class RechargeOrderListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    TextView txtMoneys;

    Context context;

    public RechargeOrderListAdapter(Context context, List<SysPetRechargeOrder> sysPetRechargeOrders){
        this.context = context;
        this.sysPetRechargeOrders = sysPetRechargeOrders;
        this.mInflater = LayoutInflater.from(context);
    }

    private List<SysPetRechargeOrder> sysPetRechargeOrders;

    public void Clean(){
        this.sysPetRechargeOrders.clear();
    }

    public void add(List<SysPetRechargeOrder> sysPetRechargeOrders){
        this.sysPetRechargeOrders.addAll(sysPetRechargeOrders);
    }

    public void add(SysPetRechargeOrder sysPetRechargeOrders){
        this.sysPetRechargeOrders.add(sysPetRechargeOrders);
    }

    @Override
    public int getCount() {
        return sysPetRechargeOrders.size();
    }

    @Override
    public Object getItem(int i) {
        return sysPetRechargeOrders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return sysPetRechargeOrders.get(i).getPetRechargeOrderId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_recharge_order, null);
        }
        txtMoneys = convertView.findViewById(R.id.txtMoneys);
        txtMoneys.setText("￥"+this.sysPetRechargeOrders.get(position).getMoneys().toString());
        return convertView;
    }
}