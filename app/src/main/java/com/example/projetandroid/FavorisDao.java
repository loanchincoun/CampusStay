package com.example.projetandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavorisDao {

    private FavorisDatabase dbHelper;

    public FavorisDao(Context context) {
        dbHelper = new FavorisDatabase(context);
    }

    public void ajouterFavori(Logement logement) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("logement_id", logement.getId());
        values.put("nom", logement.getNomLogement());
        values.put("adresse", logement.getAdresse());
        values.put("superficie", logement.getSuperficie());
        values.put("nb_pieces", logement.getNbPieces());
        values.put("parking", logement.getParking() ? 1 : 0);

        db.insertWithOnConflict(
                FavorisDatabase.TABLE_FAVORIS,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public void supprimerFavori(String logementId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                FavorisDatabase.TABLE_FAVORIS,
                "logement_id=?",
                new String[]{logementId}
        );
    }

    public boolean estFavori(String logementId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(
                FavorisDatabase.TABLE_FAVORIS,
                null,
                "logement_id=?",
                new String[]{logementId},
                null, null, null
        );

        boolean existe = c.moveToFirst();
        c.close();
        return existe;
    }

    public Logement getFavori(String logementId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(
                FavorisDatabase.TABLE_FAVORIS,
                null,
                "logement_id=?",
                new String[]{logementId},
                null, null, null
        );

        if (c.moveToFirst()) {
            Logement logement = new Logement();
            logement.setId(logementId);
            logement.setNomLogement(c.getString(c.getColumnIndexOrThrow("nom")));
            logement.setAdresse(c.getString(c.getColumnIndexOrThrow("adresse")));
            logement.setSuperficie(String.valueOf(c.getInt(c.getColumnIndexOrThrow("superficie"))));
            logement.setNbPieces(String.valueOf(c.getInt(c.getColumnIndexOrThrow("nb_pieces"))));
            logement.setParking(c.getInt(c.getColumnIndexOrThrow("parking")) == 1);
            c.close();
            return logement;
        }

        c.close();
        return null;
    }
    public List<Logement> getLstFavoris() {

        List<Logement> favoris = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(
                FavorisDatabase.TABLE_FAVORIS,
                null,   // toutes les colonnes
                null,   // pas de WHERE
                null,
                null,
                null,
                "nom ASC" // optionnel : tri par nom
        );

        if (c.moveToFirst()) {
            do {
                Logement logement = new Logement();

                logement.setId(
                        c.getString(c.getColumnIndexOrThrow("logement_id"))
                );
                logement.setNomLogement(
                        c.getString(c.getColumnIndexOrThrow("nom"))
                );
                logement.setAdresse(
                        c.getString(c.getColumnIndexOrThrow("adresse"))
                );
                logement.setSuperficie(
                        String.valueOf(c.getInt(c.getColumnIndexOrThrow("superficie")))
                );
                logement.setNbPieces(
                        String.valueOf(c.getInt(c.getColumnIndexOrThrow("nb_pieces")))
                );
                logement.setParking(
                        c.getInt(c.getColumnIndexOrThrow("parking")) == 1
                );

                favoris.add(logement);

            } while (c.moveToNext());
        }

        c.close();
        return favoris;
    }

}

