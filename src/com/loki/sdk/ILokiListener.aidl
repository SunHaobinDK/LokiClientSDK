package com.loki.sdk;

import android.content.Intent;

interface ILokiListener {
    boolean onReferralBroadcast(inout Intent intent);
    void onApplicationSwitch(in String packageName, boolean isLauncher);
    void onCleanNotification();
    void onGooglePlayDownload(in String packageName, in int versionCode);
}