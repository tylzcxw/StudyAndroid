package framework.simplecomm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import framework.adapter.QuickAdapter;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/22 17:29
 *  @描述：    TODO
 */
public class StrCommAdapter
        extends QuickAdapter<String> {
    public StrCommAdapter(Context context){
        super(context);
    }
    public StrCommAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1,null);
        }
        ((TextView) convertView).setText(getDataSource().get(position));
        return convertView;
    }
}
