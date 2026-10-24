package com.example.practice16;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    EditText nameBox;
    EditText yearBox;
    Spinner citySpinner;
    Button delButton;
    Button saveButton;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    Cursor cityCursor;

    long userId = 0;
    long selectedCityId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        nameBox = findViewById(R.id.name);
        yearBox = findViewById(R.id.year);
        citySpinner = findViewById(R.id.citySpinner);
        delButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        cityCursor = db.rawQuery("SELECT _id, city_name FROM " + DatabaseHelper.TABLE_CITIES, null);
        SimpleCursorAdapter cityAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_spinner_item,
                cityCursor,
                new String[]{DatabaseHelper.COLUMN_CITY_NAME},
                new int[]{android.R.id.text1},
                0);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }

        if (userId > 0) {
            userCursor = db.rawQuery(
                    "SELECT * FROM " + DatabaseHelper.TABLE
                            + " WHERE " + DatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();

            nameBox.setText(userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)));
            yearBox.setText(String.valueOf(userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_YEAR))));

            long cityId = userCursor.getLong(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CITY_ID));

            for (int i = 0; i < cityAdapter.getCount(); i++) {
                if (cityAdapter.getItemId(i) == cityId) {
                    citySpinner.setSelection(i);
                    break;
                }
            }
            userCursor.close();
        } else {
            delButton.setVisibility(View.GONE);
        }
    }

    public void save(View view) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, nameBox.getText().toString());
        cv.put(DatabaseHelper.COLUMN_YEAR, Integer.parseInt(yearBox.getText().toString()));

        selectedCityId = citySpinner.getSelectedItemId();
        cv.put(DatabaseHelper.COLUMN_CITY_ID, selectedCityId);

        if (userId > 0) {
            db.update(DatabaseHelper.TABLE, cv,
                    DatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(userId)});
        } else {
            db.insert(DatabaseHelper.TABLE, null, cv);
        }
        goHome();
    }

    public void delete(View view) {
        db.delete(DatabaseHelper.TABLE,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(userId)});
        goHome();
    }

    private void goHome() {
        if (cityCursor != null) cityCursor.close();
        db.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}