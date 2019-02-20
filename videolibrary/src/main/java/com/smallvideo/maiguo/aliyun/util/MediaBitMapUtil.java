package com.smallvideo.maiguo.aliyun.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.File;

/**
 * Create by www.lijin@foxmail.com on 2018/7/12 0012.
 * <br/>
 * 拿来截取帧用的
 */

public class MediaBitMapUtil {

    private static final String TAG = "MediaDecoder";
    private MediaMetadataRetriever retriever = null;
    private String fileLength;

    public MediaBitMapUtil(String file) {

        retriever = new MediaMetadataRetriever();
        retriever.setDataSource(file);
        fileLength = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        Log.i(TAG, "fileLength : " + fileLength);
    }

    /**
     * 获取视频某一帧
     *
     * @param timeMs 毫秒
     */
    public Bitmap getMp4GOP(long timeMs) {
        if (retriever == null) return null;
        Bitmap bitmap = retriever.getFrameAtTime(timeMs * 1000  , MediaMetadataRetriever.OPTION_CLOSEST);
        if (bitmap == null) return null;
        return bitmap;
    }

    /**
     * 取得视频文件播放长度
     *
     * @return
     */
    public String getVedioFileLength() {
        return fileLength;
    }

    /**
     * 判断文件是否有效
     *
     * @param strFile
     * @return
     */
    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
