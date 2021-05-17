package com.moengage.sample.java;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.moe.pushlibrary.MoEHelper;
import com.moengage.core.Properties;
import com.moengage.inbox.ui.MoEInboxUiHelper;
import com.moengage.inbox.ui.view.InboxActivity;
import com.moengage.sample.java.inbox.custom_inbox.NotificationsActivity;
import com.moengage.sample.java.inbox.custom_adapter.CustomInboxAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private FeatureListAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    initUI();
    //building event attributes
    Properties properties = new Properties()
        .addAttribute("sign-up-date", new Date())
        .addAttribute("type", "user-pass");
    //sample track event
    MoEHelper.getInstance(getApplicationContext()).trackEvent("Sign Up", properties);

    //identify user uniquely across installs and devices
    //this id should be the one the app uses to identify user in your system
    //should be set only after log-in
    MoEHelper.getInstance(getApplicationContext()).setUniqueId("androidsample");

    //tracking user attributes samples
    MoEHelper.getInstance(getApplicationContext()).setFullName("Michael Jordan");
    MoEHelper.getInstance(getApplicationContext()).setEmail("a@b.com");
    MoEHelper.getInstance(getApplicationContext()).setUserAttribute("last ordered", new Date());
  }

  private void initUI() {
    ExpandableListView expandableFeaturesList = findViewById(R.id.expandableListView);

    adapter = new FeatureListAdapter(this, getFeaturesList(), getData());
    expandableFeaturesList.setAdapter(adapter);

    expandableFeaturesList.setOnChildClickListener(
        (parent, v, groupPosition, childPosition, id) -> {
          handleOnChildClickEvent(groupPosition, childPosition);
          return false;
        });
  }

  private HashMap<String, List<String>> getData() {
    List<String> features = getFeaturesList();
    HashMap<String, List<String>> expandableFeatureList = new HashMap<String, List<String>>();

    List<String> inbox = new ArrayList<>();
    inbox.add("Custom Inbox");
    inbox.add("Custom InboxAdapter");

    expandableFeatureList.put(features.get(0), inbox);

    return expandableFeatureList;
  }

  private List<String> getFeaturesList() {
    List<String> features = new ArrayList<>();

    features.add("Inbox");

    return features;
  }

  public void handleOnChildClickEvent(int groupPosition, int childPosition) {
    String childItem = adapter.getChild(groupPosition, childPosition);
    switch (childItem) {
      case "Custom Inbox":
        startActivity(new Intent(this, NotificationsActivity.class));
        break;
      case "Custom InboxAdapter":
        // set the customised InboxAdapter and launch InboxActivity
        MoEInboxUiHelper.getInstance().setInboxAdapter(new CustomInboxAdapter());
        startActivity(new Intent(this, InboxActivity.class));
        break;
    }
  }
}
