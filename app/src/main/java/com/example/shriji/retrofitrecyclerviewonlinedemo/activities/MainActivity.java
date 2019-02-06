package com.example.shriji.retrofitrecyclerviewonlinedemo.activities;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shriji.retrofitrecyclerviewonlinedemo.R;
import com.example.shriji.retrofitrecyclerviewonlinedemo.adapters.AdapterUserList;
import com.example.shriji.retrofitrecyclerviewonlinedemo.models.UsersListModel;
import com.example.shriji.retrofitrecyclerviewonlinedemo.retrofitClasses.ApiController;
import com.example.shriji.retrofitrecyclerviewonlinedemo.utils.CommonUtil;
import com.example.shriji.retrofitrecyclerviewonlinedemo.utils.EndlessParentScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //Views
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.txtRemarksNotFound)
    TextView txtRemarksNotFound;
    @BindView(R.id.rvRemarkList)
    RecyclerView rvUserList;
    @BindView(R.id.include)
    NestedScrollView include;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    //Custom Objects
    private UsersListModel mUsersListModel;
    private EndlessParentScrollListener endlessParentScrollListener;
    private LinearLayoutManager mLinearLayoutManager;
    private AdapterUserList mAdapterUserList;

    //Data type and variables
    private int mIsCallWebServciceFromLoadMore = 0;
    private int mOffsetForPagination = 0;

    //Collections
    private List<UsersListModel.DataBean> mUserList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
    }

    private void initialization() {

        ButterKnife.bind(this);
        initToolbar();
        initUserRecyclerView();

        //---------load more - swipe to refresh functionality----------
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsCallWebServciceFromLoadMore = 0;
                mOffsetForPagination = 0;
                callWebserviceUserList();
            }
        });


        //-------------------Load More Functionality-----------------
        endlessParentScrollListener = new EndlessParentScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMore();
            }
        };
        include.setOnScrollChangeListener(endlessParentScrollListener);

        callWebserviceUserList();

    }

    //region initMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region InitRecyclerView
    private void initUserRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        rvUserList.setLayoutManager(mLinearLayoutManager);
        mAdapterUserList = new AdapterUserList(this, mUserList);
        rvUserList.setAdapter(mAdapterUserList);

    }
    //endregion

    //region InitToolbar
    private void initToolbar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User List");
    }
    //endregion

    //region LoadMore
    private void loadMore() {
        mIsCallWebServciceFromLoadMore = 1;
        mOffsetForPagination = mOffsetForPagination + 1;
        callWebserviceUserList();
    }
    //endregion

    //region CallWebservices
    private void callWebserviceUserList() {

        if (!CommonUtil.isInternetAvailabel(this)) {
            return;
        }

        if (!(swipeRefresh.isRefreshing())) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            swipeRefresh.setRefreshing(true);
        }


        Call<UsersListModel> usersListModelCall = ApiController
                .getAPIInterface()
                .getUserList(String.valueOf(mOffsetForPagination),String.valueOf(10));


        usersListModelCall.enqueue(new Callback<UsersListModel>() {
            @Override
            public void onResponse(Call<UsersListModel> call, Response<UsersListModel> response) {

                if (!(swipeRefresh.isRefreshing())) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    swipeRefresh.setRefreshing(false);
                }

                if (response == null) {
                    Toast.makeText(getApplicationContext(), "Can't find response", Toast.LENGTH_SHORT).show();
                } else {
                    mUsersListModel = response.body();

                    if (mIsCallWebServciceFromLoadMore != 1) {

                        if (mUsersListModel.getData().isEmpty()) {
                            txtRemarksNotFound.setVisibility(View.VISIBLE);
                            rvUserList.setVisibility(View.GONE);
                        } else {
                            txtRemarksNotFound.setVisibility(View.GONE);
                            rvUserList.setVisibility(View.VISIBLE);
                        }

                        mUserList.clear();

                    }

                    mUserList.addAll(mUsersListModel.getData());

                    if (mOffsetForPagination == 0) {
                        endlessParentScrollListener.setCurrentPage(0);
                        endlessParentScrollListener.setPreviousTotalItemCount(0);
                        endlessParentScrollListener.setStartingPageIndex(0);
                        endlessParentScrollListener.setLoading(true);

                    }
                    mAdapterUserList.notifyDataSetChanged();


                    Log.d(TAG, "onUserListResponse: == " + response.body().toString() + "userList Size == " + mUserList.size());
                }

            }

            @Override
            public void onFailure(Call<UsersListModel> call, Throwable t) {

                if (!(swipeRefresh.isRefreshing())) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    swipeRefresh.setRefreshing(false);
                }
                t.printStackTrace();
            }
        });
    }
    //endregion
}
