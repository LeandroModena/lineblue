package br.com.empregoazul.common.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public abstract class AbstractFragment extends Fragment implements IView {



    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(getLayout(), container, false);

        return view;
    }



    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    protected abstract @LayoutRes
    int getLayout();
}
