package com.app.dbrah.model;

import java.io.Serializable;

public class SettingDataModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable{
        private String terms_en;
        private String privacy_en;
        private String facebook;
        private String insta;
        private String twitter;
        private String snapchat;
        private String user_info;
        private String provider_info;

        public String getTerms_en() {
            return terms_en;
        }

        public String getPrivacy_en() {
            return privacy_en;
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
