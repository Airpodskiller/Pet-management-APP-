package com.example.petclient.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.petclient.App;
import com.example.petclient.R;
import com.example.petclient.base.BaseActivity;
import com.example.petclient.base.adapter.ShopListViewAdapter;
import com.example.petclient.base.entry.SysPetShop;
import com.example.petclient.utils.ListPageEvent;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.SysPetShopListData;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends BaseActivity {
    TextView txtTitle;
    SwipeRefreshLayout Main_srLayoutNewsList;
    ListView all_shop;
    NetClient netClient;
    NetRequest request;
    List<SysPetShop> sysPetShops;
    ShopListViewAdapter adapter;
    ListPageEvent listPageEvent;
    int petType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        petType = intent.getIntExtra("petType",0);
        txtTitle = findViewById(R.id.txtTitle);
        if(petType == 0){
            txtTitle.setText("猫猫商城");
        }else {
            txtTitle.setText("狗狗商城");
        }
        Main_srLayoutNewsList = findViewById(R.id.Main_srLayoutNewsList);
        all_shop = findViewById(R.id.all_shop);
        adapter = new ShopListViewAdapter(this,new ArrayList<SysPetShop>());
        all_shop.setAdapter(adapter);
        netClient = new NetClient();
        Main_srLayoutNewsList.setRefreshing(true);
        request = new NetRequest(ApiConfig.API_SHOP_LIST,"get").addParam("petType",petType+"")
                .addHeader("userId", App.getMember(ShopActivity.this).getMemberId()+"");
        netClient.Request(request).Callback(new NetJsonCallBack<SysPetShopListData>(SysPetShopListData.class) {
            @Override
            public void success(SysPetShopListData data) {
                if(data.getState() == 0){
                    Message message = new Message();
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("message",data.getContent());
                    message.setData(bundle);
                    ShopActivity.this.sysPetShops = data.getData();
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
                    //显示
                    listPageEvent.isOver(sysPetShops.size());
                    adapter.add(ShopActivity.this.sysPetShops);
                    adapter.notifyDataSetChanged();
                }else if(msg.what == 1){
                    ToastUtil.show(ShopActivity.this,bundle.getString("message"));
                }else{
                    ToastUtil.show(ShopActivity.this,bundle.getString("message"));
                }

                Main_srLayoutNewsList.setRefreshing(false);
            }
        });
        listPageEvent = new ListPageEvent(this,all_shop) {
            @Override
            public void Call(int pageIndex, int pageSize) {
                request.addParam("pageIndex",pageIndex+"")
                        .addParam("length",pageSize+"");
                netClient.Http();
            }
        };
        listPageEvent.refresh();
        //SwipeRefreshLayout功能介绍
        Main_srLayoutNewsList.setColorSchemeResources(R.color.white);

        Main_srLayoutNewsList.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(ShopActivity.this,R.color.colorPrimaryDark));
        Main_srLayoutNewsList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.Clean();
                listPageEvent.refresh();
            }
        });
    }
}
