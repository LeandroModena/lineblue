package br.com.empregoazul.main.news.datasource;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

import br.com.empregoazul.common.model.KeyFirebase;
import br.com.empregoazul.common.model.Post;
import br.com.empregoazul.main.news.presentation.NewsPresenter;

public class NewsFireDataSource {

    public void findRequestKey(NewsPresenter newsPresenter) {
        new NewsFireDataSource.FirebaseTask(newsPresenter).executeKey();
    }

    public void findRequest(NewsPresenter newsPresenter) {
        new NewsFireDataSource.FirebaseTask(newsPresenter).execute();
    }

    public void findRequestCache(NewsPresenter newsPresenter) {
        new NewsFireDataSource.FirebaseTask(newsPresenter).executeCache();
    }


    public interface NewsFirebaseCallback {

        void getKey(KeyFirebase keyFirebase);

        void onSuccess(List<Post> posts);

        void onError(String msg);

        void onComplete();

    }

    private static class FirebaseTask {

        private final NewsFirebaseCallback callback;
        private String errorMessage;

        public String getErrorMessage() {
            return errorMessage;
        }

        private FirebaseTask(NewsFirebaseCallback callback) {
            this.callback = callback;
        }


        protected void execute() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            List<Post> posts = new ArrayList<>();
            db.collection("ListFeed")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : documents) {
                            Post P = doc.toObject(Post.class);
                            posts.add(P);

                        }
                        callback.onSuccess(posts);
                    }).addOnFailureListener(e -> {
                errorMessage = e.getMessage();
                callback.onError(errorMessage);
            }).addOnCompleteListener(task -> {
                callback.onComplete();
            });
        }


        protected void executeCache() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            List<Post> posts = new ArrayList<>();
            db.collection("ListFeed")
                    .get(Source.CACHE)
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : documents) {
                            Post P = doc.toObject(Post.class);
                            posts.add(P);

                        }
                        callback.onSuccess(posts);
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
            db.collection("Key").document("KeyNews")
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
