package com.example.petclient.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.petclient.R;
import com.example.petclient.base.entry.SysPetOrder;

import java.util.List;

public class ShopOrderListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    TextView txtOrderCode;
    TextView txtShopName;
    TextView txtMoneys;
    TextView txtStatus;

    Context context;

    public ShopOrderListAdapter(Context context, List<SysPetOrder> sysPetOrders){
        this.context = context;
        this.sysPetOrders = sysPetOrders;
        this.mInflater = LayoutInflater.from(context);
    }

    private List<SysPetOrder> sysPetOrders;

    public void Clean(){
        this.sysPetOrders.clear();
    }

    public void add(List<SysPetOrder> sysPetOrders){
        this.sysPetOrders.addAll(sysPetOrders);
    }

    public void add(SysPetOrder sysPetOrders){
        this.sysPetOrders.add(sysPetOrders);
    }

    @Override
    public int getCount() {
        return sysPetOrders.size();
    }

    @Override
    public Object getItem(int i) {
        return sysPetOrders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return sysPetOrders.get(i).getPetOrderId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_shop_order, null);
        }
        txtOrderCode = convertView.findViewById(R.id.txtOrderCode);
        txtShopName = convertView.findViewById(R.id.txtShopName);
        txtMoneys = convertView.findViewById(R.id.txtMoneys);
        txtStatus = convertView.findViewById(R.id.txtStatus);

        txtMoneys.setText("￥"+this.sysPetOrders.get(position).getMoneys().toString());
        txtOrderCode.setText(this.sysPetOrders.get(position).getOrderCode());
        txtShopName.setText(this.sysPetOrders.get(position).getShopName());
        int status = this.sysPetOrders.get(position).getStatus();
        if(status == 0){
            txtStatus.setText("未付款");
        }else if(status == 1){
            txtStatus.setText("已付款");
        }else {
            txtStatus.setText("已发货");
        }
        return convertView;
    }
}
