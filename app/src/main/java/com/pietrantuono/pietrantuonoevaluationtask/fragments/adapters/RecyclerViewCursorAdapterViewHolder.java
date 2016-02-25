package com.pietrantuono.pietrantuonoevaluationtask.fragments.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pietrantuono.pietrantuonoevaluationtask.R;

/**
 * Created by Maurizio Pietrantuono, maurizio.pietrantuono@gmail.com.
 */
public class RecyclerViewCursorAdapterViewHolder extends RecyclerView.ViewHolder {

    private final View container;

    public RecyclerViewCursorAdapterViewHolder(View itemView) {
        super(itemView);
        container=itemView.findViewById(R.id.item_container);
    }


    public void setOnClickListener(View.OnClickListener onClickListener) {
        container.setOnClickListener(onClickListener);
    }
}
