package com.apps.dbrah.uis.activity_home.add_address_module;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.SpinnerTimeAdapter;
import com.apps.dbrah.databinding.FragmentAddAddressBinding;
import com.apps.dbrah.model.AddAddressModel;
import com.apps.dbrah.model.AddressModel;
import com.apps.dbrah.model.TimeModel;
import com.apps.dbrah.mvvm.FragmentAddAddressMvvm;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseActivity;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_base.FragmentMapTouchListener;
import com.apps.dbrah.uis.activity_home.HomeActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class FragmentAddAddress extends BaseFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private HomeActivity activity;
    private FragmentAddAddressBinding binding;
    private GeneralMvvm generalMvvm;
    private AddressModel addressModel;
    private AddAddressModel model;
    private SpinnerTimeAdapter spinnerTimeAdapter;
    private FragmentAddAddressMvvm fragmentAddAddressMvvm;
    private View view;
    private FragmentMapTouchListener fragmentMapTouchListener;
    private GoogleMap mMap;
    private Marker marker;
    private float zoom = 15.0f;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private double lat;
    private double lng;
    private String address = "";
    private ActivityResultLauncher<String> permission;

    public static FragmentAddAddress newInstance() {
        return new FragmentAddAddress();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        permission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                mMap.setMyLocationEnabled(true);
                initGoogleApi();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_address, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        model = new AddAddressModel();

        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);

        fragmentAddAddressMvvm = ViewModelProviders.of(activity).get(FragmentAddAddressMvvm.class);

        view = activity.setUpToolbar(binding.toolbar, getString(R.string.add_address), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);

        fragmentAddAddressMvvm.getOnDataSuccess().observe(this, new Observer<List<TimeModel>>() {
            @Override
            public void onChanged(List<TimeModel> timeModels) {

                if (spinnerTimeAdapter != null) {
                    spinnerTimeAdapter.updateList(timeModels);

                }


            }
        });

        generalMvvm.getOnAddressSelectedForUpdate().observe(activity, addressModel -> {
            this.addressModel = addressModel;
            model.setTitle(addressModel.getTitle());
            model.setAdmin_name(addressModel.getAdmin_name());
            model.setPhone(addressModel.getPhone());
            model.setTime_id(addressModel.getTime_id());
            model.setAddress(addressModel.getAddress());
            model.setLat(Double.parseDouble(addressModel.getLatitude()));
            model.setLng(Double.parseDouble(addressModel.getLongitude()));
            binding.setModel(model);
            view = activity.setUpToolbar(binding.toolbar, getString(R.string.upd_address), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
            binding.btnAdd.setText(getString(R.string.update));

        });

        view.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);

        });


        binding.setModel(model);

        spinnerTimeAdapter = new SpinnerTimeAdapter(activity);

        binding.spinner.setAdapter(spinnerTimeAdapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    model.setTime_id("");
                } else {
                    TimeModel timeModel = (TimeModel) parent.getSelectedItem();
                    model.setTime_id(timeModel.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fragmentAddAddressMvvm.getTime(activity);

        binding.btnAdd.setOnClickListener(v -> {
            if (getUserModel() != null) {
                generalMvvm.onHomeBackNavigate().setValue(true);
                model = new AddAddressModel();
                binding.setModel(model);
            }

        });

        setUpMapFragment();
    }

    private void setUpMapFragment() {
        fragmentMapTouchListener = (FragmentMapTouchListener) getChildFragmentManager().findFragmentById(R.id.map);
        if (fragmentMapTouchListener != null) {
            fragmentMapTouchListener.getMapAsync(this);

        }
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(activity, BaseActivity.FINELOCPerm) != PackageManager.PERMISSION_GRANTED) {
            permission.launch(BaseActivity.FINELOCPerm);
        } else {
            mMap.setMyLocationEnabled(true);
            initGoogleApi();
        }
    }

    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (addressModel == null) {
            googleApiClient.connect();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            fragmentMapTouchListener.setListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(true));

            checkPermission();

            if (addressModel != null) {
                lat = Double.parseDouble(addressModel.getLatitude());
                lng = Double.parseDouble(addressModel.getLongitude());
                address = addressModel.getAddress();
                addMarker(new LatLng(lat, lng));
            }

            mMap.setOnMapClickListener(latLng -> {
                addMarker(latLng);
                getAddress(latLng.latitude, latLng.longitude);
            });
        }
    }

    private void addMarker(LatLng latLng) {
        model.setLat(latLng.latitude);
        model.setLng(latLng.longitude);
        if (marker == null) {
            marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        } else {
            marker.setPosition(latLng);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(locationSettingsResult -> {
            Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    startLocationUpdate();
                    break;

                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(activity, 100);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        });


    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(activity)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        getAddress(lat, lng);
        addMarker(new LatLng(lat, lng));
        if (googleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            startLocationUpdate();
        }
    }

    private void getAddress(double lat, double lng) {
        Geocoder geocoder;
        List<Address> addressList = new ArrayList<>();
        geocoder = new Geocoder(activity, Locale.ENGLISH);
        try {
            addressList = geocoder.getFromLocation(lat, lng, 1);
            if (addressList.size() > 0) {
                Address address = addressList.get(0);
                if (address != null) {
                    this.address = address.getAddressLine(0) + "-" + address.getLocality() + "-" + address.getCountryName();
                } else {
                    this.address = "UnKnown";
                }
            } else {
                address = "UnKnown";
            }

            binding.setAddress(address);
            model.setAddress(address);
            model.setLng(lat);
            model.setLng(lng);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}