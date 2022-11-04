package com.example.petclient.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.petclient.R;
import com.example.petclient.base.entry.SysNotify;

import java.text.SimpleDateFormat;
import java.util.List;

public class NotifyMessageAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    TextView txtTitle;
    TextView lblNote;
    TextView txtCreateTime;


    Context context;

    public NotifyMessageAdapter(Context context, List<SysNotify> sysNotifies){
        this.context = context;
        this.sysNotifies = sysNotifies;
        this.mInflater = LayoutInflater.from(context);
    }

    private List<SysNotify> sysNotifies;

    public void Clean(){
        this.sysNotifies.clear();
    }

    public void add(List<SysNotify> sysNotifies){
        this.sysNotifies.addAll(sysNotifies);
    }

    public void add(SysNotify sysNotifies){
        this.sysNotifies.add(sysNotifies);
    }

    @Override
    public int getCount() {
        return sysNotifies.size();
    }

    @Override
    public Object getItem(int i) {
        return sysNotifies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return sysNotifies.get(i).getNotifyId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_message_view, null);
        }
        txtTitle = convertView.findViewById(R.id.txtTitle);
        lblNote = convertView.findViewById(R.id.lblNote);
        txtCreateTime = convertView.findViewById(R.id.txtCreateTime);
        String date =
                new SimpleDateFormat("yyyy-MM-dd").format(sysNotifies.get(position).getCreateTime());
        txtTitle.setText(sysNotifies.get(position).getTitle());
        lblNote.setText(sysNotifies.get(position).getMessage());
        txtCreateTime.setText(date);
        return convertView;
    }
}
