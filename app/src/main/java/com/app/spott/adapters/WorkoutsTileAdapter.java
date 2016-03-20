package com.app.spott.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.spott.R;
import com.app.spott.models.WorkoutType;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WorkoutsTileAdapter extends RecyclerView.Adapter{

    private static List<WorkoutType> workoutTypes = WorkoutType.getAll();
    private WorkoutType type;
    private TileTouchInterceptor interceptor;
    private int selectedPosition;


    public interface TileTouchInterceptor {
        void onTileSelect(WorkoutType wt);
    }

    public WorkoutsTileAdapter(Context mContext) {
        if (mContext instanceof TileTouchInterceptor){
            this.interceptor = (TileTouchInterceptor) mContext;
        } else {
            throw new ClassCastException(mContext.toString() + "must implement TileTouchInterceptor");
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View tileView = li.inflate(R.layout.item_workout_tile, parent, false);
        RecyclerView.ViewHolder viewHolder = new TileViewHolder(tileView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        type = workoutTypes.get(position);
        TileViewHolder th = (TileViewHolder) holder;
//        th.itemView.setSelected(this.selectedPosition == position);
        th.ivTileIcon.setImageResource(type.getIcon());
        th.tvTileText.setText(type.toString());
    }

    @Override
    public int getItemCount() {
        return workoutTypes.size();
    }

    public class TileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        @Bind(R.id.ivTileIcon)
        ImageView ivTileIcon;

        @Bind(R.id.tvTileText)
        TextView tvTileText;

        public TileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            setSelectedPosition(pos);
            WorkoutType w = workoutTypes.get(pos);
            interceptor.onTileSelect(w);
        }
    }
}
