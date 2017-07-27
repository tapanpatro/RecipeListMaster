package com.tapan.recipemaster.fragment;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapan.recipemaster.R;
import com.tapan.recipemaster.adapter.IngredientsAdapter;
import com.tapan.recipemaster.adapter.StepsAdapter;
import com.tapan.recipemaster.model.Ingredient;
import com.tapan.recipemaster.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link RecipeDetailFragment} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailFragment extends Fragment implements StepsAdapter.StepsItemOnClickListener {

    private static final String SAVED_LAYOUT_MANAGER_INGRIDENTS = "SavePositionIngredients";
    private static final String SAVED_LAYOUT_MANAGER_STEPS = "SavePositionSteps";


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

    private int mPositionIng = RecyclerView.NO_POSITION;
    private int mPositionStep = RecyclerView.NO_POSITION;

    private String KEY_POSITION_ING = "KeyPositionIng";
    private String KEY_POSITION_STEP = "KeyPositionStep";
    long currentVisiblePosition_ing = 0;
    long currentVisiblePosition_step = 0;
    int topview = 0;
    int topView = 0;

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

    //LayoutManagerSavedState layoutManagerSavedState;


    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
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
        ButterKnife.bind(this, rootView);
        settingAdapterForIngStep();

       /* if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(KEY_POSITION_ING)) {
                mPositionIng = savedInstanceState.getInt(KEY_POSITION_ING);
            }

            if (savedInstanceState.containsKey(KEY_POSITION_STEP)) {
                mPositionStep = savedInstanceState.getInt(KEY_POSITION_STEP);
            }

        }*/

        return rootView;
    }


    /*@Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SAVED_LAYOUT_MANAGER_INGRIDENTS, mRecyclerViewIngredients.getLayoutManager().onSaveInstanceState());
        return bundle;
    }*/

    @Override
    public void onPause() {
        super.onPause();/*
        mPositionIng= llManager.findFirstVisibleItemPosition();
        View startView = rv.getChildAt(0);
        topView = (startView == null) ? 0 : (startView.getTop() - rv.getPaddingTop());*/

        currentVisiblePosition_ing = ((LinearLayoutManager)mRecyclerViewIngredients.getLayoutManager()).findFirstVisibleItemPosition();
        View startView = mRecyclerViewIngredients.getChildAt(0);
        topview = (startView == null) ? 0 : (startView.getTop() - mRecyclerViewIngredients.getPaddingTop());


        currentVisiblePosition_step = ((LinearLayoutManager)mRecyclerViewSteps.getLayoutManager()).findFirstVisibleItemPosition();
        View startViews = mRecyclerViewSteps.getChildAt(0);
        topView = (startViews == null) ? 0 : (startViews.getTop() - mRecyclerViewSteps.getPaddingTop());

        //currentVisiblePosition_step = ((LinearLayoutManager)mRecyclerViewIngredients.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

    }

    @Override
    public void onResume() {
        super.onResume();
        //((LinearLayoutManager) mRecyclerViewIngredients.getLayoutManager()).scrollToPosition((int) currentVisiblePosition_ing);
        //currentVisiblePosition_ing = 0;

        if (currentVisiblePosition_ing != -1) {
            ((LinearLayoutManager)mRecyclerViewIngredients.getLayoutManager()).scrollToPositionWithOffset((int) currentVisiblePosition_ing, topview);
        }

        if (currentVisiblePosition_step != -1) {
            ((LinearLayoutManager)mRecyclerViewSteps.getLayoutManager()).scrollToPositionWithOffset((int) currentVisiblePosition_step, topView);
        }



        //((LinearLayoutManager) mRecyclerViewSteps.getLayoutManager()).scrollToPosition((int) currentVisiblePosition_step);
        //currentVisiblePosition_step = 0;

    }

   /* @Override
    public void onSaveInstanceState(Bundle outState) {

        int scrollPositionIng = mRecyclerViewIngredients.computeVerticalScrollOffset();
        int scrollPositionSteps = mRecyclerViewSteps.computeVerticalScrollOffset();


        mPositionIng = scrollPositionIng;
        mPositionStep = scrollPositionSteps;

        outState.putInt(KEY_POSITION_ING, mPositionIng);
        outState.putInt(KEY_POSITION_STEP, mPositionStep);
        super.onSaveInstanceState(outState);
    }*/

    /*@Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {

        if (mPositionIng != RecyclerView.NO_POSITION) {
            //gridView.smoothScrollToPosition(mPosition);
            mRecyclerViewIngredients.getLayoutManager().scrollToPosition(mPositionIng);
        }
        mRecyclerViewIngredients.getLayoutManager().scrollToPosition(mPositionIng);
        if (mPositionStep != RecyclerView.NO_POSITION) {

            mRecyclerViewSteps.getLayoutManager().scrollToPosition(mPositionStep);
        }

        super.onViewStateRestored(savedInstanceState);
    }*/

    private void settingAdapterForIngStep() {


        //setting the adapter for ingredients
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredientArrayList);
        mRecyclerViewIngredients.setAdapter(ingredientsAdapter);
        mRecyclerViewIngredients.setLayoutManager(new LinearLayoutManager(getActivity()));



        //setting the adapter for steps
        StepsAdapter stepsAdapter = new StepsAdapter(stepsArrayList, this);
        mRecyclerViewSteps.setAdapter(stepsAdapter);
        mRecyclerViewSteps.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (currentVisiblePosition_ing != -1) {
            ((LinearLayoutManager)mRecyclerViewIngredients.getLayoutManager()).scrollToPositionWithOffset((int) currentVisiblePosition_ing, topview);
        }

        if (currentVisiblePosition_step != -1) {
            ((LinearLayoutManager)mRecyclerViewSteps.getLayoutManager()).scrollToPositionWithOffset((int) currentVisiblePosition_step, topView);
        }


    }

    @Override
    public void onStepItemClicked(int position) {
        DetailStepOnClickListener detailStepOnClickListener = (DetailStepOnClickListener) getActivity();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.video_url), stepsArrayList.get(position).videoUrl);
        bundle.putString(getString(R.string.description_url), stepsArrayList.get(position).description);
        bundle.putString(getString(R.string.thumb_url), stepsArrayList.get(position).thumbnailUrl);
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


    static class SavedState extends android.view.View.BaseSavedState {
        public int mScrollPosition;

        SavedState(Parcel in) {
            super(in);
            mScrollPosition = in.readInt();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mScrollPosition);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }


}


