package chanlytech.mycamera.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import chanlytech.mycamera.R;
import chanlytech.mycamera.bean.ImageFloder;
import chanlytech.mycamera.utils.ImageLoader;

/**
 * Created by Lyy on 2016/4/27.
 */
public class ListDirAdapter extends ArrayAdapter<ImageFloder> {
    private List<ImageFloder> mImageFloders;
    private LayoutInflater mInflater;

    public ListDirAdapter(Context context, List<ImageFloder> objects) {
        super(context,0,objects);
        mInflater = LayoutInflater.from(context);
        mImageFloders=objects;
    }

    @Override
    public int getCount() {
        return mImageFloders.size();
    }

    @Override
    public ImageFloder getItem(int position) {
        return mImageFloders.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_dir_item, parent, false);
            viewHolder.mImg = (ImageView) convertView.findViewById(R.id.id_dir_item_image);
            viewHolder.mDirName = (TextView) convertView.findViewById(R.id.id_dir_item_name);
            viewHolder.mDirCount = (TextView) convertView.findViewById(R.id.id_dir_item_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageFloder floder = getItem(position);
        viewHolder.mImg.setBackgroundResource(R.drawable.pic_dir);
        viewHolder.mDirName.setText(floder.getName());
        viewHolder.mDirCount.setText(floder.getCount()+"");
        ImageLoader.getImageLoader().loadImage(floder.getFirstImagePath(), viewHolder.mImg);
        return convertView;
    }


    private class ViewHolder {
        ImageView mImg;
        TextView mDirName;
        TextView mDirCount;
    }
}
