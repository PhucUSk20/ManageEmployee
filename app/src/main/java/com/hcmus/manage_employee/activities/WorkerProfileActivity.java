package com.hcmus.manage_employee.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hcmus.manage_employee.R;
import com.hcmus.manage_employee.models.WorkerModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class WorkerProfileActivity extends AppCompatActivity {
    private FirebaseFirestore mDatabase;
    public static String employeeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Detailed Information");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        employeeId = getIntent().getStringExtra("EMPLOYEE_ID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (employeeId != null) {
            db.collection("Employees").document(employeeId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        WorkerModel worker = document.toObject(WorkerModel.class);
                        if (worker != null) {
                            updateUI(worker);
                        }
                    }
                } else {
                    Toast.makeText(WorkerProfileActivity.this, "Error getting employee details.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateUI(WorkerModel workerModel) {
        ImageView profileImage = findViewById(R.id.profile_img);
        TextView tvEmpId = findViewById(R.id.tv_emp_id), empName = findViewById(R.id.emp_name),
                empRole = findViewById(R.id.emp_role), tvEmail = findViewById(R.id.tv_email),
                tvMob = findViewById(R.id.tv_mob), tvSex = findViewById(R.id.tv_sex),
                tvBdate = findViewById(R.id.tv_bdate), tvAllowance = findViewById(R.id.tv_allowances_given);
        TextView tvTaskAssigned = findViewById(R.id.tv_task_assigned);
        TextView tvTaskComplete = findViewById(R.id.tv_task_completed);

        Picasso.get().load(workerModel.getImgUrl()).into(profileImage);

        tvEmpId.setText(employeeId);
        empName.setText(workerModel.getName());
        empRole.setText(workerModel.getRole());
        tvEmail.setText(workerModel.getMail());
        tvMob.setText(workerModel.getMobile());
        tvSex.setText(workerModel.getSex());
        tvBdate.setText(workerModel.getBirthdate());
        if (workerModel.getAllowance_ids() != null && !workerModel.getAllowance_ids().isEmpty()) {
            String allowances = TextUtils.join("\n", workerModel.getAllowance_ids());
            tvAllowance.setText(allowances);
        } else {
            tvAllowance.setText("No allowances");
        }
        if (workerModel.getTaskID() != null && !workerModel.getTaskID().isEmpty()) {
            int numberOfTasks = workerModel.getTaskID().size();
            tvTaskAssigned.setText(String.valueOf(numberOfTasks));
        } else {
            tvTaskAssigned.setText("0");
        }
        if (workerModel.getTaskID() != null && !workerModel.getTaskID().isEmpty()) {
            tvTaskComplete.setText(String.valueOf(workerModel.getTaskComplete()));
        } else {
            tvTaskComplete.setText("0");
        }
    }
}
