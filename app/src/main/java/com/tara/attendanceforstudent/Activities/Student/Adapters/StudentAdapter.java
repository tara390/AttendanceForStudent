package com.tara.attendanceforstudent.Activities.Student.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tara.attendanceforstudent.Models.StudentModel;
import com.tara.attendanceforstudent.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    List<StudentModel> studentmodel;
    Context context;
    FirebaseFirestore dbstudent;

    public StudentAdapter(List<StudentModel> studentmodel, Context applicationContext, FirebaseFirestore dbstudent) {

        this.context = applicationContext;
        this.dbstudent = dbstudent;
        this.studentmodel = studentmodel;
    }

    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_student_recyclerview, parent, false);
        return new StudentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder holder, int position) {
        final StudentModel studentModel = studentmodel.get(position);

        holder.tvfullname.setText("Full Name :" + studentModel.getFirstname() + " " + studentModel.getLastname());
        holder.tvmobileno.setText("Mobile No :" + studentModel.getMobile());
        holder.tvparentmobileno.setText("Parent MobileNo :" + studentModel.getParentmobile());
        holder.tvdept.setText("Department :" + studentModel.getDept());
        holder.tvsem.setText("Sem :" + studentModel.getSem());
        Picasso.get().load(studentModel.getImage()).placeholder(R.drawable.loading).into(holder.ivstudentround);


    }

    @Override
    public int getItemCount() {
        return studentmodel.size();
    }

    public void searchfilter(List<StudentModel> filterdNames) {
        studentmodel = filterdNames;
        notifyDataSetChanged();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView ivstudentround;
        TextView tvfullname, tvmobileno, tvparentmobileno, tvdept, tvsem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            ivstudentround = itemView.findViewById(R.id.ivcamera);
            tvfullname = itemView.findViewById(R.id.tvFullName);
            tvmobileno = itemView.findViewById(R.id.tvmobile);
            tvparentmobileno = itemView.findViewById(R.id.tvparent);
            tvdept = itemView.findViewById(R.id.tvDepartment);
            tvsem = itemView.findViewById(R.id.tvSem);


        }
    }
}
