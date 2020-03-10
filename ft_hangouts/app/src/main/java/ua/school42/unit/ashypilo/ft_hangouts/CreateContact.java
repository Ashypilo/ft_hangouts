package ua.school42.unit.ashypilo.ft_hangouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CreateContact extends AppCompatActivity {
    private EditText name;
    private EditText surname;
    private EditText phone;
    private EditText email;
    private ImageView buttonCreate;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int CAMERA_REQUEST = 0;
    private ImageView imageView1;
    private static boolean theme = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!Theme.THEME.isTheme())
            setTheme(R.style.AppTheme);
        else
            setTheme(R.style.AppTheme2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_contact);

        name = findViewById(R.id.c_name);
        surname = findViewById(R.id.c_surname);
        phone = findViewById(R.id.c_phone);
        email = findViewById(R.id.c_mail);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_create);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.create:
                        createNewContact(getCurrentFocus());
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

    public void createNewContact(View view) {
            if (name.getText().length() == 0 || surname.getText().length() == 0 || phone.getText().length() == 0) {
                Toast.makeText(getApplicationContext(), R.string.write_data, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("surname", surname.getText().toString());
            intent.putExtra("phone", phone.getText().toString());
            intent.putExtra("email", email.getText().toString());
            setResult(RESULT_OK, intent);
            Theme.FON.setFon(false);
            finish();
        }
    }


