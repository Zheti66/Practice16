package com.example.practice16;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ListView userList;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userList = findViewById(R.id.list);

        userList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        databaseHelper = new DatabaseHelper(getApplicationContext());
    }

    public void add(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        db = databaseHelper.getReadableDatabase();

        userCursor = db.rawQuery(
                "SELECT u." + DatabaseHelper.COLUMN_ID + " AS _id, "
                        + "u." + DatabaseHelper.COLUMN_NAME + ", "
                        + "u." + DatabaseHelper.COLUMN_YEAR + ", "
                        + "c." + DatabaseHelper.COLUMN_CITY_NAME + " "
                        + "FROM " + DatabaseHelper.TABLE + " u "
                        + "LEFT JOIN " + DatabaseHelper.TABLE_CITIES + " c "
                        + "ON u." + DatabaseHelper.COLUMN_CITY_ID + " = c." + DatabaseHelper.COLUMN_CITY_ID_PK,
                null);

        String[] headers = new String[]{
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_YEAR
        };
        int[] views = new int[]{android.R.id.text1, android.R.id.text2};

        userAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.two_line_list_item,
                userCursor,
                headers,
                views,
                0);

        userList.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (userCursor != null) userCursor.close();
        if (db != null) db.close();
    }
}