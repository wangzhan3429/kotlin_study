package com.wz.kotlin_study;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;
import com.wz.kotlin_study.part01.MyAdapter;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class FibndService2 {
    NsdManager.RegistrationListener nsRegListener;
    NsdManager.DiscoveryListener nsDicListener;
    NsdManager.ResolveListener nsResolveListener;
    Context context;
    NsdManager nsdManager;
    List beans;
    MyAdapter adapter;
    OnServiceListener a;

    public FibndService2(Context context) {
        this.context = context;
    }

    public FibndService2(Context context, List beans, MyAdapter adapter) {
        this.context = context;
        this.beans = beans;
        this.adapter = adapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void registerService() {
// 注意：注册网络服务时不要对端口进行硬编码，通过如下这种方式为你的网络服务获取
// 一个可用的端口号.
        int port = 0;
        try {
            ServerSocket sock = new ServerSocket(0);
            port = sock.getLocalPort();
            sock.close();
        } catch (Exception e) {
            Toast.makeText(context, "can not set port", Toast.LENGTH_SHORT);
        }

        // 注册网络服务的名称、类型、端口
        NsdServiceInfo nsdServiceInfo = new NsdServiceInfo();
        nsdServiceInfo.setServiceName("NSD_Test_Program");
        nsdServiceInfo.setServiceType("_ipp._tcp");
        nsdServiceInfo.setPort(port);

        // 实现一个网络服务的注册事件监听器，监听器的对象应该保存起来以便之后进行注销
        nsRegListener = new NsdManager.RegistrationListener() {
            @Override
            public void onUnregistrationFailed(NsdServiceInfo arg0, int arg1) {
                Toast.makeText(context, "Unregistration Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                Toast.makeText(context, "Service Unregistered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceRegistered(NsdServiceInfo arg0) {
                Toast.makeText(context, "Service Registered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
                Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        };

        // 获取系统网络服务管理器，准备之后进行注册
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        nsdManager.registerService(nsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, nsRegListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void discoverService() {
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        nsDicListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Toast.makeText(context, "Stop Discovery Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Toast.makeText(context,
                        "Start Discovery Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Toast.makeText(context, "Service Lost", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                // 发现网络服务时就会触发该事件
                // 可以通过switch或if获取那些你真正关心的服务
                Toast.makeText(context, "Service Found", Toast.LENGTH_SHORT).show();
                nsdManager.resolveService(serviceInfo, nsResolveListener);

            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Toast.makeText(context, "Discovery Stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Toast.makeText(context, "Discovery Started", Toast.LENGTH_SHORT).show();
            }
        };
        //NsdManager nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        nsdManager.discoverServices("_services._dns-sd._udp.local.", NsdManager.PROTOCOL_DNS_SD, nsDicListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initResolveListener() {
        nsResolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onServiceResolved(NsdServiceInfo arg0) {
                // 可以再这里获取相应网络服务的地址及端口信息，然后决定是否要与之建立连接。
                // 之后就是一些socket操作了
//                Bean bean = new Bean();
//                bean.setName(arg0.getServiceName());
//                bean.setIp(arg0.getHost() + "");
//                bean.setPort(arg0.getPort() + "");
//                beans.add(bean);

//                a.not();
                String name = arg0.getServiceName();
                if ("NSD_Test_Program".equalsIgnoreCase(name)) {
                    nsdManager.stopServiceDiscovery(nsDicListener);
                    if (a != null) {
                        a.onService(arg0);
                    }
                }
                Log.d("@lie", arg0.toString());

            }

            @Override
            public void onResolveFailed(NsdServiceInfo arg0, int arg1) {
                Toast.makeText(context, "onResolveFailed", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void sendData() {

    }


    public void setOnServiceListener(OnServiceListener a) {
        this.a = a;
    }

    interface OnServiceListener {
        void onService(NsdServiceInfo info);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void unregisterService() {

        nsdManager.stopServiceDiscovery(nsDicListener); // 关闭网络发现
    }
}