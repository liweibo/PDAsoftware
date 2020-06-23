package com.crrc.pdasoftware.fragments.guzhangchuli;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crrc.pdasoftware.activity.all.guzhangchuli.FuwuxyTianxieActivity;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.adapter.guzhanggongdanadapter.SeeHistoryAdapter;
import com.crrc.pdasoftware.fragments.dummy.DummyContent;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataProvider;
import com.crrc.pdasoftware.widget.MaterialLoadMoreView;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xutil.data.DateUtils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class SeeHistoryFragment extends Fragment {
    private TimePickerView mBegainDatePicker;
    private TimePickerView mLastDatePicker;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private FuwuFragment.OnListFragmentInteractionListener mListener;

    SwipeRecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;
    private SeeHistoryAdapter mAdapter;
    private Handler mHandler = new Handler();
    private boolean mEnableLoadMore;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView et_begain_date;
    TextView et_last_date;
    Button btn_find_his;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences("guzhanggddata", MODE_PRIVATE);
        editor = pref.edit();

        View view = inflater.inflate(R.layout.fragment_see_history, container, false);
        recyclerView = view.findViewById(R.id.recyclerView__see_history);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_see_history);

        et_begain_date = view.findViewById(R.id.et_begain_date);
        et_last_date = view.findViewById(R.id.et_last_date);
        btn_find_his = view.findViewById(R.id.btn_find_his);

        recyclerView.setLayoutManager(new XLinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new SeeHistoryAdapter());
        swipeRefreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
        initListen();
        return view;
    }

    private void initListen() {
        // 刷新监听。
        swipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        refresh();


        btn_find_his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XToastUtils.success("查询....");
            }
        });
        et_last_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XToastUtils.success("....");

                showLastDatePicker();
            }
        });

        et_begain_date.setClickable(true);
        et_begain_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("0000000000000000000000");
                showBegainDatePicker();

            }
        });

        //item点击监听

        mAdapter.setOnItemClickListener(new RecyclerViewHolder.OnItemClickListener<FuwuDataInfo>() {
            @Override
            public void onItemClick(View itemView, FuwuDataInfo item, int position) {
                startActivity(new Intent(getActivity(), FuwuxyTianxieActivity.class));
            }
        });
    }


    /**
     * 刷新。
     */
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadData();
        }
    };

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadData();
    }

    private void loadData() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.refresh(FuwuDataProvider.getFwxyListdata());
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                enableLoadMore();
            }
        }, 1000);
    }

    /**
     * 确保有数据加载了才开启加载更多
     */
    private void enableLoadMore() {
        if (recyclerView != null && !mEnableLoadMore) {
            mEnableLoadMore = true;
            useMaterialLoadMore();
            // 加载更多的监听。
            recyclerView.setLoadMoreListener(mLoadMoreListener);
            recyclerView.loadMoreFinish(false, true);
        }
    }

    private void useMaterialLoadMore() {
        MaterialLoadMoreView loadMoreView = new MaterialLoadMoreView(getContext());
        recyclerView.addFooterView(loadMoreView);
        recyclerView.setLoadMoreView(loadMoreView);
    }

    /**
     * 加载更多。
     */
    private SwipeRecyclerView.LoadMoreListener mLoadMoreListener = new SwipeRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAdapter.loadMore(FuwuDataProvider.getFwxyListdata());
                    if (recyclerView != null) {
                        recyclerView.loadMoreFinish(false, true);
                    }
                }
            }, 1000);
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FuwuFragment.OnListFragmentInteractionListener) {
            mListener = (FuwuFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyContent.DummyItem item);
    }

    @Override
    public void onDestroyView() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }


    private void showLastDatePicker() {
        if (mLastDatePicker == null) {
            mLastDatePicker = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    et_last_date.setText(DateUtils.date2String(date, DateUtils.yyyyMMdd.get()));
                }
            })
                    .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                        @Override
                        public void onTimeSelectChanged(Date date) {
                        }
                    })
                    .setTitleText("日期选择")
                    .build();
        }
        mLastDatePicker.show();
    }

    private void showBegainDatePicker() {
        if (mBegainDatePicker == null) {
            mBegainDatePicker = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    et_begain_date.setText(DateUtils.date2String(date, DateUtils.yyyyMMdd.get()));
                }
            })
                    .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                        @Override
                        public void onTimeSelectChanged(Date date) {
                        }
                    })
                    .setTitleText("日期选择")
                    .build();
        }
        mBegainDatePicker.show();
    }
}
