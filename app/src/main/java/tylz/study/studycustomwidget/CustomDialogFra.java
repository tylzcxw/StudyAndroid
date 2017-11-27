package tylz.study.studycustomwidget;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;

import com.tylz.myutils.comm.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import framework.simplecomm.ListViewFragment;
import framework.ui.AlertDialog;
import framework.ui.CcbDialog;
import framework.ui.LoadingDialog;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/16
 *  @描述：    TODO
 */
public class CustomDialogFra extends ListViewFragment {
    public CustomDialogFra() {
        initTitleBar("自定义弹窗");
    }

    @Override
    protected List<String> onCreateDataSource() {
        List<String> datas = new ArrayList<>();
        datas.add("弹出建行弹窗");
        datas.add("弹出建行Loading加载框");
        datas.add("弹出自定义弹窗");
        return datas;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        switch (position) {
            case 0:
                showCcbDialog();
                break;
            case 1:
                LoadingDialog.getInstance().showLoading();
                break;
            case 2:
                showAlertDialog();
                break;
            case 3:
                break;
            default:
                break;
        }
    }

    private void showAlertDialog() {
        AlertDialog alertDialog = new AlertDialog(getActivity()).builder();
        alertDialog.setCancelable(true)
                   .setMsg("自定义弹窗文本信息")
                   .setTitle("自定义弹窗标题")
                   .setNegativeButton("取消", new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           ToastUtils.showShortSafe("哈哈，取消");
                       }
                   })
                   .setPositiveButton("确定", new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           ToastUtils.showShortSafe("哈哈，确定");
                       }
                   })
                   ;
        alertDialog.show();
    }

    private void showCcbDialog() {
        CcbDialog.getInstance()
                 .showDialog(getActivity(),
                             "",
                             "测试弹窗",
                             "取消",
                             new CcbDialog.OnClickCancelListener() {
                                 @Override
                                 public void clickCancel(Dialog dialog) {
                                     ToastUtils.showShortSafe("取消");
                                 }
                             },
                             "确定",
                             new CcbDialog.OnClickConfirmListener() {

                                 @Override
                                 public void clickConfirm(Dialog dialog) {
                                     ToastUtils.showShortSafe("确定");
                                 }
                             },
                             true);
    }
}
