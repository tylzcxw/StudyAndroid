package tylz.study.studycustomwidget;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import framework.app.BaseFragment;
import framework.ui.widget.CustomCalendar;
import framework.utils.ToastUtils;
import tylz.study.R;

/**
 * @author cxw
 * @date 2018/4/2
 * @des TODO
 */

public class CustomCalendarFra extends BaseFragment {
    public CustomCalendarFra(){
        initTitleBar("自定义日历控件");
    }
    @Override
    protected int onCreateRootView() {
        return R.layout.fra_custom_calendar;
    }

    @Override
    protected void initData() {
        super.initData();
        CustomCalendar customCalendar = findViewById(R.id.custom_calendar);

        customCalendar.setDayDesData(getMapData());
        customCalendar.setOnClickListener(new CustomCalendar.OnClickListener() {

            @Override
            public void onLeftRowClick() {
                customCalendar.monthChange(-1);
            }

            @Override
            public void onRightRowClick() {
                customCalendar.monthChange(1);
            }

            @Override
            public void onTitleClick(final String monthStr, final Date month) {

            }

            @Override
            public void onWeekClick(final int weekIndex, final String weekStr) {
                ToastUtils.showToast("星期点击了 weekIndex = " + weekIndex + "  weekStr = " + weekStr);
            }

            @Override
            public void onDayClick(final int day, final String dayStr, final CustomCalendar.DayDes dayDes) {
                ToastUtils.showToast("日期点击了 day = " + day + "  dayStr = " + dayStr + "   dayDes = " + dayDes);
            }
        });

    }
    private Map<Integer,CustomCalendar.DayDes> getMapData(){
        Map<Integer,CustomCalendar.DayDes> map = new HashMap<>();
        Random random = new Random();

        for(int i = 1;i<=30;i++){
            int nextInt = random.nextInt(3);
            if(nextInt == 0){
                map.put(i,new CustomCalendar.DayDes(2018,4,i,"休"));
            }else {
                map.put(i,new CustomCalendar.DayDes(2018,4,i,"班"));
            }
        }
        return map;
    }
}
