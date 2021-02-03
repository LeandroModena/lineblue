package br.com.empregoazul.main.company.presentation;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.com.empregoazul.R;
import br.com.empregoazul.common.model.Company;
import br.com.empregoazul.common.model.KeyFirebase;
import br.com.empregoazul.common.view.AbstractFragment;
import br.com.empregoazul.main.company.datasource.CompanyFireDataSource;
import br.com.empregoazul.main.presentation.MainActivity;
import br.com.empregoazul.main.presentation.WidgetControl;

public class CompanyFragment extends AbstractFragment {

    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter;
    private CompanyPresenter companyPresenter;
    private CompanyFireDataSource dataSource;
    private SharedPreferences keyControl;
    private KeyFirebase keyMain;
    private ProgressBar progressBar;


    public CompanyFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);


        if (getActivity() != null) {
            progressBar = getActivity().findViewById(R.id.main_progress_bar);
        }

        keyMain = new KeyFirebase();
        keyControl = getActivity().getSharedPreferences("bd_key", Context.MODE_PRIVATE);
        keyControl.getString("companyKey", "0");
        keyMain.setKey(keyControl.getString("companyKey", "0"));


        feedAdapter = new FeedAdapter();
        recyclerView = view.findViewById(R.id.recycler_view_company_jobs);
        recyclerView.setAdapter(feedAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        dataSource = new CompanyFireDataSource();
        companyPresenter = new CompanyPresenter(dataSource, this);
        companyPresenter.requestKey();

        if (getActivity() != null) {
            ((MainActivity) getActivity()).scrollToolbarEnable(true);
            ((MainActivity) getActivity()).setVisibilityBottomNavigationView(false);
        }


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //TODO: Criar o menu para fazer a busca de empresa 01/02/2021

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_company_job;
    }

    public void showRequest(List<Company> response) {
        feedAdapter.setCompanyList(response);
        feedAdapter.notifyDataSetChanged();
    }

    public void showRequestKey(KeyFirebase keyFirebase) {
        if (!keyFirebase.getKey().equals(keyMain.getKey())) {
            companyPresenter.requestAll();
            SharedPreferences.Editor editor = keyControl.edit();
            editor.putString("companyKey", keyFirebase.getKey());
            editor.apply();
        } else {
            companyPresenter.requestCache();
        }
    }


    private class FeedViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView imageView;
        private LinearLayout linearLayout;
        private String linkUrl;
        boolean isValidUrl;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_text_view_company_jobs);
            imageView = itemView.findViewById(R.id.item_image_view_company_jobs);
            linearLayout = itemView.findViewById(R.id.item_linear_layout_company_jobs);

        }


        public void bind(Company company) {
            this.title.setText(company.getTitle());
            this.linkUrl = company.getLinkUrl();
            Glide.with(itemView.getContext()).load(company.getPhotoUrl()).into(this.imageView);
            isValidUrl = URLUtil.isValidUrl(linkUrl);


            linearLayout.setOnClickListener(v -> {

                if (isValidUrl) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(getContext(), Uri.parse(linkUrl));

                } else {
                    Toast.makeText(getContext(), R.string.error_url, Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

        private List<Company> companyList = new ArrayList<>();

        public void setCompanyList(List<Company> companyList) {
            this.companyList = companyList;
        }

        @NonNull
        @Override
        public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FeedViewHolder(getLayoutInflater().inflate(R.layout.item_company_jobs, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
            holder.bind(companyList.get(position));

        }

        @Override
        public int getItemCount() {
            return companyList.size();
        }
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}


