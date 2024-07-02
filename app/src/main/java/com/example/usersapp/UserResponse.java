package com.example.usersapp;

import java.util.List;

public class UserResponse {
    public List<Result> results;

    public static class Result {
        public Name name;
        public Dob dob;
        public String email;
        public Location location;
        public Picture picture;
        public Id id;

        public static class Name {
            public String first;
            public String last;
        }

        public static class Dob {
            public int age;
        }

        public static class Location {
            public String city;
            public String country;
        }

        public static class Picture {
            public String large;
        }

        public static class Id {
            public String value;
        }
    }
}
