package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding // 뷰바인딩을 위한 바인딩 객체 선언
    private lateinit var dbHelper: DBHelper // sqlite를 사용하기 위한 객체 선언
    private lateinit var auth: FirebaseAuth // Firebase 인증 객체 선언

    override fun onCreate(savedInstanceState: Bundle?) { // 최초 실행시 수행
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setContentView(view) // 액티비티의 레이아웃으로 설정

        dbHelper = DBHelper(this) // sqlite 사용하기



        binding.btnA.setOnClickListener { // 리스트 뷰로 이동 버튼
            Toast.makeText(this@MainActivity, "리스트 뷰로 이동합니다.", Toast.LENGTH_SHORT).show() // 토스트 메시지 띄우기
            val intent = Intent(this, SecondAtivity::class.java) // 다음 화면으로 이동하기 위한 인텐트 객체
            intent.putExtra("msg", binding.tvResult.text.toString()) // 메시지 전달
            startActivity(intent)
            // finish() // 자기 자신 엑티비티 파괴
        }

        binding.btnBoard.setOnClickListener { // 게시판으로 이동 버튼
            Toast.makeText(this@MainActivity, "게시판으로 이동합니다.", Toast.LENGTH_SHORT).show() // 토스트 메시지 띄우기
            val intent = Intent(this, BoardActivity::class.java) // 다음 화면으로 이동하기 위한 인텐트 객체
            intent.putExtra("msg", binding.tvResult.text.toString()) // 메시지 전달
            startActivity(intent)
            // finish() // 자기 자신 엑티비티 파괴
        }

        binding.btnLogin.setOnClickListener { // 회원가입 버튼 클릭
            Toast.makeText(this@MainActivity, "로그인 페이지로 이동합니다.", Toast.LENGTH_SHORT).show() // 토스트 메시지 띄우기
            val intent = Intent(this, LoginActivity::class.java) // 다음 화면으로 이동하기 위한 인텐트 객체
            intent.putExtra("msg", binding.tvResult.text.toString()) // 메시지 전달
            startActivity(intent)
            // finish() // 자기 자신 엑티비티 파괴
        }

        auth = Firebase.auth // Firebase 인증 객체 초기화


        // 사용자가 로그인되어 있는지 확인
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // 사용자가 로그인되어 있다면 로그인 성공 메시지를 띄우고 사용자의 이름을 표시
            val userName = currentUser.displayName
            val userId = currentUser.uid



            var message = " $userName 님, 로그인 성공"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()



            // 사용자의 이름을 화면에 표시
            message = "$userName 님, "
            binding.tvUserName.text = message
            binding.btnLogin.text = "로그아웃"
            binding.btnLogin.setOnClickListener {
                Firebase.auth.signOut() // 로그아웃
                Toast.makeText(this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show()

                // 로그아웃 후 현재 액티비티를 재시작하여 로그인 화면으로 이동
                val intent = intent
                finish()
                startActivity(intent)
            }
        } else {
            // 사용자가 로그인되어 있지 않다면 로그인 페이지로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}
