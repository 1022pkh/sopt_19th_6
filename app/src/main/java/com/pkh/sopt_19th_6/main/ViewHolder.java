package com.pkh.sopt_19th_6.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pkh.sopt_19th_6.R;

/**
 * Created by kh on 2016. 11. 21..
 */
public class ViewHolder extends RecyclerView.ViewHolder {


    public TextView titleView;
    public TextView contentView;

    public ViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView)itemView.findViewById(R.id.titleView);
        contentView =(TextView)itemView.findViewById(R.id.contentView);
    }
}
