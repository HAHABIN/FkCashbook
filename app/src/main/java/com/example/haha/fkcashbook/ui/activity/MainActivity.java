package com.example.haha.fkcashbook.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.haha.fkcashbook.R;
import com.example.haha.fkcashbook.base.BaseActivity;
import com.example.haha.fkcashbook.common.Constants;
import com.example.haha.fkcashbook.model.bean.Kfaccount;
import com.example.haha.fkcashbook.model.bean.local.BSort;
import com.example.haha.fkcashbook.model.bean.local.NoteBean;
import com.example.haha.fkcashbook.ui.adapter.FragmentAdapter;
import com.example.haha.fkcashbook.ui.fragment.BillsFragment;
import com.example.haha.fkcashbook.ui.fragment.ChartFragment;
import com.example.haha.fkcashbook.ui.fragment.DetailedFragment;
import com.example.haha.fkcashbook.utils.IntentUtils;
import com.example.haha.fkcashbook.utils.SharedPUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.main_drawer_ly)
    DrawerLayout mainDrawerLy;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.main_cl)
    LinearLayout mainCl;



    //侧滑栏头部
    private View drawerHeader;
    private TextView drawerTvName,drawerTvmail;
    protected static final int USERINFOACTIVITY_CODE = 0;
    protected static final int LOGINACTIVITY_CODE = 1;
    //Fragment适配器
    private FragmentAdapter mFragAdapter;
    //Tab 文字
    private final int[] TAB_TITLES = new int[]{R.string.detailed,R.string.chart,R.string.bills};
    //Tab 图片
    private final int[] TAB_IMGS = new int[]{R.drawable.tab_detailed_selector,R.drawable.tab_chart_selector,R.drawable.tab_bills_selector};
    //Fragment 数组
    private List<Fragment> listFragment = new ArrayList<>();
//            TAB_FRAGMENTS = new Fragment[] {new DetailedFragment(),new ChartFragment(),new Billsragment()};
    //Tab 数目
    private final int COUNT = TAB_TITLES.length;


    //记住密码
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    boolean isRemember;

    private Kfaccount currentUser;
    //启动页面跳转
    public static Intent newInstance(Context lunchActivity) {
        return new Intent(lunchActivity, MainActivity.class);
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //判断是否登录
        currentUser = setDrawerHeader();

        //第一次进入将默认账单分类添加到数据库
        if (SharedPUtils.isFirstStart(mContext)) {
            NoteBean note = new Gson().fromJson(Constants.BILL_NOTE, NoteBean.class);
            List<BSort> sorts = note.getOutSortlis();
            sorts.addAll(note.getInSortlis());
            getLocalRepository().saveBsorts(sorts);
            getLocalRepository().saveBPays(note.getPayinfo());
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();


        //初始化Toolbar
        mainToolbar.setTitle("酷狐记账");
        setSupportActionBar(mainToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mainDrawerLy, mainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainDrawerLy.setDrawerListener(toggle);
        toggle.syncState();

        drawerHeader = navView.getHeaderView(0);
        drawerTvName = drawerHeader.findViewById(R.id.drawer_tv_name);
        drawerTvmail = drawerHeader.findViewById(R.id.drawer_tv_mail);
        setTabs(mTabLayout,this.getLayoutInflater(),TAB_TITLES,TAB_IMGS);


        //设置用户信息
        setDrawerHeaderAccount();

        //把Fragment添加到List集合里面
        listFragment.add(new DetailedFragment());
        listFragment.add(new ChartFragment() );
        listFragment.add(new BillsFragment() );
        //Fragment适配器
        mFragAdapter = new FragmentAdapter(getSupportFragmentManager(),listFragment);
        mViewPager.setAdapter(mFragAdapter);
        //TabLayout与ViewPager的绑定
        //左右滑动
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        //点击
        mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    protected void initClick() {
        super.initClick();
        navView.setNavigationItemSelectedListener(this);
        //侧滑头部点击事件
        drawerHeader.setOnClickListener(v -> {
           if (currentUser==null){
                startActivityForResult(new Intent(mContext, LandActivity.class), LOGINACTIVITY_CODE);
            }else{
                startActivityForResult(new Intent(mContext, UserinfoActivity.class), USERINFOACTIVITY_CODE);
            }
        });
    }
    /***************************************************************************/
    /**
     * 设置toolbar右侧菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    //Toolbar点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_date:
                Toast.makeText(mContext, "日历", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                mainDrawerLy.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_setting:
                IntentUtils.SetIntent(this, BillSortActivity.class);
                break;
            case R.id.nav_about:
                drawerTvName.setText("zhang");
                break;
            case R.id.nav_exit:
                exitUser();
                break;

        }
        mainDrawerLy.closeDrawers();
        return true;
    }

    /**
     * 设置DrawerHeader的用户信息
     */
    public void setDrawerHeaderAccount() {

//        获取当前用户
        if (currentUser != null) {
            drawerTvName.setText(currentUser.getUsername());
            drawerTvmail.setText(currentUser.getKfmail());

        } else {
            drawerTvName.setText("账号");
            drawerTvmail.setText("点我登陆");
        }
    }

    /**
     * @description: 设置添加底部Tab
     */
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, int[] tabTitlees, int[] tabImgs){
        for (int i = 0; i < tabImgs.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = inflater.inflate(R.layout.tab_item,null);
            tab.setCustomView(view);
            TextView tvTitle = view.findViewById(R.id.tab_tv_des);
            tvTitle.setText(tabTitlees[i]);
            ImageView imgTab = view.findViewById(R.id.tab_iv_img);
            imgTab.setImageResource(tabImgs[i]);
            tabLayout.addTab(tab);

        }
    }

    /**
     * 监听Activity返回值
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case USERINFOACTIVITY_CODE:
                    setDrawerHeaderAccount();
                    break;
                case LOGINACTIVITY_CODE:
                    setDrawerHeaderAccount();
                    break;
            }
        }
    }

    /**
     * 退出登陆 Dialog
     */
    private void exitUser(){
        new MaterialDialog.Builder(mContext)
                .title("确认退出")
                .content("退出后将清空所有数据")
                .positiveText("确定")
                .onPositive((dialog, which) -> {
                    pref = getSharedPreferences("User",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    currentUser = null;
                    //重启
                    finish();
                    Intent intent = getIntent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                })
                .negativeText("取消")
                .show();
    }
}
