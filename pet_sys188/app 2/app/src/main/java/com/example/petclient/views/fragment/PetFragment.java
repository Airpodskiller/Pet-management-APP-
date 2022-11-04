package com.example.petclient.views.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.petclient.App;
import com.example.petclient.R;
import com.example.petclient.base.BaseFragment;
import com.example.petclient.base.adapter.PetListViewAdapter;
import com.example.petclient.base.entry.SysPet;
import com.example.petclient.utils.CallBack;
import com.example.petclient.utils.ListPageEvent;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.SysPetListData;
import com.example.petclient.views.activity.PetActivity;

import java.util.ArrayList;
import java.util.List;

public class PetFragment extends BaseFragment {

    SwipeRefreshLayout Main_srLayoutNewsList;
    ListView all_friend;
    NetClient netClient;
    List<SysPet> pets;
    PetListViewAdapter adapter;
    Button btnSearch;
    Button btnAdd;
    EditText txtSearchName;

    ListPageEvent listPageEvent;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        adapter = new PetListViewAdapter(this.getContext(),new ArrayList<SysPet>(),PetFragment.this);
        Main_srLayoutNewsList = view.findViewById(R.id.Main_srLayoutNewsList);
        all_friend = view.findViewById(R.id.all_friend);
        txtSearchName = view.findViewById(R.id.txtSearchName);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnAdd = view.findViewById(R.id.btnAdd);
        all_friend.setAdapter(adapter);
        netClient = new NetClient();
        Main_srLayoutNewsList.setRefreshing(true);
        NetRequest request = new NetRequest(ApiConfig.API_PET_LIST,"get").addHeader("userId", App.getMember(getContext()).getMemberId()+"");
        netClient.Request(request).Callback(new NetJsonCallBack<SysPetListData>(SysPetListData.class) {
            @Override
            public void success(SysPetListData data) {
                if(data.getState() == 0){
                    Message message = new Message();
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("message",data.getContent());
                    message.setData(bundle);
                    PetFragment.this.pets = data.getData();
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
                    adapter.add(PetFragment.this.pets);
                    adapter.notifyDataSetChanged();
                }else if(msg.what == 1){
                    ToastUtil.show(PetFragment.this.getContext(),bundle.getString("message"));
                }else{
                    ToastUtil.show(PetFragment.this.getContext(),bundle.getString("message"));
                }

                Main_srLayoutNewsList.setRefreshing(false);
            }
        });
        listPageEvent = new ListPageEvent(this.getContext(),all_friend) {
            @Override
            public void Call(int pageIndex, int pageSize) {
                request.addParam("pageIndex",pageIndex+"")
                        .addParam("length",pageSize+"");
                netClient.Http();
            }
        };
        listPageEvent.refresh();
        adapter.Call(new CallBack() {
            @Override
            public void call() {
                adapter.Clean();
                listPageEvent.refresh();
            }
        });
        //SwipeRefreshLayout功能介绍
        Main_srLayoutNewsList.setColorSchemeResources(R.color.white);

        Main_srLayoutNewsList.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(PetFragment.this.getContext(),R.color.colorPrimaryDark));
        Main_srLayoutNewsList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.Clean();
                listPageEvent.refresh();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netClient.Request().addParam("petName",txtSearchName.getText().toString());
                netClient.Http();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击添加
                Intent intent=new Intent(view.getContext(), PetActivity.class);
                intent.putExtra("petId",-1);
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }
}
