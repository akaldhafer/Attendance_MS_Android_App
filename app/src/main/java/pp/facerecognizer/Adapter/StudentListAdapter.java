package pp.facerecognizer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pp.facerecognizer.R;
import pp.facerecognizer.Students.StudentModules;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<StudentModules> studentList = new ArrayList<>();

    public StudentListAdapter(Context context, ArrayList<StudentModules> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.StudentName.setText(studentList.get(position).getStudentName());
        holder.courseName.setText(studentList.get(position).getStudentCourse());
        holder.StudentId.setText(studentList.get(position).getStudentID());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView courseName, StudentName, StudentId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.NameOfCourse);
            StudentName = itemView.findViewById(R.id.NameOfStudent);
            StudentId = itemView.findViewById(R.id.StudenID);
        }
    }
}
