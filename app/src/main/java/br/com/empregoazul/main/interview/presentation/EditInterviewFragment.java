package br.com.empregoazul.main.interview.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.pattern.MaskPattern;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;

import br.com.empregoazul.R;
import br.com.empregoazul.common.model.Interview;
import br.com.empregoazul.common.view.AbstractFragment;
import br.com.empregoazul.main.interview.datasource.InterviewSQLite;
import br.com.empregoazul.main.presentation.MainActivity;

public class EditInterviewFragment extends AbstractFragment {

    private TextInputEditText textInputEditTextCompany, textInputEditTextLocal, textInputEditTextOpportunity,
            textInputEditTextDay, textInputEditTextHour, textInputEditTextSpeak, textInputEditTextAnnotations;
    private Button button;
    private SwitchCompat switchCompat;
    private Interview interview;
    private int idIterview;
    private boolean status;
    private int newStatus;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (getActivity() != null) {
            ((MainActivity)getActivity()).setVisibilityBottomNavigationView(false);
        }


        textInputEditTextCompany = view.findViewById(R.id.newinterview_text_input_edit_company);
        textInputEditTextLocal = view.findViewById(R.id.newinterview_text_input_edit_local);
        textInputEditTextOpportunity = view.findViewById(R.id.newinterview_text_input_edit_opportunity);
        textInputEditTextDay = view.findViewById(R.id.newinterview_text_input_edit_day);
        textInputEditTextHour = view.findViewById(R.id.newinterview_text_input_edit_hour);
        textInputEditTextSpeak = view.findViewById(R.id.newinterview_text_input_edit_speak);
        textInputEditTextAnnotations = view.findViewById(R.id.newinterview_text_input_edit_annotations);
        switchCompat = view.findViewById(R.id.interview_switch_compat_status);

        button = view.findViewById(R.id.newinterview_button_ok);

        if (getArguments() != null) {

            idIterview = getArguments().getInt("ID");
            textInputEditTextCompany.setText(getArguments().getString("company"));
            textInputEditTextLocal.setText(getArguments().getString("local"));
            textInputEditTextOpportunity.setText(getArguments().getString("opportunity"));
            textInputEditTextDay.setText(getArguments().getString("date"));
            textInputEditTextHour.setText(getArguments().getString("hour"));
            textInputEditTextSpeak.setText(getArguments().getString("speak"));
            textInputEditTextAnnotations.setText(getArguments().getString("annotation"));

            if (getArguments().getInt("status") == 1) {
                status = true;
                switchCompat.setText(R.string.interview_close);
            } else {
                status = false;
                switchCompat.setText(R.string.interview_open);
            }
            switchCompat.setChecked(status);
        }

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                switchCompat.setText(R.string.interview_close);
            } else {
                switchCompat.setText(R.string.interview_open);
            }
            newStatus = (isChecked ? 1 : 0);
        });

        newStatus = (switchCompat.isChecked() ? 1 : 0);

        button.setOnClickListener(v -> {
            onClickEdit();

        });

        MaskPattern mp03 = new MaskPattern("[0-1]");
        MaskPattern mp09 = new MaskPattern("[0-9]");
        MaskPattern mp01 = new MaskPattern("[0-3]");
        MaskPattern mp02 = new MaskPattern("[0-2]");
        MaskPattern mp05 = new MaskPattern("[0-5]");
        MaskFormatter mfHr = new MaskFormatter("[0-2][0-9]:[0-5][0-9]");
        MaskFormatter mfDt = new MaskFormatter("[0-3][0-9]/[0-1][0-9]/[0-9][0-9][0-9][0-9]");
        mfDt.registerPattern(mp01);
        mfDt.registerPattern(mp03);
        mfDt.registerPattern(mp09);
        mfHr.registerPattern(mp02);
        mfHr.registerPattern(mp09);
        mfHr.registerPattern(mp05);
        MaskTextWatcher mwtData = new MaskTextWatcher(textInputEditTextDay, mfDt);
        MaskTextWatcher mwtHora = new MaskTextWatcher(textInputEditTextHour, mfHr);
        textInputEditTextDay.addTextChangedListener(mwtData);
        textInputEditTextHour.addTextChangedListener(mwtHora);

        return view;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_new_interview;
    }

    public void onClickEdit(){
        Interview interviewEdit = getDate();
        if (interviewEdit != null){
            InterviewPresenter presenter = new InterviewPresenter(InterviewSQLite.getInstance(getContext()),this);
            presenter.updateInterview(interview);
            Toast.makeText(getContext(), R.string.save_interview, Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().popBackStack();

        } else {
            Toast.makeText(getContext(), R.string.interview_mandatory_filed, Toast.LENGTH_SHORT).show();
        }
    }

    public Interview getDate() {
        interview = new Interview();


        interview.setAnnotation(textInputEditTextAnnotations.getText().toString());

        if (idIterview > 0){
            interview.setID(idIterview);

        } else {
            return null;
        }

        if (!textInputEditTextCompany.getText().toString().isEmpty()) {
            interview.setCompany(textInputEditTextCompany.getText().toString());
        } else {
            return null;
        }
        if (!textInputEditTextLocal.getText().toString().isEmpty()) {
            interview.setLocal(textInputEditTextLocal.getText().toString());
        } else {
            return null;
        }
        if (!textInputEditTextOpportunity.getText().toString().isEmpty()) {
            interview.setOpportunity(textInputEditTextOpportunity.getText().toString());

        } else {
            return null;
        }
        if (!this.textInputEditTextHour.getText().toString().isEmpty() && textInputEditTextHour.getText().toString().length() == 5) {
            this.interview.setHour(textInputEditTextHour.getText().toString());

        } else {
            Toast.makeText(getContext(), R.string.standard_hour, Toast.LENGTH_LONG).show();
            return null;
        }
        if (!textInputEditTextDay.getText().toString().isEmpty() && textInputEditTextDay.getText().toString().length() == 10) {
            this.interview.setDate(textInputEditTextDay.getText().toString());
        } else {
            Toast.makeText(getContext(), R.string.standard_date, Toast.LENGTH_LONG).show();
            return null;
        }
        if (!textInputEditTextSpeak.getText().toString().isEmpty()) {
            this.interview.setSpeak(textInputEditTextSpeak.getText().toString());
        } else {
            return null;
        }
        if (newStatus <= 1) {
            this.interview.setStatus(newStatus);
        } else {
            return null;
        }
        return interview;
    }
}



