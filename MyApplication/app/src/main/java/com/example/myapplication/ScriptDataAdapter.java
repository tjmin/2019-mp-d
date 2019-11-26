//작성자 : 박재효
package com.example.myapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ScriptListener;

import java.util.List;
import java.util.Random;

public class ScriptDataAdapter extends RecyclerView.Adapter<ScriptDataAdapter.ScriptViewHolder>{

    public List<Script> mList;
    private ScriptListener listener;

    private String id;

    public class ScriptViewHolder extends RecyclerView.ViewHolder {

        protected TextView userId;
        protected TextView title;
        protected Button scriptBtn;

        public ScriptViewHolder(View view) {
            super(view);
            this.userId = (TextView) view.findViewById(R.id.userId);
            this.title = (TextView) view.findViewById(R.id.title);
            this.scriptBtn = (Button) view.findViewById(R.id.scriptBtn);
        }
    }

    public ScriptDataAdapter(List<Script> list) {
        this.mList = list;
    }

    @Override
    public ScriptViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.script_row, viewGroup, false);

        ScriptViewHolder viewHolder = new ScriptViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScriptViewHolder viewholder, int position) {

        final Script script = getScript(position);

        if (script != null){

        viewholder.userId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        viewholder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        viewholder.userId.setGravity(Gravity.LEFT);
        viewholder.title.setGravity(Gravity.LEFT);

        viewholder.title.setTypeface(Typeface.DEFAULT_BOLD);

        viewholder.userId.setText(mList.get(position).getUserId());
        viewholder.title.setText(mList.get(position).getScriptTitle());


        String mid = script.getUserId();
        Log.d("TAG",mid+" "+id);
        if (!mid.equals(id))
            viewholder.title.setTextColor(Color.parseColor("#5e5e5e"));
        else {
            viewholder.title.setTextColor(Color.parseColor("#564675"));

        }


        viewholder.scriptBtn.setOnClickListener(new View.OnClickListener()
        {
         @Override
         public void onClick(View view){
             listener.onScriptClick(script);
         }
        });
        }
    }


    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

    //  작성자: 이원구
    public void setListener(ScriptListener listener) {
        this.listener = listener;
    }

    //  작성자: 이원구
    public Script getScript(int position) {
        return mList.get(position);
    }

    public void setId(String id) {
        this.id = id;
    }
}
