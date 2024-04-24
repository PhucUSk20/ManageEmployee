package com.hcmus.manage_employee.ui.leave;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.hcmus.manage_employee.models.LeaveStatusModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class LeaveViewModel extends ViewModel {
    private ArrayList<LeaveStatusModel> leaveStatusModels;

    FirebaseFirestore db;
    private DataOnChangedListener listener;
    public LeaveViewModel() {
        leaveStatusModels = new ArrayList<>();
    }
    public void initwithUserId(String userID){
        db = FirebaseFirestore.getInstance();
        Log.d("TAGXXX",userID);
        db.collection("Leaves").whereEqualTo("empid",userID).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String reason = document.getString("reason");
                            String name = document.getString("name");
                            String img_url = document.getString("profile_image");
                            String dateend = document.getString("to");
                            String status = document.getString("Status");
                            String date = document.getString("date");
                            if(status!=null) {
                                LeaveStatusModel leaveStatusModel = new LeaveStatusModel(name,reason,img_url,userID,date,dateend,status);
                                leaveStatusModels.add(leaveStatusModel);
                            }else{
                                LeaveStatusModel leaveStatusModel = new LeaveStatusModel(name,reason,img_url,userID,date,dateend,"Waiting...");
                                leaveStatusModels.add(leaveStatusModel);
                            }
                            if (listener != null) {
                                listener.onDataChanged();
                            }
                        }
                    }
                });
    }
    public ArrayList<LeaveStatusModel> getLeaveStatusModels() {
        return leaveStatusModels;
    }
    public interface DataOnChangedListener{
        void onDataChanged();
    }
    public void setDataChangedListener(DataOnChangedListener listener){
        this.listener = listener;
    }
}