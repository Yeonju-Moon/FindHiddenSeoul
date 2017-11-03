package com.seoulapp.findhiddenseoul;

import com.seoulapp.findhiddenseoul.ListViewItem.ListViewItem;
import com.seoulapp.findhiddenseoul.ListViewItem.ListViewItem2;
import com.seoulapp.findhiddenseoul.Model.Find;
import com.seoulapp.findhiddenseoul.Model.Rank;
import com.seoulapp.findhiddenseoul.Model.Review;
import com.seoulapp.findhiddenseoul.Model.Token;
import com.seoulapp.findhiddenseoul.Model.UserInfo;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by HOME on 2017-10-12.
 */
public interface NetworkService {

    /*** LOGIN  ***/

    @POST("/login")
    retrofit2.Call<UserInfo> post_login(@Body UserInfo userInfo);

    @POST("/get_main_data")
        // 스플래시, 로그인 화면
    retrofit2.Call<Token> post_info(@Body Token token);

    @POST("/get_user_data/user")
    retrofit2.Call<UserInfo> post_user(@Body UserInfo userInfo);

    @POST("/get_ranking")
    retrofit2.Call<List<ListViewItem2>> post_rank(@Body Rank rank);


    /*** SIGN UP ***/

    @POST("/signup")
    Call<UserInfo> post_signup(@Body UserInfo userInfo);

    @POST("/user_check/id_check")
    Call<UserInfo> post_idCheck(@Body UserInfo userInfo);

    @POST("user_check/nick_check")
    Call<UserInfo> post_nickCheck(@Body UserInfo userInfo);

    @POST("user_check/phone_check")
    Call<UserInfo> post_phoneCheck(@Body UserInfo userInfo);


    /**** FIND ID, PW ****/

    @POST("/find_user/find_id")
    retrofit2.Call<Find> post_find_id(@Body Find find);

    @POST("/find_user/find_pw")
    retrofit2.Call<Find> post_find_pw(@Body Find find);

    @POST("/find_user/find_pw2")
    retrofit2.Call<Find> post_change_pw(@Body Find find);


    /*** Heritage, My page **/

    // Heritage List 받아옴 - (H_ListActivity.java)
    @GET("/get_data/gu")
    Call<List<ListViewItem>> getGu(@Query("gu") String gu);

    //해당 Heritage 설명 - (H_DetailActivity.java)
    @GET("/get_data/detail")
    Call<ResponseBody> getDetail(@Query("heritage_index") String heritage_index);

    //해당 Heritage 이미지들 - (H_DetailActivity.java)
    @GET("/get_data/detail_img")
    Call<ResponseBody> getDetail_img(@Query("heritage_index") String heritage_index);

    //해당 Heritage 이미지 총 개수 - (H_DetailActivity.java)
    @GET("/get_data/img_count")
    Call<ResponseBody> getImg_count(@Query("heritage_index") String heritage_index);

    //해당 Heritage의 리뷰 - (H_DetailActivity.java)
    @GET("/get_data/heritage_review")
    Call<ResponseBody> getHeritage_review(@Query("heritage_index") String heritage_index);

    //Heritage 좋아요 보내기 - (H_DetailActivity.java)
    @POST("/get_data/like")
    Call<UserInfo> post_like(@Body UserInfo userInfo);

    //마이페이지 좋아요 최신 2개 리스트 - (MyPageActivity.java)
    @POST("/get_mypage_data/mylike")
    Call<List<ListViewItem>> get_user_like_recent(@Body UserInfo userInfo);

    //마이페이지 리뷰 최신 2개 리스트 - (MyPageActivity.java)
    @POST("/get_mypage_data/myreview_recent")
    Call<List<ListViewItem>> get_user_reivew_recent(@Body UserInfo userInfo);

    //마이페이지 좋아요 더보기 페이지 리스트 - (MyPageListActivity.java)
    @POST("/get_mypage_data/more_liked")
    Call<List<ListViewItem>> get_user_like(@Body UserInfo userInfo);

    //마이페이지 리뷰 더보기 페이지 리스트 - (MyPageListActivity.java)
    @POST("/get_mypage_data/myreview")
    Call<List<ListViewItem>> get_user_review(@Body UserInfo userInfo);

    //리뷰 추가 가능 여부 확인 - (H_DetailActivity.java)
    @POST("/get_data/review_check")
    Call<UserInfo> get_review_check(@Body UserInfo userInfo);

    //유산 디테일 페이지 실행되자마자 받아오는 좋아요 상태
    @POST("/get_data/like_check")
    Call<UserInfo> getLike_status(@Body UserInfo userInfo);

    //Heritage 좋아요 클릭시마다 보내기 - (H_DetailActivity.java)
    @POST("/get_data/like")
    Call<UserInfo> postClicked_like(@Body UserInfo userInfo);

    /*****  REVIEW ******/

    // create all review
    @Multipart
    @POST("fileupload")
    Call<Review> getCreateReview(@PartMap Map<String, RequestBody> params);

    // create review no image file
    @POST("fileupload")
    Call<Review> getCreateReviewNoImage(@Body Review review);

    // read review
    @POST("get_data/review")
    Call<List<Review>> getReadReview(@Body Review review);

    // update all review
    @Multipart
    @POST("review/modify")
    Call<Review> getModifyAllReview(@PartMap Map<String, RequestBody> params);

    // update only review_text
    @POST("review/modify")
    Call<Review> getModifyOnlyReviewContent(@Body Review review);

    // delete review
    @POST("review/delete")
    Call<Review> getDeleteReview(@Body Review review);
}
