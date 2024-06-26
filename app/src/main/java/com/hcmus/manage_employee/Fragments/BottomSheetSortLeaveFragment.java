package com.hcmus.manage_employee.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.hcmus.manage_employee.R;
import com.hcmus.manage_employee.activities.SupervisorDashboard;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

public class BottomSheetSortLeaveFragment extends BottomSheetDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button button;

    public BottomSheetSortLeaveFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static BottomSheetSortLeaveFragment newInstance(String param1, String param2) {
        BottomSheetSortLeaveFragment fragment = new BottomSheetSortLeaveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_bottom_sheet_sort_leave, container, false);
        final Chip chip_name = view.findViewById(R.id.chip_sort_name);
        final Chip chip_date = view.findViewById(R.id.chip_sort_date);

        Bundle sorting = getArguments();
        if( !sorting.isEmpty() )
        {
            chip_name.setChecked(sorting.getBoolean("nameChip"));
            chip_date.setChecked(sorting.getBoolean("dateChip"));

            if(chip_name.isChecked())
                chip_name.setChipIcon(getResources().getDrawable(R.drawable.ic_sort_by_attributes));
            else
                chip_name.setChipIcon(getResources().getDrawable(R.drawable.ic_sort_by_attributes_interface_button_option));
            if(chip_date.isChecked())
                chip_date.setChipIcon(getResources().getDrawable(R.drawable.ic_sort_by_attributes));
            else
                chip_date.setChipIcon(getResources().getDrawable(R.drawable.ic_sort_by_attributes_interface_button_option));
        }


        chip_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    chip_name.setChipIcon(getResources().getDrawable(R.drawable.ic_sort_by_attributes));
                }
                else
                {
                    chip_name.setChipIcon(getResources().getDrawable(R.drawable.ic_sort_by_attributes_interface_button_option));
                }
            }
        });

        chip_date.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    chip_date.setChipIcon(getResources().getDrawable(R.drawable.ic_sort_by_attributes));
                }
                else
                {
                    chip_date.setChipIcon(getResources().getDrawable(R.drawable.ic_sort_by_attributes_interface_button_option));
                }
            }
        });

        button = view.findViewById(R.id.apply_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting sorting data;
                boolean nameChip = chip_name.isChecked();
                boolean dateChip = chip_date.isChecked();

                Bundle b = new Bundle();
                b.putBoolean("nameChip", nameChip);
                b.putBoolean("dateChip", dateChip);

                ((SupervisorDashboard) getActivity()).onLeaveSortingChanged(b);
                dismiss();
            }
        });


        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}