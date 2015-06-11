package com.loki.sdk.internal;

import android.content.Intent;
import android.os.IBinder;

import java.lang.reflect.Method;

public class BinderIntent {

	private static Method metGetIBinderExtra;
	private static Method metPutExtra;
	
	static {
		try {
			metGetIBinderExtra = Intent.class.getDeclaredMethod("getIBinderExtra", String.class);
			metGetIBinderExtra.setAccessible(true);
			metPutExtra = Intent.class.getDeclaredMethod("putExtra", String.class, IBinder.class);
			metPutExtra.setAccessible(true);
		} catch (Exception e) {
		}
	}
	
	public static void putBinderExtra(Intent intent, String key, IBinder value) {
		try {
			metPutExtra.invoke(intent, key, value);
		} catch (Exception e) {
		}
	}
	
	public static IBinder getBinderExtra(Intent intent, String key) {
		try {
			return (IBinder) metGetIBinderExtra.invoke(intent, key);
		} catch (Exception e) {
			return null;
		}
	}
}
