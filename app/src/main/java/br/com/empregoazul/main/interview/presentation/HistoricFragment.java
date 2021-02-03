package br.com.empregoazul.main.interview.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.empregoazul.R;
import br.com.empregoazul.common.model.Interview;
import br.com.empregoazul.common.view.AbstractFragment;
import br.com.empregoazul.main.interview.datasource.InterviewSQLite;
import br.com.empregoazul.main.presentation.MainActivity;

public class HistoricFragment extends AbstractFragment {

    private InterviewPresenter interviewPresenter;
    private FeedAdapter feedAdapter;

    public HistoricFragment (){

    }

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        recyclerView = view.findViewById(R.id.historic_recycler_view);
        feedAdapter = new FeedAdapter();
        recyclerView.setAdapter(feedAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getActivity() != null) {
            ((MainActivity)getActivity()).setVisibilityBottomNavigationView(false);
        }

        setInterviewHistoric();

        return view;
    }

    public void setInterviewHistoric(){
        interviewPresenter = new InterviewPresenter(InterviewSQLite.getInstance(getContext()), this);
        List<Interview> list = interviewPresenter.getListInterviewHistoric();
        feedAdapter.setInterviewList(list);
        feedAdapter.notifyDataSetChanged();
    }

    private void deleteItemSelect(long ID){
        interviewPresenter = new InterviewPresenter(InterviewSQLite.getInstance(getContext()),this);
        interviewPresenter.deleteInterview(ID);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_interview_historic;
    }

    private class FeedViewHolder extends RecyclerView.ViewHolder {

        private final TextView id;
        private final TextView company;
        private final TextView opportunity;
        private final TextView local;
        private final TextView date;
        private final TextView hour;
        private final TextView speak;
        private final TextView annotation;
        private LinearLayout linearLayout;


        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.interview_edit_text_id);
            company = itemView.findViewById(R.id.interview_edit_text_company);
            opportunity = itemView.findViewById(R.id.interview_edit_text_opportunity);
            local = itemView.findViewById(R.id.interview_edit_text_local);
            date = itemView.findViewById(R.id.interview_edit_text_date);
            hour = itemView.findViewById(R.id.interview_edit_text_hour);
            speak = itemView.findViewById(R.id.interview_edit_text_speak);
            annotation = itemView.findViewById(R.id.interview_edit_text_annotation);
            linearLayout = itemView.findViewById(R.id.interview_linear_layout);

        }


        public void bind(Interview interview) {
            company.setText(interview.getCompany());
            opportunity.setText(interview.getOpportunity());
            local.setText(interview.getLocal());
            date.setText(interview.getDate());
            hour.setText(interview.getHour());
            speak.setText(interview.getSpeak());
            annotation.setText(interview.getAnnotation());


            linearLayout.setOnClickListener(v -> {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(R.string.interview);

                alert.setMessage(R.string.interview_ask);
                alert.setNeutralButton(R.string.cancel, (dialog, which) -> {
                    dialog.cancel();

                });

                alert.setNegativeButton(R.string.delete, (dialog, which) -> {
                    deleteItemSelect(interview.getID());
                    setInterviewHistoric();

                });

                alert.setPositiveButton("Editar", ((dialog, which) -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", interview.getID());
                    bundle.putString("company", interview.getCompany());
                    bundle.putString("opportunity", interview.getOpportunity());
                    bundle.putString("local", interview.getLocal());
                    bundle.putString("date", interview.getDate());
                    bundle.putString("hour", interview.getHour());
                    bundle.putString("speak", interview.getSpeak());
                    bundle.putString("annotation", interview.getAnnotation());
                    bundle.putInt("status",  interview.getStatus());

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    EditInterviewFragment editInterviewFragment = new EditInterviewFragment();
                    editInterviewFragment.setArguments(bundle);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.main_frame_layout, editInterviewFragment);
                    fragmentTransaction.commit();

                }));

                alert.create().show();
            });
        }

    }

    private class FeedAdapter extends RecyclerView.Adapter<HistoricFragment.FeedViewHolder> {

        private List<Interview> interviewList = new ArrayList<>();

        public void setInterviewList(List<Interview> interviewList) {
            this.interviewList = interviewList;
        }

        @NonNull
        @Override
        public HistoricFragment.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new HistoricFragment.FeedViewHolder(getLayoutInflater().inflate(R.layout.item_interview_edit, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull HistoricFragment.FeedViewHolder holder, int position) {
            holder.bind(interviewList.get(position));
        }

        @Override
        public int getItemCount() {
            return interviewList.size();
        }
    }
}
