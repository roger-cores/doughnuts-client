package com.frostox.doughnuts.web.endpoints;

import com.frostox.doughnuts.web.webmodels.CreatedResponse;
import com.frostox.doughnuts.web.webmodels.LoginRequest;
import com.frostox.doughnuts.web.webmodels.LoginResponse;
import com.frostox.doughnuts.web.webmodels.Response;
import com.frostox.doughnuts.web.webmodels.ValidateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Roger Cores on 1/8/16.
 */
public interface AuthenticationService {

    /**
     * Returns accessToken using grantType=password or grantType=refreshToken
     * @param loginRequest
     * @return
     */
    @POST("user/oauth/token")
    public Call<LoginResponse> login(@Body LoginRequest loginRequest);

    /**
     * Validates a newly fetched accessToken and starts a session with the server
     * @param validateRequest
     * @return
     */
    @POST("user/validate-token")
    public Call<Response> validateToken(@Body ValidateRequest validateRequest);


    /**
     * Signs up a user
     * @param loginRequest
     * @return id and version of the created user
     */
    @POST("user/signup")
    public Call<CreatedResponse> signUp(@Body LoginRequest loginRequest);



}
