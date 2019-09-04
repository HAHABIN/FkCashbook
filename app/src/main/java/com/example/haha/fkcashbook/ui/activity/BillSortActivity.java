package com.example.haha.fkcashbook.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.haha.fkcashbook.R;
import com.example.haha.fkcashbook.base.BaseActivity;
import com.example.haha.fkcashbook.model.bean.LocalRepository;
import com.example.haha.fkcashbook.model.bean.local.BSort;
import com.example.haha.fkcashbook.model.bean.local.NoteBean;
import com.example.haha.fkcashbook.ui.adapter.BillSortAdapter;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BillSortActivity extends BaseActivity {

    @BindView(R.id.tb_note_income)
    TextView incomeTv;    //收入按钮
    @BindView(R.id.tb_note_outcome)
    TextView outcomeTv;   //支出按钮
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.back_btn)
    ImageView backBtn;
    @BindView(R.id.add_btn)
    ImageView addBtn;

    public boolean isOutcome = true;
    private BillSortAdapter billSortAdapter;

    private NoteBean noteBean;
    private List<BSort> mDatas;

    @Override
    protected int getLayout() {
        return R.layout.activity_base_list;
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

    }

    @Override
    protected void initWidget() {
        super.initWidget();
        noteBean = getNoteBean();
        billSortAdapter = new BillSortAdapter(mContext, mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(billSortAdapter);
        setTitleStatus();
    }


    @Override
    protected void initClick() {
        super.initClick();
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //首先回调的方法 返回int表示是否监听该方向
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//拖拽
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;//侧滑删除
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //滑动事件
                Collections.swap(mDatas, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                billSortAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                //更新账单分类排序
                saveBSorts();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int index = viewHolder.getAdapterPosition() -1 ;
                //侧滑事件
                new MaterialDialog.Builder(mContext)
                        .title("确定删除此分类")
                        .content("删除后该分类下的账单会继续保留")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(((dialog, which) -> {
                            mDatas.remove(index);
                            int count =billSortAdapter.getItemCount();
                            billSortAdapter.notifyItemRemoved(index+1);
                            //删除账单分类
                            count =billSortAdapter.getItemCount();
                            LocalRepository.getInstance().deleteBSortById(mDatas.get(index).getId());
                            //更新排序
                            saveBSorts();
                        }))
                        .show();
            }

            @Override
            public boolean isLongPressDragEnabled() {
                //是否可拖拽
                return true;
            }
        });

        helper.attachToRecyclerView(mRecyclerView);
    }

    @OnClick({R.id.back_btn, R.id.tb_note_outcome, R.id.tb_note_income, R.id.type_layout, R.id.add_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case R.id.tb_note_outcome://支出
                isOutcome = true;
                setTitleStatus();
                break;
            case R.id.tb_note_income://收入
                isOutcome = false;
                setTitleStatus();
                break;
            case R.id.add_btn:
                showContentDialog();
                break;
        }
    }

    /**
     * 设置状态栏
     */
    private void setTitleStatus() {
        if (isOutcome) {
            //设置支付状态
            outcomeTv.setSelected(true);
            incomeTv.setSelected(false);
            mDatas = noteBean.getOutSortlis();
        } else {
            //设置收入状态
            incomeTv.setSelected(true);
            outcomeTv.setSelected(false);
            mDatas = noteBean.getInSortlis();
        }
        billSortAdapter.setItems(mDatas);
        billSortAdapter.notifyDataSetChanged();


    }
    /**
     * 监听返回按键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
            setResult(RESULT_OK, new Intent());
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示备注内容输入框
     */
    private void showContentDialog() {
        Toast.makeText(mContext, "添加分类", Toast.LENGTH_SHORT).show();
    }

    /**
     * 保存修改
     */
    private void saveBSorts() {
        //更新账单分类排序
        for (int i = 0; i < mDatas.size(); i++)
            mDatas.get(i).setPriority(i);
        LocalRepository.getInstance().updateBSoers(mDatas);
    }


}
