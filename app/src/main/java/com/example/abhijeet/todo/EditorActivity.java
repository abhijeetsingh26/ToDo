package com.example.abhijeet.todo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import com.example.abhijeet.todo.Data.TodoContract;
import com.example.abhijeet.todo.Data.TodoDbHelper;
import java.util.Calendar;


public class EditorActivity extends AppCompatActivity {

    //Selected time by the user
    private static Calendar selecteddatetime;
    //Global Variable for the EditTextTime Field
    private static TextView time_textview;

    //IMPORTANT: This variable needs to be updated everytime anything in the task info has changed,
    // else a lot of functionalities would break :(
    private boolean misaNewTask = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        time_textview = (TextView) findViewById(R.id.time_edit_field);

        time_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picker timepicker = new Picker();
                timepicker.show(getFragmentManager(),"TimePicker");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.taskeditor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Return early if nothing has changed
        //   if (!misaNewTask) {
        //        return false;
        //   }

        //Get the data from Task textview
        TextView task_view = (TextView) findViewById(R.id.task_edit_field);
        String task = task_view.getText().toString().trim();

        //get time from the global calender variable
        int time = selecteddatetime.get(Calendar.HOUR_OF_DAY);
        switch (item.getItemId()) {
            case R.id.add_menu_button:
                ContentValues values = new ContentValues();
                values.put(TodoContract.TodoEntry.COLUMN_TASK, task);
                values.put(TodoContract.TodoEntry.COLUMN_TIME, time);
                getContentResolver().insert(TodoContract.TodoEntry.CONTENT_URI, values);
                return true;

            // Implementation of CONFIRM DIALOG if misaNewtask == true and the user has pressed back button
            case android.R.id.home:
                //// TODO: 9/9/2017 implement confirm dialog box
        }
        return super.onOptionsItemSelected(item);
    }

    //Inner Class for the time picker
    public static class Picker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, false);
        }


        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            String am_pm = "";

            selecteddatetime = Calendar.getInstance();
            selecteddatetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selecteddatetime.set(Calendar.MINUTE, minute);

            if (selecteddatetime.get(Calendar.AM_PM) == Calendar.AM) //Ignore Error This code runs perfect
                am_pm = "AM";
            else if (selecteddatetime.get(Calendar.AM_PM) == Calendar.PM)
                am_pm = "PM";

            String strHrsToShow = (selecteddatetime.get(Calendar.HOUR) == 0) ? "12" : selecteddatetime.get(Calendar.HOUR) + "";
            time_textview.setText(strHrsToShow + ":" + selecteddatetime.get(Calendar.MINUTE) + " " + am_pm);
        }

    }

}

