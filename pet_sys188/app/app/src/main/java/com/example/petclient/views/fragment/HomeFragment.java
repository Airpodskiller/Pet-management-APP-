package com.example.petclient.views.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.petclient.App;
import com.example.petclient.R;
import com.example.petclient.base.BaseFragment;
import com.example.petclient.base.entry.SysMember;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.SysMemberData;
import com.example.petclient.views.activity.HomeActivity;
import com.example.petclient.views.activity.InfoActivity;
import com.example.petclient.views.activity.MessageActivity;
import com.example.petclient.views.activity.PasswordActivity;
import com.example.petclient.views.activity.RechargeActivity;
import com.example.petclient.views.activity.RechargeOrderActivity;
import com.example.petclient.views.activity.ShopOrderActivity;

import recycleview.huanglinqing.com.dialogutils.DialogUtils;

public class HomeFragment extends BaseFragment {
    HomeActivity activity;
    public HomeFragment(HomeActivity activity){
        this.activity = activity;
    }
    ImageView images_head;
    LinearLayout lnVipState;
    LinearLayout lbRecord;
    LinearLayout lnRecharge;

    LinearLayout menuMimeInfo;
    LinearLayout menuShopOrder;
    LinearLayout menuMimeMessage;
    LinearLayout menuMimePassword;

    TextView txtAccount;

    Button btn_quit;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        //绑定事件
        images_head = view.findViewById(R.id.images_head);
        images_head.bringToFront();
        lnVipState = view.findViewById(R.id.lnVipState);
        lbRecord = view.findViewById(R.id.lbRecord);
        lnRecharge = view.findViewById(R.id.lnRecharge);

        menuMimeInfo = view.findViewById(R.id.menuMimeInfo);
        menuShopOrder = view.findViewById(R.id.menuShopOrder);
        menuMimeMessage = view.findViewById(R.id.menuMimeMessage);
        menuMimePassword = view.findViewById(R.id.menuMimePassword);

        //点击查询
        lnVipState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfo();
            }
        });

        //充值记录
        lbRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //充值记录
                Intent intent=new Intent(view.getContext(), RechargeOrderActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        //充值界面
        lnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //充值页面
                Intent intent=new Intent(view.getContext(), RechargeActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        //中间的四个按钮
        menuMimeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), InfoActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        menuShopOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //自己的订单记录
                Intent intent=new Intent(view.getContext(), ShopOrderActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        //点击查看消息
        menuMimeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), MessageActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        menuMimePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), PasswordActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        txtAccount = view.findViewById(R.id.txtAccount);
        SysMember sysMember = App.getMember(this.getContext());
        txtAccount.setText(sysMember.getUserName());
        btn_quit = view.findViewById(R.id.btn_quit);
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.LoginOut(view.getContext());
                activity.finish();
            }
        });


        return view;
    }
    SysMember sysMember;
    private void userInfo(){
        AlertDialog alertDialog = DialogUtils.dialogloading(this.getContext(), "请稍等...", false, false);
        NetClient netClient = new NetClient();
        NetRequest request = new NetRequest(ApiConfig.API_MIME_INFO,"post").addHeader("userId", App.getMember(HomeFragment.this.getContext()).getMemberId()+"");
        netClient.Request(request).Callback(new NetJsonCallBack<SysMemberData>(SysMemberData.class) {
            @Override
            public void success(SysMemberData data) {
                if(data.getState() == 0){
                    HomeFragment.this.sysMember = data.getData();
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
                    //保存token，账号和密码
                    if(HomeFragment.this.sysMember.getMemberVip() != null && HomeFragment.this.sysMember.getMemberVip() > 0){
                        ToastUtil.showLong(HomeFragment.this.getContext(),sysMember.getVipName());
                    }else {
                        ToastUtil.show(HomeFragment.this.getContext(),"您还不是会员哦~");
                    }
                }else if(msg.what == 1){
                    ToastUtil.show(HomeFragment.this.getContext(),bundle.getString("message"));
                }else{
                    ToastUtil.show(HomeFragment.this.getContext(),bundle.getString("message"));
                }
                alertDialog.dismiss();
            }
        });
        netClient.Http();
    }
}
