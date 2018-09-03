package herold.wgucalendar.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.model.Assessment;

public class AssessmentAdapter extends ArrayAdapter<Assessment> {
    public AssessmentAdapter(Context context, List<Assessment> assessments) {
        super(context, 0, assessments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Assessment assessment = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_assessment, parent, false);
        }
        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtDue = convertView.findViewById(R.id.txtDue);
        TextView txtType =  convertView.findViewById(R.id.txtType);
        txtTitle.setText(assessment.getTitle());
        txtDue.setText(assessment.getDueDateDisplay());
        txtType.setText(assessment.getType());
        return convertView;
    }
}
