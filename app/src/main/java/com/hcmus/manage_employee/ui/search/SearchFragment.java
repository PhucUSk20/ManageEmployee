package com.hcmus.manage_employee.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.manage_employee.R;
import com.hcmus.manage_employee.adapters.WorkerChatAdapter;
import com.hcmus.manage_employee.models.UserModel;
import com.hcmus.manage_employee.others.SharedPref;
import com.google.firebase.firestore.FirebaseFirestore;

public class SearchFragment extends Fragment {
    private RecyclerView rcvSearch;
    private WorkerChatAdapter adapter;
    FirebaseFirestore db;
    UserModel userModel;
    static private SearchViewModel searchViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        SearchView searchView = root.findViewById(R.id.search_view);
        searchView.setIconified(false);
        rcvSearch = root.findViewById(R.id.rcv_search);

        adapter = new WorkerChatAdapter(searchViewModel,getContext());

        rcvSearch.setNestedScrollingEnabled(false);
        rcvSearch.setAdapter(adapter);
        rcvSearch.setLayoutManager(new LinearLayoutManager(getContext()));

        Context context = getContext();
        SharedPref sharedPref = new SharedPref(context);
        String currentUserId = sharedPref.getEMP_ID();

        searchViewModel.initWithUserId(currentUserId);
        searchViewModel.setDataChangedListener2(() -> {
            getActivity().runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
                rcvSearch.setVisibility(View.VISIBLE);
            });
        });


        return root;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
//
}