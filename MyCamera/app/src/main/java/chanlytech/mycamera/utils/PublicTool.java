package chanlytech.mycamera.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import chanlytech.mycamera.apploction.MyApploction;


public class PublicTool
{
    
    /**
     * 获取设备宽度
     * 
     * @return
     */
    public static int getDeviceWidth()
    {
        return MyApploction.getApp().getResources().getDisplayMetrics().widthPixels;

    }
    
    /**
     * 获取设备高度
     * 
     * @return
     */
    public static int getDeviceHeight()
    {
        return MyApploction.getApp().getResources().getDisplayMetrics().heightPixels;
    }


    

    public static void closeKeyBoard(Context context)
    {
        /**隐藏软键盘**/
        View view = ((Activity)context).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) ((Activity)context).getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    
}
