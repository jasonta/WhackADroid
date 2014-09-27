package com.adlerandroid.whackadroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

public class StartActivity extends ActionBarActivity {

    private static final String TAG = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Spinner difficultyChoice = (Spinner) findViewById(R.id.difficultyChoice);
        difficultyChoice.setSelection(1);
        difficultyChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String[] choices = getResources().getStringArray(R.array.difficulties);
                final String message = "Selected difficulty: " + choices[position];
                Log.v(TAG, message);
                Toast.makeText(StartActivity.this, message, Toast.LENGTH_SHORT).show();
                // TODO persist difficulty selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the 'start game' button is clicked.
     *
     * @param view
     */
    public void startGameClicked(View view) {
        // TODO start game activity
        startActivity(new Intent(this, GameActivity.class));
    }
}
