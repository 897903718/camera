package chanlytech.mycamera.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.util.List;
import chanlytech.mycamera.R;
import chanlytech.mycamera.adapter.ListDirAdapter;
import chanlytech.mycamera.bean.ImageFloder;
import chanlytech.mycamera.utils.PublicTool;

/**
 * Created by Lyy on 2016/4/27.
 */
public class ListImageDirPopupWindow extends PopupWindow {
    private int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;
    private List<ImageFloder> mImageFloders;
    private ListDirAdapter mListDirAdapter;
    public interface  OnDirSelectedListener{
        void onSelect(ImageFloder floder);
    }

    public OnDirSelectedListener mListener;


    public void setOnDirSelectedListener(OnDirSelectedListener mListener) {
        this.mListener = mListener;
    }

    public  ListImageDirPopupWindow(Context context, List<ImageFloder> imageFloders) {
        this.mImageFloders = imageFloders;
        //获取屏幕的宽高
        mWidth = PublicTool.getDeviceWidth();
        mHeight = (int) (PublicTool.getDeviceHeight() * 0.7);
        mConvertView = LayoutInflater.from(context).inflate(R.layout.list_dir, null);
        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        //设置点击外部区域消失
        setBackgroundDrawable(new BitmapDrawable());
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
//        setOnDirSelectedListener(mListener);
        initView(context);
        initEvent();
    }

    private void initView(Context context){
        mListView= (ListView)mConvertView.findViewById(R.id.id_list_dir);
        mListDirAdapter=new ListDirAdapter(context,mImageFloders);
        mListView.setAdapter(mListDirAdapter);
    }

    private void initEvent(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mListener!=null){
                    mListener.onSelect(mImageFloders.get(position));
                }
            }
        });
    }

}
