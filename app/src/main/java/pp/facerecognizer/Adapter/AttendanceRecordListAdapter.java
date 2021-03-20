package pp.facerecognizer.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import pp.facerecognizer.AssignedCourses.AssignedCourse;
import pp.facerecognizer.AttendancePackage.AttendanceRecord;
import pp.facerecognizer.R;

public class AttendanceRecordListAdapter extends RecyclerView.Adapter<AttendanceRecordListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AttendanceRecord> attendanceRecords = new ArrayList<>();

    public AttendanceRecordListAdapter(Context context, ArrayList<AttendanceRecord> attendanceRecords) {
        this.context = context;
        this.attendanceRecords = attendanceRecords;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_attendance_records, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int absent = 0;
        int attended = 0;
        int studentlist = 0;
        holder.courseID.setText(attendanceRecords.get(position).getCourseID());
        holder.intakeCode.setText(attendanceRecords.get(position).getIntake());
        holder.LecturerID.setText(attendanceRecords.get(position).getLecturerID());
        holder.ClassNo.setText(Integer.toString(attendanceRecords.get(position).getClassNo())+"/"
                +attendanceRecords.get(position).getNumberOfClasses());
        holder.StudentID.setText(attendanceRecords.get(position).getStudentIDs().toString());
        holder.attendedStudentIDs.setText(attendanceRecords.get(position).getStudentAttended().toString());

        studentlist = attendanceRecords.get(position).getStudentIDs().size();
        attended = attendanceRecords.get(position).getStudentAttended().size();
        absent = studentlist - attended;
        ArrayList<PieEntry> attendance = new ArrayList<>();
        attendance.add(new PieEntry(studentlist, "Student List"));
        attendance.add(new PieEntry(absent,"Absent Student"));
        attendance.add(new PieEntry(attended,"Attended Student"));
        PieDataSet pieDataSet = new PieDataSet(attendance, "CourseID "+attendanceRecords.get(position).getCourseID());
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);

        PieData pieData = new PieData(pieDataSet);
        holder.pieChart.setData(pieData);
        holder.pieChart.setDrawEntryLabels(false);
        holder.pieChart.getDescription().setEnabled(false);
        holder.pieChart.setCenterText("Attendance");
        holder.pieChart.setCenterTextSize(10f);
        holder.pieChart.animate();

    }

    @Override
    public int getItemCount() {
        return attendanceRecords.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView courseID, LecturerID, intakeCode, ClassNo, StudentID, attendedStudentIDs;
        PieChart pieChart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseID = itemView.findViewById(R.id.seCourseID);
            intakeCode = itemView.findViewById(R.id.seIntakeCode);
            LecturerID = itemView.findViewById(R.id.seLecturerID);
            ClassNo = itemView.findViewById(R.id.seClassNo);
            StudentID = itemView.findViewById(R.id.seStudentIDs);
            attendedStudentIDs = itemView.findViewById(R.id.seAttendedStudentIDs);
            pieChart = itemView.findViewById(R.id.pieChartLecturer);
        }
    }
}
