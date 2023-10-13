package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySecondAtivityBinding // 뷰 바인딩 임포트 문 추가

class SecondAtivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondAtivityBinding // 뷰 바인딩 객체 선언

    val UserList = arrayListOf<User>(
        User(R.drawable.android_black, name = "홍드로이드", age = "28", greet = "안녕하세요"),
        User(R.drawable.android_black, name = "자바", age = "21", greet = "반갑습니다"),
        User(R.drawable.android_black, name = "ㅇㅇ", age = "22", greet = "하이"),
        User(R.drawable.faker, name = "대상혁", age = "24", greet = "ㅎㅎㅎ"),
        User(R.drawable.android_black, name = "김도일", age = "22", greet = "ㅋㅋ")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondAtivityBinding.inflate(layoutInflater) // 뷰 바인딩 초기화 및 인플레이션
        val view = binding.root // 뷰 바인딩 객체로부터 루트 뷰를 가져와서 저장
        setContentView(view) // 액티비티의 레이아웃으로 설정

        // 이제 binding 객체를 사용하여 뷰에 접근할 수 있습니다.
        binding.btnBack.setOnClickListener{ //뒤로가기 버튼
            val intent = Intent(this, MainActivity::class.java) //다음 화면으로 이동하기 위한 인텐트 객체
            startActivity(intent)
            // finish() //자기 자신 엑티비티 파괴
        }

        //리스트 뷰 직접 작성
        //val itam = arrayOf("사과", "배", "딸기", "홍드로이드")
        //binding.listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itam)

        val Adapter = UserAdapter(this, UserList)
        binding.listView.adapter = Adapter

        binding.listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectItem = parent.getItemAtPosition(position) as User
            Toast.makeText(this, selectItem.name, Toast.LENGTH_SHORT).show()

        }

    }
}