package com.example.musicapp.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicapp.R;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    Context context;
    int layout;
    List<String> srcList;

    public ImageAdapter(Context context, int layout, List<String> srcList) {
        this.context = context;
        this.layout = layout;
        this.srcList = srcList;
    }

    @Override
    public int getCount() {
        return srcList.size();
    }

    @Override
    public Object getItem(int i) {
        return srcList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        TextView name;
        ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            viewHolder = new ViewHolder();

            viewHolder.name = convertView.findViewById(R.id.name_item);
            viewHolder.image = convertView.findViewById(R.id.image_item);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(srcList.get(position).substring(29));
        viewHolder.image.setImageBitmap(BitmapFactory.decodeFile(srcList.get(position)));

        return convertView;
    }
}
