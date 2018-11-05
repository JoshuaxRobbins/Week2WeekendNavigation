package com.example.josh.week2weekend_navigation;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

public class Factorial extends AsyncTaskLoader<String> {


    public Factorial(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return null;
    }
}
