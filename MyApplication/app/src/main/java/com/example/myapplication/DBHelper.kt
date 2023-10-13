package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "MyDatabase" // 데이터베이스 이름
        const val DATABASE_VERSION = 1 // 데이터베이스 버전
        const val TABLE_NAME = "MyTable" // 테이블 이름
        const val COLUMN_ID = "_id" // ID 컬럼 이름
        const val COLUMN_TEXT = "text_column" // 텍스트 컬럼 이름
    }

    /**
     * 데이터베이스에서 메모 텍스트를 선택합니다.
     * @return 데이터베이스에서 가져온 메모 텍스트 또는 메모가 없을 경우 빈 문자열을 반환합니다.
     */

    override fun onCreate(db: SQLiteDatabase?) {
        // 데이터베이스 테이블을 생성합니다. 테이블이 이미 존재할 경우 실행되지 않습니다.
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TEXT TEXT
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    fun insertMemo(text: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TEXT, text)

        // insert() 메서드를 사용하여 값을 DB에 추가합니다.
        val newRowId = db.insert(TABLE_NAME, null, values)

        // 새로 추가된 행의 ID가 -1이 아니면 추가 성공으로 간주합니다.
        return newRowId != -1L
    }

    fun deleteMemo(): Boolean {
        val db = this.writableDatabase

        // delete() 메서드를 사용하여 모든 행을 삭제합니다.
        val deletedRows = db.delete(TABLE_NAME, null, null)

        // 삭제된 행의 수가 1 이상이면 삭제 성공으로 간주합니다.
        return deletedRows > 0
    }


    fun selectMemos(): List<String> {
        val db = this.readableDatabase
        val memoList = mutableListOf<String>()

        val cursor = db.rawQuery("SELECT $COLUMN_TEXT FROM $TABLE_NAME", null)
        cursor.use {
            while (it.moveToNext()) {
                val columnIndex = it.getColumnIndex(COLUMN_TEXT)
                if (columnIndex != -1) {
                    memoList.add(it.getString(columnIndex))
                }
            }
        }

        cursor.close()
        return memoList
    }

    fun selectMemo(): String {
        val db = this.readableDatabase
        var memoText = ""

        val cursor = db.rawQuery("SELECT $COLUMN_TEXT FROM $TABLE_NAME", null)
        cursor.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(COLUMN_TEXT)
                if (columnIndex != -1) {
                    memoText = it.getString(columnIndex)
                }
            }
        }

        cursor.close()
        return memoText
    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // 데이터베이스 스키마를 업그레이드하는 로직을 추가할 수 있습니다.
    }


}