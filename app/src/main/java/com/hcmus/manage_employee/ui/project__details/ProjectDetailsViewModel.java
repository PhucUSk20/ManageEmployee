package com.hcmus.manage_employee.ui.project__details;

import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.hcmus.manage_employee.models.ProjectModel;
import com.hcmus.manage_employee.ui.worker_details.WorkerDetailsViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class ProjectDetailsViewModel extends ViewModel {
    private ArrayList<ProjectModel> projectModels;
    private WorkerDetailsViewModel.DataChangedListener listener;
    public ProjectDetailsViewModel() {
        projectModels = new ArrayList<>();
        getData();
    }

    public ArrayList<ProjectModel> getArrayListMutableLiveData() {
        return projectModels;
    }

    public void getData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Projects")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String projectID = document.getId();
                            String title = document.getString("Title");
                            String stDate = document.getString("Start Date");
                            String duDate = document.getString("Due Date");

                            db.collection("Tasks")
                                    .whereEqualTo("Project Name", title)
                                    .get()
                                    .addOnCompleteListener(tasksTask -> {
                                        if (tasksTask.isSuccessful()) {
                                            ArrayList<String> taskStatusList = new ArrayList<>();
                                            for (QueryDocumentSnapshot document2 : Objects.requireNonNull(tasksTask.getResult())) {
                                                if (document2.contains("Status(%)")) {
                                                    String status = document2.getString("Status(%)");
                                                    taskStatusList.add(status);
                                                }
                                            }
                                            ProjectModel projectModel = new ProjectModel(projectID, taskStatusList, title, stDate, duDate);
                                            projectModels.add(projectModel);
                                            if (listener != null) {
                                                listener.onDataChanged();
                                            }
                                        } else {
                                            Toast.makeText(null, "Error fetching tasks: " + tasksTask.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(null, "Error fetching projects: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    public void sort(Bundle b) {
        projectModels.remove(0);
    }
    public interface DataChangedListener {
        void onDataChanged();
    }
    public void setDataChangedListener(WorkerDetailsViewModel.DataChangedListener listener) {
        this.listener = listener;
    }
}