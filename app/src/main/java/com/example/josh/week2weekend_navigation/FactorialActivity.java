package com.example.josh.week2weekend_navigation;

import android.annotation.SuppressLint;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FactorialActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    final int loaderId = 1;
    final String loaderKey = "FACTORIAL";
    EditText etInput;
    TextView tvResult;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factorial);

        etInput = findViewById(R.id.etInput);
        tvResult = findViewById(R.id.tvResult);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            public String loadInBackground() {

                int factorial = Integer.valueOf(etInput.getText().toString());
                factorial = Factorial(factorial);


                return factorial + "";
            }

            @Override
            protected void onStartLoading() {
                //Think of this as AsyncTask onPreExecute() method,you can start your progress bar,and at the end call forceLoad();
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        tvResult.setText(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        //Leave it for now as it is
    }

    public void calcFactorial(View view) {
        getSupportLoaderManager().initLoader(loaderId,null,this);
    }

    public int Factorial(int value){

        if (value == 0){
            return 1;
        }else{
            return Factorial(value - 1) * Factorial(value);
        }
    }

    private void factorialOperation() {

        // Create a bundle called queryBundle
        Bundle queryBundle = new Bundle();
        // Use putString with OPERATION_QUERY_URL_EXTRA as the key and the String value of the URL as the value
        //url value here is https://jsonplaceholder.typicode.com/posts
        queryBundle.putString("factorial",etInput.getText().toString());
        // Call getSupportLoaderManager and store it in a LoaderManager variable
        LoaderManager loaderManager = getSupportLoaderManager();
        // Get our Loader by calling getLoader and passing the ID we specified
        Loader<String> loader = loaderManager.getLoader(loaderId);
        // If the Loader was null, initialize it. Else, restart it.
        if(loader==null){
            loaderManager.initLoader(loaderId, queryBundle, this);
        }else{
            loaderManager.restartLoader(loaderId, queryBundle, this);
        }
    }
}
