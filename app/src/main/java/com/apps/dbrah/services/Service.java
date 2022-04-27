package com.apps.dbrah.services;


import com.apps.dbrah.model.AddressesDataModel;
import com.apps.dbrah.model.CategoryDataModel;
import com.apps.dbrah.model.MessagesDataModel;
import com.apps.dbrah.model.OrdersModel;
import com.apps.dbrah.model.RecentProductDataModel;
import com.apps.dbrah.model.NotificationDataModel;
import com.apps.dbrah.model.PlaceGeocodeData;
import com.apps.dbrah.model.SearchHomeDataModel;
import com.apps.dbrah.model.SettingDataModel;
import com.apps.dbrah.model.SingleAddressData;
import com.apps.dbrah.model.SingleMessageModel;
import com.apps.dbrah.model.SingleOfferModel;
import com.apps.dbrah.model.SingleOrderDataModel;
import com.apps.dbrah.model.SingleProductModel;
import com.apps.dbrah.model.SliderDataModel;
import com.apps.dbrah.model.StatusResponse;
import com.apps.dbrah.model.TimeDataModel;
import com.apps.dbrah.model.UserModel;
import com.apps.dbrah.model.cart_models.CartModel;
import com.apps.dbrah.model.cart_models.CartResponse;
import com.apps.dbrah.model.cart_models.CartSingleModel;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
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
    Single<Response<RecentProductDataModel>> getRecentProduct(@Query("user_id") String user_id);

    @GET("api/mostSales")
    Single<Response<RecentProductDataModel>> getMostSaleProduct(@Query("user_id") String user_id);


    @GET("api/search")
    Single<Response<RecentProductDataModel>> searchByCatProduct(@Query("user_id") String user_id,
                                                                @Query("category_id") String category_id,
                                                                @Query("sub_category_id") String sub_category_id,
                                                                @Query("search_word") String search_word);

    @GET("api/home_search")
    Single<Response<SearchHomeDataModel>> searchProduct(@Query("user_id") String user_id,
                                                        @Query("search_word") String search_word);


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

    @Multipart
    @POST("api/editProfile")
    Observable<Response<UserModel>> updateProfile(@Part("user_id") RequestBody user_id,
                                                  @Part("email") RequestBody email,
                                                  @Part MultipartBody.Part logo


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
    Single<Response<SingleProductModel>> getSingleProduct(@Query("user_id") String user_id,
                                                          @Query("id") String id);

    @GET("api/delivery_times")
    Single<Response<TimeDataModel>> getTime();

    @GET("api/address")
    Single<Response<AddressesDataModel>> getMyAddresses(@Query("user_id") String user_id);


    @FormUrlEncoded
    @POST("api/storeAddress")
    Single<Response<SingleAddressData>> addAddress(@Field("user_id") String user_id,
                                                   @Field("time_id") String time_id,
                                                   @Field("title") String title,
                                                   @Field("admin_name") String admin_name,
                                                   @Field("phone_code") String phone_code,
                                                   @Field("phone") String phone,
                                                   @Field("address") String address,
                                                   @Field("latitude") double latitude,
                                                   @Field("longitude") double longitude);

    @POST("api/storeOrder")
    Single<Response<CartResponse>> sendAllOrder(@Body CartModel cartModel);

    @POST("api/storeOrder")
    Single<Response<CartResponse>> sendSingleOrder(@Body CartSingleModel cartModel);

    @GET("api/getOrders")
    Single<Response<OrdersModel>> getOrders(@Query("user_id") String user_id);

    @GET("api/orderDetails")
    Single<Response<SingleOrderDataModel>> getOrderDetails(@Query("order_id") String order_id);

    @GET("api/offerDetails")
    Single<Response<SingleOfferModel>> getOfferDetails(@Query("offer_id") String offer_id);

    @FormUrlEncoded
    @POST("api/storeToList")
    Single<Response<StatusResponse>> favUnFav(@Field("user_id") String user_id,
                                              @Field("product_id") String product_id
    );

    @GET("api/myList")
    Single<Response<RecentProductDataModel>> getMyList(@Query("user_id") String user_id);


    @FormUrlEncoded
    @POST("api/updateOfferStatus")
    Single<Response<StatusResponse>> acceptRefuseOffer(@Field("offer_id") String offer_id,
                                                       @Field("status") String status);

    @GET("api/setting")
    Single<Response<SettingDataModel>> getSettings();

    @GET("api/getChat")
    Single<Response<MessagesDataModel>> getChatMessages(@Query(value = "order_id") String order_id);

    @Multipart
    @POST("api/storeMessage")
    Single<Response<SingleMessageModel>> sendMessages(@Part("order_id") RequestBody order_id,
                                                      @Part("from_type") RequestBody from_type,
                                                      @Part("type") RequestBody type,
                                                      @Part("message") RequestBody message,
                                                      @Part MultipartBody.Part image
    );


    @FormUrlEncoded
    @POST("api/updateOrderStatus")
    Single<Response<StatusResponse>> updateOrderStatus(@Field("order_id") String order_id,
                                                       @Field("status") String status
    );

    @FormUrlEncoded
    @POST("api/storeToken")
    Single<Response<StatusResponse>> updateFireBaseToken(@Field("user_id") String user_id,
                                                         @Field("token") String token,
                                                         @Field("type") String type

    );

    @FormUrlEncoded
    @POST("api/logout")
    Single<Response<StatusResponse>> logout(@Field("user_id") String user_id,
                                            @Field("token") String token
    );

}