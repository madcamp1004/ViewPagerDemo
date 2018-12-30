package com.example.viewpagerdemo;

import android.app.Service;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class ContactFragment extends Fragment {

    protected View fragView;

    EditText nameText;
    EditText phoneText;
    EditText addressText;

    Button nextBtn;

    String selectedString;

    public static ArrayAdapter adapter;

    ListView listview;

    SoftKeyboard softKeyboard;
    ConstraintLayout constraintLayout;

    private OnFragmentInteractionListener mListener;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        this.fragView = view;

        listview = (ListView) fragView.findViewById(R.id.listview);
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_single_choice, MainActivity.items);
        listview.setAdapter(adapter);

        nameText = (EditText) fragView.findViewById(R.id.nameText);
        phoneText = (EditText) fragView.findViewById(R.id.phoneText);
        addressText = (EditText) fragView.findViewById(R.id.addressText);

        nextBtn = (Button) fragView.findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                onClickNextBtn();
            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (-1 < position && position < adapter.getCount()) {
                    // SET NAME AND PHONE NUMBER!
                    nameText.setText(MainActivity.dataList.get(position).get("name"));
                    phoneText.setText(MainActivity.dataList.get(position).get("phone"));

                } else {
                    Log.i("Error", "ITEM DOES NOT EXIST!");
                }
            }
        });
        constraintLayout = (ConstraintLayout) fragView.findViewById(R.id.contactlayout);

        InputMethodManager controlManager = (InputMethodManager)getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        softKeyboard = new SoftKeyboard(constraintLayout, controlManager);

        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged()
        {
            @Override
            public void onSoftKeyboardHide()
            {
                new Handler(Looper.getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //키보드 내려왔을때
                        MainActivity.mNavigation.setVisibility(View.VISIBLE);
                        nextBtn.setVisibility(View.VISIBLE);

                    }
                });
            }

            @Override
            public void onSoftKeyboardShow()
            {
                new Handler(Looper.getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //키보드 올라왔을때
                        MainActivity.mNavigation.setVisibility(View.INVISIBLE);
                        nextBtn.setVisibility(View.GONE);

                    }
                });
            }
        });
        return view;
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
    public void onClickNextBtn(){
        int count, idx;
        count = adapter.getCount();

        Log.i("***COUNT***", "" + MainActivity.dataList.size());

        if(count > 0){
            idx = listview.getCheckedItemPosition();
            if(idx > -1 && idx < count){
                // selectedString = items.get(idx);
                Toast.makeText(getContext(), nameText.getText() + " " + phoneText.getText() + " " + addressText.getText() ,Toast.LENGTH_SHORT).show();
                MainActivity.mViewPager.setCurrentItem(1, true);
            }
        }
    }
}
