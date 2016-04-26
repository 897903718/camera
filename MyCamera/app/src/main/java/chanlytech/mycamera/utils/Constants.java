package chanlytech.mycamera.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by wsx on 2015/7/14.
 */
public class Constants {
    public static String IMAGE_PATH;
    public static String TYPE_ID=null;//咨询问题id
    public static String TYPE_NAME=null; //咨询问题名字
    public static String DOWN_URL=null; //APK下载路径
    public static boolean state; //用于判断在首页点击更多案列后，在咨询界面选中案列分析
    public static boolean state_address; //用于判断在首页到切换地区后，是否选择了地区
    public static boolean flag_state; //用于判断在切换地区后在个人中心是否保存了，保存了就存储
    public static String TYPE;
    public static int flag=0;
    private static long mFirstClickTime = 0;
    public static String getSDPath(){
        File sdDir = null;
        if (isExistSD()){
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }else{
            sdDir = Environment.getRootDirectory();
        }
        return sdDir.toString()+"/laodongshebao/user_icon";
    }

    public static boolean isExistSD(){
        return Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
    }
    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - mFirstClickTime < 2000) {
            return true;
        }
        mFirstClickTime = time;
        return false;
    }

}
