package com.ashstudios.safana.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashstudios.safana.R;
import com.ashstudios.safana.adapters.ChatRecyclerAdapter;
import com.ashstudios.safana.models.ChatMessageModel;
import com.ashstudios.safana.models.ChatroomModel;
import com.ashstudios.safana.models.OtherUserModel;
import com.ashstudios.safana.models.UserModel;
import com.ashstudios.safana.models.WorkerModel;
import com.ashstudios.safana.others.SharedPref;
import com.ashstudios.safana.utils.AndroidUtil;
import com.ashstudios.safana.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class ChatActivity extends AppCompatActivity {
    UserModel otherUser;
    String chatroomId;
    ChatroomModel chatroomModel;
    ChatRecyclerAdapter adapter;

    EditText messageInput;
    ImageButton sendMessageBtn;
    ImageButton backBtn;
    TextView otherUsername;
    RecyclerView recyclerView;
    ImageView imageView;
    FirebaseFirestore db;
    OtherUserModel otherUserModel;
    UserModel userModel;
    String employeeId2;
    String currentUserId2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Context context = getApplicationContext();
        SharedPref sharedPref = new SharedPref(context);
        String currentUserId = sharedPref.getEMP_ID();
        currentUserId2 = currentUserId;

        db=FirebaseFirestore.getInstance();
        String employeeId = getIntent().getStringExtra("EMPLOYEE_ID");
        db.collection("Employees").document(employeeId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
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
                    otherUserModel = new OtherUserModel(name, role, profileImg, empId, mail, mobile, sex, birthdate, password, allowanceIds);
                    otherUsername.setText(name);
                    Picasso.get().load(profileImg).into(imageView);
                }
            } else {
                Toast.makeText(null, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
        db.collection("Employees").document(currentUserId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
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
                    userModel = new UserModel(name, role, profileImg, empId, mail, mobile, sex, birthdate, password, allowanceIds);
                }
            } else {
                Toast.makeText(null, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
        //get otherUser người dùng khác được truy vấn bằng emp_id gửi từ intent workerdviewadapter
        // current other đã được truy vấn vào usermodel
        //  otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        employeeId2 = employeeId;
        chatroomId = FirebaseUtil.getChatroomId(currentUserId, employeeId);

        messageInput = findViewById(R.id.chat_message_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        backBtn = findViewById(R.id.back_btn);
        otherUsername = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_recycler_view);
        imageView = findViewById(R.id.profile_pic_image_view);



        backBtn.setOnClickListener((v) -> {
            onBackPressed();
        });


        sendMessageBtn.setOnClickListener((v -> {
            String message = messageInput.getText().toString().trim();
            if (message.isEmpty())
                return;
            sendMessageToUser(message);
        }));

        getOrCreateChatroomModel();
        setupChatRecyclerView();
    }

    void setupChatRecyclerView() {
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query, ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapter(options, getApplicationContext(), currentUserId2);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    void sendMessageToUser(String message) {

        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(currentUserId2);
        chatroomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message, currentUserId2, Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            messageInput.setText("");
                            sendNotification(message);
                        }
                    }
                });
    }

    void getOrCreateChatroomModel() {
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatroomModel = task.getResult().toObject(ChatroomModel.class);
                if (chatroomModel == null) {
                    //first time chat

                    chatroomModel = new ChatroomModel(
                            chatroomId,
                            Arrays.asList(currentUserId2, employeeId2),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);
                }
            }
        });
    }

    void sendNotification(String message) {
        try {
            JSONObject jsonObject = new JSONObject();

            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", "Phuc");
            notificationObj.put("body", message);

            JSONObject dataObj = new JSONObject();
            dataObj.put("userId", currentUserId2);

            jsonObject.put("notification", notificationObj);
            jsonObject.put("data", dataObj);
            jsonObject.put("to", otherUser.getFcmToken());

            callApi(jsonObject);


        } catch (Exception e) {

        }


    }

    void callApi(JSONObject jsonObject) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer YOUR_API_KEY")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });

    }
}