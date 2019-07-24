package com.wz.kotlin_study;

/**
 * Created by wangzhan on 2019-07-11 14:54
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class IPActivity extends Activity implements OnClickListener {
    public static final String TAG = "IPActivity";

    private Button StartConnect, scan;
    //目的主机ip和端口

    private EditText IP, Port;
    //本机监听端口
    //private EditText LOCAL_PORT;

    private TextView destination, porttext;

    //目的ip和端口

    private String Server_IP;

    private String Server_Port;


    private Map<String, Object> map;

    private Application myApplication;


    private WifiAdmin WifiAdmin;

    private WifiManager mWifiManager;


    Handler handler = new Handler();

    //mdns服务类型

    private String type = "_udpserver._udp.local.";
// private String type = "_sleep-proxy._udp.local.";

    private JmDNS jmdns = null;

    private ServiceListener listener = null;

    private ServiceInfo serviceInfo;

    //组播锁

    private MulticastLock lock;
    //展示扫描的mDNS服务

    private ListView mlistView;

//    private Utils utils = new Utils();

    //存放扫描的mDNS服务
    private List<Map<String, Object>> mdnsList = new ArrayList<Map<String, Object>>();

    private Map<String, Object> mdnsMap;
    private boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        initViews();

        mlistView.setOnItemClickListener(new OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {


                AlertDialog.Builder alert = new AlertDialog.Builder(IPActivity.this);
                Server_IP = mdnsList.get(position).get("Service_IP").toString();
                Server_Port = mdnsList.get(position).get("Service_Port").toString();
                alert.setTitle(Server_IP);
                alert.setMessage("点击获取");


                alert.setPositiveButton("获取", new DialogInterface.OnClickListener() {
                    @Override


                    public void onClick(DialogInterface dialog, int which) {
                        IP.setText(Server_IP);
                        Port.setText(Server_Port);

                    }

                });
                alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override


                    public void onClick(DialogInterface dialog, int which) {
                        //
                        //mWifiAdmin.removeWifi(mWifiAdmin.getNetworkId());
                    }

                });
                alert.create();
                alert.show();

            }
        });

    }

    //开启查找服务


    private void setUp() {
        //打开组播
        openBroadcast();

        try {
            //InetAddress ip = InetAddress.getByName("192.168.0.108");
            jmdns = JmDNS.create();
            //创建mDNS服务
            serviceInfo = ServiceInfo.create(type, "AndroidTest", 5353, "plain test service from android");
            jmdns.registerService(serviceInfo);

            //监听mDNS服务
            jmdns.addServiceListener(type, listener = new ServiceListener() {

                @Override


                public void serviceResolved(ServiceEvent event) {
// mdnsMap = new HashMap<String, Object>();
//
// mdnsMap.put("Service_IP", ev.getInfo().getHostAddress());
// mdnsMap.put("Service_Port", ev.getInfo().getPort());
// if (!mdnsList.contains(mdnsMap)) {
// mdnsList.add(mdnsMap);
// }

                    //ServiceInfo serviceInfo = jmdns.getServiceInfo(type, "light udpServer",1);

                    String addr = "";
                    if (event.getInfo().getHostAddress() != null && event.getInfo().getInetAddress() != null) {
                        addr = event.getInfo().getHostAddress();
                    }
                    mdnsMap = new HashMap<String, Object>();

                    mdnsMap.put("Service_IP", addr);
                    mdnsMap.put("Service_Port", event.getInfo().getPort());
                    mdnsMap.put("service_Name", event.getName());
                    Log.d(TAG, addr + "---" + event.getInfo().getPort() + "---" + event.getName());

                    if (!mdnsList.contains(mdnsMap)) {
                        mdnsList.add(mdnsMap);
                    }

                }


                @Override


                public void serviceRemoved(ServiceEvent ev) {

                }


                @Override


                public void serviceAdded(ServiceEvent event) {
                    // Required to force serviceResolved to be called again (after the first search)
                    jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }


    @Override


    protected void onStart() {
        super.onStart();

    }


    /**
     * * 打开wifi组播服务
     */


    public void openBroadcast() {
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(android.content.Context.WIFI_SERVICE);
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
        lock = mWifiManager.createMulticastLock(getClass().getSimpleName());
        lock.setReferenceCounted(true);
        lock.acquire();//to receive multicast packets
    }


    /**
     * * ListView的Item显示
     * * @authorSunward
     * *
     */


    public class MyAdapter extends BaseAdapter {
        LayoutInflater inflater;
        List<Map<String, Object>> list;

        public MyAdapter(Context context, List<Map<String, Object>> list) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }


        @Override
        public int getCount() {
            return list.size();
        }


        @Override
        public Object getItem(int position) {
            return list.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }


        //忽略指定的警告
        @SuppressLint({"ViewHolder", "InflateParams"})

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//针对每一个数据（即每一个List ID）创建一个ListView实例，
            View view = null;
            view = inflater.inflate(R.layout.wifi_listitem, null);
            Map<String, Object> result = list.get(position);
            TextView wifi_ssid = (TextView) view.findViewById(R.id.ssid);
            ImageView wifi_level = (ImageView) view.findViewById(R.id.wifi_level);
            wifi_ssid.setText(result.get("service_Name") + " " + result.get("Service_IP"));
            wifi_level.setImageResource(android.R.drawable.arrow_up_float);

            //判断信号强度，显示对应的指示图标
            return view;
        }


    }


    /**
     * * 控件初始化
     */


    public void initViews() {
        //连接
        StartConnect = (Button) findViewById(R.id.startCon);
        //扫描
        scan = (Button) findViewById(R.id.scanService);
        IP = (EditText) findViewById(R.id.IPText);
        Port = (EditText) findViewById(R.id.PortText);
        //LOCAL_PORT = (EditText) findViewById(R.id.LOCAL_PORT);

        destination = (TextView) findViewById(R.id.destination);
        porttext = (TextView) findViewById(R.id.porttext);

        //扫描的mdns服务
        mlistView = (ListView) findViewById(R.id.mdns_list);

        StartConnect.setOnClickListener(IPActivity.this);
        scan.setOnClickListener(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.wifi_config) {
//            Intent intent = new Intent();
//            intent.setClass(IPActivity.this, WifiAdmin.class);
//            this.startActivity(intent);
//            finish();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * * 返回键事件
     */

    @Override


    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * * 按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startCon:
                myApplication = (Application) this.getApplicationContext();
                if (IP.getText().toString().trim().equals("") ||
//LOCAL_PORT.getText().toString().trim().equals("")||
                        Port.getText().toString().trim().equals("")) {
                    Toast.makeText(IPActivity.this, "请输入完整", Toast.LENGTH_SHORT).show();
                } else {
                    Server_IP = IP.getText().toString();
                    Server_Port = Port.getText().toString();
                    Toast.makeText(IPActivity.this, Server_IP + "" + Server_Port, Toast.LENGTH_SHORT).show();
//Application存储全局变量
                    map = new HashMap<String, Object>();
                    map.put("IP", Server_IP);
                    map.put("Port", Server_Port);
                    map.put("LOCAL_PORT", 8929);

//                    myApplication.setMap(map);

                    Intent intent = new Intent();
                    intent.setClass(IPActivity.this, MainActivity.class);

                    startActivity(intent);
                    finish();
                }
                break;

            case R.id.scanService:
                new Thread() {
                    @Override


                    public void run() {
//  Looper.prepare();
                        setUp();
//加载扫描到的服务
//  Looper.loop();
                    }

                }.start();
                while (flag) {
                    if (mdnsList.size() > 0) {
                        ListAdapter mAdapter = new MyAdapter(IPActivity.this, mdnsList);
                        mlistView.setAdapter(mAdapter);
//                        new WifiUtils.Utility().setListViewHeightBasedOnChildren(mlistView);
                        flag = false;
                    }

                }
                break;
            default:
                break;
        }


    }

}
