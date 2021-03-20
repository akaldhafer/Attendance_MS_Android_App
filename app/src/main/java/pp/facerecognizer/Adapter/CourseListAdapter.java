package pp.facerecognizer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pp.facerecognizer.CourseList;
import pp.facerecognizer.Module.Modules;
import pp.facerecognizer.R;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Modules> moduleslist = new ArrayList<>();

    public CourseListAdapter(Context context, ArrayList<Modules> moduleslist) {
        this.context = context;
        this.moduleslist = moduleslist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.CourseId.setText(moduleslist.get(position).getModuleID());
        holder.courseName.setText(moduleslist.get(position).getModuleName());
        holder.LeaderName.setText(moduleslist.get(position).getLeader());
        holder.level.setText(moduleslist.get(position).getLevel());

    }

    @Override
    public int getItemCount() {
        return moduleslist.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView courseName, LeaderName, CourseId, level;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.NameOfCourse2);
            LeaderName = itemView.findViewById(R.id.LeaderOfCourse);
            CourseId = itemView.findViewById(R.id.IDOfCourse);
            level = itemView.findViewById(R.id.Level);
        }
    }
}
