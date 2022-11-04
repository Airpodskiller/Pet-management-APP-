package com.example.petclient.views.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petclient.R;
import com.example.petclient.base.BaseFragment;
import com.example.petclient.views.activity.ShopActivity;

public class ShopFragment extends BaseFragment {
    Button btnCatShop;
    Button btnDogShop;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shop;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        btnDogShop = view.findViewById(R.id.btnDogShop);
        btnCatShop = view.findViewById(R.id.btnCatShop);

        btnDogShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), ShopActivity.class);
                intent.putExtra("petType",1);
                view.getContext().startActivity(intent);
            }
        });

        btnCatShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), ShopActivity.class);
                intent.putExtra("petType",0);
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }
}
