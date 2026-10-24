package com.example.practice16;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "usercontact.db";
    private static final int SCHEMA = 2;

    static final String TABLE = "users";
    static final String TABLE_CITIES = "cities";

    // users
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_YEAR = "year";
    static final String COLUMN_CITY_ID = "city_id";

    // cities
    static final String COLUMN_CITY_ID_PK = "_id";
    static final String COLUMN_CITY_NAME = "city_name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Таблица городов
        db.execSQL("CREATE TABLE " + TABLE_CITIES + " ("
                + COLUMN_CITY_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CITY_NAME + " TEXT NOT NULL);");

        // Начальное заполнение городов
        db.execSQL("INSERT INTO " + TABLE_CITIES + " (" + COLUMN_CITY_NAME + ") VALUES "
                + "('Новотроицк'), ('Санкт-Петербург'), ('Гай'), ('Оренбург'), ('Орск');");

        db.execSQL("CREATE TABLE " + TABLE + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_YEAR + " INTEGER, "
                + COLUMN_CITY_ID + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_CITY_ID + ") REFERENCES "
                + TABLE_CITIES + "(" + COLUMN_CITY_ID_PK + "));");

        // Начальные пользователи
        db.execSQL("INSERT INTO " + TABLE + " (" + COLUMN_NAME + ", " + COLUMN_YEAR + ", " + COLUMN_CITY_ID + ") VALUES "
                + "('Руслан Гамшитов', 1978, 1),"
                + "('Татьяна Стрельцова', 1992, 2),"
                + "('Жетибаев Еркин', 2008, 3);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITIES);
        onCreate(db);
    }
}