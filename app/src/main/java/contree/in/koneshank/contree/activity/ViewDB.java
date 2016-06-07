package contree.in.koneshank.contree.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import contree.in.koneshank.contree.R;
import contree.in.koneshank.contree.data.DBContract;
import contree.in.koneshank.contree.data.DBControl;

/**
 * Created by shashankgupta on 07/06/16.
 */
public class ViewDB extends AppCompatActivity {

    private Spinner spPlatforms;
    private EditText mUsername;
    private Button bView;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_db_layout);
        spPlatforms = (Spinner)findViewById(R.id.platforms);
        mUsername=(EditText)findViewById(R.id.username_input);
        tv=(TextView)findViewById(R.id.tvDetail);
        bView=(Button)findViewById(R.id.store_button);
        bView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptStore();
            }
        });

    }

    private void attemptStore() {
        // Reset errors.
        tv.setText("");
        mUsername.setError(null);

        // Store values at the time of the login attempt.
        String platform=spPlatforms.getSelectedItem().toString();
        String username = mUsername.getText().toString().trim();

        // Check for a valid username.
        if (TextUtils.isEmpty(username) && spPlatforms.getSelectedItemPosition()==0) {
            Toast.makeText(ViewDB.this,"Search by username or platform or both",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(username)) {
            getPasswordList(platform, null);
        }else if(spPlatforms.getSelectedItemPosition()==0){
            getPasswordList(null,username);
        }else{
            getPasswordList(platform,username);
        }

    }

    public void getPasswordList(String platform,String username) {

        DBControl db=new DBControl(ViewDB.this);
        SQLiteDatabase ourDatabase=db.getWritableDatabase();
        Cursor cursor = null;
        String sPlatform,sPassword,text="";
        try{
            if(username!=null && platform!=null){
                //String Query = "SELECT * FROM user_table where platform =" + platform + " AND username= " + username;
                cursor = ourDatabase.rawQuery("SELECT * FROM " + DBContract.USER_TABLE_NAME + " WHERE    platform=? AND username=?", new String[]{platform,username});
            }
            else if(username==null){
                cursor = ourDatabase.rawQuery("SELECT * FROM user_table WHERE platform=?", new String[] {platform + ""});
            }else if(platform==null){
                cursor = ourDatabase.rawQuery("SELECT * FROM user_table WHERE username=?", new String[] {username + ""});
            }else{

            }
            if(cursor.getCount()<=0){
                Toast.makeText(ViewDB.this,"No result Found",Toast.LENGTH_LONG).show();
            }else{
                for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                    sPlatform = cursor.getString(cursor.getColumnIndex(DBContract.PLATFORM));
                    sPassword = cursor.getString(cursor.getColumnIndex(DBContract.PASSWORD));
                    text+=sPlatform+" : "+sPassword+ " \n";
                }
                tv.setText(text);
            }

        }finally {

            cursor.close();
            db.close();
        }
    }

}

