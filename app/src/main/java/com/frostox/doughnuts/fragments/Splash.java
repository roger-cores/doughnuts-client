package com.frostox.doughnuts.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.frostox.doughnuts.R;
import com.frostox.doughnuts.activities.MainActivity;
import com.frostox.doughnuts.dbase.DaoMaster;
import com.frostox.doughnuts.dbase.DaoSession;
import com.frostox.doughnuts.dbase.Key;
import com.frostox.doughnuts.dbase.Utility;
import com.frostox.doughnuts.web.webmodels.LoginRequest;
import com.frostox.doughnuts.web.webmodels.LoginResponse;
import com.frostox.doughnuts.web.webmodels.Response;
import com.frostox.doughnuts.web.webmodels.ValidateRequest;
import com.frostox.doughnuts.web.webservices.AuthenticationService;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Splash.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Splash#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Splash extends Fragment {

    DaoMaster daoMaster;
    DaoSession daoSession;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Splash() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Splash.
     */
    // TODO: Rename and change types and number of parameters
    public static Splash newInstance(String param1, String param2) {
        Splash fragment = new Splash();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //Init keys if they dont exist
        final Key key = Utility.getKey(getActivity().getApplicationContext());
        if(key == null) {
            Utility.insertKey(getActivity().getApplicationContext(), "", "");
            ((MainActivity) getActivity()).goTo(MainActivity.LOG_IN);
        } else {
            //Check accessToken, refreshToken
            final AuthenticationService authenticationService = ((MainActivity) getActivity()).getRetrofit().create(AuthenticationService.class);

            com.frostox.doughnuts.web.Utility.validateAccessToken(getActivity(), true, key, authenticationService);



        }




    }

//    public void validateAccessToken(final boolean validateRefresh, Key key, final AuthenticationService authenticationService){
//
//        if(key.getAccess().equals("")) {
//            refreshToken(key, authenticationService);
//            return;
//        }
//
//        final ValidateRequest validateRequest = new ValidateRequest();
//        validateRequest.setAccessToken(key.getAccess());
//        validateRequest.setClientId(getString(R.string.client_id));
//        validateRequest.setClientSecret(getString(R.string.client_secret));
//        validateRequest.setGrantType(getString(R.string.grant_access));
//        final Key key1 = key;
//        final Call<Response> validateCall = authenticationService.validateToken(validateRequest);
//
//        validateCall.enqueue(new Callback<Response>() {
//            @Override
//            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//
//                if(response != null && !response.isSuccessful() && response.errorBody() != null){
//
//                    Converter<ResponseBody, Response> errorConverter =
//                            ((MainActivity)getActivity()).getRetrofit().responseBodyConverter(Response.class, new Annotation[0]);
//                    try {
//                        Response error = errorConverter.convert(response.errorBody());
//
//                        switch (error.getCode()){
//                            case -1:
//                            case -2:
//                                //token expired
//                                //try refresh token
//                                if(validateRefresh) {
//                                    refreshToken(key1, authenticationService);
//                                }
//                                else {
//                                    Toast.makeText(getContext(), getString(R.string.error_session), Toast.LENGTH_LONG).show();
//                                    ((MainActivity) getActivity()).goTo(MainActivity.LOG_IN);
//                                }
//
//                                break;
//                            case -3:
//                                Toast.makeText(getContext(), getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
//                                break;
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.print("");
//
//
//                    return;
//                }
//
//                Response validationResponse = response.body();
//                if(validationResponse!=null && validationResponse.getCode() != null && validationResponse.getCode() == 1) {
//                    ((MainActivity) getActivity()).goTo(MainActivity.HOME);
//                } else {
//                    Toast.makeText(getContext(), getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Response> call, Throwable t) {
//                Toast.makeText(getContext(), getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//
//    public void refreshToken(final Key key, final AuthenticationService authenticationService){
//
//
//
//        if(key.getRefresh().equals("")) {
//            ((MainActivity) getActivity()).goTo(MainActivity.LOG_IN);
//            return;
//        }
//
//
//
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setGrantType(getString(R.string.grant_refresh));
//        loginRequest.setClientSecret(getString(R.string.client_secret));
//        loginRequest.setClientId(getString(R.string.client_id));
//        loginRequest.setRefreshToken(key.getRefresh());
//        Call<LoginResponse> refreshToken = authenticationService.login(loginRequest);
//
//        refreshToken.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
//
//                if(response != null && !response.isSuccessful() && response.errorBody() != null) {
//
//                    Converter<ResponseBody, Response> errorConverter =
//                            ((MainActivity) getActivity()).getRetrofit().responseBodyConverter(Response.class, new Annotation[0]);
//                    try {
//                        Response error = errorConverter.convert(response.errorBody());
//
//                        switch (error.getCode()){
//                            case -1:
//                            case -2:
//                                Toast.makeText(getContext(), getString(R.string.error_session), Toast.LENGTH_LONG).show();
//                                ((MainActivity) getActivity()).goTo(MainActivity.LOG_IN);
//                                break;
//                            case -3:
//                            case 0:
//                                Toast.makeText(getContext(), getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
//                                break;
//                        }
//
//
//                    } catch (IOException e){
//                        e.printStackTrace();
//                    }
//
//                    return;
//                }
//
//                if(response != null && response.body() != null){
//                    Utility.updateKey(getActivity().getApplicationContext(), response.body().getAccessToken(), response.body().getRefreshToken());
//                    //validate again
//                    validateAccessToken(false, key, authenticationService);
//                }  else {
//                    Toast.makeText(getContext(), getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                Toast.makeText(getContext(), getString(R.string.error_contingency), Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
