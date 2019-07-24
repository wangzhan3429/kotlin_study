package com.wz.kotlin_study.part01;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.wz.kotlin_study.IPActivity;
import com.wz.kotlin_study.R;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class DnssdDiscovery extends Activity implements View.OnClickListener {

    android.net.wifi.WifiManager.MulticastLock lock;
    android.os.Handler handler = new android.os.Handler();
    private String LOGTAG = getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        setUp();
    }

    private Button StartConnect, scan;
    //目的主机ip和端口

    private EditText IP, Port;
    private TextView destination, porttext;
    private ListView mlistView;

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

        scan.setOnClickListener(this);

    }


    private String type = "_dynamix._tcp.local.";
    private JmDNS jmdns = null;
    private ServiceListener listener = null;
    private ServiceInfo serviceInfo;

    private void setUp() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) getApplicationContext().getSystemService(android.content.Context.WIFI_SERVICE);


                lock = wifi.createMulticastLock(getClass().getSimpleName());

                lock.setReferenceCounted(false);


                try {
                    InetAddress addr = getLocalIpAddress();
                    String hostname = addr.getHostName();
                    lock.acquire();
                    Log.d(LOGTAG, "Addr : " + addr);
                    Log.d(LOGTAG, "Hostname : " + hostname);
                    jmdns = JmDNS.create(InetAddress.getLocalHost());


                    listener = new ServiceListener() {

                        /*
                         * Note:This event is only the service added event. The
                         * service info associated with this event does not
                         * include resolution information.
                         */
                        @Override
                        public void serviceAdded(ServiceEvent event) {
                            /*
                             * Request service information.
                             * The information about the service is requested and the
                             * ServiceListener.resolveService method is called
                             * as soon as it is available.
                             */
                            jmdns.requestServiceInfo(event.getType(),
                                    event.getName(), 1000);
                        }

                        /*
                         * A service has been resolved. Its details are now
                         * available in the ServiceInfo record.
                         */
                        @Override
                        public void serviceResolved(ServiceEvent event) {
                            Log.d(LOGTAG, "Service resolved: " + event.getInfo().getQualifiedName() + " port:" + event.getInfo().getPort());
                            Log.d(LOGTAG, "Service Type : " + event.getInfo().getType());

                            String addr = "";
                            if (event.getInfo().getHostAddress() != null && event.getInfo().getInetAddress() != null) {
                                addr = event.getInfo().getHostAddress();
                            }
                            HashMap mdnsMap = new HashMap<String, Object>();

                            mdnsMap.put("Service_IP", addr);
                            mdnsMap.put("Service_Port", event.getInfo().getPort());
                            mdnsMap.put("service_Name", event.getName());

                            Log.d(LOGTAG, "IP==" + addr + "==name==" + event.getName() + "-port-" + event.getInfo().getPort());
                            if (!mdnsList.contains(mdnsMap)) {
                                mdnsList.add(mdnsMap);
                            }
                        }

                        @Override
                        public void serviceRemoved(ServiceEvent ev) {
                            Log.d(LOGTAG, "Service removed: " + ev.getName());
                        }

                    };
                    // Add a service listener
                    jmdns.addServiceListener("_http._tcp.local.", listener);

                    /*
                     * Advertising a JmDNS Service

                     * Construct a service description for registering with JmDNS.
                     * Parameters:
                     * type : fully qualified service type name, such as _dynamix._tcp.local
                     * name : unqualified service instance name, such as DynamixInstance
                     * port : the local port on which the service runs text string describing the service
                     * text : text describing the service
                     */
                    serviceInfo = ServiceInfo.create(type,
                            "DynamixInstance", 7433,
                            "Service Advertisement for Ambient Dynamix");

                    /*A Key value map that can be advertised with the service*/
                    serviceInfo.setText(getDeviceDetailsMap());
                    jmdns.registerService(serviceInfo);
                    Log.d(LOGTAG, "Service Type : " + serviceInfo.getType());
                    Log.d(LOGTAG, "Service Registration thread complete");
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //Unregister services
        if (jmdns != null) {
            if (listener != null) {
                jmdns.removeServiceListener(type, listener);
                listener = null;
            }
            jmdns.unregisterAllServices();
            try {
                jmdns.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            jmdns = null;
        }
        //Release the lock
        lock.release();
    }

    private InetAddress getLocalIpAddress() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        InetAddress address = null;
        try {
            address = InetAddress.getByName(String.format(Locale.ENGLISH,
                    "%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address;
    }

    private Map<String, String> getDeviceDetailsMap() {
        Map<String, String> info = new HashMap<String, String>();
        info.put("device_name", "my_device_name");
        return info;
    }

    boolean flag = true;
    private List<Map<String, Object>> mdnsList = new ArrayList<Map<String, Object>>();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scanService:
                setUp();

                while (flag) {
                    if (mdnsList.size() > 0) {
                        ListAdapter mAdapter = new MyAdapter(this, mdnsList);
                        mlistView.setAdapter(mAdapter);
//                        new WifiUtils.Utility().setListViewHeightBasedOnChildren(mlistView);
                        flag = false;
                    }

                }
                break;
        }


    }
}