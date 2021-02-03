package br.com.empregoazul.common.view;

import android.content.Context;

import br.com.empregoazul.main.presentation.MainActivity;

public interface IView {

    Context getContext();

    void showProgressBar();

    void hideProgressBar();

}
