package com.app.dbrah.services;


import com.app.dbrah.model.AddressesDataModel;
import com.app.dbrah.model.CategoryDataModel;
import com.app.dbrah.model.MessagesDataModel;
import com.app.dbrah.model.OrderDataModel;
import com.app.dbrah.model.OrdersModel;
import com.app.dbrah.model.RecentProductDataModel;
import com.app.dbrah.model.NotificationDataModel;
import com.app.dbrah.model.PlaceGeocodeData;
import com.app.dbrah.model.SearchHomeDataModel;
import com.app.dbrah.model.SettingDataModel;
import com.app.dbrah.model.SingleAddressData;
import com.app.dbrah.model.SingleMessageModel;
import com.app.dbrah.model.SingleOfferModel;
import com.app.dbrah.model.SingleOrderDataModel;
import com.app.dbrah.model.SingleProductModel;
import com.app.dbrah.model.SliderDataModel;
import com.app.dbrah.model.StatusResponse;
import com.app.dbrah.model.TimeDataModel;
import com.app.dbrah.model.UserModel;
import com.app.dbrah.model.cart_models.CartModel;
import com.app.dbrah.model.cart_models.CartResponse;
import com.app.dbrah.model.cart_models.CartSingleModel;

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
                                                                @Query("sub_sub_category_id") String sub_sub_category_id,
                                                                @Query("search_word") String search_word);

    @GET("api/home_search")
    Single<Response<SearchHomeDataModel>> searchProduct(@Query("user_id") String user_id,
                                                        @Query("search_word") String search_word);


    @FormUrlEncoded
    @POST("api/contact_us")
    Single<Response<StatusResponse>> contactUs(@Field("name") String name,
                                               @Field("email") String email,
                                               @Field("subject") String subject,
                                               @Field("message") String message,
                                               @Field("order_id") String order_id


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
    @POST("api/auth/check/email")
    Single<Response<StatusResponse>> sendcode(@Field("email") String email
    );
    @FormUrlEncoded
    @POST("api/auth/check/code")
    Single<Response<StatusResponse>> checkCode(@Field("email") String email,
                                          @Field("code") String code
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
    Single<Response<MessagesDataModel>> getChatMessages(@Query("order_id") String order_id,
                                                        @Query("representative_id") String representative_id,
                                                        @Query("user_id") String user_id,
                                                        @Query("provider_id") String provider_id);

    @Multipart
    @POST("api/storeMessage")
    Single<Response<SingleMessageModel>> sendMessages(@Part("order_id") RequestBody order_id,
                                                      @Part("type") RequestBody type,
                                                      @Part("from_type") RequestBody from,
                                                      @Part("message") RequestBody message,
                                                      @Part("representative_id") RequestBody representative_id,
                                                      @Part("user_id") RequestBody user_id,
                                                      @Part("provider_id") RequestBody provider_id,
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

    @GET("api/getNotifications")
    Single<Response<NotificationDataModel>> getNotifications(@Query(value = "user_id") String user_id);

    @FormUrlEncoded
    @POST("api/storeRate")
    Single<Response<StatusResponse>> addRate(@Field("provider_id") String provider_id,
                                             @Field("user_id") String user_id,
                                             @Field("order_id") String order_id,
                                             @Field("rate") float rate,
                                             @Field("text") String text
    );
    @GET("api/searchOrders")
    Single<Response<OrderDataModel>> searchOrders(@Query("user_id") String user_id,
                                                  @Query("search_key") String search_key);

    @GET("api/sub_sub_categories")
    Single<Response<CategoryDataModel>> getSubSubCategory(@Query("sub_category_id") String sub_category_id);
}