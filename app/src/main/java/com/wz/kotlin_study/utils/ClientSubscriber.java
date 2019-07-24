package com.wz.kotlin_study.utils;

import com.ceshui.mh.Const;
import com.ceshui.mh.event.MsgEvent;
import org.greenrobot.eventbus.EventBus;
import redis.clients.jedis.JedisPubSub;
import com.ceshui.mh.event.NetEvent;

public class ClientSubscriber extends JedisPubSub {

    public ClientSubscriber(){}
    @Override
    public void onMessage(String channel, String message) {       //收到消息会调用
        System.out.println(String.format("receive redis published message, channel %s, message %s", channel, message));
        EventBus.getDefault().post(new MsgEvent(Const.CLIENT_RECEIVE_MSG_PC,message));
    }
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {    //订阅了频道会调用
        System.out.println(String.format("subscribe redis channel success, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {   //取消订阅 会调用
        System.out.println(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d",
                channel, subscribedChannels));

    }
}