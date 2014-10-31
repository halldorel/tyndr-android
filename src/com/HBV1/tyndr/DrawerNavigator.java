package com.HBV1.tyndr;

import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * 
 * @author Björn Sigurðsson
 * 18. september, 2014
 * Klasi sér um að activity sem sett er sem viðfang hafi skúffu-navigator.
 * 
 */
public class DrawerNavigator implements ListView.OnItemClickListener {
	private ListView drawerNavigator;
	private DrawerLayout drawerLayout;
    public ActionBarDrawerToggle mDrawerToggle;
	private Activity parent;
	private boolean finishActivity;
	
	/**
	 * @param parent er það activity sem á að fá skúffu-navigator.
	 * parent hefur skúffu-navigator.
	 */
	public DrawerNavigator(Activity parent) {
		this.parent = parent;
		finishActivity = false;
		initializeDrawer();
	}
	
	/**
	 * @param finish segir hvort enda eigi það activity sem farið er úr.
	 * Activity mun enda eða ekki þegar valið er nýtt activity í skúffu-navigator.
	 */
	public void setFinishActivity(boolean finish) {
		finishActivity = finish;
	}
	
	/**
	 * Skúffu-navigator er frumstilltur.
	 */
	private void initializeDrawer() {
		parent.getActionBar().setDisplayHomeAsUpEnabled(true);
		parent.getActionBar().setHomeButtonEnabled(true);
		drawerNavigator = (ListView) parent.findViewById(R.id.drawer_navigator);
		drawerLayout = (DrawerLayout) parent.findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(
                parent,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer_white,  /* nav drawer icon to replace 'Up' caret */
                R.string.hello_world,  /* "open drawer" description */
                R.string.hello_world  /* "close drawer" description */
                ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                parent.getActionBar().setTitle(parent.getTitle());
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                parent.getActionBar().setTitle("hallo hallo");
            }
        };
        
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(mDrawerToggle);
		String[] operators = new String[]{
				parent.getResources().getString(R.string.tyntTitle),
				parent.getResources().getString(R.string.fundidTitle)
				};
		drawerNavigator.setAdapter(new ArrayAdapter<String>(
				parent, android.R.layout.simple_list_item_1, operators));
		drawerNavigator.setOnItemClickListener(this);
	}

	/**
	 * Takkar í skúffu-navigator hafa virkni.
	 */
	@Override
	public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
//		String op = ((TextView) view).getText().toString();
//		if (op.equals(parent.getResources().getString(R.string.addition))) {
//			Intent intent = new Intent(parent, AdditionActivity.class);
//			parent.startActivity(intent);
//			if (finishActivity) parent.finish();
//		} else if (op.equals(parent.getResources().getString(R.string.multiplication))) {
//			Intent intent = new Intent(parent, MultiplicationActivity.class);
//			parent.startActivity(intent);
		if (finishActivity) parent.finish();
//		}
		drawerLayout.closeDrawer(drawerNavigator);
	}
	
	public boolean flupp(MenuItem item) {
		mDrawerToggle.onOptionsItemSelected(item);
		return true;
	}
}
