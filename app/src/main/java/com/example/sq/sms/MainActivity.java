package com.example.sq.sms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import static android.telephony.SmsMessage.createFromPdu;


public class MainActivity extends Activity implements View.OnClickListener {


    private GoogleApiClient client;

    private EditText DingZhi_Value;
    private EditText  DingZhi_Num;
    private EditText editText;
    private EditText year;
    private EditText month;
    private EditText day;
    private EditText hour;
    private EditText min;
    private EditText sec;

    private TextView order_content;
    private TextView DingZhi;

    private Button Chaxun_time;
    private Button Save_Num;
    private Button Close;
    private Button Chaxun_DingZhi;
    private Button Chakan_DingZhi_dingyi;
    private Button set_DingZhi;
    private Button set_Times;
    private Button check_zhuangtai;

    private String fullMessage;
    private String Num;
    private String Year;
    private String Month;
    private String Day;
    private String Hour;
    private String Min;
    private String Sec;
    private String times;
    private String dingzhi;



    private MessageReceiver messageReceiver;
    private IntentFilter sendFilter;
    private SendStatusReceiver sendStatusReceiver;
    private String dingzhi_value;
    private String dingzhi_Num;
    private Button yufen;
    private Button hezha;
    private Button yuhe;
    private Button fenzha;
    private Button fugui;
    private String Order_content;

    private long firstPressTime = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter receiveFilter = new IntentFilter();
        receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        receiveFilter.setPriority(1000);
        messageReceiver = new MessageReceiver();
        registerReceiver(messageReceiver,receiveFilter);

        sendFilter = new IntentFilter();
        sendFilter.addAction("SEND_SMS_ACTION");
        sendStatusReceiver = new SendStatusReceiver();
        registerReceiver(sendStatusReceiver,sendFilter);

       order_content = (TextView) findViewById(R.id.order_content);
        year = (EditText) findViewById(R.id.year);
        month = (EditText) findViewById(R.id.monthr);
        day = (EditText) findViewById(R.id.day);
        hour = (EditText) findViewById(R.id.hour);
        min = (EditText) findViewById(R.id.min);
        sec = (EditText) findViewById(R.id.sec);
        DingZhi_Num = (EditText) findViewById(R.id.DingZhi_Num);
        DingZhi_Value = (EditText) findViewById(R.id.DingZhi_Value);
        editText = (EditText) this.findViewById(R.id.editText);
        Save_Num = (Button) this.findViewById(R.id.Save_Num);
        Close = (Button) this.findViewById(R.id.close);
        Chaxun_time = (Button) this.findViewById(R.id.check_time);
        Chaxun_DingZhi = (Button) this.findViewById(R.id.check_DingZhi);
        Chakan_DingZhi_dingyi = (Button) this.findViewById(R.id.chakan_dingzhi_dingyi);
        check_zhuangtai = (Button) this.findViewById(R.id.check_zhuangtai);
        set_DingZhi = (Button) this.findViewById(R.id.set_dingzhi);
        set_Times = (Button) this.findViewById(R.id.set_times);
        yufen = (Button) this.findViewById(R.id.yufen);
        hezha = (Button) this.findViewById(R.id.hezha);
        yuhe = (Button) this.findViewById(R.id.yuhe);
        fenzha = (Button) this.findViewById(R.id.fenzha);
        fugui = (Button) this.findViewById(R.id.check_fugui);

        fugui.setOnClickListener(this);
        fenzha.setOnClickListener(this);
        yuhe.setOnClickListener(this);
        hezha.setOnClickListener(this);
        yufen.setOnClickListener(this);
        set_Times.setOnClickListener(this);
        set_DingZhi.setOnClickListener(this);
        check_zhuangtai.setOnClickListener(this);
        Chakan_DingZhi_dingyi.setOnClickListener(this);
        Chaxun_DingZhi.setOnClickListener(this);
        Chaxun_time.setOnClickListener(this);
        Close.setOnClickListener(this);
        Save_Num.setOnClickListener(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        /*
        * 每次启动就获取之前保存的手机号
        * */
        SharedPreferences pref = getSharedPreferences("Number",MODE_PRIVATE);
        String number = pref.getString("number","");

            editText.setText(number);


    }

    @Override
    public void onClick(View v) {

        Num = editText.getText().toString();

        Year = year.getText().toString();
        Month = month.getText().toString();
        Day = day.getText().toString();
        Hour = hour.getText().toString();
        Min = min.getText().toString();
        Sec = sec.getText().toString();
        times = "设置时间#"+Year+"-"+Month+"-"+Day+"/"+Hour+":"+Min+":"+Sec;

        Order_content = order_content.getText().toString();

        dingzhi_Num = DingZhi_Num.getText().toString();
        dingzhi_value = DingZhi_Value.getText().toString();
        dingzhi = "设置定值#"+dingzhi_Num+"#"+dingzhi_value;

        SmsManager smsmanager = SmsManager.getDefault();
        Intent sentIntent = new Intent("SEND_SMS_ACTION");
        PendingIntent Pi = PendingIntent.getBroadcast(MainActivity.this,0,sentIntent,0);

        switch (v.getId()) {
            case R.id.Save_Num:
                /*
                * 使用SharedPrefence保存输入的手机号
                * */
                if (Num.length() ==11)
                {
                    SharedPreferences.Editor editer = getSharedPreferences("Number",MODE_PRIVATE).edit();
                    editer.putString("number",Num);
                    editer.commit();
                    Toast toast = Toast.makeText(MainActivity.this,"保存成功！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(MainActivity.this,"请输入11位手机号码", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            break;
            case R.id.check_time:
                if (Num.length()==11)
                {
                    smsmanager.sendTextMessage(Num, null,"查询时间" , Pi, null);
                }else {
                    Toast time_toast = Toast.makeText(MainActivity.this, "请输入正确的手机号！", Toast.LENGTH_SHORT);
                    time_toast.setGravity(Gravity.CENTER, 0, 0);
                    time_toast.show();
                }

                break;
            case R.id.check_zhuangtai:
                if (Num.length()==11)
                {
                    smsmanager.sendTextMessage(Num, null,"查询状态", Pi, null);
                }else {
                    Toast zhuangtai_toast = Toast.makeText(MainActivity.this, "请输入正确的手机号！", Toast.LENGTH_SHORT);
                    zhuangtai_toast.setGravity(Gravity.CENTER, 0, 0);
                    zhuangtai_toast.show();
                }
                break;
            case R.id.check_DingZhi:
                if (Num.length()==11)
                {
                    smsmanager.sendTextMessage(Num, null, "查询定值", Pi, null);
                }
                Toast dingzhi_toast = Toast.makeText(MainActivity.this,"请输入正确的手机号！",Toast.LENGTH_SHORT);
                dingzhi_toast.setGravity(Gravity.CENTER,0,0);
                dingzhi_toast.show();

                break;
            case R.id.yufen:
                if (Num.length()==11)
                {
                    smsmanager.sendTextMessage(Num, null,"预分" , Pi, null);
                }else {
                    Toast yufen_toast = Toast.makeText(MainActivity.this, "请输入正确的手机号！", Toast.LENGTH_SHORT);
                    yufen_toast.setGravity(Gravity.CENTER, 0, 0);
                    yufen_toast.show();
                }
                break;
            case R.id.fenzha:
                if (Order_content == "合位")
                smsmanager.sendTextMessage(Num, null,"分闸", Pi, null);
                else {
                    Toast toast = Toast.makeText(MainActivity.this,"请确定主机是否处于合闸状态", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                break;
            case R.id.yuhe:
                if (Num.length()==11)
                {
                    smsmanager.sendTextMessage(Num, null, "预合", Pi, null);
                }else {
                    Toast yuhe_toast = Toast.makeText(MainActivity.this, "请输入正确的手机号！", Toast.LENGTH_SHORT);
                    yuhe_toast.setGravity(Gravity.CENTER, 0, 0);
                    yuhe_toast.show();
                }
                break;
            case R.id.hezha:
                if (Order_content == "分位")
                    smsmanager.sendTextMessage(Num, null,"合闸", Pi, null);
                else {

                    Toast toast = Toast.makeText(MainActivity.this,"请确定主机是否处于分闸状态", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                break;
            case R.id.check_fugui:
                if (Num.length()==11)
                {
                    smsmanager.sendTextMessage(Num, null, "复归", Pi, null);
                }else {
                    Toast fugui_toast = Toast.makeText(MainActivity.this, "请输入正确的手机号！", Toast.LENGTH_SHORT);
                    fugui_toast.setGravity(Gravity.CENTER, 0, 0);
                    fugui_toast.show();
                }
                break;
            case R.id.set_times:
                if (Year.length() == 2 && Month.length() == 2 && Day.length() == 2 && Hour.length() == 2 && Min.length() == 2 && Sec.length() == 2)
                {
                    smsmanager.sendTextMessage(Num, null, "设置时间#"+Year+"-"+Month+"-"+Day+"/"+Hour+":"+Min+":"+Sec, Pi, null);
                }else {
                    Toast toast = Toast.makeText(MainActivity.this,"      请输入正确的时间!\n 例如2016-01-01/00:00:00", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }

                break;
            case R.id.set_dingzhi:
                if (dingzhi_Num.length() == 2 )
                {
                    smsmanager.sendTextMessage(Num, null, dingzhi, Pi, null);
                }else {
                    Toast toast = Toast.makeText(MainActivity.this,"请在定值序号前加 0 ！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                break;
            case R.id.chakan_dingzhi_dingyi:
                Toast picture = Toast.makeText(MainActivity.this,"定值序号定义表",Toast.LENGTH_LONG);
                ImageView DingYi_image = new ImageView(MainActivity.this);
                DingYi_image.setImageResource(R.drawable.dingzhi_dingyi);
                picture.setView(DingYi_image);
                picture.setGravity(Gravity.CENTER,0,0);
                picture.show();
                break;
            case R.id.close:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("确定退出当前应用？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("是",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                    finish();   //退出当前应用
                }
            });
                dialog.setNegativeButton("否",new DialogInterface.OnClickListener(){
                   public void onClick(DialogInterface dialog ,int which){}
                });
                dialog.show();

                break;
            default:
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.sq.sms/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.sq.sms/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    class MessageReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[]) bundle.get("pdus");//提取短信信息
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = createFromPdu((byte[]) pdus[i]);
            }
            String address = messages[0].getOriginatingAddress();
            String fullMessage = "";
            for (SmsMessage message : messages) {
                fullMessage += message.getMessageBody();//获取短信内容
            }
            abortBroadcast();
            order_content.setText(fullMessage);
        }
    }

    protected void onDestory()
    {
        super.onDestroy();
        unregisterReceiver(messageReceiver);
        unregisterReceiver(sendStatusReceiver);
    }

    class SendStatusReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (getResultCode() == RESULT_OK)
            {
                //短信发送成功
                Toast.makeText(MainActivity.this,"短信发送成功，请等待...",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this,"短信发送失败，请重新发送",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onBackPressed()
    {
        long now = System.currentTimeMillis();
        if ((now - firstPressTime) > 2000)
        {
            Toast.makeText(MainActivity.this,"再按一次退出",Toast.LENGTH_SHORT).show();
            firstPressTime = now;
        }else
        {
            finish();
            System.exit(0);
        }
    }
}



