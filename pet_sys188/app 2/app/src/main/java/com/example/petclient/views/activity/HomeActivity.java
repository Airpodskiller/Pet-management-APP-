package com.example.petclient.views.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.example.petclient.App;
import com.example.petclient.R;
import com.example.petclient.base.BaseActivity;
import com.example.petclient.base.entry.SysNotify;
import com.example.petclient.utils.ToastUtil;
import com.example.petclient.utils.http.ApiConfig;
import com.example.petclient.utils.http.NetClient;
import com.example.petclient.utils.http.NetJsonCallBack;
import com.example.petclient.utils.http.NetRequest;
import com.example.petclient.utils.http.entry.SysNotifyListData;
import com.example.petclient.views.fragment.HomeFragment;
import com.example.petclient.views.fragment.PetFragment;
import com.example.petclient.views.fragment.ShopFragment;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 主页
 */
public class HomeActivity extends BaseActivity {
    private SparseArray<Fragment> mFragments;
    Fragment current;
    FrameLayout fragment;
    RadioGroup mTabRadioGroup;
    private List<SysNotify> sysNotifies;
    private void init(){
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    //do something
                    Log.e("请求通知","");
                    notifyData();
                }
                super.handleMessage(msg);
            }
        };

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };

        //主线程中调用：
        timer.schedule(timerTask, 1000, 1000);//延时1s，每隔500毫秒执行一次run方法
    }

    private void notifyData(){
        //发送请求
        NetClient netClient = new NetClient();
        NetRequest request = new NetRequest(ApiConfig.API_NOTIFY_MESSAGE,"get").addHeader("userId", App.getMember(HomeActivity.this).getMemberId()+"");
        netClient.Request(request).Callback(new NetJsonCallBack<SysNotifyListData>(SysNotifyListData.class) {
            @Override
            public void success(SysNotifyListData data) {
                if(data.getState() == 0){
                    Message message = new Message();
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("message",data.getContent());
                    message.setData(bundle);
                    HomeActivity.this.sysNotifies = data.getData();
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
                    if(HomeActivity.this.sysNotifies != null && HomeActivity.this.sysNotifies.size() > 0){
                        //消息通知
                        Context context=getApplicationContext();
                        String channelId = "petApp";
                        Notification notification = new Notification.Builder(context,channelId)
                                .setContentTitle(sysNotifies.get(0).getTitle())
                                .setContentText(sysNotifies.get(0).getMessage())
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.icon_logo)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.icon_logo))   //设置大图标
                                .build();
                        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
                        NotificationChannel channel = new NotificationChannel(channelId,"petApp", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel(channel);
                        notificationManager.notify(sysNotifies.get(0).getNotifyId().intValue(), notification);
                    }
                }else if(msg.what == 1){
                    ToastUtil.show(HomeActivity.this,bundle.getString("message"));
                }else{
                    ToastUtil.show(HomeActivity.this,bundle.getString("message"));
                }
            }
        });
        netClient.Http();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = findViewById(R.id.fragment_vp);
        mTabRadioGroup = findViewById(R.id.tabs_rg);
        mFragments = new SparseArray<>();
        mFragments.append(R.id.pet_tab,new PetFragment());
        mFragments.append(R.id.shop_tab,new ShopFragment());
        mFragments.append(R.id.home_tab,new HomeFragment(this));
        current = mFragments.get(R.id.pet_tab);
        mTabRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 具体的fragment切换逻辑可以根据应用调整，例如使用show()/hide()
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_vp,
                        mFragments.get(checkedId)).commit();
                current = mFragments.get(checkedId);
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_vp,
                mFragments.get(R.id.pet_tab)).commit();
        init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }
}
