package br.com.empregoazul.main.company.datasource;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

import br.com.empregoazul.common.model.Company;
import br.com.empregoazul.common.model.KeyFirebase;
import br.com.empregoazul.main.company.presentation.CompanyPresenter;

public class CompanyFireDataSource {

    public void findRequestKey(CompanyPresenter companyPresenter) {
        new FirebaseTask(companyPresenter).executeKey();
    }

    public void findRequest(CompanyPresenter companyPresenter) {
        new FirebaseTask(companyPresenter).execute();
    }

    public void findRequestCache(CompanyPresenter companyPresenter) {
        new FirebaseTask(companyPresenter).executeCache();
    }


    public interface CompanyFirebaseCallback {

        void getKey(KeyFirebase keyFirebase);

        void onSuccess(List<Company> companyList);

        void onError(String msg);

        void onComplete();

    }

    private static class FirebaseTask {

        private final CompanyFirebaseCallback callback;
        private String errorMessage;

        public String getErrorMessage() {
            return errorMessage;
        }

        private FirebaseTask(CompanyFirebaseCallback callback) {
            this.callback = callback;
        }


        protected void execute() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            List<Company> companies = new ArrayList<>();
            db.collection("ListCompany")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : documents) {
                            Company f = doc.toObject(Company.class);
                            companies.add(f);

                        }
                        callback.onSuccess(companies);
                    }).addOnFailureListener(e -> {
                errorMessage = e.getMessage();
                callback.onError(errorMessage);
            }).addOnCompleteListener(task -> {
                callback.onComplete();
            });
        }


        protected void executeCache() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            List<Company> companies = new ArrayList<>();
            db.collection("ListCompany")
                    .get(Source.CACHE)
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : documents) {
                            Company f = doc.toObject(Company.class);
                            companies.add(f);

                        }
                        callback.onSuccess(companies);
                    }).addOnFailureListener(e -> {
                errorMessage = e.getMessage();
                callback.onError(errorMessage);
            }).addOnCompleteListener(task -> {
                callback.onComplete();
            });
        }

        protected void executeKey() {
            KeyFirebase keyFirebase = new KeyFirebase();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Key").document("KeyCompany")
                    .get().addOnSuccessListener(documentSnapshot -> {
                KeyFirebase key = documentSnapshot.toObject(KeyFirebase.class);
                if (key != null) {
                    keyFirebase.setKey(key.getKey());
                }
                callback.getKey(keyFirebase);
            })
                    .addOnFailureListener(e -> {
                        errorMessage = e.getMessage();
                        callback.onError(errorMessage);
                    }).addOnCompleteListener(task -> {
                callback.onComplete();
            });
        }
    }

}
