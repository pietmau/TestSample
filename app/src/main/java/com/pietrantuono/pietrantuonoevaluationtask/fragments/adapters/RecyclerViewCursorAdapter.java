package com.pietrantuono.pietrantuonoevaluationtask.fragments.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pietrantuono.pietrantuonoevaluationtask.R;
import com.pietrantuono.pietrantuonoevaluationtask.contentprovider.Contract;
import com.pietrantuono.pietrantuonoevaluationtask.utils.Utils;

/**
 * Created by Maurizio Pietrantuono, maurizio.pietrantuono@gmail.com.
 */
public class RecyclerViewCursorAdapter extends RecyclerView.Adapter<RecyclerViewCursorAdapterViewHolder> {
    private final Context contex;
    CursorAdapter cursorAdapter;
    private OnItemClickListener onItemClickListener;

    public RecyclerViewCursorAdapter(final Context contex) {
        this.contex=contex;
        this.cursorAdapter = new CursorAdapter(contex,null,false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(context);
                return inflater.inflate(R.layout.row,parent,false);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                //We need this if we are running on Lossipop and above
                if(Utils.weAreLollipop())view.findViewById(R.id.card).setElevation(contex.getResources().getDimension(R.dimen.elevation));
                String title=cursor.getString(cursor.getColumnIndexOrThrow(Contract.ITEM_TITLE));
                long itemID=cursor.getLong(cursor.getColumnIndexOrThrow(Contract.ITEM_ID));
                String date=cursor.getString(cursor.getColumnIndexOrThrow(Contract.ITEM_DATE));
                String sub=cursor.getString(cursor.getColumnIndexOrThrow(Contract.ITEM_SUBTITLE));
                TextView titleTextView= (TextView) view.findViewById(R.id.title);
                TextView subTextView= (TextView) view.findViewById(R.id.sub);
                TextView idTextView= (TextView) view.findViewById(R.id.item_id);
                TextView dateTextView= (TextView) view.findViewById(R.id.date);
                titleTextView.setText(title!=null?title:"");
                subTextView.setText(sub!=null?sub:"");
                idTextView.setText(""+itemID);
                dateTextView.setText(date!=null?date:"");
            }
        };
    }

    @Override
    public RecyclerViewCursorAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = cursorAdapter.newView(contex, cursorAdapter.getCursor(), parent);
        return new RecyclerViewCursorAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewCursorAdapterViewHolder holder, final int position) {
        cursorAdapter.getCursor().moveToPosition(position);
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursorAdapter.getCursor().moveToPosition(position);
                long itemID = cursorAdapter.getCursor().getLong(cursorAdapter.getCursor().getColumnIndexOrThrow(Contract.ITEM_ID));
                onItemClickListener.onItemClick(itemID);
            }
        });
        cursorAdapter.bindView(holder.itemView, contex, cursorAdapter.getCursor());

    }

    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }

    public void changeCursor(Cursor cursor) {
        cursorAdapter.changeCursor(cursor);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(long itemID);
    }
}
