package demo.android.hello.com.bmobdemo1;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.andview.refreshview.XRefreshView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhenhua.he on 2017/11/3.
 */

public class RecycleViewActivity extends  BaseActivity{
    private RecyclerView recyclerView;
    private SimpleAdapter adapter;
    private ArrayList<Person> personList = new ArrayList<Person>();
    private XRefreshView xRefreshView;

    LinearLayoutManager layoutManager;

    private boolean isList = true;//false 为grid布局
    private boolean noMoreData = false;

    private static  int pagesize = 10;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);

        initComponents();
        xRefreshView.startRefresh();
//        initData();
    }

    private void initComponents() {
        xRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_test_rv);
        recyclerView.setHasFixedSize(true);

//        initData();
        adapter = new SimpleAdapter(personList, this);
        // 设置静默加载模式
//        xRefreshView1.setSilenceLoadMore();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // 静默加载模式不能设置footerview
        recyclerView.setAdapter(adapter);
        //设置刷新完成以后，headerview固定的时间
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setMoveForHorizontal(true);
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setAutoLoadMore(true);
        adapter.setCustomLoadMoreView(new NoMoreDataFooterView(this));
        xRefreshView.enableReleaseToLoadMore(true);
        xRefreshView.enableRecyclerViewPullUp(true);
        xRefreshView.enablePullUpWhenLoadCompleted(true);
        //设置静默加载时提前加载的item个数
//        xefreshView1.setPreLoadCount(4);
        //设置Recyclerview的滑动监听
        xRefreshView.setOnRecyclerViewScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                Log.i("test","onRefresh called...");
                count = 0;
                personList.clear();

                queryPersons();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                Log.i("test","onLoadMore called...");
                queryPersons();
            }
        });
    }

    /**
     * 初始化数据
     */
//    private void initData() {
//        queryPersons();
//    }

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
                    count++;
                    Log.i("test", "count = " + count);
                    personList.addAll(list);
                    adapter.notifyDataSetChanged();

                    xRefreshView.stopRefresh();

                    if(list.size() < pagesize) {
                        Log.i("test", "stop.....................");
                        xRefreshView.stopLoadMore(false);
                        noMoreData = true;

                    }

                }else {
                    ShowToast("出错了" + e.toString());

                    xRefreshView.stopRefresh();
                }
            }
        });
    }


}
