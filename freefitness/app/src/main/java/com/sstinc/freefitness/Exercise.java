package com.sstinc.freefitness;

public class Exercise {

    // used for the workout array adapter
    // each list item is made up of Exercise instances
    private String excerise;
    private int set;

    public Exercise(String exercise, int set) {
        this.excerise = exercise;
        this.set = set;
    }

    public String getExercise() {
        return excerise;
    }

    public int getSet() {
        return set;
    }

}
