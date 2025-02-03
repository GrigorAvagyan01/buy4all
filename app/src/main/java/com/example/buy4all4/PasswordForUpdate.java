//package com.example.buy4all4;
//
//
//import com.example.buy4all4.R;
//import com.example.buy4all4.databinding.ActivityPasswordForUpdateBinding;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.auth.FirebaseAuth;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//
//public class PasswordForUpdate extends AppCompatActivity {
//
//    private ActivityPasswordForUpdateBinding binding;
//    private FirebaseFirestore db;
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityPasswordForUpdateBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        db = FirebaseFirestore.getInstance();
//        mAuth = FirebaseAuth.getInstance();
//
//        binding.passchen.setOnClickListener(view -> {
//            String enteredPassword = binding.passwordInput.getText().toString().trim();
//
//            if (TextUtils.isEmpty(enteredPassword)) {
//                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            String userId = mAuth.getCurrentUser().getUid();
//            db.collection("users").document(userId)
//                    .get()
//                    .addOnSuccessListener(documentSnapshot -> {
//                        if (documentSnapshot.exists()) {
//                            String storedPassword = documentSnapshot.getString("password");
//
//                            if (enteredPassword.equals(storedPassword)) {
////                                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
////                                navController.navigate(R.id.action_checkPasswordFragment_to_Update);
//                            } else {
//                                Toast.makeText(this, "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(this, "Error fetching password. Please try again.", Toast.LENGTH_SHORT).show();
//                    });
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        binding = null;
//    }
//}
