package demo.android.hello.com.bmobdemo1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import demo.android.hello.com.bmobdemo1.adapter.LoadMoreAdapter;
import demo.android.hello.com.bmobdemo1.listener.EndlessRecyclerOnScrollListener;

/**
 * Created by zhenhua.he on 2017/11/6.
 */

public class LoadMoreActivity extends  BaseActivity{
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LoadMoreAdapter loadMoreAdapter;
    private List<String> dataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more);

        init();
    }

    private void init() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        //设置刷新控件的颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));

        //模拟获取数据
        getData();

        loadMoreAdapter = new LoadMoreAdapter(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(loadMoreAdapter);

        //设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                dataList.clear();
                getData();
                loadMoreAdapter.notifyDataSetChanged();

                //延时1s关闭下拉刷新
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

        //设置加载更多监听
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING);

                if (dataList.size() < 52) {
                    //模拟取网络数据，延时1s
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getData();
                                    loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
                                }
                            });
                        }
                    }, 1000);
                }else {
                    //显示加载到底的提示
                    loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                }
            }
        });
    }

    private  void getData() {
        char letter = 'A';
        for (int i = 0; i < 26; i++) {
            dataList.add(String.valueOf(letter));
            letter++;
        }
    }
}
