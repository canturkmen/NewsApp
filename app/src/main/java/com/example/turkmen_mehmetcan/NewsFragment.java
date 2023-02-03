package com.example.turkmen_mehmetcan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

public class NewsFragment extends Fragment {

    private int newsId;
    private RecyclerView recView;
    private ProgressBar prg;

    Handler newsHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            List<News> data = (List<News>)msg.obj;
            MyAdapter myAdapter = new MyAdapter(getActivity(), data);
            recView.setAdapter(myAdapter);
            recView.setVisibility(View.VISIBLE);
            prg.setVisibility(View.INVISIBLE);
            return true;
        }
    });

    public NewsFragment() {
        // Required empty public constructor
    }

    public NewsFragment(int newsId) {
        this.newsId = newsId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prg = view.findViewById(R.id.progressBarList);
        recView = view.findViewById(R.id.rec_view);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        prg.setVisibility(View.VISIBLE);
        recView.setVisibility(View.INVISIBLE);

        MainRepo repo = new MainRepo();
        repo.getNewsDataByCategory(((MainApplication)requireActivity().getApplication()).srv, newsHandler, newsId);
    }
}