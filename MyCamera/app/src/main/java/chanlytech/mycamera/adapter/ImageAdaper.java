package chanlytech.mycamera.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chanlytech.mycamera.R;
import chanlytech.mycamera.utils.ImageLoader;

/**
 * Created by Lyy on 2016/4/27.
 */
public class ImageAdaper extends BaseAdapter {
    private Context mContext;
    private List<String> mImgPaths;
    private LayoutInflater mInflater;
    private String mDirPath;
    private static Set<String> mSeletedImg = new HashSet<String>();

    public ImageAdaper(Context context, List<String> mDads, String dirPath) {
        this.mDirPath = dirPath;
        this.mImgPaths = mDads;
        mInflater = LayoutInflater.from(context);
        this.mContext = context;

    }

    @Override
    public int getCount() {
        return mImgPaths.size();
    }

    @Override
    public Object getItem(int position) {
        return mImgPaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      final   ViewHolder viewHolder ;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.grid_item, parent, false);
            viewHolder.mImg = (ImageView) convertView.findViewById(R.id.id_item_image);
            viewHolder.mSelect = (ImageButton) convertView.findViewById(R.id.id_item_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //重置状态
        viewHolder.mImg.setImageResource(R.drawable.pictures_no);
        viewHolder.mSelect.setImageResource(R.drawable.picture_unselected);
        viewHolder.mImg.setColorFilter(null);
        ImageLoader.getImageLoader(3, ImageLoader.Type.FIFO).loadImage(mDirPath + "/" + mImgPaths.get(position), viewHolder.mImg);
       final String filePath = mDirPath + "/" + mImgPaths.get(position);
        viewHolder.mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //已经被选择
                if (mSeletedImg.contains(filePath)) {
                    mSeletedImg.remove(filePath);
                    viewHolder.mImg.setColorFilter(null);
                    viewHolder.mSelect.setImageResource(R.drawable.picture_unselected);
                } else//未被选择
                {
                    mSeletedImg.add(filePath);
                    viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
                    viewHolder.mSelect.setImageResource(R.drawable.pictures_selected);
                }
            }

        });
        if(mSeletedImg.contains(filePath)){
            viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
            viewHolder.mSelect.setImageResource(R.drawable.pictures_selected);
        }else {

        }
        return convertView;
    }

    private class ViewHolder {
        ImageView mImg;
        ImageButton mSelect;
    }
}
