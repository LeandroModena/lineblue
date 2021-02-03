package br.com.empregoazul.main.interview.presentation;

import java.util.List;

import br.com.empregoazul.common.model.Interview;
import br.com.empregoazul.main.interview.datasource.InterviewSQLite;

public class InterviewPresenter {

    private final InterviewSQLite datasource;
    private NotificationInterviewFragment notificationInterviewFragment;
    private NewInterviewFragment newInterviewFragment;
    private PreviewInterviewFragment previewInterviewFragment;
    private EditInterviewFragment editInterviewFragment;
    private HistoricFragment historicFragment;

    public InterviewPresenter(InterviewSQLite datasource, HistoricFragment historicFragment ){
        this.datasource = datasource;
        this.historicFragment = historicFragment;
    }

    public InterviewPresenter(InterviewSQLite datasource, EditInterviewFragment editInterviewFragment ){
        this.datasource = datasource;
        this.editInterviewFragment = editInterviewFragment;
    }



    public InterviewPresenter (InterviewSQLite datasource, PreviewInterviewFragment previewInterviewFragment){
        this.datasource = datasource;
        this.previewInterviewFragment = previewInterviewFragment;
    }


    public InterviewPresenter(InterviewSQLite datasource, NewInterviewFragment newInterviewFragment) {
        this.datasource = datasource;
        this.newInterviewFragment = newInterviewFragment;
    }

    public InterviewPresenter(InterviewSQLite datasource, NotificationInterviewFragment notificationInterviewFragment) {
        this.datasource = datasource;
        this.notificationInterviewFragment = notificationInterviewFragment;
    }

    public void addItem(Interview interview){
        datasource.addItem(interview);
    }

    public void updateInterview(Interview interview){
        datasource.updateInterviewDB(interview);
    }

    public void deleteInterview(long ID){
        datasource.deleteInterviewDB(ID);
    }

    public List<Interview> getListInterviewAll(){
        return datasource.getListInterviewAll();
    }

    public List <Interview> getListInterviewHistoric(){
        return datasource.getListInterviewClose();
    }

    public List<Interview> getListInterviewOpen(){
        return datasource.getListInterviewOpen();
    }
}
