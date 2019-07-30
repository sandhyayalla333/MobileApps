package com.example.sandhyayalla.studentprofile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link selectimagefrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link selectimagefrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class selectimagefrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public selectimagefrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment selectimagefrag.
     */
    // TODO: Rename and change types and number of parameters
    public static selectimagefrag newInstance(String param1, String param2) {
        selectimagefrag fragment = new selectimagefrag();
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
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_selectimagefrag, container, false);
        getActivity().setTitle("Select Avatar");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ImageView avatarf1=(ImageView)getActivity().findViewById(R.id.imageView);
        final ImageView avatarf2=(ImageView)getActivity().findViewById(R.id.imageView2);
        final ImageView avatarf3=(ImageView)getActivity().findViewById(R.id.imageView3);
        final ImageView avatarm1=(ImageView)getActivity().findViewById(R.id.imageView4);
        final ImageView avatarm2=(ImageView)getActivity().findViewById(R.id.imageView5);
        final ImageView avatarm3=(ImageView)getActivity().findViewById(R.id.imageView6);
        final String Imagename = String.valueOf(avatarf1.getTag());
        final Intent intent=new Intent();
        avatarf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.updateimage(1);
                getActivity().getSupportFragmentManager().popBackStack();


            }
        });
        avatarf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.updateimage(2);
                getActivity().getSupportFragmentManager().popBackStack();


            }
        });
        avatarf3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.updateimage(3);
                getActivity().getSupportFragmentManager().popBackStack();


            }
        });
        avatarm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.updateimage(4);
                getActivity().getSupportFragmentManager().popBackStack();


            }
        });
        avatarm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.updateimage(5);
                getActivity().getSupportFragmentManager().popBackStack();


            }
        });
        avatarm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.updateimage(6);
                getActivity().getSupportFragmentManager().popBackStack();


            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
           // mListener.onFragmentInteraction(uri);
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
        //void onFragmentInteraction(Uri uri);
        public void updateimage(int i);
    }
}
