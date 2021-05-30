package com.project_ikni.leprecone.DB;

import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

public class DBHelp extends SQLiteOpenHelper {

    public  DBHelp(Context c){
        super(c, "User", null, 1);
    }
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "User";

    private static final String GROUPS_TABLE_NAME = "markers";


    public static final String GROUP_ID = "group_id";
    public static final String GROUP_NAME = "marker_name";
    public static final String GROUP_NOTES = "marker_notes";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table about( id int, name text, " +
                "about text, img blob)");
        db.execSQL("create table markers( id INTEGER PRIMARY KEY AUTOINCREMENT, title, about text, " +
                "latLng text, img blob)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists about");
        db.execSQL("drop table if exists markers");
    }

    public boolean insert(int id, String name,  String about, byte[] img) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("about", about);
        contentValues.put("img", img);
        long res = db.insert("about", null, contentValues);
        System.out.print(res);
        return true;
    }

    public boolean update(int id,String name, String about, byte[] img){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("about", about);
        contentValues.put("img", img);
        long res = db.update("about", contentValues, "id=?", new String[]{String.valueOf(id)});
        System.out.print(res);

        return true;
    }

    public Cursor queueAll(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select  name,  about,\n" +
                "                         img from about", null);
        return cursor;
    }


// ---------------------Markers-------------------------------------------

    public boolean add_marker(String title, String about,  String latLng, byte[] img) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("about", about);
        contentValues.put("latLng", latLng);
        contentValues.put("img", img);
        db.insert("markers", null, contentValues);
        return true;
    }

    public int deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("markers", null, null);
    }

    public Cursor getAllMarkers(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select  id, title, about,  latLng,\n" +
                "                         img from markers ORDER BY id DESC", null);
        return cursor;
    }
    public Cursor getLastMarkers(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM markers ORDER BY id DESC LIMIT 3;",  null);
        return cursor;
    }


    public Cursor get_all_coordinate(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor  cursor = db.rawQuery("select title, about, latLng from markers",null);
        return cursor;
    }


}
