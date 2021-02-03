package br.com.empregoazul.main.company.presentation;

import java.util.List;

import br.com.empregoazul.common.model.Company;
import br.com.empregoazul.common.model.KeyFirebase;
import br.com.empregoazul.main.company.datasource.CompanyFireDataSource;

public class CompanyPresenter implements CompanyFireDataSource.CompanyFirebaseCallback {

    private final CompanyFireDataSource companyFireDataSource;
    private final CompanyFragment companyFragment;

    public CompanyPresenter(CompanyFireDataSource companyFireDataSource, CompanyFragment companyFragment) {
        this.companyFireDataSource = companyFireDataSource;
        this.companyFragment = companyFragment;
    }

    public void requestAll(){
        this.companyFragment.showProgressBar();
        this.companyFireDataSource.findRequest(this);
    }

    public void requestKey(){
        this.companyFragment.showProgressBar();
        this.companyFireDataSource.findRequestKey(this);
    }

    public void requestCache(){
        this.companyFragment.showProgressBar();
        this.companyFireDataSource.findRequestCache(this);
    }

    @Override
    public void getKey(KeyFirebase keyFirebase) {
        this.companyFragment.showRequestKey(keyFirebase);
    }

    @Override
    public void onSuccess(List<Company> companyList) {
        this.companyFragment.showRequest(companyList);
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void onComplete() {
        this.companyFragment.hideProgressBar();
    }
}
