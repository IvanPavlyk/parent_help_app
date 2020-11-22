package ca.cmpt276.prj.model;

import android.app.AlertDialog;
import android.app.Dialog;
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

public class MyTaskDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.task, null);

        // 从WhoseTurnActivity传过来的arguments
        Bundle bundle = getArguments();
        String child_name = bundle.getString("child_name", "Default Child Name Here");
        String task_name = bundle.getString("task_name", "Default Task Name");
        String task_description = bundle.getString("description", "Default Description");

        //用第一个argument，也就是child的名字，找到对应child的图片
        //假设你的project里child的名字放在drawable文件夹下，并且用child的名字命名
        ImageView pic = (ImageView) view.findViewById(R.id.pic);
        pic.setImageResource(getResources().getIdentifier(child_name, "drawable", getActivity().getPackageName()));

        //用第二个和第三个argument设置task name和task description
        TextView task_name_view = (TextView) view.findViewById(R.id.TaskNameView);
        task_name_view.setText(task_name);
        TextView task_description_view = (TextView) view.findViewById(R.id.TaskDescription);
        task_description_view.setText(task_description);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }
}
