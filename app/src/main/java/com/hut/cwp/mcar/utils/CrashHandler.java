package com.hut.cwp.mcar.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.hut.cwp.mcar.app.Globel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

/**
 * Created by hutcwp on 2017/4/27.
 * Mail : hutcwp@foxmail.com
 * Blog : hutcwp.club
 * GitHub : github.com/hutcwp
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;

    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/"+ Globel.AppName+"/log/";

    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".txt";

    //因为Application 的生命周期是整个app，所以一般不用考虑内存泄漏
    private static CrashHandler sInstance;

    private Context mContext;

    //私有构造器
    private CrashHandler() {

    }

    //单例模式
    public static CrashHandler getInstance() {
        if (sInstance == null) {
            sInstance = new CrashHandler();
        }
        return sInstance;
    }

    //初始化
    public void init(Context context) {

        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context;

    }

    /**
     * 重写uncaughtException
     *
     * @param t  发生Crash的线程
     * @param ex Throwale对象
     */
    @Override
    public void uncaughtException(Thread t, Throwable ex) {

        if (Build.VERSION.SDK_INT >= 23) {
            int checkWriteStoragePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                //没有权限
            } else {
                //有权限
                handleException(ex);
            }
        } else {
            //低于6.0，不需要动态申请
            handleException(ex);
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
//        Process.killProcess(Process.myPid());
    }

    //处理异常
    private void handleException(final Throwable ex) {
        try {
            Executors.newSingleThreadExecutor().submit(() -> {
//                Toast.makeText(mContext,"应用发生bug，即将退出！",Toast.LENGTH_SHORT).show();
                dumpExceptionToSDCard(ex);
                uploadExceptionToServer();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 将异常信息上传至服务器
     */
    private void uploadExceptionToServer() {


    }

    /**
     * 将异常信息写入sd卡
     *
     * @param ex
     */
    private void dumpExceptionToSDCard(Throwable ex) {
        //判断是否支持SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Log.i(TAG, "sdcard unfind ,skip dump exception");
                return;
            }
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));

        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);
        Log.i(TAG, "sdcard path: "+PATH);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            //将抛出的异常信息写入到文件
            ex.printStackTrace(pw);
            pw.close();
            Log.i(TAG, "sdcard write success: ");
        } catch (Exception e) {
            Log.d(TAG, "dump Exception Exception" + e.getMessage());
            e.printStackTrace();
        }

    }


    /**
     * 获取手机信息
     *
     * @param pw 写入流
     * @throws PackageManager.NameNotFoundException 异常
     */
    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {

        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);

        pw.print("App Version : ");
        pw.print(pi.versionName);

        pw.print('_');
        pw.println(pi.versionCode);

        pw.print("OS Version : ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        pw.print("Vendor : ");
        pw.println(Build.MANUFACTURER);

        pw.print("Model : ");
        pw.println(Build.MODEL);
        pw.print("Cpu ABI : ");
        pw.println(Build.CPU_ABI);
    }

}
