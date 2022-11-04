package com.example.petclient.base.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.petclient.App;
import com.example.petclient.R;
import com.example.petclient.base.entry.SysPetShop;
import com.example.petclient.utils.ImageViewFilter;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.BaseResponseData;
import com.example.petclient.views.widget.ConfirmDialog;

import java.util.List;

import recycleview.huanglinqing.com.dialogutils.DialogUtils;

public class ShopListViewAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    TextView txtShopName;
    TextView txtShopPrice;
    ImageView imgShop;
    Button btnShop;
    TextView txtShopVipPrice;

    Context context;
    NetClient netClient;
    AlertDialog alertDialog;

    public ShopListViewAdapter(Context context, List<SysPetShop> sysPetShops){
        this.context = context;
        this.sysPetShops = sysPetShops;
        this.mInflater = LayoutInflater.from(context);
    }

    private List<SysPetShop> sysPetShops;

    public void Clean(){
        this.sysPetShops.clear();
    }

    public void add(List<SysPetShop> sysPetShops){
        this.sysPetShops.addAll(sysPetShops);
    }

    public void add(SysPetShop sysPetShops){
        this.sysPetShops.add(sysPetShops);
    }

    @Override
    public int getCount() {
        return sysPetShops.size();
    }

    @Override
    public Object getItem(int i) {
        return sysPetShops.get(i);
    }

    @Override
    public long getItemId(int i) {
        return sysPetShops.get(i).getPetShopId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_shop_view, null);
        }
        txtShopName = convertView.findViewById(R.id.txtShopName);
        txtShopPrice = convertView.findViewById(R.id.txtShopPrice);
        btnShop = convertView.findViewById(R.id.btnShop);
        imgShop = convertView.findViewById(R.id.imgShop);
        txtShopVipPrice = convertView.findViewById(R.id.txtShopVipPrice);
        if(sysPetShops.get(position).getVipPrice() > 0){
            txtShopVipPrice.setVisibility(View.VISIBLE);
            txtShopVipPrice.setText("Vip 价格:￥"+sysPetShops.get(position).getVipPrice());
        }
        txtShopPrice.setText("价格：￥"+this.sysPetShops.get(position).getMoneys().toString());
        txtShopName.setText(this.sysPetShops.get(position).getShopName());
        new ImageViewFilter(imgShop,this.sysPetShops.get(position).getImage()).filter();
        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击购买
                ConfirmDialog confirmDialog = new ConfirmDialog(view.getContext());
                confirmDialog.setTitle("购买商品提示");
                confirmDialog.setMessage("是否购买商品【"+sysPetShops.get(position).getShopName()+"】?");
                confirmDialog.setNoOnclickListener("取消", new ConfirmDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        //请求
                        confirmDialog.dismiss();
                    }
                });
                confirmDialog.setYesOnclickListener("确定", new ConfirmDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        confirmDialog.dismiss();
                        //请求
                        alertDialog = DialogUtils.dialogloading(ShopListViewAdapter.this.context, "请稍等...", false, false);
                        netClient = new NetClient();
                        NetRequest request = new NetRequest(ApiConfig.API_BUY_SHOP,"post")
                                .addHeader("userId", App.getMember(ShopListViewAdapter.this.context).getMemberId()+"").addParam("shopId",sysPetShops.get(position).getPetShopId()+"");
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
                                    ToastUtil.show(ShopListViewAdapter.this.context,bundle.getString("message"));
                                }else if(msg.what == 1){
                                    ToastUtil.show(ShopListViewAdapter.this.context,bundle.getString("message"));
                                }else{
                                    ToastUtil.show(ShopListViewAdapter.this.context,bundle.getString("message"));
                                }
                                alertDialog.dismiss();
                            }
                        });
                        netClient.Http();
                    }
                });
                confirmDialog.show();
            }
        });
        return convertView;
    }
}
