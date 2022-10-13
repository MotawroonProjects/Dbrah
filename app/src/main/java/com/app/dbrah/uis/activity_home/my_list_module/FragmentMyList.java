package com.app.dbrah.uis.activity_home.my_list_module;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.dbrah.R;
import com.app.dbrah.adapter.FavoriteAdapter;
import com.app.dbrah.adapter.MainProductCategoryAdapter;
import com.app.dbrah.adapter.SubProductCategoryAdapter;
import com.app.dbrah.databinding.FragmentMyListBinding;
import com.app.dbrah.databinding.FragmentNotificationBinding;
import com.app.dbrah.databinding.FragmentProductsBinding;
import com.app.dbrah.model.ProductAmount;
import com.app.dbrah.model.ProductModel;
import com.app.dbrah.model.cart_models.ManageCartModel;
import com.app.dbrah.mvvm.FragmentMyListMvvm;
import com.app.dbrah.mvvm.FragmentProductsMvvm;
import com.app.dbrah.mvvm.GeneralMvvm;
import com.app.dbrah.uis.activity_base.BaseFragment;
import com.app.dbrah.uis.activity_home.HomeActivity;


public class FragmentMyList extends BaseFragment {
    private HomeActivity activity;
    private FragmentMyListBinding binding;
    private GeneralMvvm generalMvvm;
    private FragmentMyListMvvm mvvm;
    private FavoriteAdapter adapter;
    private ManageCartModel manageCartModel;
    private int removePos = -1;


    public static FragmentMyList newInstance() {
        return new FragmentMyList();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        mvvm = ViewModelProviders.of(this).get(FragmentMyListMvvm.class);
        manageCartModel = ManageCartModel.newInstance();

        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        View view = activity.setUpToolbar(binding.toolbar, getString(R.string.my_list), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
        view.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);

        });

        generalMvvm.getOnUserLoggedIn().observe(activity, loggedIn -> {
            if (loggedIn) {
                if (getUserModel() != null) {
                    mvvm.getMyList(activity,getUserModel());
                }

            }
        });

        generalMvvm.getOnLoggedOutSuccess().observe(activity, loggedOut -> {
            if (loggedOut) {
                mvvm.getMyList(activity,null);


            }
        });


        generalMvvm.getOnCartItemUpdated().observe(activity, productModel -> {
            int productPos = getProductPos(productModel.getId());
            if (productPos != -1) {
                if (mvvm.getOnDataSuccess().getValue() != null) {
                    mvvm.getOnDataSuccess().getValue().get(productPos).setAmount(productModel.getAmount());
                    if (adapter != null) {
                        adapter.notifyItemChanged(productPos);
                    }
                }
            }

        });

        generalMvvm.getOnCartMyListRefreshed().observe(activity, isRefreshed -> {
            mvvm.getMyList(activity, getUserModel());

        });

        generalMvvm.getOnProductItemUpdated().observe(activity, productModel -> {
            int productPos = getProductPos(productModel.getId());
            if (productModel.getIs_list().equals("true")) {
                if (productPos == -1) {
                    if (mvvm.getOnDataSuccess().getValue() != null) {
                        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
                        mvvm.getOnDataSuccess().getValue().add(0, productModel);
                        if (adapter != null) {
                            //productAdapter.updateItem(productModel, productPos);
                            adapter.notifyItemInserted(productPos);

                        }
                    }
                }
            } else {

                if (productPos != -1) {
                    if (mvvm.getOnDataSuccess().getValue() != null) {
                        mvvm.getOnDataSuccess().getValue().remove(productModel);
                        if (adapter != null) {
                            adapter.notifyItemRemoved(productPos);

                            if (mvvm.getOnDataSuccess().getValue() != null && mvvm.getOnDataSuccess().getValue().size() > 0) {
                                binding.recViewLayout.tvNoData.setVisibility(View.GONE);

                            } else {
                                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

                            }

                        }
                    }
                }

            }


        });

        binding.recViewLayout.tvNoData.setText(getString(R.string.no_products_to_show));

        mvvm.getIsLoading().observe(activity, isLoading -> {
            binding.recViewLayout.tvNoData.setVisibility(View.GONE);
            binding.recViewLayout.swipeRefresh.setRefreshing(isLoading);
        });

        mvvm.getOnDataSuccess().observe(activity, list -> {
            if (list.size() > 0) {
                binding.recViewLayout.tvNoData.setVisibility(View.GONE);

            } else {
                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

            }
            adapter.updateList(list);
        });

        mvvm.getOnFavUnFavSuccess().observe(activity, model -> {
            model.setIs_list("false");
            generalMvvm.getOnProductItemUpdated().setValue(model);
            if (mvvm.getOnDataSuccess().getValue() != null && mvvm.getOnDataSuccess().getValue().size() > 0) {
                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
            } else {
                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

            }
            if (adapter != null && removePos != -1) {
                adapter.notifyItemRemoved(removePos);
                removePos = -1;
            }
        });

        adapter = new FavoriteAdapter(activity, this, getLang());
        binding.recViewLayout.recView.setLayoutManager(new GridLayoutManager(activity, 2));
        binding.recViewLayout.recView.setAdapter(adapter);

        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(() -> mvvm.getMyList(activity, getUserModel()));


        mvvm.getMyList(activity, getUserModel());

    }

    public void showProductDetails(ProductModel productModel) {
        ProductAmount productAmount = new ProductAmount(productModel.getId(), productModel.getAmount());
        generalMvvm.getProductAmount().setValue(productAmount);
        generalMvvm.onHomeNavigate().setValue(6);
    }

    public void addProductToCart(ProductModel productModel) {
        generalMvvm.getOnCartItemUpdated().setValue(productModel);
        manageCartModel.add(productModel, activity);
        generalMvvm.getOnCartRefreshed().setValue(true);
        Toast.makeText(activity, R.string.added_cart_suc, Toast.LENGTH_LONG).show();


    }

    public void removeProductFromCart(ProductModel productModel) {
        productModel.setAmount(0);
        generalMvvm.getOnCartItemUpdated().setValue(productModel);
        manageCartModel.delete(productModel, activity);
        generalMvvm.getOnCartRefreshed().setValue(true);

    }

    public void favUnFav(ProductModel productModel, int adapterPosition) {
        removePos = adapterPosition;
        mvvm.favUnFav(getUserModel(), productModel, adapterPosition);
    }

    private int getProductPos(String product_id) {
        if (mvvm.getOnDataSuccess().getValue() != null) {
            for (int index = 0; index < mvvm.getOnDataSuccess().getValue().size(); index++) {
                if (mvvm.getOnDataSuccess().getValue().get(index).getId().equals(product_id)) {
                    return index;
                }
            }
        }

        return -1;
    }
}