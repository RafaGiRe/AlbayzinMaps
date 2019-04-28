package com.tutorial.rafagire.albayzinmaps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBLocal extends SQLiteOpenHelper implements DBAccess{

    //ATTRIBUTES
    private SQLiteDatabase db;
    private String tableName = "AM_Sites";
    private String sqlCreateTable = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY, name TEXT NOT NULL, description TEXT, viewpoint INTEGER, church INTEGER, mosque INTEGER, monument INTEGER, zambra INTEGER, restaurant INTEGER, fountain INTEGER, latitude FLOAT, longitude FLOAT)";
    private String sqlDeleteTable = "DROP TABLE IF EXISTS " + tableName;

    //CONSTRUCTOR
    public DBLocal(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    //METHODS
    public void onCreate(SQLiteDatabase db) {
        //Create the DB if it doesn't exist
        db.execSQL(sqlCreateTable);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Me la cargo entera
        //db.execSQL("DROP TABLE IF EXISTS " + tableName);

        //La vuelvo a crear
        //db.execSQL(sqlCreate2);
    }

    public void createTable(){
        db = this.getWritableDatabase();

        db.execSQL(sqlCreateTable);
    }

    public void deleteTable(){
        db = this.getWritableDatabase();

        db.execSQL(sqlDeleteTable);
    }


    public void add(Site site) throws DBException{
        if (findById(site.id) == null) {
            try {
                db = this.getWritableDatabase();

                if (db != null) {
                    ContentValues newRegister = new ContentValues();
                    newRegister.put("id", site.id);
                    newRegister.put("name", site.name);
                    newRegister.put("description", site.description);
                    newRegister.put("viewpoint", ((site.viewpoint) ? 1 : 0));
                    newRegister.put("church", ((site.church) ? 1 : 0));
                    newRegister.put("mosque", ((site.mosque) ? 1 : 0));
                    newRegister.put("monument", ((site.monument) ? 1 : 0));
                    newRegister.put("zambra", ((site.zambra) ? 1 : 0));
                    newRegister.put("restaurant", ((site.restaurant) ? 1 : 0));
                    newRegister.put("fountain", ((site.fountain) ? 1 : 0));
                    newRegister.put("latitude", site.latitude);
                    newRegister.put("longitude", site.longitude);

                    db.insert(tableName, null, newRegister);
                    db.close();
                }
            }catch(Exception e){
                if (db != null)
                    db.close();
                throw new DBException(e);
            }
        }
        else{
            throw new DBException("Site identifier already used");
        }
    }


    public void update(Site site) throws DBException{
        if (findById(site.id) != null) {
            try {
                db = this.getWritableDatabase();

                if (db != null) {
                    ContentValues updateRegister = new ContentValues();
                    updateRegister.put("name", site.name);
                    updateRegister.put("description", site.description);
                    updateRegister.put("viewpoint", ((site.viewpoint) ? 1 : 0));
                    updateRegister.put("church", ((site.church) ? 1 : 0));
                    updateRegister.put("mosque", ((site.mosque) ? 1 : 0));
                    updateRegister.put("monument", ((site.monument) ? 1 : 0));
                    updateRegister.put("zambra", ((site.zambra) ? 1 : 0));
                    updateRegister.put("restaurant", ((site.restaurant) ? 1 : 0));
                    updateRegister.put("fountain", ((site.fountain) ? 1 : 0));
                    updateRegister.put("latitude", site.latitude);
                    updateRegister.put("longitude", site.longitude);

                    db.update(tableName, updateRegister, "id="+site.id, null);
                    db.close();
                }
            }catch(Exception e){
                if (db != null)
                    db.close();
                throw new DBException(e);
            }
        }
        else{
            throw new DBException("Site not found");
        }
    }


    public void remove(int id) throws DBException{
        if (findById(id) != null) {
            try {
                db = this.getWritableDatabase();

                if (db != null) {
                    db.delete(tableName, "id=" + id, null);
                    db.close();
                }
            }catch(Exception e){
                if (db != null)
                    db.close();
                throw new DBException(e);
            }
        }
        else{
            throw new DBException("Site not found");
        }
    }


    public Site findById(int id) throws DBException{
        Site site = null;

        try {
            db = this.getReadableDatabase();
            if (db != null) {
                String sqlSelId = "SELECT * FROM " + tableName + " WHERE id=" + id;
                Cursor c = db.rawQuery(sqlSelId, null);
                if (c.moveToFirst()) {
                    site = new Site();
                    site.id = c.getInt(0);
                    site.name = c.getString(1);
                    site.description = c.getString(2);
                    site.viewpoint = (c.getInt(3) != 0);
                    site.church = (c.getInt(4) != 0);
                    site.mosque = (c.getInt(5) != 0);
                    site.monument = (c.getInt(6) != 0);
                    site.zambra = (c.getInt(7) != 0);
                    site.restaurant = (c.getInt(8) != 0);
                    site.fountain = (c.getInt(9) != 0);
                    site.latitude =(double) c.getFloat(10);
                    site.longitude =(double) c.getFloat(11);
                }
                db.close();
            }
        }catch(Exception e){
            if(db!=null)
                db.close();
            throw new DBException(e);
        }
        return site;
    }


    public List<Site> findByFilters(boolean viewpoint, boolean church, boolean mosque, boolean monument, boolean zambra, boolean restaurant, boolean fountain) throws DBException{
        List<Site> list = null;

        try {
            db = this.getReadableDatabase();

            if (db != null) {
                String sqlSelFil = "SELECT * FROM " + tableName;
                boolean first = true;

                if(viewpoint){
                    if(first) {
                        sqlSelFil += " WHERE viewpoint = 1 ";
                        first = false;
                    } else{
                        sqlSelFil += "AND viewpoint = 1 ";
                    }
                }
                if(church){
                    if(first) {
                        sqlSelFil += " WHERE church = 1 ";
                        first = false;
                    } else{
                        sqlSelFil += "AND church = 1 ";
                    }
                }
                if(mosque){
                    if(first) {
                        sqlSelFil += " WHERE mosque = 1 ";
                        first = false;
                    } else{
                        sqlSelFil += "AND mosque = 1 ";
                    }
                }
                if(monument){
                    if(first) {
                        sqlSelFil += " WHERE monument = 1 ";
                        first = false;
                    } else{
                        sqlSelFil += "AND monument = 1 ";
                    }
                }
                if(zambra){
                    if(first) {
                        sqlSelFil += " WHERE zambra = 1 ";
                        first = false;
                    } else{
                        sqlSelFil += "AND zambra = 1 ";
                    }
                }
                if(restaurant){
                    if(first) {
                        sqlSelFil += " WHERE restaurant = 1 ";
                        first = false;
                    } else{
                        sqlSelFil += "AND restaurant = 1 ";
                    }
                }
                if(fountain){
                    if(first) {
                        sqlSelFil += " WHERE fountain = 1 ";
                        first = false;
                    } else{
                        sqlSelFil += "AND fountain = 1 ";
                    }
                }


                Cursor c = db.rawQuery(sqlSelFil, null);
                if (c.moveToFirst()) {
                    list = new ArrayList<Site>();
                    do {
                        Site site = new Site();
                        site.id = c.getInt(0);
                        site.name = c.getString(1);
                        site.description = c.getString(2);
                        site.viewpoint = (c.getInt(3) != 0);
                        site.church = (c.getInt(4) != 0);
                        site.mosque = (c.getInt(5) != 0);
                        site.monument = (c.getInt(6) != 0);
                        site.zambra = (c.getInt(7) != 0);
                        site.restaurant = (c.getInt(8) != 0);
                        site.fountain = (c.getInt(9) != 0);
                        site.latitude =(double) c.getFloat(10);
                        site.longitude =(double) c.getFloat(11);
                        list.add(site);
                    } while (c.moveToNext());
                }
                db.close();
            }
        }catch(Exception e){
            if(db!=null)
                db.close();
            throw new DBException(e);
        }
        return list;
    }


    public List<Site> findAll() throws DBException{
        List<Site> all = null;

        try {
            db = this.getReadableDatabase();

            if (db != null) {
                String sqlSelId = "SELECT * FROM " + tableName;
                Cursor c = db.rawQuery(sqlSelId, null);
                if (c.moveToFirst()) {
                    all = new ArrayList<Site>();
                    do {
                        Site site = new Site();
                        site.id = c.getInt(0);
                        site.name = c.getString(1);
                        site.description = c.getString(2);
                        site.viewpoint = (c.getInt(3) != 0);
                        site.church = (c.getInt(4) != 0);
                        site.mosque = (c.getInt(5) != 0);
                        site.monument = (c.getInt(6) != 0);
                        site.zambra = (c.getInt(7) != 0);
                        site.restaurant = (c.getInt(8) != 0);
                        site.fountain = (c.getInt(9) != 0);
                        site.latitude =(double) c.getFloat(10);
                        site.longitude =(double) c.getFloat(11);
                        all.add(site);
                    } while (c.moveToNext());
                }
                db.close();
            }
        }catch(Exception e){
            if(db!=null)
                db.close();
            throw new DBException(e);
        }
        return all;
    }


}
