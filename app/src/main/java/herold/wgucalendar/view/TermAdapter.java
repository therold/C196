package herold.wgucalendar.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.model.Term;


public class TermAdapter extends ArrayAdapter<Term> {
    public TermAdapter(Context context, List<Term> terms) {
        super(context, 0, terms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Term term = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_term, parent, false);
        }
        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtStart = convertView.findViewById(R.id.txtStart);
        TextView txtEnd =  convertView.findViewById(R.id.txtEnd);
        txtTitle.setText(term.getTitle());
        txtStart.setText(term.getStartDisplay());
        txtEnd.setText(term.getEndDisplay());
        return convertView;
    }
}