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
    }
}
