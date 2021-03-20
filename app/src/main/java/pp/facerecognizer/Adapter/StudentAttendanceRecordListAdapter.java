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

import pp.facerecognizer.AttendancePackage.AttendanceRecord;
import pp.facerecognizer.R;

public class StudentAttendanceRecordListAdapter extends RecyclerView.Adapter<StudentAttendanceRecordListAdapter.ViewHolder> {

    private Context context;
    private String  StudentID;
    private ArrayList<AttendanceRecord> attendanceRecords = new ArrayList<>();

    public StudentAttendanceRecordListAdapter(Context context, ArrayList<AttendanceRecord> attendanceRecords, String sid) {
        this.context = context;
        this.attendanceRecords = attendanceRecords;
        this.StudentID = sid;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_records, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int absent = 0;
        int attended= 0;
        holder.intakeCode.setText(attendanceRecords.get(position).getIntake());
        holder.LecturerID.setText(attendanceRecords.get(position).getLecturerID());

        ArrayList<PieEntry> attendance = new ArrayList<>();
        attendance.add(new PieEntry(Integer.parseInt(attendanceRecords.get(position).getNumberOfClasses()), "Total Classes"));
        if (attendanceRecords.get(position).getStudentAttended().contains(StudentID)){
            attended =1;
        }
        else{
            absent=1;
        }
        attendance.add(new PieEntry(absent,"Absent classes"));
        attendance.add(new PieEntry(attended,"Attended classes"));
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
        private TextView  LecturerID, intakeCode;
        PieChart pieChart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            intakeCode = itemView.findViewById(R.id.stIntakeCode);
            LecturerID = itemView.findViewById(R.id.stLecturerID);
            pieChart = itemView.findViewById(R.id.pieChart);
        }
    }
}
