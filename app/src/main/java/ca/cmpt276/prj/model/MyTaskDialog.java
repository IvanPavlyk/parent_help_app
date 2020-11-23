package ca.cmpt276.prj.model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import ca.cmpt276.prj.R;
import ca.cmpt276.prj.ui.ManageChildrenActivity;

import static ca.cmpt276.prj.ui.ManageChildrenActivity.stringToBitmap;

public class MyTaskDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.task, null);


        Bundle bundle = getArguments();
        String child_name = bundle.getString("child_name", "Default Child Name Here");
        String task_name = bundle.getString("task_name", "Default Task Name");
        String task_description = bundle.getString("description", "Default Description");

       // Bitmap bitmap = ManageChildrenActivity.stringToBitmap(your_string);


       // pic.setImageResource(getResources().getIdentifier(child_name, "drawable", getActivity().getPackageName()));


        TextView task_child_view = (TextView) view.findViewById(R.id.NameOfChild);
        task_child_view.setText("It's "+child_name+"'s turn");
        TextView task_name_view = (TextView) view.findViewById(R.id.TaskNameView);
        task_name_view.setText("Task: "+task_name);
        TextView task_description_view = (TextView) view.findViewById(R.id.TaskDescription);
        task_description_view.setText("Task Description: "+task_description);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }
}
