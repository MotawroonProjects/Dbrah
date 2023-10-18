package com.app.dbrah.uis.activity_home.cart_module;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.dbrah.R;
import com.app.dbrah.adapter.MainCartAdapter;
import com.app.dbrah.adapter.SpinnerTimeAdapter;
import com.app.dbrah.databinding.FragmentCartBinding;
import com.app.dbrah.model.AddressModel;
import com.app.dbrah.model.ProductModel;
import com.app.dbrah.model.TimeModel;
import com.app.dbrah.model.cart_models.CartModel;
import com.app.dbrah.model.cart_models.CartSingleModel;
import com.app.dbrah.model.cart_models.ManageCartModel;
import com.app.dbrah.mvvm.FragmentCartMvvm;
import com.app.dbrah.mvvm.GeneralMvvm;
import com.app.dbrah.uis.activity_base.BaseFragment;
import com.app.dbrah.uis.activity_home.HomeActivity;
import com.app.dbrah.uis.activity_home.profile_module.FragmentProfile;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class FragmentCart extends BaseFragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    private HomeActivity activity;
    private FragmentCartMvvm mvvm;
    private FragmentCartBinding binding;
    private ManageCartModel manageCartModel;
    private MainCartAdapter adapter;
    private GeneralMvvm generalMvvm;
    private AddressModel selectedAddress;

    private int selectedOrderPos = -1;
    private int type=1;
    private CartSingleModel cartSingleModel;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private String time = null, date = null, time_id;
    String expectedtime ;
    private boolean isDatachanged;
    private SpinnerTimeAdapter spinnerTimeAdapter;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentCart newInstance() {
        return new FragmentCart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        mvvm = ViewModelProviders.of(activity).get(FragmentCartMvvm.class);
        binding.setLang(getLang());
        manageCartModel = ManageCartModel.newInstance();
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        generalMvvm.getOnCartRefreshed().observe(activity, isRefreshed -> {
            refreshCart();
        });
        mvvm.getOnDataSuccess().observe(this, timeModels -> {

            if (spinnerTimeAdapter != null) {
                spinnerTimeAdapter.updateList(timeModels);

            }


        });
        spinnerTimeAdapter = new SpinnerTimeAdapter(activity);

        binding.spinner.setAdapter(spinnerTimeAdapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    time = null;
                    time_id = "";
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                    TimeModel timeModel = (TimeModel) parent.getSelectedItem();
                    time_id = timeModel.getId();
                    time = dateFormat.format(new Date(timeModel.getFrom() * 1000)) + " - " + dateFormat.format(new Date(timeModel.getTo() * 1000));
                    ;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        generalMvvm.getOnAddressSelectedForOrder().observe(activity, addressModel -> {
            this.selectedAddress = addressModel;
           if(type==2){
            manageCartModel.addAddress(selectedAddress.getId(), activity);
            if (manageCartModel.getCartModel()!=null){
                mvvm.sendAllOrder(activity,manageCartModel.getCartModel());

            }}
           else if(type==1){
               if (selectedAddress != null) {
                   cartSingleModel.setAddress_id(selectedAddress.getId());
                   mvvm.sendSingleOrder(activity, cartSingleModel);
               }
           }
        });

        mvvm.getOnAllOrderSentSuccess().observe(activity, cartModel -> {
            generalMvvm.onOrderNavigate().setValue(0);
            generalMvvm.getActionFragmentHomeNavigator().setValue(1);

            manageCartModel.clear(activity);
            binding.flOrderAll.setVisibility(View.GONE);
            generalMvvm.getOnCartRefreshed().setValue(true);
            generalMvvm.getOnCurrentOrderRefreshed().setValue(true);
            for (CartModel.CartObject object : cartModel.getCartList()){
                for (ProductModel productModel :object.getProducts()){
                    productModel.setAmount(0);
                    generalMvvm.getOnCartItemUpdated().setValue(productModel);

                }
            }

        });

        
        mvvm.getOnSingleOrderSentSuccess().observe(activity, cartSingleModel -> {
            generalMvvm.onOrderNavigate().setValue(0);
            generalMvvm.getActionFragmentHomeNavigator().setValue(1);

            generalMvvm.getOnCurrentOrderRefreshed().setValue(true);

            if (selectedOrderPos != -1) {
                manageCartModel.deleteMainCategory(selectedOrderPos, activity);
                generalMvvm.getOnCartRefreshed().setValue(true);
                for (CartModel.CartObject object : cartSingleModel.getCartList()){
                    for (ProductModel productModel :object.getProducts()){
                        productModel.setAmount(0);
                        generalMvvm.getOnCartItemUpdated().setValue(productModel);

                    }
                }
                this.selectedOrderPos = -1;

            }
        });

        adapter = new MainCartAdapter(activity, this, getLang());
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(adapter);
        binding.tvNoData.setText(R.string.empty_cart);
        binding.flOrderAll.setOnClickListener(v -> {
            type=2;
            if (getUserModel() != null) {
                binding.flExpectedTime.setVisibility(View.VISIBLE);

            } else {
                generalMvvm.getActionFragmentHomeNavigator().setValue(8);


            }
        });

        refreshCart();

        binding.btnConfirm.setOnClickListener(view -> {
            if (time != null && date != null) {
                expectedtime = date;
                manageCartModel.setDate(date);
                binding.flExpectedTime.setVisibility(View.GONE);
                try {
                    manageCartModel.setDelivery_date_time(dateFormat.parse(expectedtime).getTime() + "");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                manageCartModel.setDelivery_date_time_id(time_id);
                manageCartModel.setTime(time);
                if(type==2){
                manageCartModel.addUser(getUserModel().getData().getId(), activity);



                if (selectedAddress != null) {
                    manageCartModel.addAddress(selectedAddress.getId(), activity);
                    if (manageCartModel.getCartModel()!=null){
                        mvvm.sendAllOrder(activity,manageCartModel.getCartModel());

                    }
                }
                else {
                    generalMvvm.getMyAddressFragmentAction().setValue("forOrder");
                    generalMvvm.onHomeNavigate().setValue(7);

                }
            }
            else {
                    cartSingleModel.setDate(date);

                    try {
                        cartSingleModel.setDelivery_date_time(dateFormat.parse(expectedtime).getTime() + "");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    cartSingleModel.setDelivery_date_time_id(time_id);
                    cartSingleModel.setTime(time);
                    cartSingleModel.setUser_id(getUserModel().getData().getId());
                    if (selectedAddress != null) {
                        cartSingleModel.setAddress_id(selectedAddress.getId());
                        mvvm.sendSingleOrder(activity, cartSingleModel);
                    } else {
                        generalMvvm.getMyAddressFragmentAction().setValue("forOrder");
                        generalMvvm.onHomeNavigate().setValue(7);
                    }
                }
            }
            else {
                if (time == null) {
                    Toast.makeText(activity, getResources().getString(R.string.ch_time), Toast.LENGTH_LONG).show();
                    //binding.tvTime.setError(getResources().getString(R.string.field_required));
                } else {
                    // binding.tvTime.setError(null);

                }
                if (date == null) {
                    binding.tvDate.setError(getResources().getString(R.string.field_required));
                } else {
                    binding.tvDate.setError(null);

                }
            }

        });
        //binding.tvTime.setOnClickListener(view -> timePickerDialog.show(getSupportFragmentManager(), ""));
        binding.tvDate.setOnClickListener(view -> datePickerDialog.show(getParentFragmentManager(), ""));


        createDateDialog();
        createTimeDialog();

        mvvm.getTime(activity);
    }
    private void createDateDialog() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setAccentColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        datePickerDialog.setCancelColor(ActivityCompat.getColor(activity, R.color.grey4));
        datePickerDialog.setOkColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        datePickerDialog.setMinDate(calendar);
        datePickerDialog.setOkText(getString(R.string.select));
        datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);

    }

    private void createTimeDialog() {

        Calendar calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), true);
        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setAccentColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        timePickerDialog.setCancelColor(ActivityCompat.getColor(activity, R.color.grey4));
        timePickerDialog.setOkColor(ActivityCompat.getColor(activity, R.color.colorPrimary));

        // datePickerDialog.setOkText(getString(R.string.select));
        //datePickerDialog.setCancelText(getString(R.string.cancel));
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        //  timePickerDialog.setMinTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));

    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        time = dateFormat.format(new Date(calendar.getTimeInMillis()));
        //binding.tvTime.setText(time);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, monthOfYear);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        date = dateFormat.format(new Date(calendar.getTimeInMillis()));
        binding.tvDate.setText(date);
    }

    private void refreshCart() {
        if (manageCartModel.getCartList(activity).size() > 0) {
            if (manageCartModel.getCartList(activity).size() > 1) {
                binding.flOrderAll.setVisibility(View.VISIBLE);
            } else {
                binding.flOrderAll.setVisibility(View.GONE);

            }
            binding.tvNoData.setVisibility(View.GONE);
        } else {
            binding.tvNoData.setVisibility(View.VISIBLE);
            this.selectedAddress = null;
            manageCartModel.clear(activity);


        }
        adapter.updateList(manageCartModel.getCartList(activity));

    }

    public void addProductToCart(ProductModel productModel) {
        generalMvvm.getOnCartItemUpdated().setValue(productModel);
        manageCartModel.add(productModel, activity);
    }

    public void removeProductFromCart(ProductModel productModel) {
        productModel.setAmount(0);
        generalMvvm.getOnCartItemUpdated().setValue(productModel);
        manageCartModel.delete(productModel, activity);
        generalMvvm.getOnCartRefreshed().setValue(true);

    }

    public void sendSingleOrder(CartModel.CartObject cartObject, int adapterPosition) {
        type=1;
        selectedOrderPos = adapterPosition;
         cartSingleModel = new CartSingleModel();
        cartSingleModel.addItem(cartObject);
        if (getUserModel() != null) {

            binding.flExpectedTime.setVisibility(View.VISIBLE);

        } else {
            generalMvvm.getActionFragmentHomeNavigator().setValue(3);

        }


    }


}