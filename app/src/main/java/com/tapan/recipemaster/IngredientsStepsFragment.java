package com.tapan.recipemaster;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientsStepsFragment extends Fragment  implements StepsRecyclerViewAdapter.StepsOnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ArrayList<Ingredient> ingredientArrayList;
    private ArrayList<Step> stepsArrayList;



    @BindView(R.id.rv_ingredients)
    RecyclerView mRecyclerViewIngredients;


    @BindView(R.id.rv_steps)
    RecyclerView mRecyclerViewSteps;


    @BindView(R.id.tv_ingredient)
    TextView mTextIngredientHeader;

    @BindView(R.id.tv_step)
    TextView mTextStepsHeader;


    public IngredientsStepsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IngredientsStepsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IngredientsStepsFragment newInstance(String param1, String param2) {
        IngredientsStepsFragment fragment = new IngredientsStepsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_ingredients_steps, container, false);

        ingredientArrayList = getArguments().getParcelableArrayList("Ingredients");
        stepsArrayList = getArguments().getParcelableArrayList("Steps");
        ButterKnife.bind(this,rootView);
        IngredientsRecyclerViewAdapter ingredientsRecyclerViewAdapter=new IngredientsRecyclerViewAdapter(ingredientArrayList);
        mRecyclerViewIngredients.setAdapter(ingredientsRecyclerViewAdapter);
        mRecyclerViewIngredients.setLayoutManager(new LinearLayoutManager(getActivity()));
        StepsRecyclerViewAdapter stepsRecyclerViewAdapter = new StepsRecyclerViewAdapter(stepsArrayList,this);
        mRecyclerViewSteps.setAdapter(stepsRecyclerViewAdapter);
        mRecyclerViewSteps.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void onStepItemClicked(int position) {

        DetailStepOnClickListener detailStepOnClickListener =(DetailStepOnClickListener) getActivity();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.video_url_step),stepsArrayList.get(position).videoUrl);
        bundle.putString(getString(R.string.step_description),stepsArrayList.get(position).description);
        bundle.putString(getString(R.string.img_thumbnail),stepsArrayList.get(position).thumbnailUrl);
        detailStepOnClickListener.onDetailStepItemClicked(bundle);

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

    public interface DetailStepOnClickListener {
        void onDetailStepItemClicked(Bundle bundle);
    }



}
