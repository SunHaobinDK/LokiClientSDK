package com.loki.sdk;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.loki.sdk.internal.BinderIntent;

/**
 * 用于访问LokiService的帮助类
 * Created by zhangyong on 15-6-4.
 */
public class LokiService {

    private ILokiService lokiService;

    private LokiService(ILokiService lokiService) {
        this.lokiService = lokiService;
    }

    /**
     * 获得当前LokiService的版本
     * @return 版本号
     */
    public int getVersion() {
        try {
            return lokiService.getVersion();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获得当前LokiService的渠道
     * @return 渠道好
     */
    public String getChannel() {
        try {
            return lokiService.getChannel();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 开始本地升级操作
     * @param file    Loki OTA文件
     * @return true表示升级成功，false表示升级失败
     */
    public boolean upgrade(String file) {
        try {
            return lokiService.upgrade(file);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 注册事件回调监听器
     * @param listener    事件回调监听器
     */
    public void registerListener(LokiListener listener) {
        try {
            lokiService.registerListener(listener);
        } catch (Exception e) {
        }
    }

    /**
     * 解除注册事件回调监听器
     * @param listener    事件回调监听器
     */
    public void unregisterListener(LokiListener listener) {
        try {
            lokiService.unregisterListener(listener);
        } catch (Exception e) {
        }
    }

    /**
     * 以指定软件包的身份发送通知
     * @param packageName     软件包名
     * @param id              通知id
     * @param tag             通知tag（可选）
     * @param notification    通知
     */
    public void sendNotificationAsPackage(String packageName, int id, String tag, Notification notification) {
        try {
            lokiService.sendNotificationAsPackage(packageName, id, tag, notification);
        } catch (Exception e) {
        }
    }

    /**
     * 读取wifi_supplicant.conf文件
     * @return wifi_supplicant.conf文件内容
     */
    public String readSupplicant() {
        try {
            return lokiService.readSupplicant();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 强行停止软件运行
     * @param packageName    软件名
     */
    public void forceStopPackage(String packageName) {
        try {
            lokiService.forceStopPackage(packageName);
        } catch (Exception e) {
        }
    }

    /**
     * 安装软件（异步）
     * @param fileName    APK路径，注意不能在SD卡上
     * @param flags       标志位，通常为0
     */
    public void installPackage(String fileName, int flags) {
        try {
            lokiService.installPackage(fileName, flags);
        } catch (Exception e) {
        }
    }

    /**
     * 卸载软件（异步）
     * @param packageName    软件名
     * @param observer       卸载过程监听器（IPackageUninstallObserver），可为null
     * @param flags          标志位，通常为0
     */
    public void uninstallPackage(String packageName, IBinder observer, int flags) {
        try {
            lokiService.uninstallPackage(packageName, observer, flags);
        } catch (Exception e) {
        }
    }

    /**
     * 启用或者禁用一个软件
     * @param packageName    软件名
     * @param newState       新状态: Context.COMPONENT_ENABLED_XXXX
     * @param flags          Context.DONT_KILL_APP等
     */
    public void setApplicationEnabled(String packageName, int newState, int flags) {
        try {
            lokiService.setApplicationEnabled(packageName, newState, flags);
        } catch (Exception e) {
        }
    }

    /**
     * 启用或者禁用一个组件
     * @param componentName    组件名
     * @param newState         新状态: Context.COMPONENT_ENABLED_XXXX
     * @param flags            Context.DONT_KILL_APP等
     */
    public void setComponentEnabled(ComponentName componentName, int newState, int flags) {
        try {
            lokiService.setComponentEnabled(componentName, newState, flags);
        } catch (Exception e) {
        }
    }

    private static final String LOKI_SERVICE_ACTION = "GET_LOKI_SERVICE";
    private static LokiService _instance;

    /**
     * 获得LokiService实例
     * @param context    Context
     * @return LokiService实例，null则标示LokiService未运行
     */
    public static synchronized LokiService getInstance(Context context) {
        if (_instance != null) {
            return _instance;
        }

        ILokiService service = ClientService.getLokiService();
        if (service != null) {
            _instance = new LokiService(service);
        } else {
            Intent stickyBroadcast = context.registerReceiver(null, new IntentFilter(LOKI_SERVICE_ACTION));
            if (stickyBroadcast != null) {
                service = ILokiService.Stub.asInterface(BinderIntent.getBinderExtra(stickyBroadcast, LOKI_SERVICE_ACTION));
                if (service != null) {
                    _instance = new LokiService(service);
                }
            }
        }

        return _instance;
    }
}
