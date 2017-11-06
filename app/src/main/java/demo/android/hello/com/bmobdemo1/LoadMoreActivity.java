package demo.android.hello.com.bmobdemo1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
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
    private ArrayList<Person> dataList = new ArrayList<Person>();

    private static  int pagesize = 15;
    private int count;
    private int currentSize;
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
                count = 0;
                getData();
//                loadMoreAdapter.notifyDataSetChanged();

                //延时1s关闭下拉刷新
//                swipeRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                    }
//                }, 1000);
            }
        });

        //设置加载更多监听
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING);
                if (currentSize <pagesize) {
                    //显示加载到底的提示
                    loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                }else {
                    loadMoreData();
                }
            }
        });
    }



    private  void getData() {
        BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
        //注意分页的写法 count * pagesize
        bmobQuery.setLimit(pagesize).setSkip(count * pagesize).order("-createdAt").findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> list, BmobException e) {
                if(e == null) {
//                    ShowToast("查到" + list.size() + "条数据");
                    count = ++count;
                    for (Person person : list) {
                        Log.i("test", person.getName() + " " + person.getAddress());
                    }
                    dataList.addAll(list);
                    loadMoreAdapter.notifyDataSetChanged();

                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                    }

                    currentSize = list.size();
                }else {
                    ShowToast("出错了" + e.toString());
                }
            }
        });
    }

    private void loadMoreData() {
        BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
        //注意分页的写法 count * pagesize
        bmobQuery.setLimit(pagesize).setSkip(count * pagesize).order("-createdAt").findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> list, BmobException e) {
                if(e == null) {
//                    ShowToast("查到" + list.size() + "条数据");
                    count = ++count;
                    for (Person person : list) {
                        Log.i("test", person.getName() + " " + person.getAddress());
                    }
                    dataList.addAll(list);
                    loadMoreAdapter.notifyDataSetChanged();
                    loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);


                    currentSize = list.size();
//                    if (list.size() < pagesize) {
//                        //显示加载到底的提示
//                        loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
//                    }
                }else {
                    ShowToast("出错了" + e.toString());
                }
            }
        });
    }

    /**
     * 分页查询
     */
    private void queryPersons() {
        BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
        //注意分页的写法 count * pagesize
        bmobQuery.setLimit(pagesize).setSkip(count * pagesize).order("-createdAt").findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> list, BmobException e) {
                if(e == null) {
                    ShowToast("查到" + list.size() + "条数据");
                    count = ++count;
                    for (Person person : list) {
                        Log.i("test", person.getName() + " " + person.getAddress());
                    }
                }else {
                    ShowToast("出错了" + e.toString());
                }
            }
        });
    }
}
