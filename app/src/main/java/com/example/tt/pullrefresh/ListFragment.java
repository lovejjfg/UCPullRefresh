package com.example.tt.pullrefresh;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tt.pullrefresh.adapter.ListAdapter;
import com.example.tt.pullrefresh.base.BaseFragment;
import com.example.tt.pullrefresh.model.ModelBean;
import com.example.tt.pullrefresh.widget.CurveLayout;
import com.example.tt.pullrefresh.widget.HeaderRefreshLayout;
import com.example.tt.pullrefresh.widget.TouchCircleView;

import java.util.ArrayList;


/**
 * Created by Joe on 2016/10/8.
 * Email lovejjfg@gmail.com
 */
public class ListFragment extends BaseFragment implements  TouchCircleView.OnLoadingListener {
    private static final String TAG = ListFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ArrayList<ModelBean> list;
    private ListAdapter adapter;
    private int currentType;
    private boolean isRefrsh;
    private boolean isVisible;
    private boolean isUpdate;
    public static final String CURRENT_TYPE = "CURRENT_TYPE";
    private HeaderRefreshLayout mHeader;
    private static CurveLayout mCurveLayout;

    public ListFragment() {

    }

    public static ListFragment newInstance(int type) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(CURRENT_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: fragment创建了 ！！");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: 创建View ！！");
        View inflate = inflater.inflate(R.layout.layout_fragment_list, container, false);
        final Toast toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.swipe_recycler);
        mHeader = (HeaderRefreshLayout) inflate.findViewById(R.id.header_container);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        adapter = new ListAdapter(currentType);
        mRecyclerView.setAdapter(adapter);
        mHeader.addLoadingListener(this);
        if (savedInstanceState != null) {
            list = savedInstanceState.getParcelableArrayList("beans");
            if (list != null && list.size() > 0) {
                adapter.setList(list);
                isRefrsh = true;
            }
        }
        if (mCurveLayout.isExpanded() && !isRefrsh && isVisible) {
            isRefrsh = true;
            Log.e(TAG, "onCreateView: 在创建的时候请求数据了！！" + mCurveLayout.isExpanded());
            mHeader.setRefresh(true);
            getData();
        }
        adapter.setTotalCount(50);
        adapter.setListener(new OnItemClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onItemClick(View itemView, ImageView image, int id) {
                toast.setText(String.format("这是第%d个", id));
                toast.show();
            }
        });

        mCurveLayout.registerCallback(new CurveLayout.Callbacks() {
            @Override
            public void onSheetExpanded() {
                mHeader.setEnabled(true);
            }

            @Override
            public void onSheetNarrowed() {
                mHeader.setEnabled(false);
            }
        });

        return inflate;
    }

    private void getData() {
        if (list == null) {
            list = new ArrayList<>();
        }
        for (int i = 0; i < 15; i++) {
            list.add(new ModelBean());
        }
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Log.e(TAG, "setUserVisibleHint: 可见了！！");
        } else {
            Log.e(TAG, "setUserVisibleHint: 不可见了！！");
        }
        isVisible = isVisibleToUser;
        if (mCurveLayout.isExpanded()&&!isRefrsh && isVisible && mRecyclerView != null) {
            isRefrsh = true;
            Log.e(TAG, "onCreateView: 在创建的时候请求数据了！！");
            mHeader.setRefresh(true);
            getData();
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayList<ModelBean> list = new ArrayList<>();
        list.addAll(adapter.getList());
        outState.putParcelableArrayList("beans", list);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy:fragment销毁了！");
        super.onDestroy();
    }

    @Override
    public void onProgressStateChange(int state, boolean hide) {

    }

    @Override
    public void onProgressLoading() {
        getData();
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setList(list);
                mHeader.setRefreshSuccess();

            }
        }, 1000);
    }

    @Override
    public void onGoBackHome() {
        Log.e(TAG, "onGoBackHome: ");
        mCurveLayout.dismiss();
    }


    public static void setCurveLayout(CurveLayout mBoottom) {
        mCurveLayout = mBoottom;
    }


}
