package br.com.empregoazul.main.terms;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import br.com.empregoazul.R;
import br.com.empregoazul.common.view.AbstractFragment;
import br.com.empregoazul.main.presentation.MainActivity;

public class TermsFragment extends AbstractFragment {

    private TextView textView;
    private Toolbar toolbar;

    public TermsFragment (){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (getActivity() != null) {
            ((MainActivity)getActivity()).setVisibilityBottomNavigationView(false);
            toolbar = getActivity().findViewById(R.id.main_toolbar);

        }

        textView = view.findViewById(R.id.terms_text_view);
        textView.setText(Html.fromHtml(getResources().getString(R.string.terms_of_use)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle(R.string.terms);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_terms;
    }
}
