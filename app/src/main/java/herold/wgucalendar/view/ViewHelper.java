package herold.wgucalendar.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import herold.wgucalendar.R;

public class ViewHelper {

    public static void switchToActivity(Context context, Class c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    public static NavigationView.OnNavigationItemSelectedListener getNavigationListener(Context context, DrawerLayout drawerLayout) {
        final Context c = context;
        final DrawerLayout d = drawerLayout;
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                itemSelected(c, d, menuItem);
                return true;
            }
        };
    }

    private static void itemSelected(Context context, DrawerLayout drawerLayout, MenuItem menuItem) {
        drawerLayout.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.nav_add_term:
                ViewHelper.switchToActivity(context, AddTermActivity.class);
                break;
            case R.id.nav_all_term:
                ViewHelper.switchToActivity(context, AllTermActivity.class);
                break;
        }
    }

}
