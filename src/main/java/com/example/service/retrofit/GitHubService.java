package com.example.service.retrofit;

import com.example.autoconfigure.retrofit.RetrofitService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import lombok.Value;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

@RetrofitService(urlProperty = "github.baseUrl")
public interface GitHubService {

    @GET("/users/{user}/repos")
    Call<List<User>> listRepos(@Path("user") String user);

    @Value
    @ToString(of = {"name", "fullName"})
    class User {
        private final Long id;
        private final String name;

        @JsonProperty("full_name")
        private final String fullName;
    }

}
