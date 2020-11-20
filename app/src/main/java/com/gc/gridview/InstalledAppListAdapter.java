package com.gc.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.gc.R;
import com.gc.home.models.PackageAppData;

public class InstalledAppListAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private List<PackageAppData> mInstalledApp;
    private Context mContext;

    public InstalledAppListAdapter(LayoutInflater mInflater, List<PackageAppData> mInstalledApp, Context mContext) {
        this.mInflater = mInflater;
        this.mInstalledApp = mInstalledApp;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mInstalledApp.size();
    }

    @Override
    public Object getItem(int i) {
        return mInstalledApp.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        View convertView = null;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.app_item,null);
            viewHolder=new ViewHolder();
            viewHolder.mAppName= (TextView) convertView.findViewById(R.id.text);
            viewHolder.mIcon= (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.mAppName.setText(mInstalledApp.get(i).name);
        viewHolder.mIcon.setImageDrawable(mInstalledApp.get(i).icon);
        return convertView;
    }


     class ViewHolder{
        TextView  mAppName;
        ImageView mIcon;
    }
}
