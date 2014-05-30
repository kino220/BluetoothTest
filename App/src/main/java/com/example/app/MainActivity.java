package com.example.app;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Set;

/* 大体TechBoosterのコードを参考に作ってる */

public class MainActivity extends ActionBarActivity {
    String tag = "BluetoothSample";
    private final int REQUEST_ENABLE_BLUETOOTH = 10;
    ArrayAdapter pairedDeviceAdapter;
    ArrayAdapter nonPairedDeviceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        //BluetoothAdapter取得
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!mBtAdapter.equals(null)){
            //Bluetooth対応端末の場合の処理
            Log.i(tag,"Bluetoothがサポートされてます。");
        }else{
            //Bluetooth非対応端末の場合の処理
            Log.i(tag,"Bluetoothがサポートれていません。");
            finish();
        }
        boolean btEnable = mBtAdapter.isEnabled();

        if(btEnable == true){
            //BluetoothがONだった場合の処理
        }else{
            //OFFだった場合、ONにすることを促すダイアログを表示する画面に遷移
            Intent btOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btOn, REQUEST_ENABLE_BLUETOOTH);
        }

        //接続履歴のあるデバイスを取得
        pairedDeviceAdapter = new ArrayAdapter(this, R.layout.rowdata);
//BluetoothAdapterから、接続履歴のあるデバイスの情報を取得
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            //接続履歴のあるデバイスが存在する
            for(BluetoothDevice device:pairedDevices){
                //接続履歴のあるデバイスの情報を順に取得してアダプタに詰める
                //getName()・・・デバイス名取得メソッド
                //getAddress()・・・デバイスのMACアドレス取得メソッド
                pairedDeviceAdapter.add(device.getName() + "\n" + device.getAddress());
            }

            //レイアウトインフレーター使用
            LayoutInflater factory = LayoutInflater.from(this);
            View layInfView = factory.inflate(R.layout.fragment_main,null);

            ListView deviceList = (ListView)layInfView.findViewById(R.id.pairedDeviceList);
            deviceList.setAdapter(pairedDeviceAdapter);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int ResultCode, Intent date){
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(ResultCode == Activity.RESULT_OK){

                //BluetoothがONにされた場合の処理
                Log.i(tag,"BluetoothをONにしてもらえました。");
            }else{
                Log.i(tag,"BluetoothがONにしてもらえませんでした。");
                finish();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
