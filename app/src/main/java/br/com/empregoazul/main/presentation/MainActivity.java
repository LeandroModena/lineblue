package br.com.empregoazul.main.presentation;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import br.com.empregoazul.BuildConfig;
import br.com.empregoazul.R;
import br.com.empregoazul.common.view.AbstractActivity;
import br.com.empregoazul.login.LoginActivity;
import br.com.empregoazul.main.company.datasource.CompanyFireDataSource;
import br.com.empregoazul.main.company.presentation.CompanyFragment;
import br.com.empregoazul.main.company.presentation.CompanyPresenter;
import br.com.empregoazul.main.help.HelpFragment;
import br.com.empregoazul.main.home.datasource.HomeFireDataSource;
import br.com.empregoazul.main.home.presentation.HomeFragment;
import br.com.empregoazul.main.interview.datasource.InterviewSQLite;
import br.com.empregoazul.main.interview.presentation.InterviewFragment;
import br.com.empregoazul.main.news.presentation.NewsFragment;
import br.com.empregoazul.main.service.presentation.ServiceFragment;
import br.com.empregoazul.main.terms.TermsFragment;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AbstractActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener, WidgetControl, WidgetControl.Progressbar {

    public static final String CHANNEL_ID = "emprego_azul";

    private ProgressBar progressBar;
    private BottomNavigationView bottomNavigationView;

    private HomeFireDataSource homeFireDataSource;


    private CompanyFragment companyFragment;
    private CompanyPresenter companyPresenter;
    private CompanyFireDataSource companyFireDataSource;

    private HomeFragment homeFragment;
    private NewsFragment newsFragment;
    private Fragment interviewFragment;
    private Fragment serviceFragment;
    private Fragment active;
    private TermsFragment termsFragment;
    private StorageReference mStorageRef;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private FirebaseAnalytics mFirebaseAnalytics;

    private CircleImageView imageViewUser;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView textViewUserName;
    private HelpFragment helpFragment;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewId();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        MobileAds.initialize(this, initializationStatus -> {

        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (savedInstanceState == null) {
            onInject();
        }

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);


        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        } else {
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headView = navigationView.getHeaderView(0);

        imageViewUser = headView.findViewById(R.id.nav_head_image_view);
        textViewUserName = headView.findViewById(R.id.nav_head_text_view_name);

        if (user != null) {
            textViewUserName.setText(user.getDisplayName());
            Glide.with(this).load(user.getPhotoUrl()).into(this.imageViewUser);
        }

        toolbar.setTitle(R.string.home);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInject() {

        InterviewSQLite.getInstance(this);

        active = new Fragment();
        homeFragment = new HomeFragment();
        newsFragment = new NewsFragment();
        interviewFragment = new InterviewFragment();
        serviceFragment = new ServiceFragment();
        helpFragment = new HelpFragment();
        termsFragment = new TermsFragment();

        homeFireDataSource = new HomeFireDataSource();


        companyFragment = new CompanyFragment();
        companyFireDataSource = new CompanyFireDataSource();
        companyPresenter = new CompanyPresenter(companyFireDataSource, companyFragment);

        active = homeFragment;

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.main_frame_layout, homeFragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        int id = item.getItemId();

        if (id == R.id.menu_button_home) {
            fm.beginTransaction().replace(R.id.main_frame_layout, homeFragment).commit();
            active = homeFragment;
            toolbar.setTitle(R.string.home);
            scrollToolbarEnable(false);

            return true;

        } else if (id == R.id.menu_button_interview) {
            fm.beginTransaction().replace(R.id.main_frame_layout, interviewFragment).commit();
            active = interviewFragment;
            scrollToolbarEnable(false);
            toolbar.setTitle(R.string.interview);
            return true;

        } else if (id == R.id.menu_button_service) {
            fm.beginTransaction().replace(R.id.main_frame_layout, serviceFragment).commit();
            active = serviceFragment;
            scrollToolbarEnable(false);
            toolbar.setTitle(R.string.service);
            return true;

        } else if (id == R.id.menu_button_news) {
            fm.beginTransaction().replace(R.id.main_frame_layout, newsFragment).commit();
            active = newsFragment;
            scrollToolbarEnable(false);
            toolbar.setTitle(R.string.news);
            return true;

        } else if (id == R.id.nav_account) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
            this.drawer.closeDrawer(GravityCompat.START);
            return false;

        } else if (id == R.id.nav_help) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (!helpFragment.isAdded()) {
                active = helpFragment;
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.main_frame_layout, helpFragment);
                fragmentTransaction.commit();
            }
            this.drawer.closeDrawer(GravityCompat.START);
            return false;


        } else if (id == R.id.nav_message) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:empregoazul@empregoazul.com.br")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, "std");
            intent.putExtra(Intent.EXTRA_SUBJECT, "std");
            startActivity(intent);
            this.drawer.closeDrawer(GravityCompat.START);
            return false;

        } else if (id == R.id.nav_terms) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (!termsFragment.isAdded()) {
                active = termsFragment;
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.main_frame_layout, termsFragment);
                fragmentTransaction.commit();
            }
            this.drawer.closeDrawer(GravityCompat.START);
            return false;

        } else if (id == R.id.nav_version) {
            Toast.makeText(this, R.string.version + BuildConfig.VERSION_NAME, Toast.LENGTH_LONG).show();
            return false;
        } else if (id == R.id.nav_youtube) {
            Toast.makeText(this, "Canal em construção, aguarde atualizações", Toast.LENGTH_SHORT).show();
            this.drawer.closeDrawer(GravityCompat.START);
            return false;
        } else if (id == R.id.nav_linkedin) {
            Toast.makeText(this, "Pagina em construção, aguarde atualizações", Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company/emprego-azul/"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            this.drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        return false;
    }


    private void findViewId() {

        progressBar = findViewById(R.id.main_progress_bar);
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);

    }


    @Override
    public void scrollToolbarEnable(boolean enabled) {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.main_app_bar);

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();

        if (enabled) {
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
            appBarLayoutParams.setBehavior(new AppBarLayout.Behavior());
        } else {
            params.setScrollFlags(0);
            appBarLayoutParams.setBehavior(null);
        }
        appBarLayout.setLayoutParams(appBarLayoutParams);
    }

    @Override
    public void setVisibilityBottomNavigationView(boolean enabled) {
        if (enabled) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    @Override
    public void progressBarEnable(boolean enabled) {
        ProgressBar progressBar = findViewById(R.id.main_progress_bar);

        if (enabled) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }

    }

}