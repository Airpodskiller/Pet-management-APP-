package com.example.petclient.views.activity;

import android.os.Bundle;
import android.os.Message;
import android.widget.ListView;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.petclient.App;
import com.example.petclient.R;
import com.example.petclient.base.BaseActivity;
import com.example.petclient.base.adapter.ShopOrderListAdapter;
import com.example.petclient.base.entry.SysPetOrder;
import com.example.petclient.utils.ListPageEvent;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.SysPetOrderListData;

import java.util.ArrayList;
import java.util.List;

public class ShopOrderActivity extends BaseActivity {
    SwipeRefreshLayout Main_srLayoutNewsList;
    ListView all_list;
    NetClient netClient;
    List<SysPetOrder> sysPetOrders;
    ShopOrderListAdapter adapter;
    ListPageEvent listPageEvent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_order;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定事件
        adapter = new ShopOrderListAdapter(this,new ArrayList<SysPetOrder>());
        Main_srLayoutNewsList = findViewById(R.id.Main_srLayoutNewsList);
        all_list = findViewById(R.id.all_list);
        all_list.setAdapter(adapter);
        netClient = new NetClient();
        Main_srLayoutNewsList.setRefreshing(true);
        NetRequest request = new NetRequest(ApiConfig.API_SHOP_ORDER_LIST,"get").addHeader("userId", App.getMember(this).getMemberId()+"");
        netClient.Request(request).Callback(new NetJsonCallBack<SysPetOrderListData>(SysPetOrderListData.class) {
            @Override
            public void success(SysPetOrderListData data) {
                if(data.getState() == 0){
                    Message message = new Message();
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("message",data.getContent());
                    message.setData(bundle);
                    ShopOrderActivity.this.sysPetOrders = data.getData();
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
                    listPageEvent.isOver(ShopOrderActivity.this.sysPetOrders.size());
                    adapter.add(ShopOrderActivity.this.sysPetOrders);
                    adapter.notifyDataSetChanged();
                }else if(msg.what == 1){
                    ToastUtil.show(ShopOrderActivity.this,bundle.getString("message"));
                }else{
                    ToastUtil.show(ShopOrderActivity.this,bundle.getString("message"));
                }

                Main_srLayoutNewsList.setRefreshing(false);
            }
        });
        listPageEvent = new ListPageEvent(this,all_list) {
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

        Main_srLayoutNewsList.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(ShopOrderActivity.this,R.color.colorPrimaryDark));
        Main_srLayoutNewsList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.Clean();
                listPageEvent.refresh();
            }
        });
    }
}
