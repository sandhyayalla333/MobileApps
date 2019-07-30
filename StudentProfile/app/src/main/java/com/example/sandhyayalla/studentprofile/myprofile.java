package com.example.sandhyayalla.studentprofile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link myprofile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class myprofile extends Fragment {

    ImageView iv;
    int i=0;
    private OnFragmentInteractionListener mListener;

    public myprofile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_myprofile, container, false);
       getActivity().setTitle("My Profile");
        ImageView iv=(ImageView)view.findViewById(R.id.iv_selectImage);
        Bundle args = getArguments();

        if(args!=null) {

            i = (Integer) args.getInt("imageNumber", 0);
            Log.d("demo","id"+i);
            if (i == 1) {
                Log.d("demo", "imagedisplayed");
                iv.setImageResource(R.drawable.avatar_f_1);
            }
            else  if(i==2)
            {
                iv.setImageResource(R.drawable.avatar_f_2);
            }
            else  if(i==3)
            {
                iv.setImageResource(R.drawable.avatar_f_3);
            }
            else  if(i==4)
            {
                iv.setImageResource(R.drawable.avatar_m_1);
            }
            else  if(i==5)
            {
                iv.setImageResource(R.drawable.avatar_m_2);
            }
            else  if(i==6)
            {
                iv.setImageResource(R.drawable.avatar_m_3);
            }
        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.loadimagefragment();
            }
        });

       //validation

        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("demo", "Saved button clicked");
                EditText etFName = getView().findViewById(R.id.et_name);
                EditText etLName = getView().findViewById(R.id.et_lastName);
                EditText etId = getView().findViewById(R.id.et_Id);

                RadioGroup rbDept = (RadioGroup) getView().findViewById(R.id.rgDept);
                final RadioButton rb_1=(RadioButton)getView().findViewById(R.id.rb_1) ;

                ImageView image1=(ImageView)getView().findViewById(R.id.iv_selectImage);
                String dep = ((RadioButton)getView().findViewById(rbDept.getCheckedRadioButtonId())).getText().toString();
                Log.d("demo", dep+"");
                rbDept.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton rb=    (RadioButton)getView().findViewById(checkedId);
                        Log.d("demo", checkedId+"");


                    }
                });

                if (etFName.getText()== null){
                    etFName.setError("Enter first name");
                }else if (etLName.getText()== null){
                    etLName.setError("Enter last name");
                }else if (etId.getText()== null || etId.getText().length()!=9){
                    etId.setError("Enter 9 digit ID");
                }else {
                    //DisplayProfileFragment displayFrag = new DisplayProfileFragment();
                    Bundle args = new Bundle();
                    args.putString("fname", etFName.getText().toString());
                    args.putString("lname", etLName.getText().toString());
                    args.putString("dept", dep);
                    args.putInt("id", Integer.parseInt(String.valueOf(etId.getText())));
                    args.putInt("imageNumber", i);
                    //displayFrag.setArguments(args);
                    mListener.onsaveclicked(args);

                    //getFragmentManager().beginTransaction().add(R.id.container, displayFrag).commit();

                   /* getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, displayFrag, "tag_displayFragment")
                            .addToBackStack("tag_profileFragment").commit();*/
                }
            }
        });


        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //ImageView iv_image=(ImageView)findViewById(R.id.iv_selectImage);
        iv=(ImageView)getActivity().findViewById(R.id.iv_selectImage);




    }

    public void updateimagetoprofile(int i)
    {
        //ImageView iv_image=(ImageView)getView().findViewById(R.id.iv_selectImage);
        if(i==1)
        {
            Log.d("demo","imagedisplayed");
            iv.setImageResource(R.drawable.avatar_f_1);
        }
        else  if(i==2)
        {
            iv.setImageResource(R.drawable.avatar_f_2);
        }
        else  if(i==3)
        {
            iv.setImageResource(R.drawable.avatar_f_3);
        }
        else  if(i==4)
        {
            iv.setImageResource(R.drawable.avatar_m_1);
        }
        else  if(i==5)
        {
            iv.setImageResource(R.drawable.avatar_m_2);
        }
        else  if(i==6)
        {
            iv.setImageResource(R.drawable.avatar_m_3);
        }
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
       // void onFragmentInteraction(Uri uri);
        void loadimagefragment();
        void onsaveclicked(Bundle bundle);
    }
}
