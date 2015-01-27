package company.businessinc.bathtouch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import company.businessinc.dataModels.Status;
import company.businessinc.dataModels.User;
import company.businessinc.endpoints.UserLogin;
import company.businessinc.endpoints.UserLoginInterface;
import company.businessinc.endpoints.UserNew;
import company.businessinc.endpoints.UserNewInterface;
import company.businessinc.networking.APICall;
import company.businessinc.networking.CheckNetworkConnection;


public class CreateAccountActivity extends ActionBarActivity implements UserNewInterface, UserLoginInterface {

    public static String name, email, username, password, teamName;
    private SharedPreferences mSharedPreferences;
    private String userLoggedIn = "login";
    private static final String cookie = "Cookie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        if(savedInstanceState == null)
            mSharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_preferences), Context.MODE_PRIVATE);
    }

    public void create_account(View view) {
        if (CheckNetworkConnection.check(this)) {
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.create_account_boxes);
            name = ((EditText)linearLayout.getChildAt(0)).getText().toString();
            email = ((EditText)linearLayout.getChildAt(1)).getText().toString();
            username = ((EditText)linearLayout.getChildAt(2)).getText().toString();
            password = ((EditText)linearLayout.getChildAt(3)).getText().toString();
            teamName = ((EditText)linearLayout.getChildAt(4)).getText().toString();
            Log.d("Create User", "Network is working, let's create a user");
            new UserNew(this,username,password,email,name,1).execute();
        } else {
            Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
            Log.d("Create User", "Network is not working");
        }
    }

    @Override
    public void userNewCallback(Status data) {
        if(data != null){
            if(data.getStatus().equals(true)) {
                Log.d("Create User", "User successfully created");

                if (CheckNetworkConnection.check(this)) {
                    Log.d("Create User", "Network is working, let's log in");
                    new UserLogin(this,username,password).execute();
                } else {
                    Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
                    Log.d("Create User", "Network is not working");
                }
            } else {
                Log.d("Create User", "Invalid credentials");
                Toast toast = Toast.makeText(CreateAccountActivity.this, "Bad Details", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Log.d("Create User", "Error connecting and parsing");
            Toast.makeText(CreateAccountActivity.this, "Cannot connect", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void userLoginCallback(User data) {
        if (data != null) {
            if (data.isLoggedIn()) { //User has logged in
                Log.d("Create User", "Logged in");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                mSharedPreferences.edit().putBoolean(userLoggedIn, true).commit();
                mSharedPreferences.edit().putString(cookie, APICall.getCookie()).commit();
                mSharedPreferences.edit().putString("user", data.toString()).commit();
                finish();
            } else {
                Log.d("Create User", "Invalid credentials");
                Toast toast = Toast.makeText(CreateAccountActivity.this, "Bad Details", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Log.d("Login", "Error connecting and parsing");
            Toast.makeText(CreateAccountActivity.this, "Cannot connect", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }



}
