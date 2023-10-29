package com.example.myapplication;

import static java.text.NumberFormat.getPercentInstance;

import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClazzAdapter extends RecyclerView.Adapter<ClazzAdapter.ViewHolder> {

    List<BeanS> sbs;

    List<BeanC> bcl = new ArrayList<>();

    List<BeanS> bsl;

    List<BeanB> bdl = new ArrayList<>();

    public void update(List<BeanC> beanCList) {
        this.bcl = beanCList;
        notifyDataSetChanged();
    }

    public void setStudentList(List<BeanS> beanSList) {
        this.bsl = beanSList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clazz, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BeanC beanC = bcl.get(position);

        holder.n.setText(MessageFormat.format("name：{0}", beanC.getName()));
        holder.p.setText(MessageFormat.format("endTime：{0}", beanC.geteDate()));
        holder.d.setText(MessageFormat.format("startTime：{0}", beanC.getsDate()));

        holder.a.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(holder.itemView.getContext(), (view1, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                DatabaseUtil db;
                db = MyApplication.databaseUtil;
                Thread thread = new Thread(() -> {
                    bdl = db.mbd().getBindList(beanC.getUuid(), selectedDate);
                    bsl = db.msd().getList();
                });

                thread.start();
                try {
                    thread.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                double result = (double) bdl.size() / bsl.size();
                NumberFormat percentFormat = getPercentInstance();
                Toast.makeText(holder.n.getContext(), "attendance：" + percentFormat.format(result), Toast.LENGTH_SHORT).show();

            }, year, month, dayOfMonth);
            datePickerDialog.show();
        });


        holder.de.setOnClickListener(view -> {
            DatabaseUtil db;
            db = MyApplication.databaseUtil;
            Thread thread = new Thread(() -> {
                db.mcd().delete(beanC);
            });
            thread.start();
            try {
                thread.join();
                bcl.remove(beanC);
                notifyDataSetChanged();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        holder.s.setOnClickListener(view -> {
            Calendar C = Calendar.getInstance();
            int year = C.get(Calendar.YEAR);
            int month = C.get(Calendar.MONTH);
            int dayOfMonth = C.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(holder.itemView.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                    sbs = new ArrayList<>();

                    final String[] items = bsl.stream().map(BeanS::getName).collect(Collectors.toList()).toArray(new String[bsl.size()]);
                    final boolean[] checked = {false, false, false, false, false};
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setTitle(holder.itemView.getContext().getString(R.string.student_select));
                    builder.setMultiChoiceItems(items, checked, (dialog, which, isChecked) -> checked[which] = isChecked).setPositiveButton("确定", (dialog, which) -> {
                        for (int i = 0; i < checked.length; i++) {
                            if (checked[i]) {
                                sbs.add(bsl.get(i));
                            }
                        }
                        Thread thread = new Thread(() -> {
                            DatabaseUtil db = MyApplication.databaseUtil;
                            db.mbd().deleteDate(beanC.getUuid(), selectedDate);
                            sbs.forEach(item1 -> {
                                BeanB beanB = new BeanB();
                                beanB.setCid(beanC.getUuid());
                                beanB.setSid(item1.getUuid());
                                beanB.setTime(selectedDate);
                                beanB.setUuid(UUID.randomUUID().toString());
                                db.mbd().insert(beanB);
                            });
                        });

                        thread.start();
                        try {
                            thread.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        notifyDataSetChanged();
                    });
                    builder.create();
                    builder.show();
                }
            }, year, month, dayOfMonth);
            dpd.show();
        });
    }

    @Override
    public int getItemCount() {
        return bcl.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView  p, n,a,d,  s;
        Button de;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            s = itemView.findViewById(R.id.item_clazz_getAttendance);
            d = itemView.findViewById(R.id.item_clazz_date);
            de = itemView.findViewById(R.id.item_clazz_delete);
            p = itemView.findViewById(R.id.item_clazz_position);
            n = itemView.findViewById(R.id.item_clazz_name);
        }
    }

}
