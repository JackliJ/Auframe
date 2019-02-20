package com.maiguoer.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.File;
import java.math.BigDecimal;


/**
 * Created by Sky on 2017/8/24.
 * <p/>
 * 文件管理
 */

public class FileUtils {

    /**
     * 下载目录
     */
    public static String downloadDir = "/download/";
    /**
     * 地图截图文件夹
     */
    public static String mapScreenShotDir = "/MapScreenShot/";

    /**
     * 环信图片
     */
    public static String hxImagesDir = "/hx/images/";

    /**
     * 环信视频文件夹
     */
    public static String hxVideosDir = "/hx/videos/";

    /**
     * 环信录音文件夹
     */
    public static String hxAudioir = "/hx/audios/";

    /**
     * APP 图片文件夹
     */
    public static String images = "/images/";
    /**
     * WebView的缓存 文件夹
     */
    public static final String APP_CACAHE_DIRNAME = "/webcache";
    /**
     * Mp4格式
     */
    public static final String MP4 = ".mp4";
    /**
     * VIVO
     */
    public static final String VIVO = "vivo";

    /**
     * 检查SD卡是否存在
     * @return
     */
    public static boolean checkSDCard() {
        final String status = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(status)) {
            return true;
        }
        return false;
    }


    /**
     * 创建存储目录
     * @param context
     * @param dir               目录  （ps:请参照静态变量）
     * @return                  目录
     */
    public static String createDir(Context context, String dir) {
        String path;
        if(context.getExternalFilesDir(null) == null) {
            path = context.getFilesDir() + dir;
        } else {
            path = context.getExternalFilesDir(null) + dir;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    /**
     * 判断某个文件是否存在
     * @param path
     */
    public static boolean checkFile(String path) {
        File file = new File(path);
        if(!file.exists()) {
            return true;
        } else {
             return false;
        }
    }

    /**
     * 创建下载App目录
     * @param context
     * @param fileName                  文件名
     * @return
     */
    public static String createDownloadAppDir(Context context, String fileName) {
        String path = createDir(context, downloadDir);
        String mFileName = path + fileName;
        // 判断文件是否存在，如果存在则删除
        if(checkFile(mFileName)) {
            new File(mFileName).delete();
        }
        return path;
    }


    /**
     * 获取缓存大小
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 清除缓存
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        if(dir != null) {
            return dir.delete();
        } else {
            return false;
        }
    }

    // 获取文件大小
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 获取手机内部存储空间
     * 注： 经测试这个方法好像不太准
     * @param context
     * @return 以M,G为单位的容量
     */
    public static String getInternalMemorySize(Context context) {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong;
        long blockCountLong;
        if (Build.VERSION.SDK_INT < 18){
            blockSizeLong = statFs.getBlockSize();
            blockCountLong = statFs.getBlockCount();
        }else {
            blockSizeLong = statFs.getBlockSizeLong();
            blockCountLong = statFs.getBlockCountLong();
        }
        long size = blockCountLong * blockSizeLong;
        return Formatter.formatFileSize(context, size);
    }

    /**
     * 获取手机内部可用存储空间
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    public static String getAvailableInternalMemorySize(Context context) {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong;
        long blockSizeLong;
        if (Build.VERSION.SDK_INT < 18){
            availableBlocksLong = statFs.getAvailableBlocks();
            blockSizeLong = statFs.getBlockSize();
        }else {
            availableBlocksLong = statFs.getAvailableBlocksLong();
            blockSizeLong = statFs.getBlockSizeLong();
        }
        return Formatter.formatFileSize(context, availableBlocksLong * blockSizeLong);
    }

    /**
     * Vivo手机
     */
    public static String savaToPhoto() {
        if (TextUtils.equals(  android.os.Build.BRAND, VIVO)) {
            return Environment.getExternalStorageDirectory() + "/相机";
        } else {
            return Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
        }
    }

}
