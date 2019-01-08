
package com.chat.business.library.util;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;

import com.blankj.utilcode.util.LogUtils;
import com.chat.business.library.R;

/**
 * @author : zejian
 * @time : 2016年1月5日 上午11:32:33
 * @email : shinezejian@163.com
 * @description :表情加载类,可自己添加多种表情，分别建立不同的map存放和不同的标志符即可
 */
public class EmotionUtils {

    /**
     * 表情类型标志符
     */
    public static final int EMOTION_TOTAL = 0;//总的
    public static final int EMOTION_CLASSIC_TYPE = 1;//经典表情
    public static final int EMOTTON_AM_TYPE = 2;//小漠表情
    public static final int EMOTTON_PHONEIX_TYPE = 3;//凤凰表情


    /**
     * key-表情文字;
     * value-表情图片资源
     */
    public static ArrayMap<String, Integer> EMPTY_MAP;
    public static ArrayMap<String, Integer> EMPTY_TOTAL;//总的表情组
    public static ArrayMap<String, Integer> EMOTION_CLASSIC_MAP;
    public static ArrayMap<String, Integer> EMOTTON_AM_MAP;
    public static ArrayMap<String, Integer> EMOTTON_PHONEIX;

    static {
        EMPTY_TOTAL = new ArrayMap<>();
        EMPTY_MAP = new ArrayMap<>();
        EMOTION_CLASSIC_MAP = new ArrayMap<>();
        /**
         * 第一组表情
         */
        EMOTION_CLASSIC_MAP.put("[bigem:1:]", R.mipmap.exp_egg_01);
        EMOTION_CLASSIC_MAP.put("[bigem:2:]", R.mipmap.exp_egg_02);
        EMOTION_CLASSIC_MAP.put("[bigem:3:]", R.mipmap.exp_egg_03);
        EMOTION_CLASSIC_MAP.put("[bigem:4:]", R.mipmap.exp_egg_04);
        EMOTION_CLASSIC_MAP.put("[bigem:5:]", R.mipmap.exp_egg_05);
        EMOTION_CLASSIC_MAP.put("[bigem:6:]", R.mipmap.exp_egg_06);
        EMOTION_CLASSIC_MAP.put("[bigem:7:]", R.mipmap.exp_egg_07);
        EMOTION_CLASSIC_MAP.put("[bigem:8:]", R.mipmap.exp_egg_08);
        EMOTION_CLASSIC_MAP.put("[bigem:9:]", R.mipmap.exp_egg_09);
        EMOTION_CLASSIC_MAP.put("[bigem:10:]", R.mipmap.exp_egg_10);
        EMOTION_CLASSIC_MAP.put("[bigem:11:]", R.mipmap.exp_egg_11);
        EMOTION_CLASSIC_MAP.put("[bigem:12:]", R.mipmap.exp_egg_12);
        EMOTION_CLASSIC_MAP.put("[bigem:13:]", R.mipmap.exp_egg_13);
        EMOTION_CLASSIC_MAP.put("[bigem:14:]", R.mipmap.exp_egg_14);
        EMOTION_CLASSIC_MAP.put("[bigem:15:]", R.mipmap.exp_egg_15);
        EMOTION_CLASSIC_MAP.put("[bigem:16:]", R.mipmap.exp_egg_16);
        EMOTION_CLASSIC_MAP.put("[bigem:17:]", R.mipmap.exp_egg_17);
        EMOTION_CLASSIC_MAP.put("[bigem:18:]", R.mipmap.exp_egg_18);
        EMOTION_CLASSIC_MAP.put("[bigem:19:]", R.mipmap.exp_egg_19);
        EMOTION_CLASSIC_MAP.put("[bigem:20:]", R.mipmap.exp_egg_20);


        /**
         * 第二组表情
         */
        EMOTTON_AM_MAP = new ArrayMap<>();
        EMOTTON_AM_MAP.put("[em:1:]", R.mipmap.ee_1);
        EMOTTON_AM_MAP.put("[em:2:]", R.mipmap.ee_2);
        EMOTTON_AM_MAP.put("[em:3:]", R.mipmap.ee_3);
        EMOTTON_AM_MAP.put("[em:4:]", R.mipmap.ee_4);
        EMOTTON_AM_MAP.put("[em:5:]", R.mipmap.ee_5);
        EMOTTON_AM_MAP.put("[em:6:]", R.mipmap.ee_6);
        EMOTTON_AM_MAP.put("[em:7:]", R.mipmap.ee_7);
        EMOTTON_AM_MAP.put("[em:8:]", R.mipmap.ee_8);
        EMOTTON_AM_MAP.put("[em:9:]", R.mipmap.ee_9);
        EMOTTON_AM_MAP.put("[em:10:]", R.mipmap.ee_10);
        EMOTTON_AM_MAP.put("[em:11:]", R.mipmap.ee_11);
        EMOTTON_AM_MAP.put("[em:12:]", R.mipmap.ee_12);
        EMOTTON_AM_MAP.put("[em:13:]", R.mipmap.ee_13);
        EMOTTON_AM_MAP.put("[em:14:]", R.mipmap.ee_14);
        EMOTTON_AM_MAP.put("[em:15:]", R.mipmap.ee_15);
        EMOTTON_AM_MAP.put("[em:16:]", R.mipmap.ee_16);
        EMOTTON_AM_MAP.put("[em:17:]", R.mipmap.ee_17);
        EMOTTON_AM_MAP.put("[em:18:]", R.mipmap.ee_18);
        EMOTTON_AM_MAP.put("[em:19:]", R.mipmap.ee_19);
        EMOTTON_AM_MAP.put("[em:20:]", R.mipmap.ee_20);
        EMOTTON_AM_MAP.put("[em:21:]", R.mipmap.ee_21);
        EMOTTON_AM_MAP.put("[em:22:]", R.mipmap.ee_22);
        EMOTTON_AM_MAP.put("[em:23:]", R.mipmap.ee_23);
        EMOTTON_AM_MAP.put("[em:24:]", R.mipmap.ee_24);
        EMOTTON_AM_MAP.put("[em:25:]", R.mipmap.ee_25);
        EMOTTON_AM_MAP.put("[em:26:]", R.mipmap.ee_26);
        EMOTTON_AM_MAP.put("[em:27:]", R.mipmap.ee_27);
        EMOTTON_AM_MAP.put("[em:28:]", R.mipmap.ee_28);
        EMOTTON_AM_MAP.put("[em:29:]", R.mipmap.ee_29);
        EMOTTON_AM_MAP.put("[em:30:]", R.mipmap.ee_30);
        EMOTTON_AM_MAP.put("[em:31:]", R.mipmap.ee_31);
        EMOTTON_AM_MAP.put("[em:32:]", R.mipmap.ee_32);
        EMOTTON_AM_MAP.put("[em:33:]", R.mipmap.ee_33);
        EMOTTON_AM_MAP.put("[em:34:]", R.mipmap.ee_34);


        /*第三组表情*/
        EMOTTON_PHONEIX = new ArrayMap<>();
        EMOTTON_PHONEIX.put("[bigfh:1:]", R.mipmap.exp_fh_small_01);
        EMOTTON_PHONEIX.put("[bigfh:2:]", R.mipmap.exp_fh_small_02);
        EMOTTON_PHONEIX.put("[bigfh:3:]", R.mipmap.exp_fh_small_03);
        EMOTTON_PHONEIX.put("[bigfh:4:]", R.mipmap.exp_fh_small_04);
        EMOTTON_PHONEIX.put("[bigfh:5:]", R.mipmap.exp_fh_small_05);
        EMOTTON_PHONEIX.put("[bigfh:6:]", R.mipmap.exp_fh_small_06);
        EMOTTON_PHONEIX.put("[bigfh:7:]", R.mipmap.exp_fh_small_07);
        EMOTTON_PHONEIX.put("[bigfh:8:]", R.mipmap.exp_fh_small_08);
        EMOTTON_PHONEIX.put("[bigfh:9:]", R.mipmap.exp_fh_small_09);
        EMOTTON_PHONEIX.put("[bigfh:10:]", R.mipmap.exp_fh_small_10);
        EMOTTON_PHONEIX.put("[bigfh:11:]", R.mipmap.exp_fh_small_11);
        EMOTTON_PHONEIX.put("[bigfh:12:]", R.mipmap.exp_fh_small_12);
        EMOTTON_PHONEIX.put("[bigfh:13:]", R.mipmap.exp_fh_small_13);
        EMOTTON_PHONEIX.put("[bigfh:14:]", R.mipmap.exp_fh_small_14);
        EMOTTON_PHONEIX.put("[bigfh:15:]", R.mipmap.exp_fh_small_15);
        EMOTTON_PHONEIX.put("[bigfh:16:]", R.mipmap.exp_fh_small_16);
        EMOTTON_PHONEIX.put("[bigfh:17:]", R.mipmap.exp_fh_small_17);
        EMOTTON_PHONEIX.put("[bigfh:18:]", R.mipmap.exp_fh_small_18);
        EMOTTON_PHONEIX.put("[bigfh:19:]", R.mipmap.exp_fh_small_19);
        EMOTTON_PHONEIX.put("[bigfh:20:]", R.mipmap.exp_fh_small_20);
        EMOTTON_PHONEIX.put("[bigfh:21:]", R.mipmap.exp_fh_small_21);
        EMOTTON_PHONEIX.put("[bigfh:22:]", R.mipmap.exp_fh_small_22);
        EMOTTON_PHONEIX.put("[bigfh:23:]", R.mipmap.exp_fh_small_23);
        EMOTTON_PHONEIX.put("[bigfh:24:]", R.mipmap.exp_fh_small_24);


        EMPTY_TOTAL.putAll((SimpleArrayMap<? extends String, ? extends Integer>) EMOTION_CLASSIC_MAP);
        EMPTY_TOTAL.putAll((SimpleArrayMap<? extends String, ? extends Integer>) EMOTTON_AM_MAP);
        EMPTY_TOTAL.putAll((SimpleArrayMap<? extends String, ? extends Integer>) EMOTTON_PHONEIX);
    }

    /**
     * 根据名称获取当前表情图标R值
     *
     * @param EmotionType 表情类型标志符
     * @param imgName     名称
     * @return
     */
    public static int getImgByName(int EmotionType, String imgName) {
        Integer integer = null;
        switch (EmotionType) {
            case EMOTION_TOTAL:
                integer = EMPTY_TOTAL.get(imgName);
                break;
            case EMOTION_CLASSIC_TYPE:
                integer = EMOTION_CLASSIC_MAP.get(imgName);
                break;
            case EMOTTON_AM_TYPE:
                integer = EMOTTON_AM_MAP.get(imgName);
                break;
            case EMOTTON_PHONEIX_TYPE:
                integer = EMOTTON_PHONEIX.get(imgName);
                break;
            default:
                LogUtils.d("EmotionUtils", "the emojiMap is null!!");
                break;
        }
        return integer == null ? -1 : integer;
    }

    /**
     * 根据类型获取表情数据
     *
     * @param EmotionType
     * @return
     */
    public static ArrayMap<String, Integer> getEmojiMap(int EmotionType) {
        ArrayMap EmojiMap = null;
        switch (EmotionType) {
            case EMOTION_CLASSIC_TYPE:
                EmojiMap = EMOTION_CLASSIC_MAP;
                break;
            case EMOTTON_AM_TYPE:
                EmojiMap = EMOTTON_AM_MAP;
                break;
            case EMOTTON_PHONEIX_TYPE:
                EmojiMap = EMOTTON_PHONEIX;
                break;
            default:
                EmojiMap = EMPTY_MAP;
                break;
        }
        return EmojiMap;
    }
}
