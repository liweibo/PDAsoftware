package com.crrc.pdasoftware.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.adapter.guzhanggongdanadapter.FuwuXiangyingAdapter;
import com.crrc.pdasoftware.adapter.guzhanggongdanadapter.YiwancGongdanAdapter;
import com.crrc.pdasoftware.fragments.dummy.DummyContent.DummyItem;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataProvider;
import com.crrc.pdasoftware.utils.guzhanggddata.YiwancDataProvider;
import com.crrc.pdasoftware.widget.MaterialLoadMoreView;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;


public class yiwanchengFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private com.crrc.pdasoftware.fragments.FuwuFragment.OnListFragmentInteractionListener mListener;

    SwipeRecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;
    private YiwancGongdanAdapter mAdapter;
    private Handler mHandler = new Handler();
    private boolean mEnableLoadMore;

    public yiwanchengFragment() {
    }

    @SuppressWarnings("unused")
    public static com.crrc.pdasoftware.fragments.FuwuFragment newInstance(int columnCount) {
        com.crrc.pdasoftware.fragments.FuwuFragment fragment = new com.crrc.pdasoftware.fragments.FuwuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

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
        View view = inflater.inflate(R.layout.fragment_yiwancheng_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_guzhang_yiwanc);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_guzhang_yiwanc);

        recyclerView.setLayoutManager(new XLinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new YiwancGongdanAdapter());
        swipeRefreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
        initListen();
        return view;
    }

    private void initListen() {
        // 刷新监听。
        swipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        refresh();
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
                mAdapter.refresh(YiwancDataProvider.getDemoNewInfos());
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
                    mAdapter.loadMore(YiwancDataProvider.getDemoNewInfos());
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
        if (context instanceof com.crrc.pdasoftware.fragments.FuwuFragment.OnListFragmentInteractionListener) {
            mListener = (com.crrc.pdasoftware.fragments.FuwuFragment.OnListFragmentInteractionListener) context;
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
        void onListFragmentInteraction(DummyItem item);
    }
    @Override
    public void onDestroyView() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }
}
