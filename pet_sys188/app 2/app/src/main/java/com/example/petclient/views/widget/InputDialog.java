package com.example.petclient.views.widget;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.text.InputFilter;
import android.widget.EditText;

/**
 * 输入框的弹窗
 */
public abstract class InputDialog {
    private Context context;
    private EditText inputServer;
    public InputDialog(Context context){
        this.context = context;
    }

    /**
     * 输入的回调
     * @param value
     */
    public abstract void Call(String value);

    /**
     * 显示输入框
     * @param title
     */
    public void show(String title){
        inputServer = new EditText(context);
        inputServer.setHeight(500);
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String value = inputServer.getText().toString();
                Call(value);
            }
        });
        builder.show();
    }
}
