package com.example.projetandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavorisDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "favoris.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_FAVORIS = "favoris";

    public FavorisDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable =
                "CREATE TABLE " + TABLE_FAVORIS + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "logement_id TEXT UNIQUE, " +
                        "nom TEXT, " +
                        "adresse TEXT, " +
                        "superficie INTEGER, " +
                        "nb_pieces INTEGER, " +
                        "parking INTEGER)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORIS);
        onCreate(db);
    }
}

