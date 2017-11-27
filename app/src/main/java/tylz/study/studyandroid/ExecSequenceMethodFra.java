package tylz.study.studyandroid;

import framework.simplecomm.TextViewFragment;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/28
 *  @描述：
 */
public class ExecSequenceMethodFra extends TextViewFragment {
    public ExecSequenceMethodFra(){
        initTitleBar("父类子类复写方法执行顺序",true,false);
    }

    @Override
    protected void initData() {
        super.initData();
        C c = new C();
        c.show();
    }

    class C extends B{
        @Override
        public void show() {
           printF("C show() 在super.show()前");
            super.show();
            printF("C show() 在super.show()后");
        }
    }
     class B extends A{
        @Override
        public void show() {
            printF("B show() 在super.show()前");
            super.show();
            printF("B show() 在super.show()后");
        }
    }
     class A{
        public void show(){
            printF("A show()");
        }
    }
}
