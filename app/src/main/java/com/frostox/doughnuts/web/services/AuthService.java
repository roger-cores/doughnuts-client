package com.frostox.doughnuts.web.services;

import android.app.Activity;
import android.widget.Toast;

import com.frostox.doughnuts.R;
import com.frostox.doughnuts.activities.MainActivity;
import com.frostox.doughnuts.app.Doughnuts;
import com.frostox.doughnuts.dbase.Key;
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

/**
 * Created by bloss on 2/8/16.
 */
public class AuthService {



    public static void validateAccessToken(Activity activity, final boolean validateRefresh, Key key, final AuthenticationService authenticationService){
        final Activity context = activity;
        if(key.getAccess().equals("")) {
            refreshToken(context, key, authenticationService);
            return;
        }

        final ValidateRequest validateRequest = new ValidateRequest();
        validateRequest.setAccessToken(key.getAccess());
        validateRequest.setClientId(context.getString(R.string.client_id));
        validateRequest.setClientSecret(context.getString(R.string.client_secret));
        validateRequest.setGrantType(context.getString(R.string.grant_access));
        final Key key1 = key;
        final Call<Response> validateCall = authenticationService.validateToken(validateRequest);

        validateCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                if(response != null && !response.isSuccessful() && response.errorBody() != null){

                    Converter<ResponseBody, Response> errorConverter =
                            ((Doughnuts) context.getApplication()).getClient().responseBodyConverter(Response.class, new Annotation[0]);
                    try {
                        Response error = errorConverter.convert(response.errorBody());

                        switch (error.getCode()){
                            case -1:
                            case -2:
                                //token expired
                                //try refresh token
                                if(validateRefresh) {
                                    refreshToken(context, key1, authenticationService);
                                }
                                else {
                                    Toast.makeText(context, context.getString(R.string.error_session), Toast.LENGTH_LONG).show();
                                    if(context instanceof MainActivity)
                                        ((MainActivity) context).goTo(MainActivity.LOG_IN, MainActivity.defaultDelay);
                                }

                                break;
                            case -3:
                                Toast.makeText(context, context.getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.print("");


                    return;
                }

                Response validationResponse = response.body();
                if(validationResponse!=null && validationResponse.getCode() != null && validationResponse.getCode() == 1) {
                    if(context instanceof MainActivity)
                        ((MainActivity) context).goTo(MainActivity.HOME, MainActivity.defaultDelay);
                } else {
                    Toast.makeText(context, context.getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
            }
        });
    }


    public static void refreshToken(Activity activity, final Key key, final AuthenticationService authenticationService){

        final Activity context = activity;

        if(key.getRefresh().equals("")) {
            if(context instanceof MainActivity)
                ((MainActivity) context).goTo(MainActivity.LOG_IN, MainActivity.defaultDelay);
            return;
        }



        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setGrantType(context.getString(R.string.grant_refresh));
        loginRequest.setClientSecret(context.getString(R.string.client_secret));
        loginRequest.setClientId(context.getString(R.string.client_id));
        loginRequest.setRefreshToken(key.getRefresh());
        Call<LoginResponse> refreshToken = authenticationService.login(loginRequest);

        refreshToken.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {

                if(response != null && !response.isSuccessful() && response.errorBody() != null) {

                    Converter<ResponseBody, Response> errorConverter =
                            ((Doughnuts) context.getApplication()).getClient().responseBodyConverter(Response.class, new Annotation[0]);
                    try {
                        Response error = errorConverter.convert(response.errorBody());

                        switch (error.getError()){
                            case "invalid_request":
                            case "invalid_grant":
                                Toast.makeText(context, context.getString(R.string.error_session), Toast.LENGTH_LONG).show();
                                if(context instanceof MainActivity)
                                    ((MainActivity) context).goTo(MainActivity.LOG_IN, MainActivity.defaultDelay);
                                break;


                            case "unauthorized_client":
                            case "unsupported_grant_type":
                            case "invalid_scope":
                            case "server_error":
                            case "invalid_client":
                                Toast.makeText(context, context.getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
                                break;
                        }


                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    return;
                }

                if(response != null && response.body() != null){
                    com.frostox.doughnuts.dbase.Utility.updateKey(context.getApplicationContext(), response.body().getAccessToken(), response.body().getRefreshToken());
                    //validate again
                    validateAccessToken(context, false, key, authenticationService);
                }  else {
                    Toast.makeText(context, context.getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
            }
        });
    }


    public static void login(Activity activity, final AuthenticationService authenticationService, String email, String password){
        final Activity context = activity;

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(email);
        loginRequest.setPassword(password);
        loginRequest.setClientSecret(context.getString(R.string.client_secret));
        loginRequest.setClientId(context.getString(R.string.client_id));
        loginRequest.setGrantType(context.getString(R.string.grant_password));

        Call<LoginResponse> login = authenticationService.login(loginRequest);

        login.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                if(response != null && !response.isSuccessful() && response.errorBody() != null){
                    Toast.makeText(context, context.getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
                } else if(response != null && response.body() != null && response.body().getAccessToken() != null && response.body().getRefreshToken() != null){
                    com.frostox.doughnuts.dbase.Utility.updateKey(context, response.body().getAccessToken(), response.body().getRefreshToken());
                    validateAccessToken(context, false, com.frostox.doughnuts.dbase.Utility.getKey(context), authenticationService);
                } else {
                    Toast.makeText(context, context.getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
            }
        });

    }


    public void signUp(Activity activity, final AuthenticationService authenticationService, String email, String password, String nickname){

    }
}
