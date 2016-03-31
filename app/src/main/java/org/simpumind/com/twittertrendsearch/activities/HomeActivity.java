package org.simpumind.com.twittertrendsearch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;
import org.simpumind.com.twittertrendsearch.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {

    public static CallbackManager callbackmanager;

    private TwitterLoginButton loginButtons;
    private Button fbbutton;

    private Button facebookd;
    private Button twtterd;

    private TwitterAuthClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "org.simpumind.com.twittertrendsearch",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        client = new TwitterAuthClient();

        fbbutton = (Button) findViewById(R.id.facebook);

        fbbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Call private method
                onFblogin();
            }
        });

        Button lodR = (Button) findViewById(R.id.loadr);
        lodR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SocialActivity.class);
                startActivity(intent);
            }
        });

        facebookd  = (Button) findViewById(R.id.facebookd);
        twtterd = (Button) findViewById(R.id.twitterd);

        facebookd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFblogin();
            }
        });

        twtterd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginTwitter();
            }
        });

        /*LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email,publish_actions, user_events", "");
        getLoginDetails(loginButton);*/

    }

    public void loginTwitter(){
        /*loginButtons = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButtons.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(HomeActivity.this, SocialActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });*/
        client.authorize(HomeActivity.this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                Toast.makeText(HomeActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(HomeActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });

    }


    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackmanager = CallbackManager.Factory.create();

       // callbackManager = CallbackManager.Factory.create();
    }

    /*
    Register a callback function with LoginButton to respond to the login result.
    On successful login,login result has new access token and  recently granted permissions.
    */

    /*protected void getLoginDetails(LoginButton login_button){

        // Callback registration
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult login_result) {
                GraphRequest request = GraphRequest.newGraphPathRequest(
                        login_result.getAccessToken(),
                        "/search",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                // Insert your code here
                                Log.d("AccessApp", login_result.getAccessToken().getToken());
                                JSONObject jsonObject = response.getJSONObject();
                                Intent intent = new Intent(HomeActivity.this, SocialActivity.class);
                                intent.putExtra("jsondata", jsonObject.toString());
                                startActivity(intent);
                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("q", "Nigeria");
                parameters.putString("type", "event");
                request.setParameters(parameters);
                request.executeAsync();

                //getUserInfo(login_result);
            }

            @Override
            public void onCancel() {
                // code for cancellation
            }

            @Override
            public void onError(FacebookException exception) {
                //  code to handle error
            }
        });
    }*/
    /*
        To get the facebook user's own profile information via  creating a new request.
        When the request is completed, a callback is called to handle the success condition.
     */
    protected void getUserInfo(LoginResult login_result){



        GraphRequest data_request = GraphRequest.newMeRequest(
                login_result.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {

                        Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                        intent.putExtra("jsondata",json_object.toString());
                        startActivity(intent);
                    }
                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackmanager.onActivityResult(requestCode, resultCode, data);
        client.onActivityResult(requestCode, resultCode, data);
        Log.e("data", data.toString());
    }

    private void onFblogin()
    {

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email,publish_actions, user_events", ""));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newGraphPathRequest(
                                loginResult.getAccessToken(),
                                "/search",
                                new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse response) {
                                        // Insert your code here
                                        Log.d("AccessApp", loginResult.getAccessToken().getToken());
                                        JSONObject jsonObject = response.getJSONObject();
                                        /*Intent intent = new Intent(HomeActivity.this, SocialActivity.class);
                                        intent.putExtra("jsondata", jsonObject.toString());
                                        startActivity(intent);*/

                                        SharedPreferences settings = getApplicationContext().getSharedPreferences("KEY_NAME",
                                                getApplicationContext().MODE_PRIVATE);
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putString("fbsession", loginResult.getAccessToken().getToken());
                                        editor.commit();
                                        editor.apply();
                                    }

                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("q", "Nigeria");
                        parameters.putString("type", "event");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Log.d("TAG_CANCEL", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("TAG_ERROR", error.toString());
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
           // startActivity(intent);
            LoginManager.getInstance().logOut();
        }

        return super.onOptionsItemSelected(item);
    }

    public static void checkLogin(){
        LoginManager.getInstance().logOut();
    }
}


