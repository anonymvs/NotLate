package hu.bme.aut.notlateapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import hu.bme.aut.notlateapp.adapter.FirebaseDbAdapter;
import hu.bme.aut.notlateapp.model.Event;

public class CreateEventActivity extends AppCompatActivity {

    public static final String KEY_EDIT_EVENT = "KEY_EDIT_PLACE";
    public static final String KEY_EDIT_ID = "KEY_EDIT_ID";
    public static final int PLACE_PICKER_REQUEST = 1;

    private FirebaseDbAdapter dbAdapter;

    private DatePickerDialog.OnDateSetListener date;
    private TimePickerDialog.OnTimeSetListener timePickerDialog;
    private Button btnCreate;

    private Calendar calendar;
    private EditText title;
    private TextView location;
    private String locationID;
    private EditText members;
    private TextView setEventDateTV;
    private TextView tvTime;

    private boolean inEditMode = false;
    private Event eventToEdit = null;

    private boolean hasLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        dbAdapter = FirebaseDbAdapter.getInstance();

        fieldBinding();

        if (getIntent().getExtras() != null) {
            editEventSetup();
        }

        setEventDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreateEventActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(CreateEventActivity.this, timePickerDialog, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().trim().equals("")) {
                    Toast.makeText(CreateEventActivity.this, "Title is required", Toast.LENGTH_SHORT).show();
                } else {
                    List<String> member_list = new ArrayList<>();
                    String strarray[] = members.getText().toString().split(" ");
                    for(String str : strarray) {
                        member_list.add(str);
                    }
                    onEventCreated(new Event(
                            title.getText().toString(),
                            calendar,
                            location.getText().toString(),
                            locationID,
                            member_list
                            ));
                }
            }
        });
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void onEventCreated(Event newEvent) {
        if(inEditMode) {
            /*Intent i = new Intent(CreateEventActivity.this, MainActivity.class);
            i.putExtra(KEY_EDIT_EVENT, newEvent);
            i.putExtra(KEY_EDIT_ID, eventToEditID);
            setResult(KEY_EDIT_EVENT_CODE, i);*/
            dbAdapter.updateEvent(eventToEdit.getEventID(), newEvent);
            finish();
        } else {
            /*Intent i = new Intent(CreateEventActivity.this, MainActivity.class);
            i.putExtra(KEY_NEW_EVENT, newEvent);
            setResult(KEY_NEW_EVENT_CODE, i);*/
            dbAdapter.createEvent(newEvent);
            finish();
        }
    }

    private void fieldBinding() {
        btnCreate = (Button) findViewById(R.id.btnCreate);

        title = (EditText) findViewById(R.id.etTitle);

        location = (TextView) findViewById(R.id.tvLocation);
        Button btnPickPlace = (Button) findViewById(R.id.btnPickPlace);
        btnPickPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(CreateEventActivity.this), PLACE_PICKER_REQUEST );
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        members = (EditText) findViewById(R.id.etMembers);

        calendar = Calendar.getInstance();
        setEventDateTV = (TextView) findViewById(R.id.set_event_dateTV);
        tvTime = (TextView) findViewById(R.id.tvTime);
        updateLabel();

        date = dateSetDialogListener();
        timePickerDialog = timeSetDialogListener();
    }

    private void editEventSetup() {
        inEditMode = true;

        eventToEdit = (Event) getIntent().getSerializableExtra(KEY_EDIT_EVENT);

        title.setText(eventToEdit.getTitle());

        calendar = eventToEdit.askDateAsCalendar();
        updateLabel();

        location.setText(eventToEdit.getLocation());
        locationID = eventToEdit.getLocationID();

        if(eventToEdit.hasMembers()) {
            members.setText(eventToEdit.askAllMembersWithoutComma());
        }

        btnCreate.setText(R.string.submit_changes);
    }

    private void updateLabel() {
        /*String myFormat = "yy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);*/
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.JAPAN);
        setEventDateTV.setText(df.format(calendar.getTime()));

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String strH, strM;
        String zero = "0";

        if(hour < 10) {
            strH = zero + hour;
        } else {
            strH = "" + hour;
        }
        if(minute < 10) {
            strM = zero + minute;
        } else {
            strM = "" + minute;
        }
        tvTime.setText(strH + ":" + strM);
    }

    private DatePickerDialog.OnDateSetListener dateSetDialogListener() {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
    }

    private TimePickerDialog.OnTimeSetListener timeSetDialogListener() {
        return new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                updateLabel();
            }
        };
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(CreateEventActivity.this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                location.setText(place.getAddress());
                locationID = place.getId();
                hasLocation = true;
            }
        } else {
            hasLocation = false;
        }
    }

}
