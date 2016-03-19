package com.app.spott.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.exceptions.ModelException;
import com.app.spott.models.Workout;
import com.app.spott.models.WorkoutType;
import com.app.spott.models.Frequency;
import com.app.spott.models.Location;
import com.app.spott.models.Time;
import com.app.spott.models.User;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WorkoutEditFragment extends DialogFragment {
    @Bind(R.id.autoWorkout)
    AutoCompleteTextView autoWorkout;

    @Bind(R.id.etLocation)
    EditText etLocation;

    @Bind(R.id.spnTime)
    Spinner spinnerTime;

    @Bind(R.id.spnFrequency)
    Spinner spinnerFrequency;

    @Bind(R.id.btnSave)
    Button btnSave;

    @Bind(R.id.btnCancel)
    Button btnCancel;

    private static final String WORKOUT_ID = "workout_id";
    private static final String TAG = WorkoutEditFragment.class.getSimpleName();

    private Workout workout;
    private ArrayAdapter<Time> timeAdapter;
    private ArrayAdapter<Frequency> frequencyAdapter;
    private ArrayAdapter<WorkoutType> workoutTypeAdapter;
    private User currentUser;
    private OnSaveListener listener;

    public interface OnSaveListener {
        void onWorkoutSave(Workout workout);
    }

    public WorkoutEditFragment() {
    }

    public static WorkoutEditFragment newInstance(String workoutId) {
        WorkoutEditFragment f = new WorkoutEditFragment();
        Bundle args = new Bundle();
        args.putString(WORKOUT_ID, workoutId);
        f.setArguments(args);
        return f;
    }

    public static WorkoutEditFragment newInstance() {
        return new WorkoutEditFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragProfileWorkouts);
        if (fragment instanceof OnSaveListener) {
            listener = (OnSaveListener) fragment;
        } else {
            throw new ClassCastException(context.toString() + " must implement WorkoutEditFragment.OnSaveListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_workout, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUser = ((SpottApplication) getActivity().getApplicationContext()).getCurrentUser();

        Bundle args = getArguments();
        timeAdapter = new ArrayAdapter<Time>(getActivity(), android.R.layout.simple_list_item_1, Time.values());
        spinnerTime.setAdapter(timeAdapter);
        frequencyAdapter = new ArrayAdapter<Frequency>(getActivity(), android.R.layout.simple_list_item_1, Frequency.values());
        spinnerFrequency.setAdapter(frequencyAdapter);
        workoutTypeAdapter = new ArrayAdapter<WorkoutType>(getActivity(), android.R.layout.select_dialog_item, WorkoutType.values());
        autoWorkout.setAdapter(workoutTypeAdapter);
        autoWorkout.setThreshold(1);

//        SupportPlaceAutocompleteFragment f = (SupportPlaceAutocompleteFragment) ((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentById(R.id.fragmentLocationAutoComplete);
        SupportPlaceAutocompleteFragment f = (SupportPlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.fragmentGplaces);
        f.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                etLocation.setText(place.getAddress());
            }

            @Override
            public void onError(Status status) {
                etLocation.setText(status.toString());
            }
        });

        if ((args != null) && args.getString(WORKOUT_ID) != null)
            updateViews(args.getString(WORKOUT_ID));
        else
            workout = new Workout();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkout();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroyView();
            }
        });
    }

    private void saveWorkout() {
        workout.setLocation(getRandomLocation());
        workout.setFrequency((Frequency) spinnerFrequency.getSelectedItem());
        workout.setTime((Time) spinnerTime.getSelectedItem());
        workout.setWorkoutType(workoutTypeAdapter.getItem(0));
        workout.setUser(currentUser);
        try {
            workout.saveModel();
            listener.onWorkoutSave(workout);
            onDestroyView();
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }

    private void updateViews(final String workoutId) {
        Workout.findOne(workoutId, true, new GetCallback<Workout>() {
            @Override
            public void done(Workout object, ParseException e) {
                if (e == null) {
                    workout = object;
                    autoWorkout.setText(workout.getWorkoutType().toString());
                    spinnerFrequency.setSelection(frequencyAdapter.getPosition(workout.getFrequency()));
                    spinnerTime.setSelection(timeAdapter.getPosition(workout.getTime()));
                    etLocation.setText(workout.getLocation().getNameAddress());
                } else {
                    Log.e(TAG, "Cannot retrieve workout: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    Location getRandomLocation() {
        Location l1 = new Location();
        l1.setName("Equinox Sports Club San Francisco");
        l1.setAddress("747 Market St, San Francisco, CA 94103, United States");
        l1.setPlaceId("ChIJ4XP5hoiAhYARDfrc7AOB_Vw");
        l1.setPoint(new ParseGeoPoint(37.7850404, -122.3967997));

        Location l2 = new Location();
        l2.setName("24 Hour Fitness Super Sport");
        l2.setAddress("303 2nd St, San Francisco, CA 94107, United States");
        l2.setPlaceId("ChIJqZwFcXyAhYARrYbnY8yNSLE");
        l2.setPoint(new ParseGeoPoint(37.7865542, -122.4065227));

        Location l3 = new Location();
        l3.setName("Tom Bates Regional Sports Complex");
        l3.setAddress("400-408 Gilman St, Berkeley, CA 94710");
        l3.setPlaceId("ChIJz0vjqdd-hYARhwTxtueu-LA");
        l3.setPoint(new ParseGeoPoint(37.8730837, -122.3103494));

        try {
            l1.saveModel();
            l2.saveModel();
            l3.saveModel();
        } catch (ModelException e) {
            e.printStackTrace();
        }

        int r = ((int) Math.random()) % 3;
        switch (r) {
            case 0:
                return l1;
            case 1:
                return l2;
            default:
                return l3;
        }
    }
}
