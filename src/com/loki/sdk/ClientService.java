package com.loki.sdk;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * 此服务会在开机时自动由LokiSDK启动，Client需要在Manifest中声明此服务
 * Created by zhangyong on 15-6-4.
 */
public class ClientService extends Service {

    private static ILokiService lokiService;

    public static ILokiService getLokiService() {
        return lokiService;
    }

    private ILokiClient.Stub clientService = new ILokiClient.Stub() {
        @Override
        public void publishService(int version, ILokiService service) {
            if (Binder.getCallingUid() == android.os.Process.SYSTEM_UID) {
                lokiService = service;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return clientService;
    }
}
