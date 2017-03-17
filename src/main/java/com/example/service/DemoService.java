package com.example.service;

import com.example.service.retrofit.GitHubService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

@Service
@Log
public class DemoService {

    @Autowired
    private GitHubService gitHubService;

    public void getUser() {
        gitHubService.listRepos("user").enqueue(new Callback<List<GitHubService.User>>() {
            @Override
            public void onResponse(Call<List<GitHubService.User>> call, Response<List<GitHubService.User>> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<List<GitHubService.User>> call, Throwable throwable) {
                log.severe(() -> "Error calling github");
            }
        });
    }

}
