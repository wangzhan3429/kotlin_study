package com.wz.kotlin_study;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by wangzhan on 2019-07-12 10:49
 */
public class ServiceRegister extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.fab).setOnClickListener(this);
        findViewById(R.id.fab_un).setOnClickListener(this);
    }

    // Create a JmDNS instance
    JmDNS jmdns;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        jmdns = JmDNS.create();

                        jmdns.addServiceListener("_services._dns-sd._udp.local",  new ServiceListener() {
                            @Override
                            public void serviceResolved(ServiceEvent ev) {
                                String addr = "";
                                if (ev.getInfo().getInetAddresses() != null && ev.getInfo().getInetAddresses().length > 0) {
                                    addr = ev.getInfo().getInetAddresses()[0].getHostAddress();
                                }
                                System.out.println("Service resolved: " + ev.getInfo().getName() +" ==> "+ addr + ":" + ev.getInfo().getPort() );
                            }
                            @Override
                            public void serviceRemoved(ServiceEvent ev) {
                                System.out.println("Service removed: " + ev.getName());
                            }
                            @Override
                            public void serviceAdded(ServiceEvent event) {
                                // Required to force serviceResolved to be called again (after the first search)
                                jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
                            }});


                        // Register a service
//                        ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "example", 1234, "path=index.html");
//
//                        jmdns.registerService(serviceInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        } else if (v.getId() == R.id.fab_un) {
            // Wait a bit

            // Unregister all services
//            jmdns.unregisterAllServices();
            LanCommunicationManager.getDefault().sendBroadcastMsg("test");
        }


    }
}
