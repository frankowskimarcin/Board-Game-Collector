package com.example.boardgamecollector

import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class RankHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank_history)

        val textViewTitle: TextView = findViewById(R.id.textViewRankTitle)
        val textViewRanks: TextView = findViewById(R.id.textViewRanks)
        val title: String? = intent.getStringExtra("gameTitle")

        textViewTitle.text = title
        textViewTitle.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL

        val dbHandler = MyDBHandler(this, null, null, 1)
        textViewRanks.text = dbHandler.getRanks(title.toString())
    }
}