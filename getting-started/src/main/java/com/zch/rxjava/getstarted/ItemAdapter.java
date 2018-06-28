package com.zch.rxjava.getstarted;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zch.rxjava.getstarted.nesting_network_request.UserActivity;
import com.zch.rxjava.getstarted.unconditional_network_request_polling.GetTranslationActivity;

import java.util.ArrayList;

/**
 * Created by zch on 2018/6/28.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mDataList;

    public ItemAdapter(Context context) {
        mContext = context;

        mDataList = new ArrayList<>();
        mDataList.add("入门介绍");
        mDataList.add("创建操作符");
        mDataList.add("变换操作符");
        mDataList.add("组合 / 合并操作符");
        mDataList.add("网络请求轮询（无条件）");
        mDataList.add("网络请求嵌套回调");
        mDataList.add("从磁盘 / 内存缓存中获取缓存数据");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView.setText(mDataList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDetailActivity(position);
            }
        });
    }

    private void gotoDetailActivity(int position) {
        Class<?> gotoClass = null;
        switch (position) {
            case 0:
                gotoClass = GetStartedActivity.class;
                break;
            case 1:
                gotoClass = CreateOperatorsActivity.class;
                break;
            case 2:
                gotoClass = ConvertOperatorsActivity.class;
                break;
            case 3:
                gotoClass = CombinedMergeOperatorsActivity.class;
                break;
            case 4:
                gotoClass = GetTranslationActivity.class;
                break;
            case 5:
                gotoClass = UserActivity.class;
                break;
            case 6:
                gotoClass = GetDataFromCacheActivity.class;
                break;
        }
        mContext.startActivity(new Intent(mContext, gotoClass));
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
