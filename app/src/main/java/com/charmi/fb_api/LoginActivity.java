package com.charmi.fb_api;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Posts>>{

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;
    private String firstName,lastName, email,birthday,gender;
    private URL profilePicture;
    private String userId;
    private String TAG = "LoginActivity";
    private static final int LOADER_ID = 1;

    private static String REQUEST_URL ="https://graph.facebook.com/v2.10/me/feed?access_token=EAAF5cZBa1bZBsBAL0ZBsxOtsveyFfTO8TvxGScu1dZBbW6t3KwdM4JRkYy65gMBEIyNyxQZAlV6iKJ5MI3aY9iSXOZAhpeT8joJbjLZBUlQ38kTzl9cu773hKSZCDSHo1MAhuZCYFIuxeuh2cJZBD5WMdjkiZCuHlnTkFoZD";

    private ListView postListView;
    private PostAdapter mAdapter;

    private ProgressBar progress;
    private TextView empty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setHeight(100);
        loginButton.setTextColor(Color.WHITE);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        loginButton.setCompoundDrawablePadding(0);







        loginButton.setReadPermissions("email", "user_birthday","user_posts");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
/*
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e(TAG,object.toString());
                        Log.e(TAG,response.toString());

                      Toast.makeText(LoginActivity.this, "done loading", Toast.LENGTH_SHORT).show();
                     try {
                            userId = object.getString("id");
                            profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");
                            if(object.has("first_name"))
                                firstName = object.getString("first_name");
                            if(object.has("last_name"))
                                lastName = object.getString("last_name");
                            if (object.has("email"))
                                email = object.getString("email");
                            if (object.has("birthday"))
                                birthday = object.getString("birthday");
                            if (object.has("gender"))
                                gender = object.getString("gender");

                           Intent main = new Intent(LoginActivity.this,LoginActivity.class);
                            main.putExtra("name",firstName);
                            main.putExtra("surname",lastName);
                            main.putExtra("imageUrl",profilePicture.toString());
                            startActivity(main);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //Here we put the requested fields to be returned from the JSONObject
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, birthday, gender");
                request.setParameters(parameters);
                request.executeAsync();
*/
                data();


            }
            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException e) {
                // App code
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "fail!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void data() {

        postListView = (ListView) findViewById(R.id.list);

        empty= (TextView) findViewById(R.id.empty);
        progress= (ProgressBar) findViewById(R.id.loading_spinner);
        mAdapter = new PostAdapter(this, new ArrayList<Posts>());

        postListView.setEmptyView(findViewById(R.id.empty));

        postListView.setAdapter(mAdapter);


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);


        } else {

            progress.setVisibility(View.INVISIBLE);
            empty.setText("No Internet Connection");
        }
    }

    @Override
    public Loader<List<Posts>> onCreateLoader(int id, Bundle bundle) {
        return new PostLoader(this, REQUEST_URL);
    }



    @Override
    public void onLoadFinished(Loader<List<Posts>> loader, final List<Posts> posts) {


        progress.setVisibility(View.INVISIBLE);
        mAdapter.clear();
        empty.setText("No posts found");

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (posts != null && !posts.isEmpty()) {

            mAdapter.addAll(posts);

        }



    }



    @Override
    public void onLoaderReset(Loader<List<Posts>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();

    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

}
