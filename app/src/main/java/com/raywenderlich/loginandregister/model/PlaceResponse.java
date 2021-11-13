package com.raywenderlich.loginandregister.model;

import java.util.List;

public class PlaceResponse {
    private Success success;
    private Error error;

    public PlaceResponse() {
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Success getSuccess() {
        return success;
    }

    public void setSuccess(Success success) {
        this.success = success;
    }

    public static class Success{
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Error{
        private List<String> latitude;
        private List<String> longitude;

        public List<String> getLatitude() {
            return latitude;
        }

        public void setLatitude(List<String> latitude) {
            this.latitude = latitude;
        }

        public List<String> getLongitude() {
            return longitude;
        }

        public void setLongitude(List<String> longitude) {
            this.longitude = longitude;
        }
    }

}
