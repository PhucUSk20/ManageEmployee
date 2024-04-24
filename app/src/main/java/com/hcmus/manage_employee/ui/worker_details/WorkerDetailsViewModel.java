package com.hcmus.manage_employee.ui.worker_details;

import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.hcmus.manage_employee.models.WorkerModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class WorkerDetailsViewModel extends ViewModel {

    private ArrayList<WorkerModel> workerModels;
    FirebaseFirestore db;

    WorkerModel workerModel;
    private DataChangedListener listener;


    public WorkerDetailsViewModel() {
        workerModels = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("Employees")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String name = document.getString("name");
                            String role = document.getString("role");
                            String profileImg = document.getString("profile_image");
                            String empId = document.getId(); // Lấy ID của document
                            String mail = document.getString("mail");
                            String mobile = document.getString("mobile");
                            String sex = document.getString("sex");
                            String birthdate = document.getString("birth_date");
                            String password = document.getString("password");
                            List<String> allowanceIds = (List<String>) document.get("allowance_ids");

                            workerModel = new WorkerModel(name, role, profileImg, empId, mail, mobile, sex, birthdate, password, allowanceIds);
                            workerModels.add(workerModel);
                            if(listener != null) {
                                listener.onDataChanged();
                            }
                        }
                    } else {
                        Toast.makeText(null, "Error"+ task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public ArrayList<WorkerModel> getWorkerModels() {
        return workerModels;
    }
    public void clearAllWorkers() {
        workerModels.clear();

        if (listener != null) {
            listener.onDataChanged();
        }
    }
    public void sort_name(Bundle b) {
        Comparator<WorkerModel> comparator = Comparator.comparing(worker -> worker.getName().substring(0, 1));
        Collections.sort(workerModels, comparator);
    }

    public void sort_male(Bundle b) {
        ArrayList<WorkerModel> male_workers = new ArrayList<>();
        for (WorkerModel workerModel : workerModels) {
            String gender = workerModel.getSex();
            if (gender.equals("male")) {
                male_workers.add(workerModel);
            }
        }
        workerModels.clear();
        workerModels.addAll(male_workers);

        if (listener != null) {
            listener.onDataChanged();
        }
    }

    public void sort_female(Bundle b) {
        ArrayList<WorkerModel> male_workers = new ArrayList<>();
        for (WorkerModel workerModel : workerModels) {
            String gender = workerModel.getSex();
            if (gender.equals("female")) {
                male_workers.add(workerModel);
            }
        }
        workerModels.clear();
        workerModels.addAll(male_workers);

        if (listener != null) {
            listener.onDataChanged();
        }
    }

    public interface DataChangedListener {
        void onDataChanged();
    }
    public void setDataChangedListener(DataChangedListener listener) {
        this.listener = listener;
    }
}