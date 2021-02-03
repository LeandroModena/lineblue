package br.com.empregoazul.main.home.presentation;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.card.MaterialCardView;

import br.com.empregoazul.R;
import br.com.empregoazul.common.view.AbstractFragment;
import br.com.empregoazul.main.company.presentation.CompanyFragment;
import br.com.empregoazul.main.presentation.MainActivity;


public class HomeFragment extends AbstractFragment {


    private Toolbar toolbar;
    private MaterialCardView cardView;




    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        toolbar = getActivity().findViewById(R.id.main_toolbar);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        cardView = view.findViewById((R.id.home_card_view_jobs_company));

        cardView.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            CompanyFragment newAddFragment = new CompanyFragment();
            fragmentTransaction.addToBackStack("home");
            fragmentTransaction.replace(R.id.main_frame_layout, newAddFragment);
            fragmentTransaction.commit();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).scrollToolbarEnable(true);
            ((MainActivity) getActivity()).setVisibilityBottomNavigationView(true);
            ((MainActivity) getActivity()).progressBarEnable(false);
            toolbar.setTitle(R.string.home);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }


}
