package herold.wgucalendar.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.model.Course;

public class CourseAdapter extends ArrayAdapter<Course> {

    public CourseAdapter(Context context, List<Course> courses) {
        super(context, 0, courses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Course course = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_course, parent, false);
        }
        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtStart = convertView.findViewById(R.id.txtStart);
        TextView txtEnd =  convertView.findViewById(R.id.txtEnd);
        txtTitle.setText(course.getTitle());
        txtStart.setText(course.getStartDisplay());
        txtEnd.setText(course.getEndDisplay());
        return convertView;
    }
}