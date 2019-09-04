package com.example.haha.fkcashbook.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.example.haha.fkcashbook.R;
import com.example.haha.fkcashbook.base.BaseFragment;
import com.example.haha.fkcashbook.model.bean.local.BBill;
import com.example.haha.fkcashbook.model.bean.local.MonthListBean;
import com.example.haha.fkcashbook.ui.activity.BillAddActivity;
import com.example.haha.fkcashbook.ui.adapter.MonthListAdapter;
import com.example.haha.fkcashbook.utils.BillUtils;
import com.example.haha.fkcashbook.utils.DateUtils;
import com.example.haha.fkcashbook.utils.IntentUtils;
import com.example.haha.fkcashbook.view.stickyheader.StickyHeaderGridLayoutManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.haha.fkcashbook.utils.DateUtils.FORMAT_M;
import static com.example.haha.fkcashbook.utils.DateUtils.FORMAT_Y;
/**
 * A simple {@link Fragment} subclass.
 * 明细
 */
public class DetailedFragment extends BaseFragment {


    @BindView(R.id.data_year)
    TextView dataYear;
    @BindView(R.id.data_month)
    TextView dataMonth;
    @BindView(R.id.layout_data)
    LinearLayout layoutData;
    @BindView(R.id.tv_current_income)
    TextView tvCurrentIncome;
    @BindView(R.id.tv_current_outcome)
    TextView tvCurrentOutcome;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.floatab_add)
    FloatingActionButton floatabAdd;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    Unbinder unbinder;

    private static final int SPAN_SIZE = 1;
    private String setYear = DateUtils.getCurYear(FORMAT_Y);
    private String setMonth = DateUtils.getCurMonth(FORMAT_M);
    private MonthListAdapter adapter;
    private List<MonthListBean.DaylistBean> list = null;
    private StickyHeaderGridLayoutManager mLayoutManager;
    public DetailedFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detailed;
    }

    @Override
    protected void initEventAndData() {

        //获取当前年月
        dataYear.setText(setYear+"\n年");
        dataMonth.setText(setMonth);
        //改变加载显示的颜色
        swipe.setColorSchemeColors(getResources().getColor(R.color.textred), getResources().getColor(R.color.textred));
        //设置向下拉多少出现刷新
        swipe.setDistanceToTriggerSync(200);
        //设置刷新出现的位置
        swipe.setProgressViewEndTarget(false, 200);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                setTestData();//恢复了假数据
            }
        });
        mLayoutManager = new StickyHeaderGridLayoutManager(SPAN_SIZE);
        mLayoutManager.setHeaderBottomOverlapMargin(5);

        // Workaround RecyclerView limitation when playing remove animations. RecyclerView always
        // puts the removed item on the top of other views and it will be drawn above sticky header.
        // The only way to fix this, abandon remove animations :(
        rvList.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                dispatchRemoveFinished(holder);
                return false;
            }
        });
        rvList.setLayoutManager(mLayoutManager);
        adapter = new MonthListAdapter(mContext, list);
        rvList.setAdapter(adapter);
        //test data---------------
        setTestData();
    }

    @Override
    protected void initClick() {
        super.initClick();
        //adapter的侧滑选项事件监听
        adapter.setOnStickyHeaderClickListener(new MonthListAdapter.OnStickyHeaderClickListener() {
            @Override
            public void OnDeleteClick(BBill item, int section, int offset) {
                item.setVersion(-1);
                //将删除的账单版本号设置为负，而非直接删除
                //便于同步删除服务器数据
                getLocalRepository().deleteBillsById(item.getId());
//                Log.d("WWWW", "OnDeleteClick: "+item.getId());
                setTestData();
            }

            @Override
            public void OnEditClick(BBill item, int section, int offset) {
                Intent intent = new Intent(mContext, BillAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", item.getId());
                bundle.putString("rid", item.getRid());
                bundle.putString("sortName", item.getSortName());
                bundle.putString("payName", item.getPayName());
                bundle.putString("content", item.getContent());
                bundle.putDouble("cost", item.getCost());
                bundle.putLong("date", item.getCrdate());
                bundle.putBoolean("income", item.isIncome());
                bundle.putInt("version", item.getVersion());
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, 0);
            }
        });
    }

    @OnClick({R.id.layout_data, R.id.floatab_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_data:
                //时间选择器
                new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        setYear = DateUtils.date2Str(date,FORMAT_Y);
                        setMonth = DateUtils.date2Str(date,FORMAT_M);
                        dataYear.setText(setYear+"\n年");
                        dataMonth.setText(setMonth);
                        setTestData();
                    }
                })
                        .setRangDate(null, Calendar.getInstance())
                        .setType(new boolean[]{true, true, false, false, false, false})
                        .build()
                        .show();
                break;
            case R.id.floatab_add:
                IntentUtils.SetIntent(getContext(), BillAddActivity.class);
                break;
        }
    }
    //恢复数据
    private void setTestData() {
        List<BBill> bBills = getLocalRepository().getBBillByUserIdWithYM(null,setYear,setMonth);
        MonthListBean data = BillUtils.packageDetailList(bBills);
        list = data.getDaylist();
        tvCurrentIncome.setText(data.getT_income());
        tvCurrentOutcome.setText(data.getT_outcome());
        adapter.setmDatas(list);
        adapter.notifyAllSectionsDataSetChanged();//需调用此方法刷新
    }
}
