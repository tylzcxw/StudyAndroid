package tylz.study.studycustomwidget;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import framework.simplecomm.ListViewFragment;
import framework.ui.ListPopSelecttor;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/23
 *  @描述：    TODO
 */
public class ListPopSelecttorFra extends ListViewFragment {
    private String[] mLetters = new String[]{"A","B","C","D",
                                             "E","F","G","H",
                                             "I","J","K","L",
                                             "M","N","O","P",
                                             "Q","R","S","T",
                                             "U","V","W","X",
                                             "Y","Z",};

    private List<List<String>> contentList= new ArrayList<>();


    public ListPopSelecttorFra(){
        initTitleBar("列表选择搜索");

    }

    @Override
    protected List<String> onCreateDataSource() {
        ArrayList<String> datas = new ArrayList<>();
        datas.add("ListPopSelecttor使用");
        return datas;
    }

    @Override protected void initData() {
        super.initData();
        List<String> a=new ArrayList<>();
        a.add("aa");
        a.add("ab");
        List<String> b=new ArrayList<>();
        b.add("ba");
        b.add("bb");
        contentList.add(a);
        contentList.add(b);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
         switch (position){
              case 0:
                 // clickListPopSelecttor();
                  break;
              case 1:
                  break;
              default:
                  break;
          }
    }

    private void clickListPopSelecttor() {
        List<String> letterList = Arrays.asList(mLetters);


        ListPopSelecttor listPopSelecttor = new ListPopSelecttor(getActivity());
        listPopSelecttor.setTitle("联系人选择");
        listPopSelecttor.setNeedSearch(true);
        listPopSelecttor.setShowSideBar(true);
        listPopSelecttor.setContentList(contentList);
        listPopSelecttor.setTitleList(letterList);
        listPopSelecttor.show();
    }
}
