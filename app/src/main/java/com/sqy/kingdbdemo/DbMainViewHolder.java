package com.sqy.kingdbdemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class DbMainViewHolder extends RecyclerView.ViewHolder {
    public TextView tv1;
    public TextView tv11;
    public TextView tv2;
    public TextView tv3;
    public TextView tv4;
    public TextView tv5;
    public TextView tv6;

    public DbMainViewHolder(@NonNull View itemView) {
        super(itemView);
        tv1 = itemView.findViewById(R.id.tv1);
        tv11 = itemView.findViewById(R.id.tv11);
        tv2 = itemView.findViewById(R.id.tv2);
        tv3 = itemView.findViewById(R.id.tv3);
        tv4 = itemView.findViewById(R.id.tv4);
        tv5 = itemView.findViewById(R.id.tv5);
        tv6 = itemView.findViewById(R.id.tv6);
    }


}
