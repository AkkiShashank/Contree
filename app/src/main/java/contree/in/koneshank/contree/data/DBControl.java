package contree.in.koneshank.contree.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shashankgupta on 06/06/16.
 */
public class DBControl extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "contree.db";

    public DBControl(Context context) {
        super(context, DATABASE_NAME, null ,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + DBContract.USER_TABLE_NAME + " (" +
                DBContract.USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.USERNAME + " VARCHAR(30) NOT NULL, " +
                DBContract.PLATFORM + " VARCHAR(30) NOT NULL, " +
                DBContract.PASSWORD + " VARCHAR(30) NOT NULL " +
                " );";


        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DBContract.USER_TABLE_NAME);
        onCreate(db);
    }
}
