package com.hedspi.hoangviet.eslrecom.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.TestActivity;
import com.hedspi.hoangviet.eslrecom.models.Question;

import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment {
    public static final String QUESTION_OBJ = "question";

    private RecyclerView recyclerView;
    private TestViewAdapter adapter;

    private Question question;


    public static TestFragment newInstance(Question question) {
        Bundle args = new Bundle();
        args.putSerializable(QUESTION_OBJ, question);
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, viewGroup, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        question = (Question) getArguments().getSerializable(QUESTION_OBJ);
        List<String> answers = new ArrayList<>();
        answers.add(question.getTrueAnswer());
        answers.addAll(question.formWrongAnswerList());
        TextView questionTextView = (TextView) view.findViewById(R.id.question);
        questionTextView.setText(question.getQuestion());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TestViewAdapter(answers);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    class TestViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> answers;
        private Context mContext;

        public TestViewAdapter(List<String> answers){
            super();

            this.answers = answers;
        }

        @Override
        public int getItemCount() {
            return answers.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            AnswerViewHolder vh = (AnswerViewHolder) holder;
            String answer = answers.get(position);

            vh.button.setText(answer);
            vh.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (buttonView.getText().toString().equals(question.getTrueAnswer()))
                            ((TestActivity)getActivity()).reevalute(true);
                        else
                            ((TestActivity)getActivity()).reevalute(false);
                    }
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
            RecyclerView.ViewHolder viewHolder = new AnswerViewHolder(view);
            return viewHolder;
        }
    }

    class AnswerViewHolder extends RecyclerView.ViewHolder{
        public RadioButton button;

        public AnswerViewHolder(View view){
            super(view);

            button = (RadioButton) view.findViewById(R.id.radioButton);
        }

    }

}
