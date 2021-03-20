package pp.facerecognizer.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pp.facerecognizer.Lecturer.LecturerModules;
import pp.facerecognizer.R;
import pp.facerecognizer.Students.StudentModules;

public class LecturerListAdapter extends RecyclerView.Adapter<LecturerListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<LecturerModules> lecturerList = new ArrayList<>();

    public LecturerListAdapter(Context context, ArrayList<LecturerModules> lecturerList) {
        this.context = context;
        this.lecturerList = lecturerList;
    }
    @NonNull
    @Override
    public LecturerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_list, parent, false);
        LecturerListAdapter.ViewHolder holder = new LecturerListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LecturerListAdapter.ViewHolder holder, int position) {
        holder.LecturerName.setText(lecturerList.get(position).getTeacherName());
        holder.LecturerEmail.setText(lecturerList.get(position).getTeacherEmail());
        holder.LecturerId.setText(lecturerList.get(position).getTeacherID());

    }

    @Override
    public int getItemCount() {
        return lecturerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView LecturerEmail, LecturerName, LecturerId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            LecturerEmail = itemView.findViewById(R.id.setEmail);
            LecturerName = itemView.findViewById(R.id.NameOfLecturer);
            LecturerId = itemView.findViewById(R.id.lecturerid);
        }
    }
}
