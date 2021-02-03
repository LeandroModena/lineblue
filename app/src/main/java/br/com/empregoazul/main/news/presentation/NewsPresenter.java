package br.com.empregoazul.main.news.presentation;

import java.util.List;

import br.com.empregoazul.common.model.KeyFirebase;
import br.com.empregoazul.common.model.Post;
import br.com.empregoazul.main.news.datasource.NewsFireDataSource;

public class NewsPresenter implements NewsFireDataSource.NewsFirebaseCallback {

    private final NewsFireDataSource dataSource;
    private final NewsFragment newsFragment;

    public NewsPresenter(NewsFireDataSource dataSource, NewsFragment newsFragment) {
        this.dataSource = dataSource;
        this.newsFragment = newsFragment;
    }

    public void requestAll(){
        this.newsFragment.showProgressBar();
        this.dataSource.findRequest(this);
    }

    public void requestKey(){
        this.newsFragment.showProgressBar();
        this.dataSource.findRequestKey(this);
    }

    public void requestCache(){
        this.newsFragment.showProgressBar();
        this.dataSource.findRequestCache(this);
    }


    @Override
    public void getKey(KeyFirebase keyFirebase) {
        this.newsFragment.showRequestKey(keyFirebase);
    }

    @Override
    public void onSuccess(List<Post> posts) {
        this.newsFragment.showRequest(posts);
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void onComplete() {
        this.newsFragment.hideProgressBar();
    }
}
