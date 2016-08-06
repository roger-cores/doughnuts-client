package com.frostox.doughnuts.web.services;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;

import com.frostox.doughnuts.R;
import com.frostox.doughnuts.activities.MainActivity;
import com.frostox.doughnuts.app.Doughnuts;
import com.frostox.doughnuts.dbase.Key;
import com.frostox.doughnuts.dbase.Utility;
import com.frostox.doughnuts.web.webmodels.CreatedResponse;
import com.frostox.doughnuts.web.webmodels.LoginRequest;
import com.frostox.doughnuts.web.webmodels.LoginResponse;
import com.frostox.doughnuts.web.webmodels.Response;
import com.frostox.doughnuts.web.webmodels.ValidateRequest;
import com.frostox.doughnuts.web.endpoints.AuthenticationService;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;

import static com.frostox.doughnuts.utilities.Utility.displayContingencyError;
import static com.frostox.doughnuts.utilities.Utility.displayError;

/**
 * Created by bloss on 2/8/16.
 */
public class AuthService {



    public static void validateAccessToken(final Activity activity, final boolean validateRefresh, Key key, final AuthenticationService authenticationService){
        
        if(key.getAccess().equals("")) {
            refreshToken(activity, key, authenticationService);
            return;
        }

        final ValidateRequest validateRequest = new ValidateRequest();
        validateRequest.setAccessToken(key.getAccess());
        validateRequest.setClientId(activity.getString(R.string.client_id));
        validateRequest.setClientSecret(activity.getString(R.string.client_secret));
        validateRequest.setGrantType(activity.getString(R.string.grant_access));
        final Key key1 = key;
        final Call<Response> validateCall = authenticationService.validateToken(validateRequest);

        validateCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                if(response != null && !response.isSuccessful() && response.errorBody() != null){

                    Converter<ResponseBody, Response> errorConverter =
                            ((Doughnuts) activity.getApplication()).getClient().responseBodyConverter(Response.class, new Annotation[0]);
                    try {
                        Response error = errorConverter.convert(response.errorBody());

                        switch (error.getCode()){
                            case -1:
                            case -2:
                                //token expired
                                //try refresh token
                                if(validateRefresh) {
                                    refreshToken(activity, key1, authenticationService);
                                }
                                else {
                                    displayError(activity, R.string.error_session);
                                    if(activity instanceof MainActivity)
                                        ((MainActivity) activity).goTo(MainActivity.LOG_IN, MainActivity.defaultDelay);
                                }

                                break;
                            case -3:
                                displayContingencyError(activity);
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        displayContingencyError(activity);
                    }
                    System.out.print("");


                    return;
                }

                Response validationResponse = response.body();
                if(validationResponse!=null && validationResponse.getCode() != null && validationResponse.getCode() == 1) {
                    if(activity instanceof MainActivity)
                        ((MainActivity) activity).goTo(MainActivity.HOME, MainActivity.defaultDelay);
                } else {
                    displayContingencyError(activity);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                displayContingencyError(activity);
            }
        });
    }


    public static void refreshToken(final Activity activity, final Key key, final AuthenticationService authenticationService){

        if(key.getRefresh().equals("")) {
            if(activity instanceof MainActivity)
                ((MainActivity) activity).goTo(MainActivity.LOG_IN, MainActivity.defaultDelay);
            return;
        }



        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setGrantType(activity.getString(R.string.grant_refresh));
        loginRequest.setClientSecret(activity.getString(R.string.client_secret));
        loginRequest.setClientId(activity.getString(R.string.client_id));
        loginRequest.setRefreshToken(key.getRefresh());
        Call<LoginResponse> refreshToken = authenticationService.login(loginRequest);

        refreshToken.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {

                if(response != null && !response.isSuccessful() && response.errorBody() != null) {

                    Converter<ResponseBody, Response> errorConverter =
                            ((Doughnuts) activity.getApplication()).getClient().responseBodyConverter(Response.class, new Annotation[0]);
                    try {
                        Response error = errorConverter.convert(response.errorBody());

                        switch (error.getError()){
                            case "invalid_request":
                            case "invalid_grant":
                                displayError(activity, R.string.error_session);
                                if(activity instanceof MainActivity)
                                    ((MainActivity) activity).goTo(MainActivity.LOG_IN, MainActivity.defaultDelay);
                                break;


                            case "unauthorized_client":
                            case "unsupported_grant_type":
                            case "invalid_scope":
                            case "server_error":
                            case "invalid_client":
                                displayContingencyError(activity);
                                break;
                        }


                    } catch (IOException e){
                        e.printStackTrace();
                        displayContingencyError(activity);
                    }

                    return;
                }

                if(response != null && response.body() != null){
                    com.frostox.doughnuts.dbase.Utility.updateKey(activity.getApplicationContext(), response.body().getAccessToken(), response.body().getRefreshToken());
                    //validate again
                    validateAccessToken(activity, false, key, authenticationService);
                }  else {
                    displayContingencyError(activity);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                displayContingencyError(activity);
            }
        });
    }


    public static void login(final Activity activity, final AuthenticationService authenticationService, String email, String password){
        
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(email);
        loginRequest.setPassword(password);
        loginRequest.setClientSecret(activity.getString(R.string.client_secret));
        loginRequest.setClientId(activity.getString(R.string.client_id));
        loginRequest.setGrantType(activity.getString(R.string.grant_password));

        Call<LoginResponse> login = authenticationService.login(loginRequest);

        login.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                if(response != null && !response.isSuccessful() && response.errorBody() != null){


                    Converter<ResponseBody, Response> errorConverter =
                            ((Doughnuts) activity.getApplication()).getClient().responseBodyConverter(Response.class, new Annotation[0]);
                    try {
                        Response error = errorConverter.convert(response.errorBody());
                        if(error != null && error.getErrorDescription() != null && error.getErrorDescription().equals(activity.getString(R.string.invalid_credentials))){
                            displayError(activity, R.string.invalid_user_credentials);
                        } else displayContingencyError(activity);
                    } catch(IOException e){
                        e.printStackTrace();
                        displayContingencyError(activity);
                    }

                } else if(response != null && response.body() != null && response.body().getAccessToken() != null && response.body().getRefreshToken() != null){
                    com.frostox.doughnuts.dbase.Utility.updateKey(activity, response.body().getAccessToken(), response.body().getRefreshToken());
                    validateAccessToken(activity, false, com.frostox.doughnuts.dbase.Utility.getKey(activity), authenticationService);
                } else {
                    displayContingencyError(activity);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                displayContingencyError(activity);
            }
        });

    }


    public static void signUp(final Activity activity, final AuthenticationService authenticationService, final String email, final String password, String nickname){

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(email);
        loginRequest.setPassword(password);
        loginRequest.setNickName(nickname);
        loginRequest.setClientSecret(activity.getString(R.string.client_secret));
        loginRequest.setClientId(activity.getString(R.string.client_id));

        Call<CreatedResponse> signUp = authenticationService.signUp(loginRequest);

        signUp.enqueue(new Callback<CreatedResponse>() {
            @Override
            public void onResponse(Call<CreatedResponse> call, retrofit2.Response<CreatedResponse> response) {
                if(response != null && !response.isSuccessful() && response.errorBody() != null){
                    Converter<ResponseBody, Response> errorConverter =
                            ((Doughnuts) activity.getApplication()).getClient().responseBodyConverter(Response.class, new Annotation[0]);
                    try {
                        Response error = errorConverter.convert(response.errorBody());
                        if(error.getError().equals("user exists")){
                            displayError(activity, R.string.email_used);
                        } else if(error.getError().equals("nickname exists")){
                            displayError(activity, R.string.nickname_used);
                        } else displayContingencyError(activity);
                    } catch(IOException e){
                        e.printStackTrace();
                        displayContingencyError(activity);
                    }

                } else if(response != null && response.body() != null){
                    Utility.updateKey(activity, "", "");
                    login(activity, authenticationService, email, password);
                } else displayContingencyError(activity);
            }

            @Override
            public void onFailure(Call<CreatedResponse> call, Throwable t) {
                displayContingencyError(activity);
            }
        });
    }
    

    
}
