package com.tara.attendanceforstudent.Activities.Subject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tara.attendanceforstudent.Activities.Student.Adapters.StudentAdapter;
import com.tara.attendanceforstudent.Models.StudentModel;
import com.tara.attendanceforstudent.Models.SubjectModel;
import com.tara.attendanceforstudent.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    List<SubjectModel> studentmodel;
    Context context;
    FirebaseFirestore dbforsubject;

    public SubjectAdapter(List<SubjectModel> subjectModels, Context applicationContext, FirebaseFirestore dbforsubject) {

        this.context = applicationContext;
        this.studentmodel = subjectModels;
        this.dbforsubject = dbforsubject;
    }

    @NonNull
    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_subject_list, parent, false);
        return new SubjectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.ViewHolder holder, int position) {
        final SubjectModel studentModel = studentmodel.get(position);

        holder.tvsubjectname.setText("Subject Name :" +" " +studentModel.getSubjectname()  );
        holder.tvdep.setText("Department :" + studentModel.getDepartment());
        holder.tvsem.setText("Sem :" + studentModel.getSem());


    }

    @Override
    public int getItemCount() {
        return studentmodel.size();
    }

    public void filterNames(List<SubjectModel> subjectModels) {
        studentmodel=subjectModels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvsubjectname, tvdep, tvsem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvsubjectname = itemView.findViewById(R.id.tvSubjectname);
            tvdep = itemView.findViewById(R.id.tvdep);
            tvsem = itemView.findViewById(R.id.tvse);


        }
    }
}
