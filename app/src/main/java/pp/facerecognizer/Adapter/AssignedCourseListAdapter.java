package pp.facerecognizer.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pp.facerecognizer.AssignedCourses.AssignedCourse;

import pp.facerecognizer.R;

public class AssignedCourseListAdapter   extends RecyclerView.Adapter<AssignedCourseListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AssignedCourse> moduleslist = new ArrayList<>();

    public AssignedCourseListAdapter(Context context, ArrayList<AssignedCourse> moduleslist) {
        this.context = context;
        this.moduleslist = moduleslist;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assigned_course_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.courseID.setText(moduleslist.get(position).getCourseID());
        holder.intakeCode.setText(moduleslist.get(position).getIntake());
        holder.LecturerID.setText(moduleslist.get(position).getLecturerID());
        holder.ClassNo.setText(moduleslist.get(position).getClassNo());
        holder.StudentID.setText(moduleslist.get(position).getStudentIDs().toString());

    }

    @Override
    public int getItemCount() {
        return moduleslist.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView courseID, LecturerID, intakeCode, ClassNo, StudentID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseID = itemView.findViewById(R.id.CourseIDlist);
            intakeCode = itemView.findViewById(R.id.IntakeCodelist);
            LecturerID = itemView.findViewById(R.id.LecturerIDlist);
            ClassNo = itemView.findViewById(R.id.ClasssNolist);
            StudentID = itemView.findViewById(R.id.StudentIDlist);
        }
    }
}
