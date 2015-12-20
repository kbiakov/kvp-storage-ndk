package io.github.kbiakov.kvp_storage.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.kbiakov.kvp_storage.R;
import io.github.kbiakov.kvp_storage.models.PairEntity;
import io.github.kbiakov.kvp_storage.storage.Storage;
import io.github.kbiakov.kvp_storage.storage.StoreType;
import io.github.kbiakov.kvp_storage.storage.ValueTypeException;

public class AddPairActivity extends AppCompatActivity {

    @Bind(R.id.uiInputKeyWrapper) TextInputLayout uiInputKeyWrapper;
    @Bind(R.id.uiInputValueWrapper) TextInputLayout uiInputValueWrapper;
    @Bind(R.id.uiSpinnerType) Spinner uiSpinnerType;
    @Bind(R.id.uiButtonAdd) Button uiButtonAdd;

    public static final String EXTRA_ADDED_NEW = "added_new_one";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pair);
        ButterKnife.bind(this);

        uiInputKeyWrapper.setOnTouchListener(resetInputListener);
        uiInputValueWrapper.setOnTouchListener(resetInputListener);

        uiSpinnerType.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, StoreType.values()));

        uiButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = uiInputKeyWrapper.getEditText().getText().toString();
                String value = uiInputValueWrapper.getEditText().getText().toString();
                StoreType type = (StoreType) uiSpinnerType.getSelectedItem();

                if (!key.equals("")) {
                    if (!value.equals("")) {
                        try {
                            Storage.getInstance().putKVPair(key, value, type);

                            uiInputKeyWrapper.setErrorEnabled(false);
                            uiInputValueWrapper.setErrorEnabled(false);

                            Intent i = new Intent();
                            i.putExtra(EXTRA_ADDED_NEW, true);
                            setResult(Activity.RESULT_OK, i);
                            finish();
                        } catch (ValueTypeException e) {
                            uiInputValueWrapper.setError(getString(R.string.error_invalid_type));
                        }
                    } else {
                        uiInputValueWrapper.setError(getString(R.string.error_null_value));
                    }
                } else {
                    uiInputKeyWrapper.setError(getString(R.string.error_null_key));
                }
            }
        });
    }

    private View.OnTouchListener resetInputListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            uiInputKeyWrapper.setErrorEnabled(false);
            uiInputValueWrapper.setErrorEnabled(false);

            if (uiInputKeyWrapper.getEditText().getText().toString().equals("")) {
                uiInputKeyWrapper.setError(getString(R.string.error_null_key));
            }

            if (uiInputValueWrapper.getEditText().getText().toString().equals("")) {
                uiInputValueWrapper.setError(getString(R.string.error_null_value));
            }

            return true;
        }
    };
}
