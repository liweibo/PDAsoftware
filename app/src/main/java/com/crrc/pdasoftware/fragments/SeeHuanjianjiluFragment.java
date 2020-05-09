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
import com.crrc.pdasoftware.adapter.guzhanggongdanadapter.SeeGzhangpaichaAdapter;
import com.crrc.pdasoftware.adapter.guzhanggongdanadapter.SeehuanjianAdapter;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataProvider;
import com.crrc.pdasoftware.utils.guzhanggddata.SeePaichaDataInfo;
import com.crrc.pdasoftware.widget.MaterialLoadMoreView;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

public class SeeHuanjianjiluFragment extends Fragment {
    private FuwuFragment.OnListFragmentInteractionListener mListener;

    SwipeRecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;
    private SeehuanjianAdapter mAdapter;
    private Handler mHandler = new Handler();
    private boolean mEnableLoadMore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_see_huanjianjilu, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_seehuanjian);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_seehuanjian);

        recyclerView.setLayoutManager(new XLinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new SeehuanjianAdapter());
        swipeRefreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
        initListen();

        return view;
    }

    private void initListen() {
        // 刷新监听。
        swipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        refresh();

        //item点击监听

        mAdapter.setOnItemClickListener(new RecyclerViewHolder.OnItemClickListener<SeePaichaDataInfo>() {
            @Override
            public void onItemClick(View itemView, SeePaichaDataInfo item, int position) {
                System.out.println("==" + itemView.findViewById(R.id.tv_fwxy_xiangmuvalue));
                XToastUtils.success(item.getchulicuoshi());

//                startActivity(new Intent(getActivity(), FuwuxyTianxieActivity.class));
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
                mAdapter.refresh(FuwuDataProvider.getDemoSeepaichaInfos());
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
                    mAdapter.loadMore(FuwuDataProvider.getDemoSeepaichaInfos());
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


}
