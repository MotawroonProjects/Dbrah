package com.app.dbrah.uis.activity_login;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.app.dbrah.R;
import com.app.dbrah.adapter.CountryCodeAdapter;
import com.app.dbrah.databinding.ActivityLoginBinding;
import com.app.dbrah.databinding.DailogVerificationCodeBinding;
import com.app.dbrah.model.CountryCodeModel;
import com.app.dbrah.model.LoginModel;
import com.app.dbrah.model.UserModel;
import com.app.dbrah.mvvm.ActivityLoginMvvm;
import com.app.dbrah.uis.activity_base.BaseActivity;
import com.app.dbrah.uis.activity_sign_up.SignUpActivity;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private String phone_code = "";
    private String phone = "";
    private LoginModel model;
    private ActivityResultLauncher<Intent> launcher;
    private ActivityLoginMvvm mvvm;
    private DailogVerificationCodeBinding dailogVerificationCodeBinding;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }


    private void initView() {
        mvvm = ViewModelProviders.of(this).get(ActivityLoginMvvm.class);

        binding.setLang(getLang());
        setUpToolbar(binding.toolbar, getString(R.string.login), R.color.white, R.color.black, 0, true);
        model = new LoginModel();
        binding.setModel(model);
        setUpSpinner();
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        });
        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().startsWith("0")) {
                    binding.edtPhone.setText("");
                }
            }
        });
        binding.btnLogin.setOnClickListener(v -> {
            mvvm.login(this, model.getPhone_code(), model.getPhone());
        });

        mvvm.timereturn.observe(this, time -> {
            if (dailogVerificationCodeBinding != null) {
                dailogVerificationCodeBinding.tvResend.setText(time);

            }
        });
        mvvm.smscode.observe(this, smsCode -> {
            dailogVerificationCodeBinding.edtCode.setText(smsCode);
        });
        mvvm.canresnd.observe(this, canResend -> {
            if (dailogVerificationCodeBinding != null) {
                dailogVerificationCodeBinding.tvResend.setEnabled(canResend);
                if (canResend) {
                    dailogVerificationCodeBinding.tvResend.setText(getString(R.string.resend));

                }
            }

        });
        mvvm.getUserData().observe(this, userModel -> {
            if(this.userModel!=null){
                setUserModel(userModel);
                setResult(RESULT_OK);
                finish();
            }
            else{
            this.userModel=userModel;
            if (userModel != null) {
                if(userModel.getData()!=null){

                    createVerificationCodeDialog();
//                setUserModel(userModel);
//                setResult(RESULT_OK);
//                finish();
                }
                else {
                    navigateToSignUpActivity();
                }
            } else {
                createVerificationCodeDialog();
            }
        }});

    }

    private void setUpSpinner() {
        List<CountryCodeModel> list = new ArrayList<>();
        list.add(new CountryCodeModel(R.drawable.saudi_arabia, getString(R.string.saudi_arabia), "+966","SA"));
        list.add(new CountryCodeModel(R.drawable.egypt_flag, getString(R.string.egypt), "+20","EG"));
        CountryCodeAdapter adapter = new CountryCodeAdapter(this);
        adapter.updateList(list);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCodeModel model = (CountryCodeModel) parent.getAdapter().getItem(position);
                LoginActivity.this.model.setPhone_code(model.getCode());
                LoginActivity.this.model.setCode(model.getCountry_code());
                binding.setModel(LoginActivity.this.model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void createVerificationCodeDialog() {
        mvvm.sendSmsCode(getLang(), model.getPhone_code(), model.getPhone(), this);

        AlertDialog builder = new AlertDialog.Builder(this)
                .create();
        builder.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dailogVerificationCodeBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dailog_verification_code, null, false);
        dailogVerificationCodeBinding.setModel(model);
        builder.setView(dailogVerificationCodeBinding.getRoot());
        builder.setCancelable(true);
        builder.setCanceledOnTouchOutside(false);
        dailogVerificationCodeBinding.edtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dailogVerificationCodeBinding.btnVerify.setEnabled(s.toString().length() == 6);

            }
        });
        dailogVerificationCodeBinding.tvResend.setOnClickListener(v ->mvvm.sendSmsCode(getLang(), model.getPhone_code(), model.getPhone(), this));
        builder.show();

        dailogVerificationCodeBinding.btnVerify.setOnClickListener(v -> {
           mvvm.checkValidCode(dailogVerificationCodeBinding.edtCode.getText().toString(),this);
        });
        builder.setOnCancelListener(dialog -> mvvm.stopTimer());

    }

    private void navigateToSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("phone_code", model.getPhone_code());
        intent.putExtra("phone", model.getPhone());
        launcher.launch(intent);
    }
}