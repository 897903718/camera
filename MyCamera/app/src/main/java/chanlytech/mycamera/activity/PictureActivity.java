package chanlytech.mycamera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import chanlytech.mycamera.R;
import chanlytech.mycamera.utils.Constants;
import chanlytech.mycamera.utils.PictureUtil;
import chanlytech.mycamera.utils.PublicTool;

public class PictureActivity extends AppCompatActivity implements View.OnClickListener {
    private Uri uri;
    private Bitmap nBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Intent intent = getIntent();
        uri = intent.getData();
        Bitmap smallbitmap=PictureUtil.getSmallBitmap(uri.getPath());
//

        ImageView btn_save = (ImageView) findViewById(R.id.btn_save);
        ImageView btn_del = (ImageView) findViewById(R.id.btn_del);
        ImageView pic = (ImageView) findViewById(R.id.iv_pic);
        boolean portrait=intent.getBooleanExtra("portrait",true);
        Toast.makeText(this,"传递过来的值"+portrait,Toast.LENGTH_LONG).show();
        if(portrait){//竖屏旋转90
            nBitmap=rotaingImageView(90,smallbitmap);
            pic.setImageBitmap(nBitmap);
        }else {//横屏旋转360
            nBitmap=rotaingImageView(360,smallbitmap);
            pic.setImageBitmap(nBitmap);
        }
//        pic.setImageURI(uri);
        btn_save.setOnClickListener(this);
        btn_del.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_del) {
            File file = new File(uri.getPath());
            file.delete();
            finish();
        }else if(v.getId()==R.id.btn_save){
            String path=saveScalePhoto(nBitmap);
            Intent mIntent=new Intent(this,MainActivity.class);
            mIntent.putExtra("path",path);
            startActivity(mIntent);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this,"返回删除预览图",Toast.LENGTH_LONG).show();
        File file = new File(uri.getPath());
        file.delete();
        finish();
    }
    /**
     * 根据路径来判断图片的方向
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 保存图片
     */
    public static String saveScalePhoto(Bitmap bitmap) {
        String fileName = ""; // 照片完整路径  
        if (bitmap != null) {
            // 文件夹路径  
            String pathUrl = Constants.getSDPath() + "/";
            String imageName = "IMG_" + System.currentTimeMillis() + ".jpg";
            FileOutputStream fos = null;
            File file = new File(pathUrl);
            file.mkdirs();// 创建文件夹  
            fileName = pathUrl + imageName;
            try {
                fos = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileName;
    }
    /*
       * 旋转图片
       * @param angle
       * @param bitmap
       * @return Bitmap
       */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作  
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片  
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }
    /**
     * 根据图片路径获取图片的宽高比在屏幕中动态显示图片
     * */
    public static int getHeight(String filePath){
        Bitmap bitmap= BitmapFactory.decodeFile(filePath);
        int width= PublicTool.getDeviceWidth();
        double size= Double.valueOf(bitmap.getWidth())/Double.valueOf(bitmap.getHeight());
        double height=Double.valueOf(width - 40)/size;
        int h;
        h=(int)height;
        bitmap.recycle();
        return h;
    }
    /***
     * 根据图片的宽和搞的比较判断是横屏拍摄还是竖屏拍摄
     * return ture--竖屏 false横屏
     * */
    private boolean portraitOrlandscape(String filePath){
        boolean state=true;
        Bitmap bitmap= BitmapFactory.decodeFile(filePath);
        if(bitmap.getWidth()>bitmap.getHeight()){
            state=false;
            bitmap.recycle();
            return state;
        }else if(bitmap.getWidth()<bitmap.getHeight()){
            state=true;
            bitmap.recycle();
            return state;
        }
        return state;
    }
}
