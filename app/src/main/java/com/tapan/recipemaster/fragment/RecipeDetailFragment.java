package com.tapan.recipemaster.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapan.recipemaster.R;
import com.tapan.recipemaster.adapter.StepsAdapter;
import com.tapan.recipemaster.adapter.IngredientsAdapter;
import com.tapan.recipemaster.model.Ingredient;
import com.tapan.recipemaster.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link RecipeDetailFragment} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailFragment extends Fragment  implements StepsAdapter.StepsItemOnClickListener {

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
     *
     * @return A new instance of fragment RecipeDetailFragment.
     */
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
        settingAdapterForIngStep();

        return rootView;
    }

    private void settingAdapterForIngStep() {

        //setting the adapter for ingredients
        IngredientsAdapter ingredientsAdapter =new IngredientsAdapter(ingredientArrayList);
        mRecyclerViewIngredients.setAdapter(ingredientsAdapter);
        mRecyclerViewIngredients.setLayoutManager(new LinearLayoutManager(getActivity()));

        //setting the adapter for steps
        StepsAdapter stepsAdapter = new StepsAdapter(stepsArrayList,this);
        mRecyclerViewSteps.setAdapter(stepsAdapter);
        mRecyclerViewSteps.setLayoutManager(new LinearLayoutManager(getActivity()));

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
