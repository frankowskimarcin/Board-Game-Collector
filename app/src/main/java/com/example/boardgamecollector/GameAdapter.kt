package com.example.boardgamecollector

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class GameAdapter(context: Context, private val gameList: MutableList<BoardGame>):
        ArrayAdapter<BoardGame>(context, R.layout.list_game, gameList) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItem = convertView
        if (listItem == null) listItem = LayoutInflater.from(context).inflate(R.layout.list_game, parent, false)

        val currentGame: BoardGame = gameList[position]

        val rankView: TextView = listItem!!.findViewById<View>(R.id.textView_rank) as TextView
        rankView.text = currentGame.ranking.toString()

        val imageView = listItem.findViewById<View>(R.id.imageView) as ImageView
        Picasso.get().load(currentGame.image).resize(120, 150).into(imageView)

        //Picasso.get().load(gameList[position].img).into(imageView)
        val titleView: TextView = listItem.findViewById(R.id.textView_title)
        titleView.text = currentGame.title.toString() + " ("+ currentGame.publicationYear.toString() + ")"

        val descriptionView: TextView = listItem.findViewById(R.id.textView_description)
        descriptionView.text = currentGame.description.toString()

        return listItem
    }
}