package com.example.boardgamecollector;

import android.annotation.SuppressLint;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BoardGameAdapter extends ArrayAdapter<BoardGame> {

    private final Context mContext;
    private List<BoardGame> gamesList = new ArrayList<>();

    public BoardGameAdapter(Context context, ArrayList<BoardGame> list) {
        super(context, 0 , list);
        mContext = context;
        gamesList = list;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_game, parent,false);

        BoardGame currentGame = gamesList.get(position);

        TextView rank = (TextView) listItem.findViewById(R.id.textView_rank);
        rank.setText(currentGame.getRanking().toString());

        ImageView image = (ImageView)listItem.findViewById(R.id.imageView);
        image.setImageResource(Integer.parseInt(currentGame.getImage()));
        //Picasso.get().load(gameList[position].img).into(imageView)

        TextView title = (TextView) listItem.findViewById(R.id.textView_title);
        title.setText(currentGame.getTitle());

        TextView description = (TextView) listItem.findViewById(R.id.textView_description);
        description.setText(currentGame.getDescription());

        return listItem;
    }
}
