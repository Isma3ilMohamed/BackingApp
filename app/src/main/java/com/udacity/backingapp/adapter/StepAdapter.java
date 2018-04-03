package com.udacity.backingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.backingapp.R;
import com.udacity.backingapp.model.Steps;

import java.util.List;

/**
 * Created by The Dev Wolf on 21-03-2018.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private List<Steps> steps;
    private Context context;
    private StepListener mCallback;


    public StepAdapter(List<Steps> steps, Context context, StepListener mCallback) {
        this.steps = steps;
        this.context = context;
        this.mCallback = mCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Steps step = steps.get(position);
        holder.tv_step_item_id.setText(step.getShortDescription());
        if (!step.getThumbnailURL().equals("")) {
            Picasso.with(context)
                    .load(step.getThumbnailURL())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(holder.iv_thumb_step);
        }



    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_step_item_id;
        ImageView iv_thumb_step;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_step_item_id = itemView.findViewById(R.id.tv_step_item_id);
            iv_thumb_step = itemView.findViewById(R.id.iv_thumb_step);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mCallback.onStepClickListener(steps.get(getAdapterPosition()));
        }
    }


    public interface StepListener {
        void onStepClickListener(Steps step);
    }
}
