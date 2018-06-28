package com.midnight.healthcare;

import android.app.Application;

import com.midnight.healthcare.API.Jobs;
import com.midnight.healthcare.API.User;
import com.midnight.healthcare.Models.RegisterModel;

import java.util.List;

/**
 * Created by TUSK.ONE on 8/29/16.
 */
public class Global extends Application{
    public static native String HelloJNI();
    static {
        System.loadLibrary("HelloJNI");
    }



    RegisterModel regModel;
    User currentUser;
    private List<String> jobId;
    private Boolean noNetworkOpened = false;
    private Boolean appInBackground = false;
    private Jobs job;


    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public RegisterModel getRegModel() {
        return regModel;
    }

    public void setRegModel(RegisterModel regModel) {
        this.regModel = regModel;
    }

    public List<String> getJobId() {
        return jobId;
    }

    public void setJobId(List<String> jobId) {
        this.jobId = jobId;
    }

    public Boolean getNoNetworkOpened() {
        return noNetworkOpened;
    }

    public void setNoNetworkOpened(Boolean noNetworkOpened) {
        this.noNetworkOpened = noNetworkOpened;
    }

    public Jobs getJob() {
        return job;
    }

    public void setJob(Jobs job) {
        this.job = job;
    }

    public Boolean getAppInBackground() {
        return appInBackground;
    }

    public void setAppInBackground(Boolean appInBackground) {
        this.appInBackground = appInBackground;
    }
}
