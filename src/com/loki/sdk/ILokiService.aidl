package com.loki.sdk;

import com.loki.sdk.ILokiListener;
import android.app.Notification;
import android.content.ComponentName;

interface ILokiService {
    int getVersion();
    String getChannel();
    boolean upgrade(String file);
    void registerListener(in ILokiListener listener);
    void unregisterListener(in ILokiListener listener);

    void sendNotificationAsPackage(in String packageName, int id, in String tag, in Notification notification);
    String readSupplicant();
    void forceStopPackage(in String packageName);
    void installPackage(in String fileName, int flags);
    void uninstallPackage(in String packageName, in IBinder observer, int flags);
    void setApplicationEnabled(in String packageName, int newState, int flags);
    void setComponentEnabled(in ComponentName componentName, int newState, int flags);
}