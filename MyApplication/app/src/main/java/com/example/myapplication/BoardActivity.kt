package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityBoardBinding


class BoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dbHelper = DBHelper(this) // DBHelper 초기화

        binding.btnBack.setOnClickListener {//뒤로가기 버튼
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val memoList = dbHelper.selectMemos()
        val memoText = memoList.joinToString("\n")
        binding.tvGetmsg.text = memoText

        binding.btnBoardInsert.setOnClickListener {//게시글 작성 버튼
            val inputText = binding.etInsert.text.toString()
            if (inputText.isNotEmpty()) {
                // DB에 값 추가
                dbHelper.insertMemo(inputText)
                // 현재까지의 게시글 불러와서 표시 (누적해서 표시)
                val updatedMemoList = dbHelper.selectMemos()
                val updatedMemoText = updatedMemoList.joinToString("\n")
                binding.tvGetmsg.text = updatedMemoText
            }

            binding.etInsert.text.clear() //etInsert 내용 지우기
        }

        binding.btnBoardDelete.setOnClickListener {
            // DB 값 삭제
            dbHelper.deleteMemo()
            // 텍스트 뷰 초기화
            binding.tvGetmsg.text = ""
        }




    }
}