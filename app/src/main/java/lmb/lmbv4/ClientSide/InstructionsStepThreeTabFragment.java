package lmb.lmbv4.ClientSide;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import lmb.lmbv4.Entities.InstructionsTabsPagerAdapter;
import lmb.lmbv4.R;


/**
 * Created by ad05n on 2/2/2017.
 */

public class InstructionsStepThreeTabFragment extends Fragment
{
    private ImageView instructions;
    private InstructionsTabsPagerAdapter.InstructionsStepOneTabFragmentListener mListener;

    public InstructionsStepThreeTabFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_instructions_stepthree, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        instructions = (ImageView) view.findViewById(R.id.step_three_image);
        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mListener.onStepThreeSelected();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InstructionsTabsPagerAdapter.InstructionsStepOneTabFragmentListener) {
            mListener = (InstructionsTabsPagerAdapter.InstructionsStepOneTabFragmentListener) context;
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
}
