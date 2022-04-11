package com.apps.dbrah.services;


import com.apps.dbrah.model.CategoryDataModel;
import com.apps.dbrah.model.RecentProductDataModel;
import com.apps.dbrah.model.NotificationDataModel;
import com.apps.dbrah.model.PlaceGeocodeData;
import com.apps.dbrah.model.SearchHomeDataModel;
import com.apps.dbrah.model.SingleProductModel;
import com.apps.dbrah.model.SliderDataModel;
import com.apps.dbrah.model.StatusResponse;
import com.apps.dbrah.model.UserModel;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @GET("api/sliders")
    Single<Response<SliderDataModel>> getSlider();


    @GET("api/main_categories")
    Single<Response<CategoryDataModel>> getCategory();

    @GET("api/sub_categories")
    Single<Response<CategoryDataModel>> getSubCategory(@Query("category_id") String category_id);

    @GET("api/latest_products")
    Single<Response<RecentProductDataModel>> getRecentProduct();

    @GET("api/mostSales")
    Single<Response<RecentProductDataModel>> getMostSaleProduct();



    @GET("api/search")
    Single<Response<RecentProductDataModel>> searchByCatProduct(@Query("category_id") String category_id,
                                                                @Query("sub_category_id") String sub_category_id,
                                                                @Query("search_word") String search_word);

    @GET("api/home_search")
    Single<Response<SearchHomeDataModel>> searchProduct(@Query("search_word") String search_word);


    @FormUrlEncoded
    @POST("api/contact_us")
    Single<Response<StatusResponse>> contactUs(@Field("name") String name,
                                               @Field("email") String email,
                                               @Field("subject") String subject,
                                               @Field("message") String message


    );

    @GET("geocode/json")
    Single<Response<PlaceGeocodeData>> getGeoData(@Query(value = "latlng") String latlng,
                                                  @Query(value = "language") String language,
                                                  @Query(value = "key") String key);





    @FormUrlEncoded
    @POST("api/login")
    Single<Response<UserModel>> login(@Field("phone_code") String phone_code,
                                      @Field("phone") String phone
    );
    @Multipart
    @POST("api/register")
    Observable<Response<UserModel>> signUpwithImage(@Part("name") RequestBody name,
                                                    @Part("phone_code") RequestBody phone_code,
                                                    @Part("phone") RequestBody phone,
                                                    @Part("email") RequestBody email,
                                                    @Part("vat_number") RequestBody vat_number,
                                                    @Part MultipartBody.Part logo


    );


    @FormUrlEncoded
    @POST("api/logout")
    Single<Response<StatusResponse>> logout(@Header("AUTHORIZATION") String token,
                                            @Field("api_key") String api_key,
                                            @Field("phone_token") String phone_token


    );

    @FormUrlEncoded
    @POST("api/firebase-tokens")
    Single<Response<StatusResponse>> updateFirebasetoken(@Header("AUTHORIZATION") String token,
                                                         @Field("api_key") String api_key,
                                                         @Field("phone_token") String phone_token,
                                                         @Field("user_id") String user_id,
                                                         @Field("software_type") String software_type


    );

    @FormUrlEncoded
    @POST("api/contact-us")
    Single<Response<StatusResponse>> contactUs(@Field("api_key") String api_key,
                                               @Field("name") String name,
                                               @Field("email") String email,
                                               @Field("subject") String phone,
                                               @Field("message") String message


    );


    @GET("api/notifications")
    Single<Response<NotificationDataModel>> getNotifications(@Header("AUTHORIZATION") String token,
                                                             @Query(value = "api_key") String api_key,
                                                             @Query(value = "user_id") String user_id
    );

    @GET("api/product_details")
    Single<Response<SingleProductModel>> getSingleProduct(@Query("id") String id);


}