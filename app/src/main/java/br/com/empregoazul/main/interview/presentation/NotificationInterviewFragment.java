package br.com.empregoazul.main.interview.presentation;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.empregoazul.R;
import br.com.empregoazul.common.model.Interview;
import br.com.empregoazul.common.view.AbstractFragment;
import br.com.empregoazul.main.interview.datasource.InterviewSQLite;
import br.com.empregoazul.main.presentation.MainActivity;

public class NotificationInterviewFragment extends AbstractFragment {


    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter;
    private InterviewPresenter interviewPresenter;

    public NotificationInterviewFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        feedAdapter = new FeedAdapter();
        recyclerView = view.findViewById(R.id.notifications_recycler_view);
        recyclerView.setAdapter(feedAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        if (getActivity() != null) {
            ((MainActivity) getActivity()).setVisibilityBottomNavigationView(false);
        }
        setInterviewOpen();

        return view;
    }

    public void setInterviewOpen() {
        interviewPresenter = new InterviewPresenter(InterviewSQLite.getInstance(getContext()), this);
        List<Interview> list = interviewPresenter.getListInterviewOpen();
        feedAdapter.setInterviewList(list);
        feedAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_notifications_interview;
    }

    private class FeedViewHolder extends RecyclerView.ViewHolder {

        private TextView id, company, opportunity, local, date, hour, speak, annotation;
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
                alert.setTitle("Entrevista");

                alert.setMessage("O que deseja fazer com sua entrevista?");
                alert.setNeutralButton("Cancelar", (dialog, which) -> {
                    dialog.cancel();

                });

                alert.setPositiveButton("Sincronizar", ((dialog, which) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyyhh:mm");
                    String dateString = interview.getDate() + interview.getHour();
                    Date date = null;
                    try {
                        date = sdf.parse(dateString);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date.getTime())
                            .putExtra(CalendarContract.Events.TITLE, "Entrevista na " + interview.getCompany())
                            .putExtra(CalendarContract.Events.DESCRIPTION, interview.getAnnotation())
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, interview.getLocal())
                            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                    startActivity(intent);



                    dialog.cancel();
                }));

                alert.create().show();
            });
        }

    }

    private class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

        private List<Interview> interviewList = new ArrayList<>();

        public void setInterviewList(List<Interview> interviewList) {
            this.interviewList = interviewList;
        }

        @NonNull
        @Override
        public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FeedViewHolder(getLayoutInflater().inflate(R.layout.item_interview_edit, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
            holder.bind(interviewList.get(position));
        }

        @Override
        public int getItemCount() {
            return interviewList.size();
        }
    }

    private void syncCalendar(String date, String hour, String company, String local){

    }


}
