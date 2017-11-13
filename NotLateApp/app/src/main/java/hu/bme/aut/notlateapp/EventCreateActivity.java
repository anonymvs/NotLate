package hu.bme.aut.notlateapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import hu.bme.aut.notlateapp.model.Event;

public class EventCreateActivity extends AppCompatActivity {

    public static final String KEY_EDIT_EVENT = "KEY_EDIT_PLACE";
    public static final int KEY_EDIT_EVENT_CODE = 2;
    public static final String KEY_NEW_EVENT = "KEY_NEW_EVENT";
    public static final int KEY_NEW_EVENT_CODE = 1;
    public static final String KEY_EDIT_ID = "KEY_EDIT_ID";


    DatePickerDialog.OnDateSetListener date;
    private Calendar calendar;
    EditText title;
    EditText location;
    EditText members;
    TextView setEventDateTV;
    private int year, month, day;

    private boolean inEditMode = false;
    private int eventToEditID = 0;
    private Event eventToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        calendar = Calendar.getInstance();

        setEventDateTV = (TextView) findViewById(R.id.set_event_dateTV);
        updateLabel();

        Button btnCreate = (Button) findViewById(R.id.btnCreate);

        title = (EditText) findViewById(R.id.etTitle);
        location = (EditText) findViewById(R.id.etLocation);
        members = (EditText) findViewById(R.id.etMembers);

        if (getIntent().getExtras() != null &&
                getIntent().getExtras().containsKey(KEY_EDIT_EVENT)) {
            inEditMode = true;

            eventToEdit = (Event) getIntent().getSerializableExtra(KEY_EDIT_EVENT);
            eventToEditID = getIntent().getIntExtra(KEY_EDIT_ID, -1);

            title.setText(eventToEdit.getTitle());

            calendar = eventToEdit.getDate();
            updateLabel();

            location.setText(eventToEdit.getLocation());

            members.setText(eventToEdit.getMembers());

            btnCreate.setText(R.string.submit_changes);
        }

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        setEventDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EventCreateActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().trim().equals("")) {
                    Toast.makeText(EventCreateActivity.this, "Title is required", Toast.LENGTH_SHORT).show();
                } else {
                    //new Event(Calendar.getInstance(), "2 nap", "Title1", "by: Owner", "Here: Bp, bla utca 45.", "members")
                    onEventCreated(new Event(
                            calendar.getInstance(),
                            "X days",
                            title.getText().toString(),
                            "by: Me",
                            location.getText().toString(),
                            members.getText().toString()));
                }
            }
        });
    }

    private void updateLabel() {
        /*String myFormat = "yy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);*/
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.JAPAN);
        setEventDateTV.setText(df.format(calendar.getTime()));
    }

    public void onEventCreated(Event newEvent) {
        /*Intent i = new Intent(EventCreateActivity.this, EventCreateActivity.class);
        i.setClass(EventCreateActivity.this, EventCreateActivity.class);
        if(inEditMode) {
            i.putExtra(EventCreateActivity.KEY_EDIT_EVENT, eventToEdit);
        } else {
            i.putExtra(EventCreateActivity.KEY_NEW_EVENT, newTodo);
        }
        i.putExtra(EventCreateActivity.KEY_EDIT_ID, eventToEditID);
        this.startActivity(i);*/
        if(inEditMode) {
            Intent i = new Intent(EventCreateActivity.this, MainActivity.class);
            i.putExtra(KEY_EDIT_EVENT, newEvent);
            i.putExtra(KEY_EDIT_ID, eventToEditID);
            setResult(KEY_EDIT_EVENT_CODE, i);
            finish();
        } else {
            Intent i = new Intent(EventCreateActivity.this, MainActivity.class);
            i.putExtra(KEY_NEW_EVENT, newEvent);
            setResult(KEY_NEW_EVENT_CODE, i);
            finish();
        }
    }

}
