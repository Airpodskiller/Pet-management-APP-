package com.example.petclient.views.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petclient.App;
import com.example.petclient.R;
import com.example.petclient.base.BaseFragment;
import com.example.petclient.base.entry.SysMember;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.views.activity.HomeActivity;
import com.example.petclient.views.activity.InfoActivity;
import com.example.petclient.views.activity.MessageActivity;
import com.example.petclient.views.activity.PasswordActivity;
import com.example.petclient.views.activity.RechargeActivity;
import com.example.petclient.views.activity.RechargeOrderActivity;
import com.example.petclient.views.activity.ShopOrderActivity;

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
                SysMember sysMember = App.getMember(view.getContext());
                if(sysMember.getVipName() != null){
                    ToastUtil.show(view.getContext(),"您是"+sysMember.getVipName());
                }else {
                    ToastUtil.show(view.getContext(),"您还不是会员哦~");
                }
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
}
