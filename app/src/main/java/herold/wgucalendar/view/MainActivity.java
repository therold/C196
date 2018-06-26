package herold.wgucalendar.view;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.List;

import herold.wgucalendar.R;
import herold.wgucalendar.data.TermData;
import herold.wgucalendar.model.Term;
import herold.wgucalendar.view.AddTermActivity;
import herold.wgucalendar.view.TermAdapter;

public class MainActivity extends ListActivity {

    private Button btnAddTerm;
    private TermData datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new TermData(this);
        datasource.open();

        List<Term> terms = datasource.all();
        TermAdapter adapter = new TermAdapter(this, terms);
        setListAdapter(adapter);
    }

    public void onClick(View view) {
        ArrayAdapter<Term> adapter = (ArrayAdapter<Term>) getListAdapter();
        switch (view.getId()) {
            case R.id.btnAddTerm:
                Intent intent = new Intent(this, AddTermActivity.class);
                startActivity(intent);
                //Term term = datasource.createTerm("New Term", "s", "e");
                //adapter.add(term);
                break;
            case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    //term = (Term) getListAdapter().getItem(0);
                    //datasource.deleteTerm(term);
                    //adapter.remove(term);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}
