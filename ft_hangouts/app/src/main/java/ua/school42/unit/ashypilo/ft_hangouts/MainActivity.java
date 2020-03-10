package ua.school42.unit.ashypilo.ft_hangouts;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ContentValues cv = new ContentValues();
    private SQLiteDatabase db;

    private String nameContact;
    private String surnameContact;
    private String  phoneContact;
    private String emailContact;

    private List<String> name = new ArrayList<>();
    private List<String> surname = new ArrayList<>();
    private List<String> phone = new ArrayList<>();
    private List<String> email = new ArrayList<>();
    private ArrayAdapter<Contact> mAdapter;
    private ListView listViewContact;
    private List<Contact> contacts = new ArrayList<>();
    private int hourOld;
    private int hourNew;
    private int minuteOld;
    private int minuteNew;
    private int secOld;
    private int secNew;
    private int positionContact;
    private BroadcastReceiver mySmsReceiver = new MySmsReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!Theme.THEME.isTheme())
            setTheme(R.style.AppTheme);
        else
            setTheme(R.style.AppTheme2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewContact = findViewById(R.id.l_list);
        getContacts();
        listViewContact.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                nameContact = name.get(position);
                surnameContact = surname.get(position);
                phoneContact = phone.get(position);
                emailContact = email.get(position);
                Intent intent = new Intent(parent.getContext(), Contacts.class);
                intent.putExtra("name", nameContact);
                intent.putExtra("surname", surnameContact);
                intent.putExtra("phone", phoneContact);
                intent.putExtra("email", emailContact);
                startActivityForResult(intent, 1);
            }
        });

        listViewContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                positionContact = position;
                registerForContextMenu(parent);
                return false;
            }
        });

        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(mySmsReceiver, filter);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, 1, Menu.NONE, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case 1:
                nameContact = name.get(positionContact);
                surnameContact = surname.get(positionContact);
                db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
                db.execSQL("DELETE FROM contacts WHERE name = '" + nameContact + "'" + " AND surname = '" + surnameContact + "'");
                Toast.makeText(getApplicationContext(),
                        R.string.deleted,Toast.LENGTH_LONG).show();
                getContacts();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume() {

        super.onResume();
//        if (Theme.FON.isFon()) {
//            Theme.FON.setFon(false);
//            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//            Date date = new Date();
//            String num = dateFormat.format(date);
//            int i = 0;
//            for (String line : num.split(":")) {
//                if (i == 0)
//                    hourNew = Integer.parseInt(line);
//                else if (i == 1)
//                    minuteNew = Integer.parseInt(line);
//                else
//                    secNew = Integer.parseInt(line);
//                i++;
//            }
//            int hour = 0;
//            int minute = 0;
//            int sec = 0;
//            if (hourNew == hourOld && minuteNew == minuteOld) {
//                sec = secNew - secOld;
//                Toast.makeText(getApplicationContext(), getString(R.string.you_were_not) + " " + sec + getString(R.string.sec), Toast.LENGTH_LONG).show();
//            }
//            else if (hourNew == hourOld) {
//                minute = minuteNew - minuteOld;
//                if (secOld > secNew)
//                    sec = secOld - secNew;
//                else
//                    sec = secNew - secOld;
//                Toast.makeText(getApplicationContext(), getString(R.string.you_were_not) + " " + minute + getString(R.string.minute) + " " + sec + getString(R.string.sec), Toast.LENGTH_LONG).show();
//            }
//            else
//                Toast.makeText(getApplicationContext(), getString(R.string.you_were_not) + " " + hour + getString(R.string.hour) + " " + minute + getString(R.string.minute) + " " + sec + getString(R.string.sec), Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Theme.FON.setFon(true);
//        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//        Date date = new Date();
//        String num = dateFormat.format(date);
//        int i = 0;
//        for (String line : num.split(":")) {
//            if (i == 0)
//                hourOld = Integer.parseInt(line);
//            else if (i == 1)
//                minuteOld = Integer.parseInt(line);
//            else
//                secOld = Integer.parseInt(line);
//            i++;
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    public void onChangeMenu(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.blue) {
            Theme.THEME.setTheme(false);
            recreate();
        }
        else if (id == R.id.green) {
            Theme.THEME.setTheme(true);
            recreate();
        }
    }

    public void createContact(View view) {
        Intent intent = new Intent(view.getContext(), CreateContact.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent == null) {
            getContacts();
            return;
        }
            db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
            nameContact = intent.getStringExtra("name");
            surnameContact = intent.getStringExtra("surname");
            phoneContact = intent.getStringExtra("phone");
            emailContact = intent.getStringExtra("email");
            cv.put("phone", phoneContact);
            cv.put("name", nameContact);
            cv.put("surname", surnameContact);
            cv.put("email", emailContact);
            db.insert("contacts", null, cv);
            cv.clear();
            db.close();
            getContacts();
    }

    private void getContacts() {
        name.clear();
        surname.clear();
        phone.clear();
        email.clear();
        db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
//        db.execSQL("DROP TABLE contacts");
        db.execSQL("CREATE TABLE IF NOT EXISTS contacts (name TEXT, surname TEXT, phone TEXT, email TEXT)");
//        db.execSQL("DELETE FROM contacts");
        Cursor query = db.rawQuery("SELECT * FROM contacts ORDER BY name, surname;", null);
        if(query.moveToFirst()){
            do{
                name.add(query.getString(0));
                surname.add(query.getString(1));
                phone.add(query.getString(2));
                email.add(query.getString(3));
            }
            while(query.moveToNext());
        }
        contacts.clear();
        for(int i = 0; i < name.size(); i++) {

            String con = name.get(i) + " " + surname.get(i);
            contacts.add(new Contact(R.drawable.contatc_image, con));
        }
        mAdapter = new ContactAdapter(this, R.layout.contact, contacts);
        listViewContact.setAdapter(mAdapter);
        query.close();
        db.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mySmsReceiver!=null)
        {
            unregisterReceiver(mySmsReceiver);
        }
    }
}


