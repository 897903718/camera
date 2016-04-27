package chanlytech.mycamera.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chanlytech.mycamera.R;
import chanlytech.mycamera.adapter.ImageAdaper;
import chanlytech.mycamera.bean.ImageFloder;
import chanlytech.mycamera.view.ListImageDirPopupWindow;

public class LibraryActivity extends AppCompatActivity implements View.OnClickListener {
    private GridView mGridView;
    private List<String> mImgs;
    private TextView mDirName;
    private TextView mDirCount;
    private RelativeLayout mBottomLy;
    private File mCurrentDir;
    private int maxDirCount;
    private ProgressDialog mProgressDialog;
    private ImageAdaper mImageAdaper;
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
    private static final int DATA_LOADED = 0X110;
    private ListImageDirPopupWindow mDirPopupWindow;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DATA_LOADED) {
                //扫描完成
                mProgressDialog.dismiss();
                //绑定数据
                bindingData();
                //初始化popWind
                initDirPopWind();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        initView();
        initDatas();
        initEvent();

    }

    private void initEvent() {
        mBottomLy.setOnClickListener(this);
    }

    private void initDirPopWind() {
        mDirPopupWindow = new ListImageDirPopupWindow(this, mImageFloders);
        mDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        mDirPopupWindow.setOnDirSelectedListener(new ListImageDirPopupWindow.OnDirSelectedListener() {
            @Override
            public void onSelect(ImageFloder floder) {
                mCurrentDir=new File(floder.getDir());
                mImgs=Arrays.asList(mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }));
                mImageAdaper = new ImageAdaper(LibraryActivity.this, mImgs, mCurrentDir.getAbsolutePath());
                mGridView.setAdapter(mImageAdaper);
                mDirCount.setText(mImgs.size() + "");
                mDirName.setText(floder.getName());
                mDirPopupWindow.dismiss();
            }
        });
    }

    /**
     * 内容区域变亮
     **/
     private void lightOn(){
         WindowManager.LayoutParams lp=getWindow().getAttributes();
         lp.alpha=1.0f;
         getWindow().setAttributes(lp);
     }
    /**
     * 内容区域变暗
     **/
    private void lightOff(){
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.3f;
        getWindow().setAttributes(lp);
    }

    private void bindingData() {
        if (mCurrentDir == null) {
            Toast.makeText(this, "一张图片也没有", Toast.LENGTH_LONG).show();
            return;
        }
        mImgs = Arrays.asList(mCurrentDir.list());
        mImageAdaper = new ImageAdaper(this, mImgs, mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(mImageAdaper);
        mDirCount.setText(maxDirCount + "");
        mDirName.setText(mCurrentDir.getName());
    }

    /**
     * 利用ContentResolver扫描手机中的图片
     */
    private void initDatas() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressDialog = ProgressDialog.show(this, null, "正在加载....");
        new Thread() {
            @Override
            public void run() {
                Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = LibraryActivity.this.getContentResolver();
                // 只查询jpeg和png的图片
                Cursor mCursor = cr.query(mImgUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                Set<String> mDirPaths = new HashSet<String>();
                while (mCursor.moveToNext()) {
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.e("TAG", path);
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    //防止重复扫描
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    if (parentFile.list() == null)
                        continue;
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }).length;
                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);
                    if (picSize > maxDirCount) {
                        maxDirCount = picSize;
                        mCurrentDir = parentFile;
                    }
                }
                //关闭游标
                mCursor.close();
                //通知Handler扫描图片完成
                mHandler.sendEmptyMessage(DATA_LOADED);
            }
        }.start();

    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.id_gridView);
        mDirName = (TextView) findViewById(R.id.id_choose_dir);
        mDirCount = (TextView) findViewById(R.id.id_total_count);
        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_bottom_ly:
                mDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
                mDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);
                lightOff();
                break;
        }
    }
}
