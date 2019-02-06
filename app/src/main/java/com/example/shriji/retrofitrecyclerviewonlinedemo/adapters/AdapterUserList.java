package com.example.shriji.retrofitrecyclerviewonlinedemo.adapters;

/**
 * ------------This AdapterAnnouncementRoleList Use to display roles in view details screen---------
 */

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shriji.retrofitrecyclerviewonlinedemo.models.UsersListModel;
import com.example.shriji.retrofitrecyclerviewonlinedemo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUserList extends RecyclerView.Adapter<AdapterUserList.RemarksViewHolder> {

    //Custom Objects
    private Context mContext;

    //Collections
    private List<UsersListModel.DataBean> mUsersList;

    //-------------------------------------- Constructor -------------------------------------------
    public AdapterUserList(Context context, List<UsersListModel.DataBean> usersList) {
        mContext = context;
        mUsersList = usersList;

    }


    //-------------------------------View Layout---------------------------------------------------
    @NonNull
    @Override
    public RemarksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_users_list, parent, false);

        return new RemarksViewHolder(view);
    }


    //-------------------------------Data Binding-------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull RemarksViewHolder holder, int position) {

        holder.txtName.setText(mUsersList.get(position).getId() + " " +
                mUsersList.get(position).getFirst_name() + " " +
                mUsersList.get(position).getLast_name());

        Glide.with(mContext)
                .load(mUsersList.get(position).getAvatar())
                .into(holder.imgImage);

    }

    //-----------------------------Item Counts-----------------------------------------------------
    @Override
    public int getItemCount() {
        return mUsersList.size();
    }


    //---------------------------------------View Holder--------------------------------------------
    public class RemarksViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgImage)
        CircleImageView imgImage;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.llParent)
        LinearLayout llParent;
        View mView;

        public RemarksViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;

        }
    }
}
