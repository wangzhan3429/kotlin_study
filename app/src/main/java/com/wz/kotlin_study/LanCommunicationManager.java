package com.wz.kotlin_study;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


import android.os.Handler;
import android.os.Message;
import android.support.annotation.WorkerThread;
import android.util.Log;


public class LanCommunicationManager {
    private static final String TAG = "LanCommunicationManager";
    public static final String SERVER_IP = "255.255.255.255";
    private static final int PORT = 9988;

    private DatagramSocket mSocket;
    private boolean isListening = false;

    private OnSendListener mOnSendListener;
    private OnReceiveListener mOnReceiveListener;
    public static final int SUCCESS = 0;
    public static final int FAILED = -1;

    private static volatile LanCommunicationManager lanCommunicationManager;

    private LanCommunicationManager() {
        init();
    }

    public static LanCommunicationManager getDefault() {
        if (lanCommunicationManager == null) {
            synchronized (LanCommunicationManager.class) {
                if (lanCommunicationManager == null) {
                    lanCommunicationManager = new LanCommunicationManager();
                }
            }
        }
        return lanCommunicationManager;
    }

    private synchronized void init() {
        try {
            mSocket = new DatagramSocket(PORT);
            mSocket.setSoTimeout(8000);
        } catch (SocketException e) {
            Log.e(TAG, "SocketException init error！" + e.getMessage());
        }
    }

    public synchronized void reInit() {
        close();
        init();
    }

    // 监听局域网广播及特定端口的消息
    public synchronized void startListening() {
        isListening = true;
        SimpleTaskExecutor.scheduleNow(new SimpleTaskExecutor.Task() {
            @Override
            public String getName() {
                return "Start Listenning";
            }

            @Override
            public void run() {
                while (isListening) {
                    byte[] buffer = new byte[4096];
                    DatagramPacket receivevPacket = new DatagramPacket(buffer, buffer.length);
                    try {
                        if (mSocket != null && !mSocket.isClosed()) {
                            mSocket.receive(receivevPacket);
                            if (mOnReceiveListener != null) {
                                String message = new String(receivevPacket.getData(), 0, receivevPacket
                                        .getLength());
                                mOnReceiveListener.onData(message);
                            }
                            Log.i(TAG, "mSocket.receive: " + new String(receivevPacket.getData(), 0, receivevPacket
                                    .getLength()));
                        } else {
                            reInit();
                        }
                    } catch (SocketTimeoutException e) {
                        Log.i(TAG, "mSocket.receive: SocketTimeoutException => " + e.getMessage());
                        if (mOnReceiveListener != null) {
                            mOnReceiveListener.onTimeout();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "mSocket.receive null! " + e.getMessage());
                        if (mOnReceiveListener != null) {
                            mOnReceiveListener.onError(e.getMessage());
                        }
                    }
                }
            }
        });
    }

    // 停止监听
    public synchronized void stopListening() {
        isListening = false;
    }

    public boolean isListening() {
        return isListening;
    }

    public synchronized void setOnSendStatusListener(OnSendListener onSendListener) {
        mOnSendListener = onSendListener;
    }

    public synchronized void setOnReceiveDataListener(OnReceiveListener onReceiveListener) {
        mOnReceiveListener = onReceiveListener;
    }

    // 默认返回调用者所在线程处理
    private final Handler workHandler = SimpleTaskExecutor.createWorkHandler(TAG, new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            int mStatus = message.what;
            if (mOnSendListener != null) {
                if (mStatus == SUCCESS) {
                    mOnSendListener.onSuccess();
                } else if (mStatus == FAILED) {
                    mOnSendListener.onFailure();
                }
            }
            return false;
        }
    });

    @WorkerThread
    public synchronized void sendMsg(final String ip, final String msg) {
        if (mSocket == null) {
            return;
        }
        workHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    byte[] data = msg.getBytes();
                    DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(ip), PORT);
                    socket.send(packet);
                    socket.close();
                    workHandler.sendEmptyMessage(SUCCESS);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    workHandler.sendEmptyMessage(FAILED);
                } catch (SocketException e) {
                    e.printStackTrace();
                    workHandler.sendEmptyMessage(FAILED);
                } catch (IOException e) {
                    e.printStackTrace();
                    workHandler.sendEmptyMessage(FAILED);
                }
            }
        });
    }

    @WorkerThread
    public synchronized void sendBroadcastMsg(String msg) {
        sendMsg(SERVER_IP, msg);
    }

    public synchronized void close() {
        if (mSocket != null && !mSocket.isClosed()) {
            mSocket.close();
            stopListening();
        }
    }

    public interface OnSendListener {
        void onSuccess();
        void onFailure();
    }

    // 可用于局域网监听，也可以用于应用内自测
    public interface OnReceiveListener {
        void onData(String message);
        void onTimeout();
        void onError(String msg);
    }
}
