package ca.cmpt276.prj.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.gson.Gson;

import java.util.Objects;

import ca.cmpt276.prj.R;
import ca.cmpt276.prj.model.Child;

/**
 * Dialog for the display of individual tasks
 */
public class TaskDialog extends AppCompatDialogFragment {
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.task_dialog, null);

        builder.setView(view)
                .setTitle(R.string.Task_Dialog_Title)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });

        Bundle bundle = getArguments();

        String task_holder = Objects.requireNonNull(bundle).getString("task_holder");
        String child_name, child_portrait;
        assert task_holder != null;
        if (!task_holder.equals("null")) {
            Child child = new Gson().fromJson(task_holder, Child.class);
            child_name = child.getName();
            child_portrait = child.getPortrait();
        } else {
            child_name = "no one";
            child_portrait = "Default Portrait";
        }

        String task_name = bundle.getString("task_name", "Default Task Name");
        String task_description = bundle.getString("description", "Default Description");

        TextView task_holder_name_view = view.findViewById(R.id.Dialog_Task_Holder_Name);
        task_holder_name_view.setText(getString(R.string.Name_Text_Front)+ " " + child_name +getString(R.string.Name_Text_Back));
        TextView task_name_view = view.findViewById(R.id.Dialog_Task_Name);
        task_name_view.setText(task_name);
        TextView task_description_view = view.findViewById(R.id.Dialog_Task_Description);
        task_description_view.setText(task_description);
        ImageView task_holder_portrait_view = view.findViewById(R.id.Dialog_Task_Holder_Portrait);
        if (!child_portrait.equals("Default Portrait")) {
            byte [] encodeByte = Base64.decode(child_portrait, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            task_holder_portrait_view.setImageBitmap(bitmap);
        }
        else task_holder_portrait_view.setImageResource(R.drawable.ic_baseline_account_circle_24);

        return builder.create();
    }
}
