package br.com.empregoazul.main.presentation;

public interface WidgetControl {

    void scrollToolbarEnable(boolean enabled);

    void setVisibilityBottomNavigationView(boolean enabled);


    interface Progressbar {

        void progressBarEnable(boolean enabled);
    }

}
