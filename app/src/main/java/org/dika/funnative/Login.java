package org.dika.funnative;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
	AppCompatEditText email,password;
	AppCompatButton login;
	AppCompatTextView forgot,signup;
	AppCompatImageView showPass;
	ProgressBar pb;
	FirebaseAuth fa;
	FirebaseDatabase fd;

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

		setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
		getWindow().setStatusBarColor(Color.TRANSPARENT);

		email=findViewById(R.id.email);
		password=findViewById(R.id.password);
		login=findViewById(R.id.btn_login);
		showPass = findViewById(R.id.showPass);
		forgot=findViewById(R.id.forgot_textview);
		signup=findViewById(R.id.signup_textview);
		pb=findViewById(R.id.pb2);
		fa= FirebaseAuth.getInstance();

		signup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Login.this, Register.class);
				startActivity(intent);
			}
		});
		showPass.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
					showPass.setImageResource(R.drawable.visible);
					password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}
				else{
					showPass.setImageResource(R.drawable.invisible);
					password.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
		forgot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Login.this, ForgotPasswordActivity.class);
				startActivity(intent);
			}
		});
		fd= FirebaseDatabase.getInstance();
		if (fa.getCurrentUser()!=null){
			pb.setVisibility(View.VISIBLE);
			DatabaseReference dr = fd.getReference("Users").child(fa.getCurrentUser().getUid()).child("nama");
			dr.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
					FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
					user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
						@Override
						public void onSuccess(GetTokenResult result) {
							String idToken = result.getToken();
							String val = dataSnapshot.getValue(String.class);
							SharedPreferences sharedPref = getSharedPreferences("LOGIN",Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = sharedPref.edit();
							editor.putString("token", idToken);
							editor.apply();
							Intent intent = new Intent(Login.this, MainActivity.class);
							intent.putExtra("nama", val);
							intent.putExtra("email", fa.getCurrentUser().getEmail().toString());
							pb.setVisibility(View.GONE);
							startActivity(intent);
						}
					});
				}
				@Override
				public void onCancelled(@NonNull DatabaseError databaseError) {

				}
			});

		}


		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pb.setVisibility(View.VISIBLE);
				String txtemail=email.getText().toString();
				String txtpass=password.getText().toString();
				if (TextUtils.isEmpty(txtemail)){
					email.setError("Email Tidak Boleh Kosong");
					Toast.makeText(getApplicationContext(),"Email Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!Patterns.EMAIL_ADDRESS.matcher(txtemail).matches())
				{
					email.setError("Format Email Salah");
					Toast.makeText(getApplicationContext(),"Format Email Salah", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (TextUtils.isEmpty(txtpass)){
					password.setError("Password Tidak Boleh Kosong");
					Toast.makeText(getApplicationContext(),"Password Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}

				fa.signInWithEmailAndPassword(txtemail,txtpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							DatabaseReference dr = fd.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("nama");
							dr.addValueEventListener(new ValueEventListener() {
								@Override
								public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
									FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
									user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
										@Override
										public void onSuccess(GetTokenResult result) {
											String idToken = result.getToken();
											String val = dataSnapshot.getValue(String.class);
											SharedPreferences sharedPref = getSharedPreferences("LOGIN",Context.MODE_PRIVATE);
											SharedPreferences.Editor editor = sharedPref.edit();
											editor.putString("token", idToken);
											editor.apply();
											Intent intent = new Intent(Login.this, MainActivity.class);
											intent.putExtra("nama", val);
											intent.putExtra("email", fa.getCurrentUser().getEmail().toString());
											pb.setVisibility(View.GONE);
											Toast.makeText(getApplicationContext(), "Berhasil Login, Selamat Datang" + val , Toast.LENGTH_LONG).show();
											startActivity(intent);
										}
									});
								}

								@Override
								public void onCancelled(@NonNull DatabaseError databaseError) {

								}
							});

						}else {
							Toast.makeText(getApplicationContext(), "Gagal Login" + task.getException()
									.getMessage(), Toast.LENGTH_LONG).show();
							pb.setVisibility(View.GONE);
						}
					}
				});
			}
		});
		}
	public static void setWindowFlag(Activity activity, final int bits, boolean on) {

		Window win = activity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
}