package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.VH>{

    List<BeanS> bsL = new ArrayList<>();

    public void update(List<BeanS> beanSList) {
        this.bsL = beanSList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentAdapter.VH onCreateViewHolder( ViewGroup parent, int viewType) {
        return new StudentAdapter.VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.VH holder, int position) {
        BeanS beanS = bsL.get(position);

        holder.id.setText(MessageFormat.format("{0}{1}", holder.itemView.getContext().getResources().getString(R.string.id), beanS.getId()));
        holder.name.setText(MessageFormat.format("{0}{1}", holder.itemView.getContext().getResources().getString(R.string.name), beanS.getName()));

        holder.delete.setOnClickListener(view -> {
            DatabaseUtil db;
            db = MyApplication.databaseUtil;
            Thread thread = new Thread(() -> {
                db.msd().delete(beanS);
            });
            thread.start();
            try {
                thread.join();
                bsL.remove(beanS);
                notifyDataSetChanged();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bsL.size();
    }

    class VH extends RecyclerView.ViewHolder {

        TextView id, name;
        Button delete;

        public VH(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.item_student_id);
            name = itemView.findViewById(R.id.item_student_name);
            delete = itemView.findViewById(R.id.item_student_delete);
        }

    }

}
