package com.example.madcamp_week3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class ViewPagerAdapter extends PagerAdapter {

    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    Context mContext;
    GridActivity mgridactivity;
    int layout;
    //int img[];
    ArrayList<String> titles;
    LayoutInflater inf;
    String userid;

    public ViewPagerAdapter(Context context, int layout, ArrayList<String> titles, String userid) {
        this.mContext = context;
        this.layout = layout;
        //this.img = img;
        this.titles = titles;
        this.userid = userid;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null ;

        if (mContext != null) {
            // LayoutInflater를 통해 "/res/layout/page.xml"을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row, container, false);

            ImageView imageView1 = (ImageView)view.findViewById(R.id.imageView1);
            TextView tv_grid = (TextView)view.findViewById(R.id.tv_grid);

            titles = PlayListList.getTitleList(userid);

            imageView1.setImageResource(R.drawable.cassette_drawing);
            tv_grid.setText(titles.get(position));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, PlayActivity.class);
                    intent.putExtra("position", position);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });

            view.setOnLongClickListener(new AdapterView.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.d("TAG", "on long click");

                    editDialog(position);
                    return true;
                }
            });
        }

        // 뷰페이저에 추가.
        container.addView(view) ;

        return view ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return PlayListList.getSize(userid);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }

    public void editDialog(int position) {
        Log.d("TAG", "show dialog executing");
        System.out.println("show dialog executing");

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View dialogeditView = inflater.inflate(R.layout.playlistedit_dialog, null);
        builder.setView(dialogeditView);

        EditText et_pl_ed = dialogeditView.findViewById(R.id.et_pl_ed);
        EditText et_dc_ed = dialogeditView.findViewById(R.id.et_dc_ed);

        ImageButton pled_edit_button = dialogeditView.findViewById(R.id.pled_edit_button);
        ImageButton pled_delete_button = dialogeditView.findViewById(R.id.pled_delete_button);
        ImageButton pled_cancel_button = dialogeditView.findViewById(R.id.pled_cancel_button);

        et_pl_ed.setText(PlayListList.getPlaylist(position, userid).getTitle());
        et_dc_ed.setText(PlayListList.getPlaylist(position, userid).getDescription());

        AlertDialog dialog = builder.create();
        dialog.show();

        System.out.println("here");

        pled_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pl_name = et_pl_ed.getText().toString();
                String pl_des = et_dc_ed.getText().toString();

                if (pl_name.equals("") || pl_des.equals("")) {
                    Toast error_m = Toast.makeText(mContext,  "작성을 완료하세요.", LENGTH_SHORT);
                    error_m.show();
                } else {
                    //수정하는 방법 바꾸기
                    /*PlayListList.deletePlaylist(position);
                    PlayListList.addPlaylist(new PlayList(pl_name, pl_des));*/
                    PlayListList.editPlaylist(position, userid, pl_name, pl_des);
                    dialog.dismiss();
                    Intent intent = new Intent(mContext, GridActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });

        pled_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayListList.deletePlaylist(position, userid);
                dialog.dismiss();
                Intent intent = new Intent(mContext, GridActivity.class);
                mContext.startActivity(intent);
            }
        });

        pled_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
