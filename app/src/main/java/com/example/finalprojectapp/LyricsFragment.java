package com.example.finalprojectapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class LyricsFragment extends Fragment {

  boolean isTablet;
 Bundle dataFromActivity;
 long id;
    private AlteredCharSequence Snackbar;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong("ID");

        View result = inflater.inflate(R.layout.activity_lfragment, container, false);

        TextView title = result.findViewById(R.id.Fragment_title);
        title.setText(dataFromActivity.getString("Title"));

        TextView information = result.findViewById(R.id.Info);
        information.setText(dataFromActivity.getString("Information"));

        // get the delete button, and add a click listener:
        Button deleteB = (Button) result.findViewById(R.id.Delete);
        deleteB.setOnClickListener(clk -> {

            if (isTablet) { //both the list and details are on the screen:
                LyricsActivity parent = (LyricsActivity) getActivity();
                parent.deleteLyricsID((int) id);//this deletes the item and updates the list
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            } else {
                LyricsFrameLayout parent = (LyricsFrameLayout) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra("ID", dataFromActivity.getLong("ID"));

                parent.setResult(Activity.RESULT_OK, backToFragmentExample);
                parent.finish();
            }
        });


        Button saveB = (Button) result.findViewById(R.id.Save);
        saveB.setOnClickListener(clk -> {

            if (isTablet) {
                LyricsActivity parent = (LyricsActivity) getActivity();
                parent.infoSave(dataFromActivity.getString("Title"), dataFromActivity.getString("Definition"));

                Snackbar sb = Snackbar.make(result.findViewById(android.R.id.content), "Saved", Snackbar.LENGTH_LONG);
                sb.setAction("Close", v -> sb.dismiss());

                sb.show();
            } else {

                LyricsFrameLayout parent = (LyricsFrameLayout) getActivity();
                Intent backToFragment = new Intent();
                backToFragment.putExtra("ID", dataFromActivity.getLong("ID"));
                backToFragment.putExtra("TITLE", dataFromActivity.getString("Title"));
                backToFragment.putExtra("DEFINITION", dataFromActivity.getString("Definition"));
                parent.setResult(Activity.RESULT_OK, backToFragment);
                parent.finish();
            }

        });

        return result;
    }
}
