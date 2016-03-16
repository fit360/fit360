package com.app.spott.exceptions;

public class WorkoutModelException extends ModelException {
    public WorkoutModelException() {
        super("Unable to save workout");
    }
}
