package com.app.dbrah.uis.activity_home.add_address_module;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.dbrah.R;
import com.app.dbrah.adapter.SpinnerTimeAdapter;
import com.app.dbrah.databinding.FragmentAddAddressBinding;
import com.app.dbrah.model.AddAddressModel;
import com.app.dbrah.model.AddressModel;
import com.app.dbrah.model.SelectedLocation;
import com.app.dbrah.model.TimeModel;
import com.app.dbrah.mvvm.FragmentAddAddressMvvm;
import com.app.dbrah.mvvm.GeneralMvvm;
import com.app.dbrah.uis.activity_base.BaseActivity;
import com.app.dbrah.uis.activity_base.BaseFragment;
import com.app.dbrah.uis.activity_base.FragmentMapTouchListener;
import com.app.dbrah.uis.activity_home.HomeActivity;
import com.app.dbrah.uis.activity_map.MapActivity;
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
    private FragmentAddAddressMvvm mvvm;
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
    private ActivityResultLauncher<Intent> launcher;
    private int req;

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
                mMap.setMyLocationEnabled(false);
                initGoogleApi();
            }
        });
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                SelectedLocation location = (SelectedLocation) result.getData().getSerializableExtra("location");
                address = location.getAddress();
                lat = location.getLat();
                lng = location.getLng();
                binding.setAddress(address);
                model.setAddress(address);
                model.setLng(lat);
                model.setLng(lng);
//                UserSettingsModel userSettingsModel = getUserSettings();
//                userSettingsModel.setLocation(location);
//                setUserSettings(userSettingsModel);
//                activityHomeGeneralMvvm.updateLocation(userSettingsModel);
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
        mvvm = ViewModelProviders.of(activity).get(FragmentAddAddressMvvm.class);

        model = new AddAddressModel();

        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        generalMvvm.getOnAddressEdit().observe(activity, new Observer<AddressModel>() {
            @Override
            public void onChanged(AddressModel addressModel) {
                setdate(addressModel);
            }
        });
        generalMvvm.getOnLoggedOutSuccess().observe(this, loggedOut -> {
            if (loggedOut) {
                if (googleApiClient != null) {
                    LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(locationCallback);
                    googleApiClient.disconnect();
                    googleApiClient = null;
                }

            }
        });

        generalMvvm.getOnUserLoggedIn().observe(this, loggedIn -> {
            if (loggedIn) {
                mvvm.getTime(activity);
                setUpMapFragment();
            }
        });

        view = activity.setUpToolbar(binding.toolbar, getString(R.string.add_address), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);

        mvvm.getOnDataSuccess().observe(this, timeModels -> {

            if (spinnerTimeAdapter != null) {
                if (model.getTime_id() != null) {
                    Log.e("fllflfl", model.getTime_id());
                    setselection();
                }
                spinnerTimeAdapter.updateList(timeModels);

            }


        });
        mvvm.getOnAddressAdded().observe(activity, addressModel -> {
            generalMvvm.getOnAddressAdded().setValue(addressModel);
            generalMvvm.onHomeBackNavigate().setValue(true);
            model = new AddAddressModel();
            binding.setModel(model);
        });
        mvvm.getOnAddressUpdated().observe(activity, addressModel -> {
            this.addressModel = null;
            generalMvvm.getOnAddressUpdated().setValue(addressModel);
            model = new AddAddressModel();
            binding.setModel(model);
            generalMvvm.onHomeBackNavigate().setValue(true);

        });


        generalMvvm.getAddAddressFragmentAction().observe(activity, action -> {
            if (action.equals("add")) {
                address = "";
                lat = 0.0;
                lng = 0.0;
                binding.setAddress(address);
                model = null;
                addressModel = null;
                model = new AddAddressModel();
                binding.spinner.setSelection(0);
                binding.setModel(model);
                checkPermission();
            }
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
                //

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


        binding.btnAdd.setOnClickListener(v -> {
            if (getUserModel() != null) {
                if (addressModel == null) {
                    mvvm.addAddress(getUserModel(), model, activity);

                } else {
                    Log.e("s;s;;s;","S;s;s;s;");
                    model.setId(addressModel.getId());
                    mvvm.updateAddress(getUserModel(), model, activity);

                }

            }

        });

        if (getUserModel() != null) {
            mvvm.getTime(activity);
            setUpMapFragment();
        }
        binding.tvaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                req = 1;
                Intent intent = new Intent(activity, MapActivity.class);

                launcher.launch(intent);

            }
        });
        binding.cardAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                req = 1;
                Intent intent = new Intent(activity, MapActivity.class);

                launcher.launch(intent);

            }
        });

    }

    private void setdate(AddressModel addressModel) {
        this.addressModel = addressModel;
        // Log.e(";;s;;s",this.addressModel.getAddress());
        model.setAdmin_name(addressModel.getAdmin_name());
        model.setTitle(addressModel.getTitle());
        model.setPhone(addressModel.getPhone());
        model.setAddress(addressModel.getAddress());
        model.setTime_id(addressModel.getTime_id());
        binding.btnAdd.setText(activity.getResources().getString(R.string.edit));
        binding.toolbar.tvTitle.setText(activity.getResources().getString(R.string.edit_address));
        getAddress(Double.parseDouble(addressModel.getLatitude()), Double.parseDouble(addressModel.getLongitude()));
   if(mvvm.getOnDataSuccess().getValue()!=null){
       setselection();
   }
    }

    private void setselection() {
        // Log.e("D'd;d;;", "D;d;d;d;");
        Log.e("e;elelel", mvvm.getOnDataSuccess().getValue().size()+"");
        for (int i = 0; i < mvvm.getOnDataSuccess().getValue().size(); i++) {
            Log.e("ldlldldl", model.getTime_id());
            // Log.e("ldlldldl", mvvm.getOnDataSuccess().getValue().get(i).getId());
            if (model.getTime_id().equals(mvvm.getOnDataSuccess().getValue().get(i).getId())) {
                binding.spinner.setSelection(i );
                return;
            }
        }
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
            mMap.setMyLocationEnabled(false);
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
                req = 1;
                Intent intent = new Intent(activity, MapActivity.class);

                launcher.launch(intent);
//                addMarker(latLng);
//                getAddress(latLng.latitude, latLng.longitude);
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