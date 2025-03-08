//package com.example.mywordle;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.mywordle.data.repository.PlayerRepository;
//
//
//public class EntranceActivity extends Fragment {
//    private EditText editTextLogin, editTextPassword;
//    private TextView textViewMessage;
//    private PlayerRepository playerRepository;
//
//    public EntranceActivity() {
//
//
//    }
//
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_entrance_activity);
//
//        editTextLogin = findViewById(R.id.editTextLogin);
//        editTextPassword = findViewById(R.id.editTextPassword);
//        Button btnRegister = findViewById(R.id.btnRegister);
//        textViewMessage = findViewById(R.id.textViewMessage);
//
//        playerRepository = PlayerRepository.getInstance(this);
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                registerUser();
//            }
//        });
//
//    }
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_entrance_activity, container, false);
//
//    }
//}