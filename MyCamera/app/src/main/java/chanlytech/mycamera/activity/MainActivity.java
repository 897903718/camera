package chanlytech.mycamera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import chanlytech.mycamera.R;
import chanlytech.mycamera.activity.GalleryActivity;
import chanlytech.mycamera.utils.PictureUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mButton1, mButton2;
    private ImageView mImageView;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton2 = (Button) findViewById(R.id.btn_2);
        mButton1 = (Button) findViewById(R.id.btn_1);
        mImageView = (ImageView) findViewById(R.id.img);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                mIntent = new Intent(this, CameraActivity.class);
                mIntent.putExtra("type",1);
                startActivity(mIntent);
                break;
            case R.id.btn_2:
                mIntent = new Intent(this, GalleryActivity.class);
//                startActivity(mIntent);
                startActivityForResult(mIntent, 900);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 900:
                if (data != null) {
                    Bitmap mBitmap = PictureUtil.getSmallBitmap(data.getStringExtra("path"));
                    mImageView.setImageBitmap(mBitmap);
                    Toast.makeText(this, data.getStringExtra("path"), Toast.LENGTH_LONG).show();
                }
                break;
            case 1000:
                if(data!=null){
                  Uri uri = data.getData();
                    mImageView.setImageURI(uri);
                }
                break;
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(getIntent().getStringExtra("path")!=null){
//            Bitmap mBitmap = PictureUtil.getSmallBitmap(getIntent().getStringExtra("path"));
//            mImageView.setImageBitmap(mBitmap);
//        }
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getStringExtra("path") != null){
            Bitmap mBitmap = PictureUtil.getSmallBitmap(intent.getStringExtra("path"));
            mImageView.setImageBitmap(mBitmap);
        }
    }
}
