package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.myapplication.databinding.ActivityJoinBinding // 뷰 바인딩 클래스 임포트
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database



class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding // 뷰 바인딩 변수 선언
    private lateinit var auth: FirebaseAuth //파이어베이스 연동
    private lateinit var spinnerGrade: Spinner //선택박스 창
    private val passwordPattern: Regex = Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*(),.?\":{}|<>]).{10,}$") //비밀번호 유효성 검사

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater) // 뷰 바인딩 초기화
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth//파이어베이스 사용하기

        // val database = FirebaseDatabase.getInstance()//Unresolved reference: FirebaseDatabase
        val database = Firebase.database
        val usersRef = database.getReference("users")
        
        //학년 목록 정리
        val grades = listOf("1학년", "2학년", "3학년", "4학년", "졸업생", "대학원생")
        // 스피너 초기화
        spinnerGrade = findViewById(R.id.spinnerGrade)
        // 스피너 어댑터 설정
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, grades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrade.adapter = adapter
        
        // 가입하기 버튼 클릭 이벤트 처리
        val joinButton = findViewById<Button>(R.id.button)

        joinButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.et_email).text.toString()
            val password = findViewById<EditText>(R.id.et_pw).text.toString()
            val passwordRe = findViewById<EditText>(R.id.et_pw_re).text.toString()
            val name = findViewById<EditText>(R.id.et_name).text.toString()
            val selectedGrade = spinnerGrade.selectedItem.toString()

            

            // 입력값 유효성 검사
            if (email.isEmpty() || password.isEmpty() || passwordRe.isEmpty() || name.isEmpty() || grades.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 비밀번호 재입력 확인
            if (password != passwordRe) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 비밀번호 유효성 검사
            if (!passwordPattern.matches(password)) {
                Toast.makeText(this, "비밀번호는 10자 이상이어야 하고, 특수 기호가 하나 이상 포함되어야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase 회원가입 처리
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 회원가입 성공
                        val user = auth.currentUser

                        // 사용자의 이름 설정 (예: 사용자가 입력한 이름을 사용)
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name) // 사용자의 이름을 설정
                            .build()

                        // 사용자의 학년 정보 저장
                        val selectedGrade = spinnerGrade.selectedItem.toString()

                        user?.let {
                            val userId = user.uid
                            usersRef.child(userId).child("grade").setValue(selectedGrade)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // 사용자의 학년 정보 저장 성공
                                    } else {
                                        // 사용자의 학년 정보 저장 실패
                                    }
                                }
                        }

                        
                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { profileUpdateTask ->
                                if (profileUpdateTask.isSuccessful) {
                                    // 회원가입 성공 후 다음 화면으로 이동하거나 필요한 처리를 진행하세요.
                                    // 로그인 처리
                                    auth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(this) { task ->
                                            if (task.isSuccessful) {
                                                // 로그인 성공
                                                val user = auth.currentUser
                                                val userName = user?.displayName // 사용자 이름 가져오기

                                                //메인화면으로 이동
                                                val intent = Intent(this, MainActivity::class.java)
                                                startActivity(intent)
                                            } else {
                                                // 로그인 실패
                                                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                } else {
                                    // 사용자의 이름 설정 실패
                                    Toast.makeText(this, "사용자 이름 설정 실패", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        // 회원가입 실패
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }


        binding.btnBack.setOnClickListener {//뒤로가기 버튼
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}