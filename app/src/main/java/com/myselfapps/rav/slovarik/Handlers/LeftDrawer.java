package com.myselfapps.rav.slovarik.Handlers;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.myselfapps.rav.slovarik.R;

public class LeftDrawer {
    private Drawer result;
    private AccountHeader accountHeader;
    private Intent intent;
    private Activity currentActivity;
    public Drawer getResult() {
        return result;
    }


    public AccountHeader getAccountHeader() {
        return accountHeader;
    }

    public LeftDrawer(Toolbar toolbar, Activity activity) {
        currentActivity = activity;
        initAccountHeader(currentActivity);
        initDrawer(toolbar, currentActivity);
    }

    public void initDrawer(Toolbar toolbar, final Activity currentActivity) {
        result = new DrawerBuilder()
                .withActivity(currentActivity)
                .withAccountHeader(accountHeader)
                .withToolbar(toolbar)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(1)
                                .withName(R.string.menu_drawer_item_Dictionary)
                                .withBadge(getWordsCount()),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.menu_drawer_item_Phrases)
                                .withIdentifier(2)
                                .withBadge(getPhrasesCount()),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.menu_drawer_item_Verbs)
                                .withIdentifier(10),
                        new SecondaryDrawerItem()
                                .withName(R.string.menu_drawer_item_BackupMen)
                                .withIdentifier(12),
                        new SecondaryDrawerItem()
                                .withName(R.string.menu_drawer_item_AboutUs)
                                .withIdentifier(11)
                )
                .withOnDrawerItemClickListener((Drawer.OnDrawerItemClickListener)currentActivity)
                .build();

    }

    private String getPhrasesCount() {
        DatabaseHandler db = new DatabaseHandler(currentActivity);
        return db.getPhraseCount()+"";
    }

    private String getWordsCount() {
        DatabaseHandler db = new DatabaseHandler(currentActivity);
        return db.getWordsCount()+"";
    }

    public void initAccountHeader(Activity activity) {
        accountHeader = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.drawble_pic)
                .build();
    }

    public void drawerClose(){
        result.closeDrawer();
    }



}
