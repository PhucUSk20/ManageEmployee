package com.hcmus.manage_employee.ui.calendar_attendance;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hcmus.manage_employee.R;
import com.hcmus.manage_employee.others.SharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarAttendanceFragment extends Fragment implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    ArrayList<DayItem> greenDays = new ArrayList<>();

    private int attendanceCount = 0;
    private TextView attendanceCountTV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.calendar_attendance_fragment, container, false);

        Button q1 = root.findViewById(R.id.quarter1);
        Button q2 = root.findViewById(R.id.quarter2);
        Button q3 = root.findViewById(R.id.quarter3);
        Button q4 = root.findViewById(R.id.quarter4);

        attendanceCountTV = root.findViewById(R.id.attendanceCountTV);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("userScans");

        Drawable buttonPressBackground = AppCompatResources.getDrawable(getActivity(), R.drawable.button_press_background);
        Drawable buttonBackground = AppCompatResources.getDrawable(getActivity(), R.drawable.button_background);

        int whiteColor = ContextCompat.getColor(getActivity(), R.color.white);
        int darkGreenColor = ContextCompat.getColor(getActivity(), R.color.dark_green);

        int currentYear;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentYear = LocalDate.now().getYear();
        } else {
            currentYear = 2024;
        }

        calendarRecyclerView = root.findViewById(R.id.calendarRecyclerView);
        monthYearText = root.findViewById(R.id.monthYearTV);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDate = LocalDate.now();
        }
        Context context = getContext();
        SharedPref sharedPref = new SharedPref(context);
        String currentUserId = sharedPref.getEMP_ID();
        loadUserAttendance(currentUserId);

        int month = selectedDate.getMonthValue();
        Button[] quarterButtons = {q1, q2, q3, q4};
        int quarterIndex = (month - 1) / 3;
        for (int i = 0; i < quarterButtons.length; i++) {
            Button qButton = quarterButtons[i];
            if (i == quarterIndex) {
                qButton.setBackground(buttonBackground);
                qButton.setTextColor(darkGreenColor);
            } else {
                qButton.setBackground(buttonPressBackground);
                qButton.setTextColor(whiteColor);
            }
        }
        if (quarterIndex == 0){
            selectedDate = LocalDate.of(currentYear, 1, 1);
        }
        if (quarterIndex == 1){
            selectedDate = LocalDate.of(currentYear, 4, 1);
        }
        if (quarterIndex == 2){
            selectedDate = LocalDate.of(currentYear, 7, 1);
        }
        if (quarterIndex == 3){
            selectedDate = LocalDate.of(currentYear, 10, 1);
        }
        setMonthView(greenDays);


        q1.setOnClickListener(v -> {
            q1.setBackground(buttonBackground);
            q2.setBackground(buttonPressBackground);
            q3.setBackground(buttonPressBackground);
            q4.setBackground(buttonPressBackground);

            q1.setTextColor(darkGreenColor);
            q2.setTextColor(whiteColor);
            q3.setTextColor(whiteColor);
            q4.setTextColor(whiteColor);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                selectedDate = LocalDate.of(currentYear, 1, 1);
            }
            setMonthView(greenDays);
        });

        q2.setOnClickListener(v -> {
            q1.setBackground(buttonPressBackground);
            q2.setBackground(buttonBackground);
            q3.setBackground(buttonPressBackground);
            q4.setBackground(buttonPressBackground);

            q1.setTextColor(whiteColor);
            q2.setTextColor(darkGreenColor);
            q3.setTextColor(whiteColor);
            q4.setTextColor(whiteColor);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                selectedDate = LocalDate.of(currentYear, 4, 1);
            }
            setMonthView(greenDays);
        });

        q3.setOnClickListener(v -> {
            q1.setBackground(buttonPressBackground);
            q2.setBackground(buttonPressBackground);
            q3.setBackground(buttonBackground);
            q4.setBackground(buttonPressBackground);

            q1.setTextColor(whiteColor);
            q2.setTextColor(whiteColor);
            q3.setTextColor(darkGreenColor);
            q4.setTextColor(whiteColor);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                selectedDate = LocalDate.of(currentYear, 7, 1);
            }
            setMonthView(greenDays);
        });

        q4.setOnClickListener(v -> {
            q1.setBackground(buttonPressBackground);
            q2.setBackground(buttonPressBackground);
            q3.setBackground(buttonPressBackground);
            q4.setBackground(buttonBackground);

            q1.setTextColor(whiteColor);
            q2.setTextColor(whiteColor);
            q3.setTextColor(whiteColor);
            q4.setTextColor(darkGreenColor);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                selectedDate = LocalDate.of(currentYear, 10, 1);
            }
            setMonthView(greenDays);
        });

        return root;
    }


    private void initWidgets()
    {

    }


    private void setMonthView(ArrayList<DayItem> greenDays) {
        monthYearText.setText(monthYearFromDate(selectedDate));

        ArrayList<DayItem> daysInMonth = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Calculate the first day of the selected month
            LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);

            // Iterate over the previous 2 months and add their days to the list
            for (int i = 0; i <= 2; i++) {
                LocalDate month = firstOfMonth.plusMonths(i);
                int monthValue = month.getMonthValue();
                int yearValue = month.getYear();
                for (int day = 1; day <= month.lengthOfMonth(); day++) {
                    daysInMonth.add(new DayItem(day, monthValue, yearValue));
                    Log.i("Day", "Day: " + day + " Month: " + monthValue + " Year: " + yearValue);
                }
            }
        }



        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, greenDays, this);

        // Set up GridLayoutManager with horizontal orientation and 7 items per row
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 7, GridLayoutManager.HORIZONTAL, false);

        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }


    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return date.format(formatter);
        }
        return null;
    }

    public void previousMonthAction(View view)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDate = selectedDate.minusMonths(1);
        }
        setMonthView(null);
    }

    public void nextMonthAction(View view)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDate = selectedDate.plusMonths(1);
        }
        setMonthView(null);
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
        Toast.makeText(getActivity(), dayText, Toast.LENGTH_LONG).show();
    }


    private void loadUserAttendance(String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("userScans");
        for (int year = 2020; year <= 2024; year++) {
            for (int month = 1; month <= 12; month++) {
                for (int day = 1; day <= 31; day++) {
                    DatabaseReference dayRef = ref.child(String.valueOf(year)).child(String.format("%02d", month)).child(String.format("%02d", day));
                    int finalMonth = month;
                    int finalDay = day;
                    int finalYear = year;
                    dayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String userId = snapshot.child("userId").getValue(String.class);
                                if (userId.equals(userId)) {
                                    greenDays.add(new DayItem(finalDay, finalMonth, finalYear, 5));
                                    Calendar today = Calendar.getInstance();
                                    int currentYear = today.get(Calendar.YEAR);
                                    int currentMonth = today.get(Calendar.MONTH)+1;
                                    if (finalYear == currentYear && finalMonth == currentMonth) {
                                        attendanceCount++;
                                    }
                                    attendanceCountTV.setText("Trong tháng này bạn đã điểm danh " + attendanceCount + " ngày");
                                    setMonthView(greenDays);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        }
                    });
                }
            }
        }


    }
}