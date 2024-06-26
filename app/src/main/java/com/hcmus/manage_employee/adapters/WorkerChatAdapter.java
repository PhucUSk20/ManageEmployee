package com.hcmus.manage_employee.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hcmus.manage_employee.R;
import com.hcmus.manage_employee.activities.ChatActivity;
import com.hcmus.manage_employee.models.WorkerModel;
import com.hcmus.manage_employee.ui.search.ChatViewModel;
import com.hcmus.manage_employee.ui.search.SearchViewModel;
import com.hcmus.manage_employee.ui.worker_details.WorkerDetailsViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WorkerChatAdapter extends RecyclerView.Adapter<WorkerChatAdapter.ViewHolder> {

    private ArrayList<WorkerModel> workerModels;
    private Context mContext;

    public WorkerChatAdapter(WorkerDetailsViewModel workerDetailsViewModel, Context mContext) {
        this.workerModels = workerDetailsViewModel.getWorkerModels();
        this.mContext = mContext;
    }
    public WorkerChatAdapter(ChatViewModel chatViewModel, Context mContext) {
        this.workerModels = chatViewModel.getSearchViewModel();
        this.mContext = mContext;
    }

    public WorkerChatAdapter(SearchViewModel searchViewModel, Context mContext) {
        this.workerModels = searchViewModel.getSearchViewModel();
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_list_item,parent,false);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        WorkerModel workerModel = workerModels.get(position);
        holder.name.setText(workerModel.getName());
        holder.role.setText(workerModel.getRole());

        Picasso.get()
                .load(workerModel.getImgUrl())
                .noFade()
                .resizeDimen(R.dimen.profile_photo,R.dimen.profile_photo)
                .into(holder.circleImageView);

        holder.ll_worker_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("EMPLOYEE_ID", workerModel.getEmp_id());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return workerModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView circleImageView;
        TextView name,role;
        LinearLayout ll_worker_item;
        TextDrawable textDrawable;
        ColorGenerator colorGenerator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profile_image);
            colorGenerator = ColorGenerator.MATERIAL;
            textDrawable = TextDrawable.builder().buildRect("H",colorGenerator.getRandomColor());
            name = itemView.findViewById(R.id.worker_name);
            role = itemView.findViewById(R.id.worker_role);
            ll_worker_item = itemView.findViewById(R.id.ll_worker_item);
        }
    }
}
