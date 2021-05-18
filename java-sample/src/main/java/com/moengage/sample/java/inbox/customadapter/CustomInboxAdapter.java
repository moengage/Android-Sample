package com.moengage.sample.java.inbox.customadapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.moengage.inbox.core.model.InboxMessage;
import com.moengage.inbox.ui.adapter.InboxAdapter;
import com.moengage.inbox.ui.adapter.InboxListAdapter;
import com.moengage.inbox.ui.adapter.ViewHolder;
import com.moengage.sample.java.databinding.CustomInboxItemViewBinding;
import org.jetbrains.annotations.NotNull;

/**
 * To use your own custom InboxAdapter, extend [InboxAdapter]and bind
 * your customised [ViewHolder] class.
 */
public class CustomInboxAdapter extends InboxAdapter {

  @Override public long getItemId(int i) {
    return i;
  }

  @Override public int getItemViewType(int i, @NotNull InboxMessage inboxMessage) {
    return 0;
  }

  @Override public void onBindViewHolder(@NotNull ViewHolder viewHolder, int i,
      @NotNull InboxMessage inboxMessage, @NotNull InboxListAdapter inboxListAdapter) {
    ((CustomInboxViewHolder) viewHolder).onBind(i, inboxMessage, inboxListAdapter);
  }

  @NotNull @Override public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
    return new CustomInboxViewHolder(
        CustomInboxItemViewBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false));
  }
}
