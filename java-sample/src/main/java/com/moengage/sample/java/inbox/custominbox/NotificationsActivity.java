package com.moengage.sample.java.inbox.custominbox;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.moengage.inbox.core.MoEInboxHelper;
import com.moengage.inbox.core.listener.OnMessagesAvailableListener;
import com.moengage.inbox.core.listener.UnClickedCountListener;
import com.moengage.inbox.core.model.InboxMessage;
import com.moengage.sample.java.R;
import com.moengage.sample.java.databinding.ActivityNotificationsBinding;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class NotificationsActivity extends AppCompatActivity
    implements @NotNull OnMessagesAvailableListener, @NotNull UnClickedCountListener {

  private NotificationAdapter adapter;
  private RecyclerView recyclerView;
  private TextView emptyNotificationCenter;
  private Long unreadCount;
  private ActivityNotificationsBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    initView();
  }

  @Override protected void onResume() {
    super.onResume();
    //Fetch the unclicked message count
    MoEInboxHelper.getInstance().getUnClickedMessagesCountAsync(this, this);
    loadData();
  }

  private void loadData() {
    //Fetch all inbox messages asynchronously
    MoEInboxHelper.getInstance().fetchAllMessagesAsync(this, this);
  }

  private void initView() {
    recyclerView = findViewById(R.id.notificationList);
    adapter = new NotificationAdapter();

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
        DividerItemDecoration.VERTICAL);
    recyclerView.addItemDecoration(dividerItemDecoration);
    recyclerView.setAdapter(adapter);

    emptyNotificationCenter = findViewById(R.id.emptyMessage);
  }

  /**
   * callback for receiving the inbox messages list
   * @param list - List of {@link InboxMessage}
   */
  @Override public void onMessagesAvailable(@NotNull List<InboxMessage> list) {
    if (list.isEmpty()) {
      emptyNotificationCenter.setVisibility(View.VISIBLE);
      recyclerView.setVisibility(View.GONE);
    } else {
      emptyNotificationCenter.setVisibility(View.GONE);
      recyclerView.setVisibility(View.VISIBLE);
      adapter.setData(list);
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    View view = menu.findItem(R.id.badge).getActionView();
    TextView unClickedCountTv = view.findViewById(R.id.tvUnClickedCount);
    unClickedCountTv.setText(unreadCount.toString());
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * callback for receiving unclicked count
   * @param count - unclicked message count
   */
  @Override public void onCountAvailable(long count) {
    unreadCount = count;
    invalidateOptionsMenu();
  }
}