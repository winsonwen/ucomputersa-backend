package com.ucomputersa.auth.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class OAuth2UserInfoModel {


    private List<Name> names;
    private List<Photo> photos;
    private List<EmailAddress> emailAddresses;

    @Data
    public static class Name {
        private Metadata metadata;
        private String givenName;
        private String familyName;

    }

    @Data
    public static class Photo {
        private Metadata metadata;
        private String url;
    }

    @Data
    public static class EmailAddress {
        private Metadata metadata;
        private String value;

    }

    @Data
    public static class Metadata {
        private boolean primary;

        public boolean isPrimary() {
            return primary;
        }
    }

}
