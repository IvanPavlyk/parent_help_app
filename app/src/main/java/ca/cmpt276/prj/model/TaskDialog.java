package ca.cmpt276.prj.model;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ca.cmpt276.prj.R;

/**
 * Dialog for the display of individual tasks
 */
public class TaskDialog extends DialogFragment {
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.task, null);

        Bundle bundle = getArguments();
        String child_name = Objects.requireNonNull(bundle).getString("child_name", "Default Child Name Here");
        String task_name = bundle.getString("task_name", "Default Task Name");
        String task_description = bundle.getString("description", "Default Description");
        String child_portrait_string = bundle.getString("portrait", "Default Portrait");

        TextView task_child_view = view.findViewById(R.id.NameOfChild);
        task_child_view.setText("It's "+child_name+"'s turn");
        TextView task_name_view = view.findViewById(R.id.TaskNameView);
        task_name_view.setText("Task: "+task_name);
        TextView task_description_view = view.findViewById(R.id.TaskDescription);
        task_description_view.setText("Task Description: "+task_description);
        ImageView child_portrait_view = view.findViewById(R.id.picture);
        if (!child_portrait_string.equals("Default Portrait")) {
            byte [] encodeByte = Base64.decode(child_portrait_string, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            child_portrait_view.setImageBitmap(bitmap);
        }
        else child_portrait_view.setImageResource(R.drawable.ic_baseline_account_circle_24);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }
}
