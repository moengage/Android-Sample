package com.moengage.sample.java;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.moe.pushlibrary.MoEHelper;
import com.moengage.core.Properties;
import com.moengage.inbox.ui.MoEInboxUiHelper;
import com.moengage.inbox.ui.view.InboxActivity;
import com.moengage.sample.java.HomeListAdapter.OnHomeCategorySelected;
import com.moengage.sample.java.databinding.ActivityMainBinding;
import com.moengage.sample.java.inbox.custom_adapter.CustomInboxAdapter;
import com.moengage.sample.java.inbox.custom_inbox.NotificationsActivity;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnHomeCategorySelected {

  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setSupportActionBar(binding.toolbar);
    initView();
    trackSampleData();
  }

  private void initView() {
    ArrayList<HomeCategory> categoryList = new ArrayList<>();
    categoryList.add(
        new HomeCategory(FeaturesCategory.INBOX_DEFAULT_UI, "Default Notification Center"));
    categoryList.add(
        new HomeCategory(FeaturesCategory.INBOX_CUSTOM_INBOX_ADAPTER, "Custom InboxAdapter"));
    categoryList.add(
        new HomeCategory(FeaturesCategory.INBOX_CUSTOM_INBOX, "Custom Inbox - Self handled"));

    binding.categoryList.setLayoutManager(new LinearLayoutManager(this));
    binding.categoryList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    binding.categoryList.setAdapter(new HomeListAdapter(categoryList, this));
  }

  private void trackSampleData() {
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

  @Override public void onHomeCategorySelected(FeaturesCategory category) {
    switch (category) {
      case INBOX_DEFAULT_UI:
        startActivity(new Intent(this, InboxActivity.class));
        break;

      case INBOX_CUSTOM_INBOX_ADAPTER:
        // set the customised InboxAdapter and launch InboxActivity
        MoEInboxUiHelper.getInstance().setInboxAdapter(new CustomInboxAdapter());
        startActivity(new Intent(this, InboxActivity.class));
        break;

      case INBOX_CUSTOM_INBOX:
        startActivity(new Intent(this, NotificationsActivity.class));
        break;
    }
  }
}

