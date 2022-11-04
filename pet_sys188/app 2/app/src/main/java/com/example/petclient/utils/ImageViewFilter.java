package com.example.petclient.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.petclient.utils.http.NetConfig;

/**
 * 后台图片的填充
 */
public class ImageViewFilter {
    private ImageView imageView;
    private String url;
    public ImageViewFilter(ImageView imageView,String url){
        this.imageView = imageView;
        this.url = url;
    }

    public void filter(){
        try{
            Glide.with(imageView).load(NetConfig.api +url).into(imageView);//边框为圆形
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
