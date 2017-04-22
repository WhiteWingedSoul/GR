package com.hedspi.hoangviet.eslrecom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hedspi.hoangviet.eslrecom.commons.Common;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.UserProfile;

public class SplashActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private SharedPreferences.Editor editor;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        SharedPreferences profile = getSharedPreferences("profile", 0);
        editor = profile.edit();
        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mUser = user;
                    // User is signed in
                    Log.d("LOG", "User Login:"+user.getUid());
                } else {
                    // User is signed out
                    Log.d("LOG", "Logout:");
                }
                // ...
            }
        };

        String uid = profile.getString("uid", null);
        if (uid!=null){
            getUserProfile(uid);
        }else {

            loginButton = (LoginButton) findViewById(R.id.loginButton);
            loginButton.setReadPermissions("email", "public_profile");

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d("FB Login Successed:", loginResult.getAccessToken().getToken());
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.d("FB Login Failed: ", exception.getMessage());
                }
            });
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LOG", "Firebase signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("LOG", "Firebase signInWithCredential", task.getException());
                            Toast.makeText(SplashActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            FirebaseUser user = task.getResult().getUser();
                            mUser = user;
                            getUserProfile(user.getUid());
                        }
                    }
                });
    }

    private void getUserProfile(final String uid){
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child(Common.USERS).orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("LOG","ahihi");
                UserProfile profile;
                if (dataSnapshot.getValue() == null){
                    profile = new UserProfile();
                    profile.setName(mUser.getDisplayName());
                    profile.setAvatarLink(mUser.getPhotoUrl().toString());
                    profile.setEmail(mUser.getEmail());
                    profile.setRole(Common.ROLE_USER);
                    profile.setUid(uid);
                    editor.putString("uid", uid);
                    editor.commit();

                    database.child(Common.USERS).child(uid).setValue(profile);
//                    DatabaseReference key = database.child(Common.USERS).push();
//                    profile.setKeyId(key.getKey());
//                    key.setValue(profile);
                }else{
                    profile = dataSnapshot.getChildren().iterator().next().getValue(UserProfile.class);
                }

                DatabaseManager.setUserProfile(profile);
                startMainActivity();
                SplashActivity.this.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void startMainActivity(){
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
