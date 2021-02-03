package br.com.empregoazul.main.news.presentation;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.com.empregoazul.R;
import br.com.empregoazul.common.model.KeyFirebase;
import br.com.empregoazul.common.model.Post;
import br.com.empregoazul.common.view.AbstractFragment;
import br.com.empregoazul.main.news.datasource.NewsFireDataSource;
import br.com.empregoazul.main.presentation.MainActivity;

public class NewsFragment extends AbstractFragment {

    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter;
    private NewsFireDataSource newsFireDataSource;
    private NewsPresenter newsPresenter;
    private SharedPreferences keyControl;
    private KeyFirebase keyMain;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    public NewsFragment (){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =super.onCreateView(inflater, container, savedInstanceState);

        if (getActivity() != null) {
            progressBar = getActivity().findViewById(R.id.main_progress_bar);
            toolbar = getActivity().findViewById(R.id.main_toolbar);
        }

        keyMain = new KeyFirebase();
        keyControl = getActivity().getSharedPreferences("bd_key", Context.MODE_PRIVATE);
        keyControl.getString("newsKey", "0");
        keyMain.setKey(keyControl.getString("newsKey", "0"));

        feedAdapter = new FeedAdapter();
        recyclerView = view.findViewById(R.id.news_recycler_view);
        recyclerView.setAdapter(feedAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsFireDataSource = new NewsFireDataSource();
        newsPresenter = new NewsPresenter(newsFireDataSource, this);
        newsPresenter.requestKey();

        ((MainActivity)getActivity()).scrollToolbarEnable(true);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle(R.string.news);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_news;
    }

    public void showRequestKey(KeyFirebase keyFirebase) {
        if (!keyFirebase.getKey().equals(keyMain.getKey())) {
            newsPresenter.requestAll();
            SharedPreferences.Editor editor = keyControl.edit();
            editor.putString("newsKey", keyFirebase.getKey());
            editor.apply();
        } else {
            newsPresenter.requestCache();
        }
    }

    public void showRequest(List<Post> posts) {
        feedAdapter.setPosts(posts);
        feedAdapter.notifyDataSetChanged();
    }


    private class FeedViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView title, desc, date;
        private String urlConnection;
        private CardView cardView;
        boolean isValidUrl;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.home_image_view_job);
            title = itemView.findViewById(R.id.home_text_view_title);
            desc = itemView.findViewById(R.id.home_text_view_desc);
            date = itemView.findViewById(R.id.home_text_view_date);
            cardView = itemView.findViewById(R.id.card_view_item);

        }

        public void bind(Post post) {
            this.title.setText(post.getTitle());
            this.desc.setText(post.getDesc());
            this.date.setText(post.getDate());
            Glide.with(itemView.getContext()).load(post.getPhotoUrl()).into(this.imageView);


            this.urlConnection = post.getUrl();
            isValidUrl = URLUtil.isValidUrl(urlConnection);

            cardView.setOnClickListener(v -> {

                if (getContext() != null) {
                    if (isValidUrl) {
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(getContext(), Uri.parse(urlConnection));
                    } else {
                        Toast.makeText(getContext(), R.string.error_url, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

        private List<Post> posts = new ArrayList<>();


        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

        @NonNull
        @Override
        public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FeedViewHolder(getLayoutInflater().inflate(R.layout.item_home_latest_news, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
            holder.bind(posts.get(position));
        }

        @Override
        public int getItemCount() {
            return posts.size();
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
