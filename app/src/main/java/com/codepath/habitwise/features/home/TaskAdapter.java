package com.codepath.habitwise.features.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codepath.habitwise.R;
import com.codepath.habitwise.models.Habit;
import com.codepath.habitwise.models.Task;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private Context context;
    private List<Task> tasks;
    public static final String TAG = "TaskAdapter";
    public TaskAdapter(Context context , List<Task> tasks) {
        this.context= context;
        this.tasks= tasks;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvInfo;
        private Switch toggleSwitch;
        private RelativeLayout counterBox;
        private Button incrementButton;
        private TextView textViewCounter;
        private Button decrementButton;
        private int frequency;
        private int recurrence;
        private String description="";
        private  int count = 0;
        private String stringCount;
        private Habit habitObject;
        private List<Habit> results;

        public ViewHolder( @NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvInfo = itemView.findViewById(R.id.tvInfo);
            toggleSwitch = itemView.findViewById(R.id.toggleSwitch);
            counterBox = itemView.findViewById(R.id.counterBox);
            incrementButton = itemView.findViewById(R.id.incrementButton);
            textViewCounter = itemView.findViewById(R.id.textViewCounter);
            decrementButton = itemView.findViewById(R.id.decrementButton);

        }

        public void bind(final Task task) {
            count = task.getCounter();
            habitObject = task.getHabit();
            stringCount = Integer.toString(count);
            description = "";
            tvTitle.setText(habitObject.getTitle());
            frequency = habitObject.getFrequency();
            recurrence = habitObject.getRecurrence();
            if (frequency == 1){
                toggleSwitch.setVisibility(View.VISIBLE);
                counterBox.setVisibility(View.GONE);
                if (count == 1)
                    toggleSwitch.setChecked(true);
                else
                    toggleSwitch.setChecked(false);
                description += "Once a day";

                toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) task.setCounter(1);
                        else task.setCounter(0);
                        task.saveInBackground();
                    }
                });
            }
            else {
                description += frequency + " times a day";
                textViewCounter.setText(stringCount);
                toggleSwitch.setVisibility(View.GONE);
                counterBox.setVisibility(View.VISIBLE);
                decrementButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Log.d(TAG, "Decreasing value...");
                        if (count - 1 < 0) return;
                        count--;
                        stringCount = Integer.toString(count);
                        textViewCounter.setText(stringCount);
                        task.setCounter(count);
                        task.saveInBackground();
                    }
                });
                incrementButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Log.d(TAG, "Increasing value...");
                        count++;
                        stringCount = Integer.toString(count);
                        textViewCounter.setText(stringCount);
                        task.setCounter(count);
                        task.saveInBackground();
                    }
                });
            }
            if (recurrence== 0){
                description += ", everyday!";
            }
            else{
                description += ", weekly!";
//                Log.i(TAG,"fetching days:" + habit.getDays().getClass().getName());
//                Log.i(TAG,"fetching days:" + habit.getDays());
            }
            tvInfo.setText(description);
        }
    }
}
