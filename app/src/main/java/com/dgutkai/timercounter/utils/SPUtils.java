package com.dgutkai.timercounter.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.dgutkai.timercounter.constant.SPKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * 工具类
 *
 * @author Aaron
 *
 */
public class SPUtils {
    private static SharedPreferences mSp;
    public static  String SP_NAME = SPKey.SHARED_USSER;


    public static SharedPreferences getSp(Context context) {
        if (mSp == null) {

            mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSp;
    }
    public static Editor getSpEd(Context context) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSp.edit();
    }
//
//	public SPUtils putLong(String key, long value) {
//		mSp.edit().putLong(key, value).apply();
//		return this;
//	}
//
//	public static long getLong(String key, Long dValue) {
//		return mSp.getLong(key, dValue);
//	}
    /**
     * 获取boolean类型的值
     *
     * @param context
     *            上下文
     * @param key
     *            对应的键
     * @param defValue
     *            如果没有对应的值，
     * @return
     */
    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 获取boolean类型的值,如果没有对应的值，默认值返回false
     *
     * @param context
     *            上下文
     * @param key
     *            对应的键
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * 设置boolean类型的值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = getSp(context);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取String类型的值
     *
     * @param context
     *            上下文
     * @param key
     *            对应的键
     * @param defValue
     *            如果没有对应的值，
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getString(key, defValue);
    }

    /**
     * 获取String类型的值,如果没有对应的值，默认值返回null
     *
     * @param context
     *            上下文
     * @param key
     *            对应的键
     * @return
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    /**
     * 设置String类型的值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = getSp(context);
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }


    /**
     * 获取long类型的值
     *
     * @param context
     *            上下文
     * @param key
     *            对应的键
     * @param defValue
     *            如果没有对应的值，
     * @return
     */
    public static long getLong(Context context, String key, long defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getLong(key, defValue);
    }

    /**
     * 获取long类型的值,如果没有对应的值，默认值返回0
     *
     * @param context
     *            上下文
     * @param key
     *            对应的键
     * @return
     */
    public static Long getLong(Context context, String key) {
        return getLong(context, key, -1L);
    }

    /**
     * 设置Long类型的值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setLong(Context context, String key, long value) {
        SharedPreferences sp = getSp(context);
        Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }


    /**
     * 设置对象类型
     * @param context
     * @param key
     * @param object
     * @return
     */
    public static boolean setObjectToShare(Context context, String key, Object object) {
        // TODO Auto-generated method stub
        SharedPreferences share = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (object == null) {
            Editor editor = share.edit().remove(key);
            return editor.commit();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
// 将对象放到OutputStream中
// 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(),
                Base64.DEFAULT));
        try {
            baos.close();
            oos.close();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        Editor editor = share.edit();
// 将编码后的字符串写到base64.xml文件中
        editor.putString(key, objectStr);
        return editor.commit();
    }

    /**
     * 获取对象的值
     * @param context
     * @param key
     * @return
     */
    public static Object getObjectFromShare(Context context, String key) {
        SharedPreferences sharePre = PreferenceManager
                .getDefaultSharedPreferences(context);
        try {
            String wordBase64 = sharePre.getString(key, "");
            // 将base64格式字符串还原成byte数组
            if (wordBase64 == null || wordBase64.equals("")) { // 不可少，否则在下面会报java.io.StreamCorruptedException
                return null;
            }
            byte[] objBytes = Base64.decode(wordBase64.getBytes(),
                    Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            // 将byte数组转换成product对象
            Object obj = ois.readObject();
            bais.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取long类型的值,如果没有对应的值，默认值返回0
     * @param <T>
     *
     * @param context
     *            上下文
     * @param key
     *            对应的键
     * @return
     */
//	public static <T> T getObj(Context context,String key, Class<T> objClass) {
//		String strObj=getString(context, key);
//		Gson gson = new Gson();
//		if(strObj!=null)
//		{
//			return gson.fromJson(strObj, objClass);
//		}
//		return null;
//	}

    /**
     * 设置Long类型的值
     *
     * @param context
     * @param key
     * @param value
     */
//	public static void setObj(Context context, String key, Object value) {
//
//		Gson gson = new Gson();
//		SharedPreferences sp = getSp(context);
//		Editor editor = sp.edit();
//		editor.putString(key, gson.toJson(value));
//		editor.commit();
//	}
//

}