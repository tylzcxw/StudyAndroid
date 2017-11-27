package tylz.study.studyjava;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/7/26
 * @描述: 正则表达式学习
 */

public class StudyRegex1 {
    public static void main(String[] args){
        String s = formatStr2TwoPoint("0.10");
        System.out.print(s);
    }
    public static String formatStr2TwoPoint(String str){
        if (TextUtils.isEmpty(str)){
            return "";
        }
        try {
            double doubleStr=Double.parseDouble(str);
            if (doubleStr==0){
                return "0.00";
            }
            String formatStr = new DecimalFormat("#.00").format(doubleStr);
            return formatStr;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * 邮箱验证
     */
    private void emailRegex(){
        //要验证的字符串
        String str = "service@xsoftlab.net";
        //邮箱验证规则
        String regex = "[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-Z0-9]-*))";
    }
}
