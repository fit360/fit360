package com.app.spott.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.spott.R;
import com.app.spott.exceptions.ModelException;
import com.app.spott.interfaces.WorkoutEditFragmentListener;
import com.app.spott.models.Frequency;
import com.app.spott.models.Location;
import com.app.spott.models.Time;
import com.app.spott.models.User;
import com.app.spott.models.Workout;
import com.app.spott.models.WorkoutType;
import com.parse.ParseGeoPoint;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WorkoutEditDetailsFragment extends Fragment {

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

    @OnClick(R.id.btnCancel) void submit() { onDestroyView();}

    private WorkoutEditFragmentListener parentListener;
    private Workout workout;
    private Location location;
    private ParseGeoPoint geoPoint;
    private ArrayAdapter<Time> timeAdapter;
    private ArrayAdapter<Frequency> frequencyAdapter;
    private ArrayAdapter<WorkoutType> workoutTypeAdapter;
    private User currentUser;
    private static final String TAG = WorkoutEditDetailsFragment.class.getSimpleName();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WorkoutEditFragmentListener) {
            parentListener = (WorkoutEditFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement WorkoutEditFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workout_edit_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        location = new Location();
        geoPoint = new ParseGeoPoint();

        timeAdapter = new ArrayAdapter<Time>(getActivity(), android.R.layout.simple_list_item_1, Time.values());
        spinnerTime.setAdapter(timeAdapter);
        frequencyAdapter = new ArrayAdapter<Frequency>(getActivity(), android.R.layout.simple_list_item_1, Frequency.values());
        spinnerFrequency.setAdapter(frequencyAdapter);
        workoutTypeAdapter = new ArrayAdapter<WorkoutType>(getActivity(), android.R.layout.select_dialog_item, WorkoutType.values());
        autoWorkout.setAdapter(workoutTypeAdapter);
        autoWorkout.setThreshold(1);

        currentUser = parentListener.getCurrentUser();
        workout = parentListener.getWorkout();

        if (!parentListener.isNewWorkout()){
            populateViews();
        }

/*        PlaceAutocompleteFragment f = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.fragmentLocationAutoComplete);
        f.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                etLocation.setText(place.getAddress());
                location.setName(place.getName().toString());
                location.setAddress(place.getAddress().toString());
                location.setPlaceId(place.getId());

                geoPoint.setLatitude(place.getLatLng().latitude);
                geoPoint.setLongitude(place.getLatLng().longitude);
                location.setPoint(geoPoint);
            }

            @Override
            public void onError(Status status) {
                etLocation.setText(status.toString());
            }
        });*/

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkout();
            }
        });
    }

    private void populateViews(){
        autoWorkout.setText(workout.getWorkoutType().toString());
        spinnerFrequency.setSelection(frequencyAdapter.getPosition(workout.getFrequency()));
        spinnerTime.setSelection(timeAdapter.getPosition(workout.getTime()));
        etLocation.setText(workout.getLocation().getNameAddress());
    }

    private void saveWorkout() {
        if (!location.isSet()) {
            Toast.makeText(getContext(), "Please select a location", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Location not set");
        }
        workout.setLocation(location);
        workout.setFrequency((Frequency) spinnerFrequency.getSelectedItem());
        workout.setTime((Time) spinnerTime.getSelectedItem());
        workout.setWorkoutType(workoutTypeAdapter.getItem(0));
        workout.setUser(currentUser);
        try {
            workout.saveModel();
            parentListener.saveWorkout(workout);
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
