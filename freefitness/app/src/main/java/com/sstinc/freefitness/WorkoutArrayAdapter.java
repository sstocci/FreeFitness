package com.sstinc.freefitness;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WorkoutArrayAdapter extends ArrayAdapter<Exercise> {
    private Context mContext;
    private int mResource;
    HashMap<Integer, View> exceriseHashMap = new HashMap<Integer, View>();
    // initialized to all -1 with a max of 10 since thats the max I allow
    // could be useful for storing info, aka stop adding if I find a -1
    public ArrayList<Integer> weightsLifted = new ArrayList<Integer>(Arrays.asList(-1,-1,-1,-1,-1,-1,-1,-1,-1,-1));
    public ArrayList<Integer> repsLifted = new ArrayList<Integer>(Arrays.asList(-1,-1,-1,-1,-1,-1,-1,-1,-1,-1));



    // constructor to initialize superclass inherited members
    public WorkoutArrayAdapter(Context context,int resource, List<Exercise> exercise) {
        super(context, -1, exercise);
        mContext=context;
        mResource = resource;
        weightsLifted.clear();
        repsLifted.clear();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Restores data back to position if user scrolls past
        // If not the data gets erased after they scroll which is not ideal
        if (exceriseHashMap.containsKey(position)) {
            return exceriseHashMap.get(position);
        }
        // gets exercise aka the name they put + set number
        String exercise = getItem(position).getExercise();
        int set = getItem(position).getSet();

        // used for recycler view
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        // gets Textviews from set_item
        TextView exerciseTextView = convertView.findViewById(R.id.exceriseTextView);
        TextView setTextview = convertView.findViewById(R.id.setTextView);
        // updates exercise name (from user input) and set number which I generate
        exerciseTextView.setText(exercise);
        setTextview.setText("Set: "+set);

        EditText weightET = convertView.findViewById(R.id.weightEditText);
        EditText repsET = convertView.findViewById(R.id.repsEditText);

        weightET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Makes sure to not append a null or empty value
                // will crash app if it does
                if (s.length()>=1){
                    //stores users weight lifted in an arrayList
                    weightsLifted.add(position,Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        repsET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Makes sure to not append a null or empty value
                // will crash app if it does
                if (s.length()>=1) {
                    //stores users reps lifted in an arrayList
                    repsLifted.add(position, Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        //allows me to scroll and not lose information in my recycler view
        exceriseHashMap.put(position, convertView);

        return convertView;

    }

    public ArrayList<Integer> getWeightsLifted(int i){
        // have to trim the extra values in my array
        // problem caused by storing values based on users entering text so we have to trim arrays
        // Way to fix: Collect all set_items after the user clicks next exercise OR finish workout
        //future update ^
        return new ArrayList<>(weightsLifted.subList(0,i)) ;
    }

    public ArrayList<Integer> getRepsLifted(int i) {
        // Same as getWeightLifted except now it stores reps
        // I have to trim arrays
        return new ArrayList<>(repsLifted.subList(0,i)) ;
    }

}
