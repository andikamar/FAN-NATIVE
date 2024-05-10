package org.dika.funnative;

import android.app.Activity;
import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Register extends AppCompatActivity {
	AppCompatEditText email,password,confirmpassword,nama;
	AppCompatTextView signin;
	AppCompatButton register;
	AppCompatImageView showPass, showConfirmPass;
	ProgressBar pb;
	FirebaseAuth fa;
	FirebaseDatabase fd;


	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

		setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
		getWindow().setStatusBarColor(Color.TRANSPARENT);

		email=  findViewById(R.id.email);
		password=  findViewById (R.id.password);
		confirmpassword= findViewById(R.id.confirm_password);
		showPass = findViewById(R.id.showPass);
		showConfirmPass = findViewById(R.id.showPassConfirm);
		nama= findViewById (R.id.name);
		register= findViewById(R.id.btn_register);
		pb=findViewById(R.id.pb1);
		signin=findViewById(R.id.signin_textview);

		fa=FirebaseAuth.getInstance();


		if (fa.getCurrentUser()!=null){
			Intent intent = new Intent(Register.this, Login.class);
			startActivity(intent);
		}

		fd=FirebaseDatabase.getInstance();

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

		showConfirmPass.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(confirmpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
					showConfirmPass.setImageResource(R.drawable.visible);
					confirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}
				else{
					showConfirmPass.setImageResource(R.drawable.invisible);
					confirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});

		signin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Register.this, Login.class);
				startActivity(intent);
			}
		});
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pb.setVisibility(View.VISIBLE);

				String txtemail=email.getText().toString();
				String txtpass=password.getText().toString();
				String txtconfirmpassowrd = confirmpassword.getText().toString();
				final String txtnama=nama.getText().toString();

				if (TextUtils.isEmpty(txtemail)){
					email.setError("Email Tidak Boleh Kosong");
					Toast.makeText(getApplicationContext(),"Email Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (TextUtils.isEmpty(txtpass)){
					password.setError("Password Tidak Boleh Kosong");
					Toast.makeText(getApplicationContext(),"Password Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (TextUtils.isEmpty(txtconfirmpassowrd)){
					confirmpassword.setError("Konfirmasi Password Tidak Boleh Kosong");
					Toast.makeText(getApplicationContext(),"Konfirmasi Password Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (TextUtils.isEmpty(txtnama)){
					nama.setError("Nama Tidak Boleh Kosong");
					Toast.makeText(getApplicationContext(),"Nama Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (txtnama.length() < 3){
					nama.setError("Minimal Nama 3 Huruf");
					Toast.makeText(getApplicationContext(),"Minimal Nama 3 Huruf", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (txtnama.length() > 50){
					nama.setError("Maximal Nama 50 Huruf");
					Toast.makeText(getApplicationContext(),"Maximal Nama 50 Huruf", Toast.LENGTH_LONG).show();
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
				if (!txtpass.matches(".*[0-9].*")) {
					password.setError("Password Harus Mengandung Angka");
					Toast.makeText(getApplicationContext(),"Password Harus Mengandung Angka", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!txtpass.matches(".*[a-z].*")) {
					password.setError("Password Harus Mengandung Huruf Kecil");
					Toast.makeText(getApplicationContext(),"Password Harus Mengandung Huruf Kecil", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!txtpass.matches(".*[A-Z].*")) {
					password.setError("Password Harus Mengandung Huruf Besar");
					Toast.makeText(getApplicationContext(),"Password Harus Mengandung Huruf Besar", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!txtpass.matches(".*[a-zA-Z].*")) {
					password.setError("Password Harus Mengandung Huruf");
					Toast.makeText(getApplicationContext(),"Password Harus Mengandung Huruf", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!txtpass.matches( ".{8,}")) {
					password.setError("Minimal Password 8 Karakter");
					Toast.makeText(getApplicationContext(),"Minimal Password 8 Karakter", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!txtpass.matches(".*[0-9].*")) {
					password.setError("Password Harus Mengandung Angka");
					Toast.makeText(getApplicationContext(),"Password Harus Mengandung Angka", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!txtpass.matches(".*[a-z].*")) {
					password.setError("Password Harus Mengandung Huruf Kecil");
					Toast.makeText(getApplicationContext(),"Password Harus Mengandung Huruf Kecil", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!txtpass.matches(".*[A-Z].*")) {
					password.setError("Password Harus Mengandung Huruf Besar");
					Toast.makeText(getApplicationContext(),"Password Harus Mengandung Huruf Besar", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!txtpass.matches(".*[a-zA-Z].*")) {
					password.setError("Password Harus Mengandung Huruf");
					Toast.makeText(getApplicationContext(),"Password Harus Mengandung Huruf", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!txtpass.matches( ".{8,}")) {
					password.setError("Minimal Password 8 Karakter");
					Toast.makeText(getApplicationContext(),"Minimal Password 8 Karakter", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}

				if (!txtconfirmpassowrd.matches(".*[0-9].*")) {
					confirmpassword.setError("Konfirmasi Password Harus Mengandung Angka");
					Toast.makeText(getApplicationContext(),"Konfirmasi Password Harus Mengandung Angka", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!txtconfirmpassowrd.matches(".*[a-z].*")) {
					confirmpassword.setError("Konfirmasi Password Harus Mengandung Huruf Kecil");
					Toast.makeText(getApplicationContext(),"Konfirmasi Password Harus Mengandung Huruf Kecil", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!txtconfirmpassowrd.matches(".*[A-Z].*")) {
					confirmpassword.setError("Konfirmasi Password Harus Mengandung Huruf Besar");
					Toast.makeText(getApplicationContext(),"Konfirmasi Password Harus Mengandung Huruf Besar", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!txtconfirmpassowrd.matches(".*[a-zA-Z].*")) {
					confirmpassword.setError("Konfirmasi Password Harus Mengandung Huruf");
					Toast.makeText(getApplicationContext(),"Konfirmasi Password Harus Mengandung Huruf", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!txtconfirmpassowrd.matches( ".{8,}")) {
					confirmpassword.setError("Minimal Konfirmasi Password 8 Karakter");
					Toast.makeText(getApplicationContext(),"Minimal Konfirmasi Password 8 Karakter", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				if (!txtpass.equals(txtconfirmpassowrd)) {
					Toast.makeText(getApplicationContext(),"Password dengan Konfirmasi Password Tidak Sama", Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}


				fa.createUserWithEmailAndPassword(txtemail,txtpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()){
							Toast.makeText(getApplicationContext(), "Berhasil Register" , Toast.LENGTH_SHORT).show();
							UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
									.setDisplayName(txtnama)
									.build();
							fd.getReference("Users").child(FirebaseAuth.getInstance()
									.getCurrentUser().getUid()).child("nama").setValue(txtnama)
									.addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									if (task.isSuccessful()){
										Toast.makeText(getApplicationContext(),
												"Berhasil Menambahkan User Ke Database" , Toast.LENGTH_SHORT).show();
										Intent intent = new Intent(Register.this.getApplicationContext(), Login.class);
										startActivity(intent);
										pb.setVisibility(View.GONE);
										finish();
									}
								}
							});
							if (fa.getCurrentUser() != null) {
								FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
								Objects.requireNonNull(user).updateProfile(profileUpdates)
										.addOnCompleteListener(new OnCompleteListener<Void>() {
											@Override
											public void onComplete(@NonNull Task<Void> task) {
												task.isSuccessful();
											}
										});
								fa.getCurrentUser()
										.sendEmailVerification()
										.addOnCompleteListener(new OnCompleteListener<Void>() {
											@Override
											public void onComplete(@NonNull Task<Void> task) {
												if (task.isSuccessful()) {
													Toast.makeText(getApplicationContext(), "Konfirmasi Email di kirim ke : " + fa.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
												} else {
													Toast.makeText(getApplicationContext(), "Gagal mengirim Konfirmasi Email", Toast.LENGTH_SHORT).show();
												}
											}
										});
							}
						}else {
							Toast.makeText(getApplicationContext(), "Gagal Register"
									+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
