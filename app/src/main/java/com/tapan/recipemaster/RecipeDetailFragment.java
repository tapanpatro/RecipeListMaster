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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailFragment extends Fragment  implements StepsRecyclerViewAdapter.StepsOnClickListener{
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


    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDetailFragment newInstance(String param1, String param2) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        //getting list of ingredients and steps from detail Activity
        ingredientArrayList = getArguments().getParcelableArrayList(getString(R.string.ingredient_extra));
        stepsArrayList = getArguments().getParcelableArrayList(getString(R.string.step_extra));

        //using butter knife to bind the view
        ButterKnife.bind(this,rootView);

        //setting the adapter for ingredients
        IngredientsRecyclerViewAdapter ingredientsRecyclerViewAdapter=new IngredientsRecyclerViewAdapter(ingredientArrayList);
        mRecyclerViewIngredients.setAdapter(ingredientsRecyclerViewAdapter);
        mRecyclerViewIngredients.setLayoutManager(new LinearLayoutManager(getActivity()));

        //setting the adapter for steps
        StepsRecyclerViewAdapter stepsRecyclerViewAdapter = new StepsRecyclerViewAdapter(stepsArrayList,this);
        mRecyclerViewSteps.setAdapter(stepsRecyclerViewAdapter);
        mRecyclerViewSteps.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void onStepItemClicked(int position) {

        DetailStepOnClickListener detailStepOnClickListener =(DetailStepOnClickListener) getActivity();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.video_url),stepsArrayList.get(position).videoUrl);
        bundle.putString(getString(R.string.description_url),stepsArrayList.get(position).description);
        bundle.putString(getString(R.string.thumb_url),stepsArrayList.get(position).thumbnailUrl);
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
