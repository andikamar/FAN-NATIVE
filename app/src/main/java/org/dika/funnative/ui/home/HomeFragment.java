package org.dika.funnative.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.dika.funnative.Login;

import org.dika.funnative.R;

import java.util.Objects;


public class HomeFragment extends Fragment {

	AppCompatButton keluar;
	FirebaseAuth auth;
	FirebaseDatabase firebaseDatabase;
	AppCompatTextView textViewemail,textViewnama, textViewstatus;


	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {

		auth= FirebaseAuth.getInstance();
		firebaseDatabase = FirebaseDatabase.getInstance();

		View root = inflater.inflate(R.layout.fragment_home, container, false);
		textViewemail = root.findViewById(R.id.text_email);
		textViewnama = root.findViewById(R.id.text_nama);
		textViewstatus = root.findViewById(R.id.text_status);
		SharedPreferences sh = getActivity().getSharedPreferences("LOGIN", MODE_PRIVATE);
		final String token = sh.getString("token", "");
		final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
		if (user != null) {
			user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
				@Override
				public void onSuccess(GetTokenResult result) {
					if (Objects.requireNonNull(result.getToken()).equals(token)){
						DatabaseReference dr = firebaseDatabase.getReference("Users").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("nama");
						dr.addValueEventListener(new ValueEventListener() {
							@Override
							public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
								String name = dataSnapshot.getValue(String.class);
								textViewemail.setText(new StringBuilder("Email : ").append(user.getEmail()));
								textViewnama.setText(new StringBuilder("Nama : ").append(name));
								textViewstatus.setText(new StringBuilder("Status Konfirmasi Email : ").append(user.isEmailVerified()));
							}
							@Override
							public void onCancelled(@NonNull DatabaseError databaseError) {

							}
						});
					}

				}
			});
		}

		keluar = root.findViewById(R.id.btn_keluar);
		keluar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				auth.signOut();
				Intent intent = new Intent(HomeFragment.this.getActivity(), Login.class);
				startActivity(intent);
				Toast.makeText(getContext(),"Anda Telah Sign Out", Toast.LENGTH_LONG).show();
			}
		});
		return root;

	}
}