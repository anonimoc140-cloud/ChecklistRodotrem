package br.com.checklistrodotrem;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context c){ super(c,"checklists.db",null,1); }
    public void onCreate(SQLiteDatabase db){ db.execSQL("CREATE TABLE checklists(id INTEGER PRIMARY KEY AUTOINCREMENT, created_at TEXT, driver TEXT, date TEXT, type TEXT, tractor TEXT, trailer1 TEXT, trailer2 TEXT, answers TEXT)"); }
    public void onUpgrade(SQLiteDatabase db,int oldV,int newV){ db.execSQL("DROP TABLE IF EXISTS checklists"); onCreate(db); }
    public long insert(ContentValues v){ return getWritableDatabase().insert("checklists",null,v); }
    public Cursor all(){ return getReadableDatabase().query("checklists",null,null,null,null,null,"id DESC"); }
    public Cursor one(long id){ return getReadableDatabase().query("checklists",null,"id=?",new String[]{String.valueOf(id)},null,null,null); }
}
