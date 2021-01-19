package com.example.madcamp_week3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaylistAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<String> playlistArrayString = new ArrayList<String>();

    public PlaylistAdapter (Context context, PlayList playlist) {
        mContext = context;
        for (int i = 0; i < playlist.size(); i++) {
            Song song = playlist.getSong(i);
            String singer = song.getSinger();
            String title = song.getTitle();
            String songString = singer + " - " + title;
            playlistArrayString.add(songString);
        }
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return playlistArrayString.size();
    }

    @Override
    public String getItem(int position) {
        return playlistArrayString.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View listview = mLayoutInflater.inflate(R.layout.listview_custom, null);

        TextView lv_title = (TextView) listview.findViewById(R.id.lv_title);
        //TextView lv_singer = (TextView) listview.findViewById(R.id.lv_singer);

        lv_title.setText(playlistArrayString.get(position));
        //lv_singer.setText(playlistArrayString.get(position).getSong(position).getSinger());

        return listview;
    }
}
