package io.github.kbiakov.kvp_storage.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.kbiakov.kvp_storage.R;
import io.github.kbiakov.kvp_storage.adapters.PairAdapter;
import io.github.kbiakov.kvp_storage.models.PairEntity;
import io.github.kbiakov.kvp_storage.storage.Storage;
import io.github.kbiakov.kvp_storage.storage.exceptions.KeyNotFoundException;
import io.github.kbiakov.kvp_storage.storage.exceptions.ValueTypeException;

public class StorageActivity extends AppCompatActivity {

    @Bind(R.id.uiListPairs) ListView uiListPairs;

    private static final int REQ_ADD_PAIR = 0;

    private ArrayList<PairEntity> mPairEntities;
    private PairAdapter mPairAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        try {
            mPairEntities = Storage.getInstance().getStoredPairEntities();
        } catch (KeyNotFoundException | ValueTypeException e) {
            mPairEntities = new ArrayList<>();
            showReadingError();
        }

        mPairAdapter = new PairAdapter(this, mPairEntities);
        uiListPairs.setAdapter(mPairAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StorageActivity.this, AddPairActivity.class);
                startActivityForResult(i, REQ_ADD_PAIR);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stored, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQ_ADD_PAIR &&
                data.getBooleanExtra(AddPairActivity.EXTRA_ADDED_NEW, false)) {

            try {
                ArrayList<PairEntity> storedPairEntities =
                        Storage.getInstance().getStoredPairEntities();

                mPairEntities.clear();
                mPairEntities.addAll(storedPairEntities);
                mPairAdapter.notifyDataSetChanged();
            } catch (KeyNotFoundException | ValueTypeException e) {
                showReadingError();
            }
        }
    }

    /**
     * Show error when something went wrong while read from storage
     */
    private void showReadingError() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error_title)
                .setMessage(R.string.error_storage_reading)
                .create()
                .show();
    }
}
