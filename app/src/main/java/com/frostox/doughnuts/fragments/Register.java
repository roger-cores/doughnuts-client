package com.frostox.doughnuts.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.frostox.doughnuts.R;
import com.frostox.doughnuts.activities.MainActivity;
import com.frostox.doughnuts.web.endpoints.AuthenticationService;
import com.frostox.doughnuts.web.services.AuthService;

import org.apache.commons.lang3.StringUtils;

import static com.frostox.doughnuts.utilities.Utility.displayError;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Register.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Register.
     */
    // TODO: Rename and change types and number of parameters
    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        final EditText email, password, repassword, nickname;

        email = ((EditText) view.findViewById(R.id.email));
        password = ((EditText) view.findViewById(R.id.password));
        repassword = ((EditText) view.findViewById(R.id.repassword));
        nickname = ((EditText) view.findViewById(R.id.nickname));


        ((Button) view.findViewById(R.id.gotoLogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goTo(MainActivity.LOG_IN, MainActivity.noDelay);
            }
        });


        ((Button) view.findViewById(R.id.register)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailT = email.getText().toString();
                String passwordT = password.getText().toString();
                String repasswordT = repassword.getText().toString();
                String nicknameT = nickname.getText().toString();

                if(emailT.equals("")){
                    displayError(getActivity(), R.string.email_missing, "register");
                    email.setError(getString(R.string.email_missing), getActivity().getResources().getDrawable(R.mipmap.ic_error));
                } else if(!emailT.contains("@")){
                    displayError(getActivity(), R.string.email_invalid);
                } else if(!emailT.contains(".")){
                    displayError(getActivity(), R.string.email_invalid);
                } else if(emailT.contains(" ")){
                    displayError(getActivity(), R.string.email_invalid);
                } else if((emailT.lastIndexOf(".") < emailT.indexOf("@"))){
                    displayError(getActivity(), R.string.email_invalid);
                } else if(emailT.endsWith(".") || emailT.endsWith("@")) {
                    displayError(getActivity(), R.string.email_invalid);
                } else if(!(StringUtils.isAlphanumeric(emailT.replaceAll("\\.", "").replaceAll("@", "")) || StringUtils.isAlpha(emailT.replaceAll("\\.", "").replaceAll("@", "")))){
                    displayError(getActivity(), R.string.email_invalid);
                } else if(nicknameT.equals("")){
                    displayError(getActivity(), R.string.nickname_missing);
                } else if(nicknameT.length() < 3){
                    displayError(getActivity(), R.string.nickname_too_small);
                } else if(nicknameT.length() > 10){
                    displayError(getActivity(), R.string.nickname_too_long);
                } else if(passwordT.length() < 5){
                    displayError(getActivity(), R.string.password_length);
                } else if(!passwordT.equals(repasswordT)){
                    displayError(getActivity(), R.string.repassword);
                } else if(!StringUtils.isAlphanumeric(nicknameT)){
                    displayError(getActivity(), R.string.nickname_invalid);
                } else {
                    AuthService.signUp(getActivity(),
                            ((MainActivity) getActivity()).getRetrofit().create(AuthenticationService.class),
                            emailT,
                            passwordT,
                            nicknameT);

                    showProgress();
                }


            }
        });

        return view;
    }

    public void showProgress(){
        RelativeLayout mainContent, waito;

        mainContent = (RelativeLayout) getView().findViewById(R.id.mainContent);
        waito = (RelativeLayout) getView().findViewById(R.id.waito);

        mainContent.setVisibility(View.GONE);
        waito.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        RelativeLayout mainContent, waito;

        mainContent = (RelativeLayout) getView().findViewById(R.id.mainContent);
        waito = (RelativeLayout) getView().findViewById(R.id.waito);

        mainContent.setVisibility(View.VISIBLE);
        waito.setVisibility(View.GONE);
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
