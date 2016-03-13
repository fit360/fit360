package com.app.spott.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.app.spott.R;
import com.app.spott.models.Activity;
import com.app.spott.models.ActivityType;
import com.app.spott.models.Frequency;
import com.app.spott.models.Time;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityEditFragment extends DialogFragment {
    @Bind(R.id.autoActivity)
    AutoCompleteTextView autoActivity;

    @Bind(R.id.etLocation)
    EditText etLocation;

    @Bind(R.id.spnTime)
    Spinner spinnerTime;

    @Bind(R.id.spnFrequency)
    Spinner spinnerFrequency;

    private static final String ACTIVITY_ID = "activity_id";
    private static final String TAG = ActivityEditFragment.class.getSimpleName();

    private Activity activity;
    private ArrayAdapter<Time> timeAdapter;
    private ArrayAdapter<Frequency> frequencyAdapter;
    private ArrayAdapter<ActivityType> activityTypeAdapter;

    public ActivityEditFragment() {}

    public static ActivityEditFragment newInstance(String activityId) {
        ActivityEditFragment f = new ActivityEditFragment();
        Bundle args = new Bundle();
        args.putString(ACTIVITY_ID, activityId);
        f.setArguments(args);
        return f;
    }

    public static ActivityEditFragment newInstance(){
        return new ActivityEditFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_activity, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        timeAdapter = new ArrayAdapter<Time>(getActivity(), android.R.layout.simple_list_item_1, Time.values());
        spinnerTime.setAdapter(timeAdapter);
        frequencyAdapter = new ArrayAdapter<Frequency>(getActivity(), android.R.layout.simple_list_item_1, Frequency.values());
        spinnerFrequency.setAdapter(frequencyAdapter);
        activityTypeAdapter = new ArrayAdapter<ActivityType>(getActivity(), android.R.layout.select_dialog_item, ActivityType.values());
        autoActivity.setAdapter(activityTypeAdapter);
        autoActivity.setThreshold(1);

        if (args.getString(ACTIVITY_ID) != null)
            updateViews(args.getString(ACTIVITY_ID));

    }

    private void updateViews(String activityId){
        Activity.getByObjectId(activityId, new GetCallback() {
            @Override
            public void done(ParseObject activity, ParseException e) {
                if (e != null){
                    //pass
                } else {
                    Log.e(TAG, "Cannot retrieve activity: " + e.getMessage());
                }
            }
        });
    }
}
