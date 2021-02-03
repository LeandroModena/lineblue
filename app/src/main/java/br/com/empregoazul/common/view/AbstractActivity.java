package br.com.empregoazul.common.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import br.com.empregoazul.common.model.Post;
import br.com.empregoazul.main.presentation.MainActivity;


public abstract class AbstractActivity extends AppCompatActivity implements IView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        onInject();
    }

    @Override
    public Context getContext() {
        return getBaseContext();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    protected abstract @LayoutRes
    int getLayout();

    protected abstract void onInject();

}
