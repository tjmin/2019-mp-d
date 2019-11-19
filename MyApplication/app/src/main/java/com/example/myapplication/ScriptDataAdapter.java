//작성자 : 박재효
package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ScriptDataAdapter extends RecyclerView.Adapter<ScriptDataAdapter.ScriptViewHolder>{
    public List<Script> mList;

    public class ScriptViewHolder extends RecyclerView.ViewHolder {
        protected TextView userId;
        protected TextView title;
        protected Button scriptBtn;


        public ScriptViewHolder(View view) {
            super(view);
            this.userId = (TextView) view.findViewById(R.id.userId);
            this.title = (TextView) view.findViewById(R.id.title);
            this.scriptBtn = (Button) view.findViewById(R.id.scriptBtn);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                }
            });
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

        viewholder.userId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        viewholder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        viewholder.userId.setGravity(Gravity.LEFT);
        viewholder.title.setGravity(Gravity.LEFT);

        viewholder.userId.setText(mList.get(position).getUserId());
        viewholder.title.setText(mList.get(position).getTitle());

        viewholder.scriptBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), EditActivity.class);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
