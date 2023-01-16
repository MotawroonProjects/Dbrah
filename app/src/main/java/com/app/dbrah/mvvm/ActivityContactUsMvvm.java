package com.app.dbrah.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.app.dbrah.R;
import com.app.dbrah.model.ContactUsModel;
import com.app.dbrah.model.OrderDataModel;
import com.app.dbrah.model.OrderModel;
import com.app.dbrah.model.SearchHomeDataModel;
import com.app.dbrah.model.StatusResponse;
import com.app.dbrah.model.UserModel;
import com.app.dbrah.remote.Api;
import com.app.dbrah.share.Common;
import com.app.dbrah.tags.Tags;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityContactUsMvvm extends AndroidViewModel {

    public MutableLiveData<Boolean> send = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<List<OrderModel>> onDataSuccess;

    public ActivityContactUsMvvm(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<List<OrderModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }
    public void contactUs(Context context, ContactUsModel contactUsModel) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .contactUs(contactUsModel.getName(), contactUsModel.getEmail(), contactUsModel.getSubject(), contactUsModel.getMessage(),contactUsModel.getOrder_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);

                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                send.postValue(true);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dialog.dismiss();
                    }
                });
    }
    public void search(String query, UserModel userModel) {
        String user_id = null;
        if (userModel!=null){
            user_id = userModel.getData().getId();
            Log.e("lllll",user_id);
        }
        if(query!=null){
            Log.e("lllll",query);}

        Api.getService(Tags.base_url).searchOrders(user_id,query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<OrderDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<OrderDataModel> response) {
                        //Log.e("dlkdkdk",response.body().getStatus()+"");
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null && response.body().getStatus() == 200) {
                                //getIsLoading().setValue(false);
                                onDataSuccess.setValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.toString());
                    }
                });
    }


}
