package tylz.study.studythree.ormlite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import framework.app.BaseFragment;
import tylz.study.R;
import tylz.study.studythree.ormlite.ormlite2.A;
import tylz.study.studythree.ormlite.ormlite2.ADao;
import tylz.study.studythree.ormlite.ormlite2.B;
import tylz.study.studythree.ormlite.ormlite2.BDao;
import tylz.study.studythree.ormlite.ormlite2.C;
import tylz.study.studythree.ormlite.ormlite2.CDao;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/9
 *  @描述：    使用ormlite升级数据库
 */
public class OrmLite2Fra extends BaseFragment {
    @BindView(R.id.btn_create) Button mBtnCreate;

    public OrmLite2Fra() {
        initTitleBar("OrmLite数据库升级学习");
    }

    @Override
    protected View onCreateRootView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fra_ormlite2, container, false);
        return view;
    }


    @OnClick(R.id.btn_create)
    public void clickCreate() {
        ADao aDao = new ADao(getActivity(),1);
        A    a    = new A();
        a.name = "a";
        aDao.add(a);

        BDao bDao = new BDao(getActivity(),1);
        B    b    = new B();
        b.name = "b";
        b.age = "18";
        bDao.add(b);
    }

    @OnClick(R.id.btn_create2)
    public void clickCreate2() {
        ADao aDao = new ADao(getActivity(), 2);
        A    a    = new A();
        a.name = "a";
        a.age = "12";
        aDao.add(a);

        BDao bDao = new BDao(getActivity(), 2);
        B    b    = new B();
        b.name = "b";
        b.age = "18";
        b.sex = "男";
        bDao.add(b);
    }
    @OnClick(R.id.btn_create3)
    public void clickCreate3(){
        CDao cDao = new CDao(getActivity(),3);
        C    c    = new C();
        c.age = "22";
        c.name = "cxw";
        cDao.add(c);
    }
    @OnClick(R.id.btn_create4)
    public void clickCreate4(){
        CDao cDao = new CDao(getActivity(),4);
        C    c    = new C();
        c.age = "24";
        c.name = "增加other";
        cDao.add(c);
    }
    @OnClick(R.id.btn_create5)
    public void clickCreate5(){
        CDao cDao = new CDao(getActivity(),5);
        C    c    = new C();
        c.age = "5";
        c.name = "删除other";
        cDao.add(c);
    }
}
