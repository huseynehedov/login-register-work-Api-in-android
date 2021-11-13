package com.raywenderlich.loginandregister.model;

import java.util.List;

public class MyPlacesResponse {
    private Success success;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Success getSuccess() {
        return success;
    }

    public void setSuccess(Success success) {
        this.success = success;
    }

    public static class Success{
        private List<PlaceRequest> locations;

        public List<PlaceRequest> getLocations() {
            return locations;
        }

        public void setLocations(List<PlaceRequest> locations) {
            this.locations = locations;
        }
    }

}
