package com.loki.sdk;

import android.content.Context;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Build;

import com.giga.sdk.ClientCallback;
import com.loki.sdk.internal.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * LokiClientSDK实现的GIGASDK所需的ClientCallback
 * 调用者通常无需修改此类
 * Created by zhangyong on 15-6-4.
 */
public class LokiClientCallback implements ClientCallback {

    private Context context;
    private File scriptPath;
    private File scriptFile;

    public LokiClientCallback(Context context) {
        this.context = context.getApplicationContext();
        scriptPath = context.getDir("lokiclient", Context.MODE_PRIVATE);
        scriptFile = new File(scriptPath, "callback.sh");
    }

    @Override
    public boolean executeWithRoot(File jarFile, File workingDirectory) {
        scriptFile.delete();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(scriptFile));
            writer.write(String.format("setenforce 0\n"));
            writer.write(String.format("export NO_ADDR_COMPAT_LAYOUT_FIXUP=1\n"));
            writer.write(String.format("export CLASSPATH=%s\n", jarFile.getAbsolutePath()));
            if (Build.VERSION.SDK_INT < 21) {
                writer.write(String.format("export LD_LIBRARY_PATH=%s\n", System.getenv("LD_LIBRARY_PATH")));
            }
            writer.write(String.format("app_process %s %s\n", workingDirectory.getAbsolutePath(), ClientCallback.ROOT_SERVICE));
            writer.flush();
            writer.close();

            FileUtils.setPermissions(scriptFile, 00755);

            LocalSocket socket = new LocalSocket();
            socket.connect(new LocalSocketAddress("loki.sdk", LocalSocketAddress.Namespace.ABSTRACT));
            byte[] scriptFilePath = scriptFile.getAbsolutePath().getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(4 + scriptFilePath.length);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(scriptFilePath.length);
            buffer.put(scriptFilePath);
            socket.getOutputStream().write(buffer.array());
            socket.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
