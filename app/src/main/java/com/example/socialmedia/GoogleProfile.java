package com.example.socialmedia;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;

public class GoogleProfile extends AppCompatActivity {

    private CircleImageView circleImageView;
    private TextView name;
    private TextView email, textView;
    private Button signoutBtn;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_profile);

        circleImageView = findViewById(R.id.profile_pic);
        name =findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        signoutBtn = findViewById(R.id.signoutBtn);
        textView =findViewById(R.id.textView);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                Toast.makeText(GoogleProfile.this,"Logged Out...",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent=new Intent(GoogleProfile.this,MainActivity.class);

                startActivity(intent);
            }
        });
    }

    private void gotoMainActivity() {
        startActivity(new Intent(GoogleProfile.this, MainActivity.class));
        finish();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                name.setText( acct.getDisplayName());
                email.setText(acct.getEmail());
                textView.setText(" Google Profile ");
                Glide.with( GoogleProfile.this).load(acct.getPhotoUrl()).into(circleImageView);
                Toast.makeText(this, "LoggedIn Successfully !", Toast.LENGTH_SHORT).show();
            }
            else {
                gotoMainActivity();
            }
        } catch (ApiException e) {
            Log.d("Message", e.toString());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Task<GoogleSignInAccount> opr = mGoogleSignInClient.silentSignIn().addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
            @Override
            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                handleSignInResult(task);
            }
        });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
}