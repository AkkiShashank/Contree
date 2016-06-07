package contree.in.koneshank.contree.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import contree.in.koneshank.contree.R;
import contree.in.koneshank.contree.data.DBContract;
import contree.in.koneshank.contree.data.DBControl;
import contree.in.koneshank.contree.data.PreferenceContract;

/**
 * Created by shashankgupta on 07/06/16.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mPassword;
    private String mPass;
    SharedPreferences spLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mPassword=(EditText)findViewById(R.id.password_login);
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button bLogin=(Button)findViewById(R.id.login_button);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

    }

    private void attemptLogin() {
        // Reset errors.
        mPassword.setError(null);
        spLogin =getSharedPreferences(PreferenceContract.login, Context.MODE_PRIVATE);
        boolean check=spLogin.getBoolean(PreferenceContract.login, false);

        // Store values at the time of the login attempt.
        String username = mPassword.getText().toString().trim();

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            mPassword.setError(getString(R.string.error_field_required));
            mPassword.requestFocus();
            return;
        }

        if(!check && username.equals("contree123")) {
            Toast.makeText(LoginActivity.this,"Successfully Login",Toast.LENGTH_LONG).show();
        }else if(!check){
            Toast.makeText(LoginActivity.this,"Default password is contree123",Toast.LENGTH_LONG).show();
            return;
        }
        else if(checkPassword(username)){
            mPassword.setError(getString(R.string.wrong_password));
            mPassword.requestFocus();
            return;
        }

        mPass = username;
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
        startActivity(intent);
        finish();
    }

    public boolean checkPassword(String password) {

        DBControl db=new DBControl(LoginActivity.this);
        SQLiteDatabase ourDatabase=db.getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = ourDatabase.rawQuery("SELECT * FROM user_table WHERE password=?", new String[] {password + ""});
            if(cursor.getCount()<=0){
                Toast.makeText(LoginActivity.this, "Incorrect credentials", Toast.LENGTH_LONG).show();
                return true;

            }else{
                return false;
            }

        }finally {

            cursor.close();
            db.close();
        }
    }


}

