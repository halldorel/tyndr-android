package com.HBV1.tyndr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
/*
 * @author: Tomas Karl Kjartansson<tkk4@hi.is>
 * @version: 0.1
 * @since: 2014-10-13
 */

public class OpnunarGluggi extends Activity {
	private DrawerNavigator drawerNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opnunar_gluggi);
		drawerNavigator = new DrawerNavigator(this);
		drawerNavigator.setFinishActivity(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.opnunar_gluggi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
		if (drawerNavigator.openItem(item)) {
			return true;
		}
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*
     * fallid sem skodaAuglysngar-takkinn kallar a
     * 
     * @param view hluturinn sem kallar a fallid
     */
    public void auglysingar(View view)
    {
    	Intent skoda = new Intent(this, Adds.class);
    	startActivity(skoda);
    }
    /*
     * fallid sem fundid-takkinn kallar a
     * 
     * @param view hluturinn sem kallar a fallid
     */
    public void fundid(View view)
    {
    	Intent fannst = new Intent(this,Form.class);
    	fannst.putExtra("titill", "fundid");
    	startActivity(fannst);
    }
    /*
     * fallid sem tynt-takkinn kallar a
     * 
     * @param view hluturinn sem kallar a fallid
     */
    public void tynt(View view)
    {
    	Intent fannst = new Intent(this,Form.class);
    	fannst.putExtra("titill", "tynt");
    	startActivity(fannst);
    }
}


