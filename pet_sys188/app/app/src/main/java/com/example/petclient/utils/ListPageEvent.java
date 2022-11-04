package com.example.petclient.utils;

import android.content.Context;
import android.widget.ListView;

/**
 * 下拉翻页的操作
 */
public abstract class ListPageEvent {
    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean hasNext = true;
    private Context context;
    private ListView listView;
    public ListPageEvent(Context context,ListView listView){
        this.context = context;
        this.listView = listView;
        //listview添加滑动到底部事件
        this.listView.setOnScrollListener(new ScrollBottomEvent(new CallBack() {
            @Override
            public void call() {
                next();
            }
        }));
    }

    public void refresh(){
        //刷新
        this.pageIndex = 1;
        this.hasNext = true;
        this.Call(this.pageIndex,this.pageSize);
    }

    public void next(){
        if(hasNext){
            this.pageIndex++;
            this.Call(this.pageIndex,this.pageSize);
        }else {
            ToastUtil.show(context,"没有更多数据~");
        }
    }

    public void isOver(int data){
        if(data < this.pageSize){
            this.hasNext = false;
        }
    }

    public abstract void Call(int pageIndex,int pageSize);
}
