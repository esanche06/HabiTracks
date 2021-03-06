package jt.autismtracks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by julian on 4/15/16.
 */
public class TaskAdapter extends ArrayAdapter<Task>  {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Task> obj = null;

    public TaskAdapter(Context mContext, int layoutResourceId, ArrayList<Task> data) {
        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.obj = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        final Task objectItem = obj.get(position);
        final View v = convertView;

        TextView TitleItem = (TextView) convertView.findViewById(R.id.TitleName);
        TitleItem.setText(objectItem.getTitle());

        TextView DateItem = (TextView) convertView.findViewById(R.id.Date);
        DateItem.setText(objectItem.getDate());

        ImageView ImageItem = (ImageView) convertView.findViewById(R.id.icon_task);
        ImageItem.setImageResource(mContext.getResources().getIdentifier(objectItem.getSrc(),null, "jt.autismtracks"));

        final CheckBox CheckItem = (CheckBox) convertView.findViewById(R.id.checkBox);
        CheckItem.setChecked(objectItem.getDone());
        CheckItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objectItem.setDone(CheckItem.isChecked());
                TaskDatabase td = new TaskDatabase(mContext);
                td.update_checked(objectItem.getRowId(),objectItem.getDone());
                if (CheckItem.isChecked()) {
                    td.updatePoints(objectItem.getPoints());
                } else {
                    td.updatePoints(-1*objectItem.getPoints());
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        remove(objectItem);
                        notifyDataSetChanged();
                    }
                }, 600);
            }
        });
        return convertView;
    }
}
