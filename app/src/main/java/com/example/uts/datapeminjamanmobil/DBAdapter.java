package com.example.uts.datapeminjamanmobil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.uts.datapeminjamanmobil.domain.Peminjam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taufik on 15/10/28.
 */
public class DBAdapter extends SQLiteOpenHelper {
    private static final String DB_NAME = "datarental";
    private static final String TABLE_NAME = "peminjam";
    private static final String COL_ID = "id";
    private static final String COL_NAMA = "nama";
    private static final String COL_ALAMAT = "alamat";
    private static final String COL_JENISKELAMIN = "jeniskelamin";
    private static final String COL_NAMAMOBIL = "namamobil";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME +";";

    private SQLiteDatabase sqliteDatabase = null;

    public DBAdapter(Context context) {super(context, DB_NAME, null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {createTable(db);}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
    }

    public void openDB() {
        if (sqliteDatabase == null) {
            sqliteDatabase = getWritableDatabase();
        }
    }

    public void closeeDB() {
        if (sqliteDatabase != null) {
            if (sqliteDatabase.isOpen()){
                sqliteDatabase.close();
            }
        }
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_ID
                + "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COL_NAMA + "TEXT," + COL_ALAMAT + "TEXT,"
                + COL_JENISKELAMIN + " TEXT," + COL_NAMAMOBIL + "TEXT);");
    }

    public void updatePeminjam(Peminjam peminjam) {
        sqliteDatabase = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_NAMA, peminjam.getNama());
        cv.put(COL_ALAMAT, peminjam.getAlamat());
        cv.put(COL_JENISKELAMIN, peminjam.getJenisKelamin());
        cv.put(COL_NAMAMOBIL, peminjam.getNamaMobil());
        String whereClause = COL_ID + "==?";
        String whereArgs[] = new String[] { peminjam.getId() };
        sqliteDatabase.update(TABLE_NAME, cv, whereClause, whereArgs);
        sqliteDatabase.close();
    }

    public void save(Peminjam peminjam) {
        sqliteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAMA, peminjam.getNama());
        contentValues.put(COL_ALAMAT, peminjam.getAlamat());
        contentValues.put(COL_JENISKELAMIN, peminjam.getJenisKelamin());
        contentValues.put(COL_NAMAMOBIL, peminjam.getNamaMobil());

        sqliteDatabase.insertWithOnConflict(TABLE_NAME, null,
                contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        sqliteDatabase.close();
    }

    public void delete(Peminjam peminjam) {
        sqliteDatabase = getWritableDatabase();
        String whereClause = COL_ID + "==?";
        String[] whereArgs = new String[] { String.valueOf(peminjam.getId()) };
        sqliteDatabase.delete(TABLE_NAME, whereClause, whereArgs);
        sqliteDatabase.close();
    }

    public void deleteAll() {
        sqliteDatabase = getWritableDatabase();
        sqliteDatabase.delete(TABLE_NAME, null, null);
        sqliteDatabase.close();
    }

    public List<Peminjam> getAllBuruh() {
        sqliteDatabase = getWritableDatabase();

        Cursor cursor = this.sqliteDatabase.query(TABLE_NAME, new String[] {
                COL_ID, COL_NAMA, COL_ALAMAT, COL_JENISKELAMIN }, null, null, null, null, null);
        List<Peminjam> peminjams = new ArrayList<Peminjam>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Peminjam peminjam = new Peminjam();
                peminjam.setId(cursor.getString(cursor.getColumnIndex(COL_ID)));
                peminjam.setNama(cursor.getString(cursor
                        .getColumnIndex(COL_NAMA)));
                peminjam.setAlamat(cursor.getString(cursor
                        .getColumnIndex(COL_ALAMAT)));
                peminjam.setJenisKelamin(cursor.getString(cursor
                        .getColumnIndex(COL_JENISKELAMIN)));
                peminjam.setNamaMobil(cursor.getString(cursor
                        .getColumnIndex(COL_NAMAMOBIL)));
                peminjams.add(peminjam);
            }
            sqliteDatabase.close();
            return peminjams;
        } else {
            sqliteDatabase.close();
            return new ArrayList<Peminjam>();
        }
    }
}