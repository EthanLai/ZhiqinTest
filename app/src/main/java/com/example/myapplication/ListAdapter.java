package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.PriorityQueue;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemHolder> {

    private PriorityQueue<Entity> mItemQueue;
    private Context mContext;
    private int mSize;

    public ListAdapter(Context context) {
        mContext = context;
    }

    public void updateList(PriorityQueue<Entity> itemQueue) {
        mItemQueue = itemQueue;
        mSize = mItemQueue.size();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.device_item, parent,false);
        ItemHolder holder = new ItemHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        if (mItemQueue.isEmpty()) {
            return;
        }
        Entity entity = mItemQueue.poll();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String strDate = sdf.format(entity.mDate);
        holder.timeText.setText(strDate);
        holder.priceText.setText(entity.mPrice);
        holder.qualityText.setText(entity.mQuality);
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView timeText, priceText, qualityText;

        public ItemHolder(View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.time);
            priceText = itemView.findViewById(R.id.price);
            qualityText = itemView.findViewById(R.id.quality);
        }
    }
}
