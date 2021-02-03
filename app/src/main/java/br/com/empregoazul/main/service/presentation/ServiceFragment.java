package br.com.empregoazul.main.service.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdService;
import com.google.android.gms.ads.AdView;

import br.com.empregoazul.R;
import br.com.empregoazul.common.view.AbstractFragment;
import br.com.empregoazul.main.presentation.MainActivity;

public class ServiceFragment extends AbstractFragment  {


    private Toolbar toolbar;

    public ServiceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        toolbar = getActivity().findViewById(R.id.main_toolbar);

        ((MainActivity) getActivity()).setVisibilityBottomNavigationView(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).progressBarEnable(false);
        toolbar.setTitle(R.string.service);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_service;
    }

}
