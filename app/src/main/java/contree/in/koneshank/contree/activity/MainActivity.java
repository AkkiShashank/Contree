package contree.in.koneshank.contree.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import contree.in.koneshank.contree.R;
import contree.in.koneshank.contree.data.DBContract;
import contree.in.koneshank.contree.data.DBControl;
import contree.in.koneshank.contree.data.PreferenceContract;

public class MainActivity extends AppCompatActivity {

    private Spinner spPlatforms;
    private EditText mUsername;
    private EditText mPassword;
    private Button bStore,bView;
    SharedPreferences spLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spLogin =getSharedPreferences(PreferenceContract.login, Context.MODE_PRIVATE);
        spPlatforms = (Spinner)findViewById(R.id.platforms);
        mUsername=(EditText)findViewById(R.id.username_input);
        mPassword=(EditText)findViewById(R.id.password_input);
        bStore=(Button)findViewById(R.id.store_button);
        bView=(Button)findViewById(R.id.view_db);
        bView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewDB.class);
                startActivity(intent);
            }
        });
        bStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptStore();
            }
        });

    }

    private void attemptStore() {
        // Reset errors.
        mUsername.setError(null);
        mPassword.setError(null);

        // Store values at the time of the login attempt.
        String platform=spPlatforms.getSelectedItem().toString();
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            mUsername.setError(getString(R.string.error_field_required));
            mUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            mPassword.setError(getString(R.string.error_field_required));
            mPassword.requestFocus();
            return;
        }

        if(!getEmployeeName(platform,username,password)){
            write_in_database(username, platform, password);
            spLogin.edit().putBoolean(PreferenceContract.login, true).commit();
            Toast.makeText(MainActivity.this,"Saved to database",Toast.LENGTH_LONG).show();
            mUsername.setText("");
            mPassword.setText("");
        }else{
            mUsername.setText("");
            mPassword.setText("");
        }


    }

    private void write_in_database(String username, String platform, String password) {
        DBControl db = new DBControl(this);
        SQLiteDatabase ourDatabase = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBContract.USERNAME, username);
        cv.put(DBContract.PLATFORM, platform);
        cv.put(DBContract.PASSWORD, password);
        ourDatabase.insert(DBContract.USER_TABLE_NAME, null, cv);
        db.close();
    }

    public boolean getEmployeeName(String platform,String username,String password) {

        DBControl db=new DBControl(MainActivity.this);
        SQLiteDatabase ourDatabase=db.getWritableDatabase();
        Cursor cursor = null;
        String userId;
        try{

            cursor = ourDatabase.rawQuery("SELECT platform FROM user_table WHERE platform=?", new String[] {platform + ""});

            if(cursor.getCount() > 0) {

                cursor.moveToFirst();
                Toast.makeText(MainActivity.this,cursor.toString(),Toast.LENGTH_LONG).show();
                int row=cursor.getColumnIndex(DBContract.PLATFORM);
                userId = cursor.getString(row);
                ContentValues cv = new ContentValues();
                cv.put(DBContract.USERNAME, username);
                cv.put(DBContract.PLATFORM, platform);
                cv.put(DBContract.PASSWORD, password);
                ourDatabase.update(DBContract.USER_TABLE_NAME, cv, "platform = ?", new String[]{userId});
                Toast.makeText(MainActivity.this,"Your "+ platform+ " username and password is updated",Toast.LENGTH_LONG).show();
                return true;
            }

            return false;
        }finally {

            cursor.close();
            db.close();
        }
    }

}
