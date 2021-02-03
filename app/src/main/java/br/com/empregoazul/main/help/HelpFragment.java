package br.com.empregoazul.main.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import br.com.empregoazul.R;
import br.com.empregoazul.common.view.AbstractFragment;
import br.com.empregoazul.main.presentation.MainActivity;

public class HelpFragment extends AbstractFragment {


    private Toolbar toolbar;

    public HelpFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (getActivity() != null) {
            ((MainActivity) getActivity()).setVisibilityBottomNavigationView(false);
            toolbar = getActivity().findViewById(R.id.main_toolbar);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle(R.string.help);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_help;
    }
}
