package com.tradexc.basicdatabase;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.ArrayList;

/**
 * Created by Spreetrip on 12/15/2016.
 */

public class DBManager {
    private SQLiteDatabase sqlDB;
    public static final String dbName="tradeXC";
    public static final int dbVersion = 1;

    public static final String tableItemGroups ="itemgroup";

    public static final String itemGroupColitemgroupid = "itemgroupid"; //PK
    public static final String itemGroupColitemgroupidnum = "itemgroupidnum";
    public static final String itemGroupColitemgroupcode = "itemgroupcode";
    public static final String itemGroupColitemgroupname = "itemgroupname";
    public static final String itemGroupColitemgroupstatus = "itemgroupstatus";
    public static final String itemGroupColitemgrouptype = "itemgrouptype";

    protected static final String buildItemGroupTable =
            "CREATE TABLE IF NOT EXISTS " + tableItemGroups
                +"("
                +itemGroupColitemgroupid +" INTEGER PRIMARY KEY, "
                +itemGroupColitemgroupidnum + " INTEGER, "
                +itemGroupColitemgroupcode + " TEXT, "
                +itemGroupColitemgroupname + " TEXT, "
                +itemGroupColitemgroupstatus + " TEXT, "
                +itemGroupColitemgrouptype + " TEXT "
                +");"
            ;

    public DBManager(Context context){
        DatabaseHelper db = new DatabaseHelper(context);
        sqlDB = db.getWritableDatabase();
    }

    public long insertNewItem(ContentValues values){
        long id= sqlDB.insert(tableItemGroups,"",values);

        return id;
    }

    public ArrayList<ItemGroupItem> getItemGroupList(){
        ArrayList<ItemGroupItem> itemGroupList = new ArrayList<>();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(tableItemGroups);

        Cursor cursor = qb.query(sqlDB,null,null,null,null,null,null);

        if(cursor.moveToFirst()){
            do{
                ItemGroupItem item = new ItemGroupItem(
                        cursor.getString(cursor.getColumnIndex(itemGroupColitemgroupcode)),
                        cursor.getString(cursor.getColumnIndex(itemGroupColitemgroupname)),
                        cursor.getString(cursor.getColumnIndex(itemGroupColitemgroupidnum)),
                        cursor.getString(cursor.getColumnIndex(itemGroupColitemgroupstatus)),
                        cursor.getString(cursor.getColumnIndex(itemGroupColitemgroupidnum))

                );
                itemGroupList.add(item);

            }while(cursor.moveToNext());
        }

        return itemGroupList;
    }



    protected static class DatabaseHelper extends SQLiteOpenHelper{
        private Context context;

        public DatabaseHelper(Context context){
            super(context,dbName,null,dbVersion);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(buildItemGroupTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

}
