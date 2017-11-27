package framework.simplecomm;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import framework.utils.LogUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/29
 *  @描述：    TODO
 */
public class IndexListViewFragment extends ListViewFragment {
    //datasource 以关键字母名称开头
    //className=  包名.subTitle + index + "Fra"
    //BaiduMap学习
    //xx.??.BaiduMap0Fra
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        String   title = getAdapter().getItem(position);
        String[] split = title.split("-");
        //split[0] = BaiduMap
        //split[1] = 1
        int index = -1;
        try {
            index = Integer.parseInt(split[1]);
        } catch (Exception e) {
            index = -1;
        }
        StringBuffer className = new StringBuffer();
        String       strIndex  = index == -1 ? "" : index + "";
        className.append(onCreatePackageName()); //com.tylz.studythree
        if(isAddHigherLevel()){
            if(TextUtils.isEmpty(getHigherLevelName())){
                className.append(split[0].toLowerCase() + "."); //loadsir
            }else{
                className.append(getHigherLevelName() + "."); //getHigherLevelName()
            }
        }
        className.append(split[0] + strIndex + "Fra");//LoadSirFra
        LogUtils.debug("position = " + position + "  className = " + className);
        try {
            Class<?> clazz = Class.forName(className.toString());
            startAcitvity(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected String getHigherLevelName() {
        return null;
    }

    protected String onCreatePackageName() {
        String allClassName = getClass().getName();
        String className    = getClass().getSimpleName();
        return allClassName.replace(className, "");
    }

    /**
     * 是否添加上一级包名
     * @return
     */
    protected boolean isAddHigherLevel() {
        return true;
    }
}
