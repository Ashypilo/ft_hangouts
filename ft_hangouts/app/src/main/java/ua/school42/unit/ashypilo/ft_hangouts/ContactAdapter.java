package ua.school42.unit.ashypilo.ft_hangouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private LayoutInflater inflater;
    private int layout;
    private List<Contact> contacts;

    public ContactAdapter(Context context, int res, List<Contact> contacts) {
        super(context, res, contacts);
        this.contacts = contacts;
        this.layout = res;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        ImageView flagView = (ImageView) view.findViewById(R.id.image_list_view);
        TextView nameView = (TextView) view.findViewById(R.id.text_list_view);

        Contact contact = contacts.get(position);

        flagView.setImageResource(contact.getImage());
        nameView.setText(contact.getText());

        return view;
    }
}
