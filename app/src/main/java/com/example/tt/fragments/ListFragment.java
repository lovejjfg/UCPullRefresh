package com.example.tt.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.tt.fragments.adapter.ListAdapter;
import com.lovejjfg.powerrecycle.SwipeRefreshRecycleView;

import java.util.ArrayList;

/**
 * Created by Joe on 2016/10/8.
 * Email lovejjfg@gmail.com
 */
public class ListFragment extends Fragment implements SwipeRefreshRecycleView.OnRefreshLoadMoreListener {
    private SwipeRefreshRecycleView mRecyclerView;
    private ArrayList<String> list;
    private ListAdapter adapter;
    private int currentType;
    private boolean isRefrsh;
    private boolean isVisible;
    private boolean isUpdate;

    public ListFragment() {

    }

    public ListFragment(int type) {
        currentType = type;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "onCreate: fragment创建了 ！！");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("TAG", "onCreateView: 创建View ！！");
        View inflate = inflater.inflate(R.layout.layout_fragment_list, container, false);
        final Toast toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        mRecyclerView = (SwipeRefreshRecycleView) inflate.findViewById(R.id.swipe_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        adapter = new ListAdapter(currentType);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnRefreshListener(this);
        list = new ArrayList<>();
        adapter.setTotalCount(50);
        adapter.setListener(new OnItemClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onItemClick(View itemView, ImageView image, int id) {
                toast.setText(String.format("这是第%d个", id));
                toast.show();
            }
        });
        if (!isRefrsh && isVisible) {
            isRefrsh = true;
            Log.e("TAG", "onCreateView: 在创建的时候请求数据了！！");
            mRecyclerView.setRefresh(true);
            getData();
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.setList(list);
                    mRecyclerView.setRefresh(false);
                }
            }, 1000);

        }
        return inflate;
    }

    private void getData() {
        if (list == null) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        for (int i = 0; i < 15; i++) {
            list.add("这是" + i);
        }
    }

    @Override
    public void onRefresh() {
        getData();
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setList(list);
                mRecyclerView.setRefresh(false);

            }
        }, 2000);

    }

    @Override
    public void onLoadMore() {
        getData();
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.appendList(list);
            }
        }, 2000);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Log.e("TAG", "setUserVisibleHint: 可见了！！");
        } else {
            Log.e("TAG", "setUserVisibleHint: 不可见了！！");
        }
        isVisible = isVisibleToUser;
        if (!isRefrsh && isVisible && mRecyclerView != null) {
            isRefrsh = true;
            Log.e("TAG", "onCreateView: 在创建的时候请求数据了！！");
//            mRecyclerView.setRefresh(true);
            getData();
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.setList(list);
//                    mRecyclerView.setRefresh(false);
                }
            }, 500);

        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
