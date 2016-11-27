package com.pkh.sopt_19th_6.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pkh.sopt_19th_6.R;

import java.util.ArrayList;

/**
 * Created by kh on 2016. 11. 21..
 */
public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    ArrayList<MainListData> mDatas;
    View.OnClickListener mOnClickListener;

    public Adapter(ArrayList<MainListData> mDatas, View.OnClickListener mOnClickListener) {
        this.mDatas = mDatas;
        this.mOnClickListener = mOnClickListener;
    }

    public void setAdapter(ArrayList<MainListData> mDatas){
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        itemView.setOnClickListener(mOnClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.titleView.setText(mDatas.get(position).title);
        holder.contentView.setText(mDatas.get(position).contents);
    }

    @Override
    public int getItemCount() {
        return (mDatas != null) ? mDatas.size() : 0;
    }
}
