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
import com.frostox.doughnuts.web.services.AuthService;
import com.frostox.doughnuts.web.endpoints.AuthenticationService;

import static com.frostox.doughnuts.utilities.Utility.displayError;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Login.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {

    EditText email, password;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();



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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ((Button) view.findViewById(R.id.sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goTo(MainActivity.REGISTER, MainActivity.noDelay);
            }
        });

        ((Button) view.findViewById(R.id.login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Login.this.email.getText().toString();
                String password = Login.this.password.getText().toString();

                if(email.equals("")){
                    displayError(getActivity(), R.string.email_missing, "login");
                } else if(!email.contains("@")){
                    displayError(getActivity(), R.string.email_invalid);
                } else if(!email.contains(".")){
                    displayError(getActivity(), R.string.email_invalid);
                } else if(email.contains(" ")){
                    displayError(getActivity(), R.string.email_invalid);
                } else if((email.lastIndexOf(".") < email.indexOf("@"))){
                    displayError(getActivity(), R.string.email_invalid);
                } else if(email.endsWith(".") || email.endsWith("@")) {
                    displayError(getActivity(), R.string.email_invalid);
                } else if(password.length() < 5){
                    displayError(getActivity(), R.string.password_length);
                } else {
                    //log in here
                    AuthenticationService authenticationService = ((MainActivity) getActivity()).getRetrofit().create(AuthenticationService.class);
                    AuthService.login(getActivity(), authenticationService, email, password);

                    showProgress();

                }
            }
        });

        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);




        return view;
    }

    public void showProgress(){
        RelativeLayout mainContent, waito;
        EditText appLabel;

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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
