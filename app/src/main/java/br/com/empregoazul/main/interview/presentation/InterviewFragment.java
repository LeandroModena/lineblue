package br.com.empregoazul.main.interview.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import br.com.empregoazul.R;
import br.com.empregoazul.common.view.AbstractFragment;
import br.com.empregoazul.main.presentation.MainActivity;

public class InterviewFragment extends AbstractFragment {

    private CardView newInterview, editInterview, notificationInterview, historicInterview;
    private Toolbar toolbar;

    public InterviewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        newInterview = view.findViewById(R.id.card_view_new_interview);
        editInterview = view.findViewById(R.id.card_view_edit_interview);
        notificationInterview = view.findViewById(R.id.card_view_alarm_interview);
        historicInterview = view.findViewById(R.id.card_view_historic_interview);
        toolbar = getActivity().findViewById(R.id.main_toolbar);

        historicInterview.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            HistoricFragment historicFragment = new HistoricFragment();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.main_frame_layout, historicFragment);
            fragmentTransaction.commit();
        });


        notificationInterview.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            NotificationInterviewFragment notificationInterviewFragment = new NotificationInterviewFragment();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.main_frame_layout, notificationInterviewFragment);
            fragmentTransaction.commit();
        });

        editInterview.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            PreviewInterviewFragment previewInterviewFragment = new PreviewInterviewFragment();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.main_frame_layout, previewInterviewFragment);
            fragmentTransaction.commit();
        });


        newInterview.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            NewInterviewFragment newInterviewFragment = new NewInterviewFragment();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.main_frame_layout, newInterviewFragment);
            fragmentTransaction.commit();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((MainActivity)getActivity()).setVisibilityBottomNavigationView(true);
            ((MainActivity) getActivity()).progressBarEnable(false);
            toolbar.setTitle(R.string.interview);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_interview;
    }
}
