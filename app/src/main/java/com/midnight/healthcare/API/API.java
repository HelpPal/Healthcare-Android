package com.midnight.healthcare.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by TUSK.ONE on 8/29/16.
 */
public interface API {
    @FormUrlEncoded
    @POST("deletePushAndroid.php")
    Call<Void> deletePushAndroid(@Field("data") String data);

    @FormUrlEncoded
    @POST("pusAndroid.php")
    Call<Void> pusAndroid(@Field("data") String data);

    @FormUrlEncoded
    @POST("sendJobTouser.php")
    Call<Void> sendJobTouser(@Field("data") String data);

    @FormUrlEncoded
    @POST("sendmessage.php")
    Call<Void> sendmessage(@Field("data") String data);

    @FormUrlEncoded
    @POST("jobsfeed.php")
    Call<JobResponse> jobsfeed(@Field("data") String data);

    @FormUrlEncoded
    @POST("myjobs.php")
    Call<JobResponse> myjobs(@Field("data") String data);

    @FormUrlEncoded
    @POST("skills.php")
    Call<SkillsResponse> getSkills(@Field("data") String data);

    @FormUrlEncoded
    @POST("terms.php")
    Call<Void> getTerms(@Field("data") String data);

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(@Field("data") String data);

    @FormUrlEncoded
    @POST("recover.php")
    Call<Void> recover(@Field("data") String data);

    @FormUrlEncoded
    @POST("editjob.php")
    Call<Void> editjob(@Field("data") String data);

    @FormUrlEncoded
    @POST("editprofile.php")
    Call<LoginResponse> editprofile(@Field("data") String data);

    @FormUrlEncoded
    @POST("deletejob.php")
    Call<Void> deletejob(@Field("data") String data);

    @FormUrlEncoded
    @POST("addjob.php")
    Call<Void> addjob(@Field("data") String data);

    @FormUrlEncoded
    @POST("addjob.php")
    Call<Jobs> addjobSpecial(@Field("data") String data);

    @FormUrlEncoded
    @POST("register.php")
    Call<Void> register(@Field("data") String data);

    @FormUrlEncoded
    @POST("nurse.php")
    Call<NurseResponse> cnafeed(@Field("data") String data);

    @FormUrlEncoded
    @POST("applications.php")
    Call<ApplicationsIndResponse> applications(@Field("data") String data);

    @FormUrlEncoded
    @POST("myapplications.php")
    Call<ApplicationsIndResponse> myapplications(@Field("data") String data);

    @FormUrlEncoded
    @POST("messages.php")
    Call<MessegesResponse> messages(@Field("data") String data);

    @FormUrlEncoded
    @POST("conversation.php")
    Call<ConversationResponse> conversation(@Field("data") String data);

    @FormUrlEncoded
    @POST("applicationAccept.php")
    Call<Void> applicationAccept(@Field("data") String data);

    @FormUrlEncoded
    @POST("applicationRefuse.php")
    Call<Void> applicationRefuse(@Field("data") String data);

    @FormUrlEncoded
    @POST("setAvalable.php")
    Call<Void> setAvalable(@Field("data") String data);

    @FormUrlEncoded
    @POST("changepassword.php")
    Call<passResponse> changepass(@Field("data") String data);

    @FormUrlEncoded
    @POST("checkCanApplyToJob.php")
    Call<CheckResponse> checkCanApply(@Field("data") String data);

    @FormUrlEncoded
    @POST("reportUser.php")
    Call<Void> reportUser(@Field("data") String data);

    @FormUrlEncoded
    @POST("report.php")
    Call<Void> reportJob(@Field("data") String data);

    @FormUrlEncoded
    @POST("deleteconversation.php")
    Call<Void> deleteConversation(@Field("data") String data);

    @FormUrlEncoded
    @POST("check_email.php")
    Call<passResponse> checkEmail(@Field("data") String data);

    @FormUrlEncoded
    @POST("applicationRemove.php")
    Call<Void> applicationRemove(@Field("data") String data);

    @FormUrlEncoded
    @POST("profile.php")
    Call<ProfileResponse> profile(@Field("data") String data);

    @FormUrlEncoded
    @POST("checksubscribed.php")
    Call<SubscribeResponse> checksubscribe(@Field("data") String data);

    @FormUrlEncoded
    @POST("subscribed.php")
    Call<SubscribeAddResponse> subscribe(@Field("data") String data);

    @FormUrlEncoded
    @POST("addRating.php")
    Call<Void> rate(@Field("data") String data);

}
