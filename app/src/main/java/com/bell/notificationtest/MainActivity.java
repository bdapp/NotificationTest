package com.bell.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private Button btn1, btn2, btn3, btn4, btn5, btn6;

    private NotificationManager notifyManager;

    final String INTENT_BROAD = "broadcast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initUI();

        notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_BROAD);
        registerReceiver(broadcastReceiver, filter);

    }


    private void initUI() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
        btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(this);
        btn5 = (Button) findViewById(R.id.btn5);
        btn5.setOnClickListener(this);
        btn6 = (Button) findViewById(R.id.btn6);
        btn6.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn1:
                notify1();
                break;

            case R.id.btn2:
                notify2();
                break;

            case R.id.btn3:
                notify3();
                break;

            case R.id.btn4:
                notify4();
                break;

            case R.id.btn5:
                notify5();
                break;

            case R.id.btn6:
                notify6();
                break;

            default:
                break;
        }
    }



    /**
     * 普通Notification
     */
    private void notify1() {

        Intent intent = new Intent(mContext, SecondActivity.class);
        PendingIntent pendIntent = PendingIntent.getActivity(mContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setContentIntent(pendIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.h));
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("这是标题111");
        builder.setContentText("这是内容11111111\n111111\n111");
        builder.setTicker("状态栏显示111111");
        builder.setOngoing(true);   //不可侧滑消失

        Notification notify = builder.build();
        notify.flags = Notification.FLAG_AUTO_CANCEL;   //单击后通知消失
        notifyManager.notify(0, notify);

    }


    /**
     * 自定义Notification（3.0之前不支持button）
     */
    private void notify2() {

        Intent intent = new Intent(INTENT_BROAD);
        //配置跳转广播
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 1, intent, PendingIntent
                .FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.my_notify);
        remoteViews.setTextViewText(R.id.but_form, "待点击");
        remoteViews.setTextViewText(R.id.tv_form, "重置的内容");
        //Button点击响应
        remoteViews.setOnClickPendingIntent(R.id.but_form, pendingIntent);

        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setContent(remoteViews);                //加载布局
        builder.setContentIntent(pendingIntent);        //点击整行跳转
        builder.setSmallIcon(R.mipmap.ic_launcher);     //状态栏小图标
        builder.setTicker("状态栏显示2222222");           //状态栏通知

        Notification notify = builder.build();
        notify.flags = Notification.FLAG_AUTO_CANCEL;   //单击后通知消失
        notifyManager.notify(0, notify);
    }


    private void notify3() {
        final Notification.Builder builder = new Notification.Builder(mContext);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.h));
        builder.setTicker("状态通知3333333333");
        builder.setContentTitle("通知标题33");
        builder.setContentText("通知内容33333333\n333333333\n3333333333\n32");  //不支持格式化
        builder.setOngoing(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i += 5) {
                    builder.setProgress(100, i, false);
                    notifyManager.notify(0, builder.build());
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }

                builder.setContentText("下载完成");
                builder.setProgress(0, 0, false);
//                builder.setOngoing(false);

                Notification notify = builder.build();
                notify.flags = Notification.FLAG_AUTO_CANCEL;
                notifyManager.notify(0, notify);
            }
        }).start();
    }


    /**
     * 大布局格式 BigTextStyle
     *      5.1及以上支持
     */
    private void notify4(){
        Notification.BigTextStyle textStyle = new Notification.BigTextStyle();
        textStyle.setBigContentTitle("通知44大标题");
        textStyle.setSummaryText("二级大标题？？4444\n444444\n444");   //不支持格式化
        textStyle.bigText("通知444444内容\n444444444\n444444\n4444\n44");   //支持格式化

        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.h));
        builder.setTicker("状态通知4444444");
        builder.setContentTitle("通知标题44");  // 5.1以下显示
        builder.setContentText("通知内容444");  // 不支持格式化, 5.1以下显示
        builder.setOngoing(true);
        builder.setStyle(textStyle);
        builder.setAutoCancel(true);

        Notification notify = builder.build();
        notify.flags = Notification.FLAG_AUTO_CANCEL;
        notifyManager.notify(0, notify);

    }


    /**
     * 大布局图片格式 BigPictureStyle
     *      5.1及以上支持
     */
    private void notify5() {

        Notification.BigPictureStyle picStyle = new Notification.BigPictureStyle();
        picStyle.setBigContentTitle("通知55大标题");
        picStyle.setSummaryText("底部文字描述555555");
        picStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.big2));

        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.h));
        builder.setTicker("状态通知55555");
        builder.setContentTitle("通知标题555"); // 5.1以下显示
        builder.setContentText("通知内容55");   // 不支持格式化, 5.1以下显示
        builder.setOngoing(true);
        builder.setStyle(picStyle);
        builder.setAutoCancel(true);

        Notification notify = builder.build();
        notify.flags = Notification.FLAG_AUTO_CANCEL;
        notifyManager.notify(0, notify);

    }


    /**
     * 大布局图片格式
     *      5.1及以上支持
     */
    private void notify6() {

        Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
        inboxStyle.setBigContentTitle("通知6大标题");
        inboxStyle.setSummaryText("底部文字描述666666");
        inboxStyle.addLine("行一一一一一一一一");
        inboxStyle.addLine("行二二二二二二二二");
        inboxStyle.addLine("行三三三三三三三三");

        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.h));
        builder.setTicker("状态通知6666");
        builder.setContentTitle("通知标题666"); // 5.1以下显示
        builder.setContentText("通知内容66");   // 不支持格式化, 5.1以下显示
        builder.setOngoing(true);
        builder.setStyle(inboxStyle);
        builder.setAutoCancel(true);

        Notification notify = builder.build();
        notify.flags = Notification.FLAG_AUTO_CANCEL;
        notifyManager.notify(0, notify);
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(INTENT_BROAD)) {
                Intent intent1 = new Intent(mContext, SecondActivity.class);
                startActivity(intent1);
                Toast.makeText(mContext, "拦截成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }
}
