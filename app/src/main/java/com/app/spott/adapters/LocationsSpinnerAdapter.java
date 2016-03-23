package com.app.spott.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.spott.R;
import com.app.spott.models.Workout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sshah on 3/15/16.
 */
public class LocationsSpinnerAdapter extends ArrayAdapter<Workout> {

    private Context mContext;

    public LocationsSpinnerAdapter(Context context, List<Workout> workouts) {
        super(context, R.layout.support_simple_spinner_dropdown_item, workouts);
        mContext = context;
    }

    public static class ViewHolder {

        @Bind(android.R.id.text1)
        TextView tvActivityLocation;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        Workout workout = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.support_simple_spinner_dropdown_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvActivityLocation.setText(workout.getWorkoutType().toString() + " at " + workout.getLocation().getName());

        return convertView;
    }
}
