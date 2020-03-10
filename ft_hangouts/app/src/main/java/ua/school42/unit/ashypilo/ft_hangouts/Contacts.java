package ua.school42.unit.ashypilo.ft_hangouts;

import android.Manifest;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Contacts extends AppCompatActivity {

    private String nameString;
    private String surnameString;
    private String phoneString;
    private String emailContact;
    private EditText name;
    private EditText surname;
    private EditText phone;
    private EditText email;
    private SQLiteDatabase db;
    private EditText message;
    private TableLayout tableLayout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!Theme.THEME.isTheme())
            setTheme(R.style.AppTheme);
        else
            setTheme(R.style.AppTheme2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        name = findViewById(R.id.result);
        surname = findViewById(R.id.result1);
        phone = findViewById(R.id.result2);
        email = findViewById(R.id.result3);
        tableLayout = (TableLayout) findViewById(R.id.sms);

        Intent intent = getIntent();
        nameString = intent.getStringExtra("name");
        surnameString = intent.getStringExtra("surname");
        phoneString = intent.getStringExtra("phone");
        emailContact = intent.getStringExtra("email");
        name.setText(nameString);
        surname.setText(surnameString);
        phone.setText(phoneString);
        email.setText(emailContact);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_save:
                        tableLayout.setVisibility(View.INVISIBLE);
                        message = (EditText) findViewById(R.id.sms_text);
                        message.setText("");
                        saveContact();
                        bottomNavigationView.getMenu().findItem(R.id.navigation_save).setEnabled(false);
                        break;
                    case R.id.navigation_sms:
                        falseEdit();
                        tableLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.navigation_edit:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_save).setEnabled(true);
                        tableLayout.setVisibility(View.INVISIBLE);
                        message = (EditText) findViewById(R.id.sms_text);
                        message.setText("");
                        trueEdit();
                        break;
                }

                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Theme.FON.setFon(false);
    }

    public void saveContact() {
        if (name.getText().length() == 0 || surname.getText().length() == 0 || phone.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.write_data, Toast.LENGTH_SHORT).show();
            return;
        }
        db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("DELETE FROM contacts WHERE name = '" + nameString + "'" + " AND surname = '" + surnameString + "'");
        Intent intent = new Intent();
        intent.putExtra("name", name.getText().toString());
        intent.putExtra("surname", surname.getText().toString());
        intent.putExtra("phone", phone.getText().toString());
        intent.putExtra("email", email.getText().toString());
        setResult(RESULT_OK, intent);
        Toast.makeText(getApplicationContext(),
                R.string.saved,Toast.LENGTH_LONG).show();
        falseEdit();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void send(View view) throws SecurityException {
        falseEdit();
        try {
            message = (EditText) findViewById(R.id.sms_text);
            Log.d(Contacts.class.getSimpleName(), "onReceive: " + message + " -------------- " + phoneString);
            SmsManager smsManager = SmsManager.getDefault();
            checkSelfPermission(Manifest.permission.SEND_SMS);
            String[] permissions = {Manifest.permission.SEND_SMS};
            requestPermissions(permissions, 1);
            smsManager.sendTextMessage(phoneString, null, message.getText().toString(), null, null);
            Toast.makeText(getApplicationContext(),
                    R.string.sms_send,Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    R.string.sms_not_send, Toast.LENGTH_LONG).show();
            e.printStackTrace();
            phone.setEnabled(false);
        }
        tableLayout.setVisibility(View.INVISIBLE);
        message = (EditText) findViewById(R.id.sms_text);
        message.setText("");
    }

    public void sendEmail(View view) {
        falseEdit();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString()});
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, ""));
    }

    public void call(View view) {
        falseEdit();
        String dial = "tel:" + phone.getText().toString();
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
    }

    private void trueEdit() {
        name.setEnabled(true);
        surname.setEnabled(true);
        phone.setEnabled(true);
        email.setEnabled(true);
    }

    private void falseEdit() {
        name.setEnabled(false);
        surname.setEnabled(false);
        phone.setEnabled(false);
        email.setEnabled(false);
        bottomNavigationView.getMenu().findItem(R.id.navigation_save).setEnabled(false);
    }
}
