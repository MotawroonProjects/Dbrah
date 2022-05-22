package com.apps.dbrah.model;

import java.io.Serializable;

public class SettingDataModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable{
        private String terms;
        private String privacy;
        private String facebook;
        private String insta;
        private String twitter;
        private String snapchat;
        private String user_info;
        private String provider_info;
        public String getTerms() {
            return terms;
        }

        public String getPrivacy() {
            return privacy;
        }

        public String getFacebook() {
            return facebook;
        }

        public String getInsta() {
            return insta;
        }

        public String getTwitter() {
            return twitter;
        }

        public String getSnapchat() {
            return snapchat;
        }

        public String getUser_info() {
            return user_info;
        }

        public String getProvider_info() {
            return provider_info;
        }
    }
}
