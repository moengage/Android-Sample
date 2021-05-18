package com.moengage.sample.java.inbox.custom_inbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.moengage.inbox.core.MoEInboxHelper;
import com.moengage.inbox.core.model.InboxMessage;
import com.moengage.sample.java.R;
import com.moengage.sample.java.inbox.custom_inbox.NotificationAdapter.NotificationItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for binding custom inbox item view
 *
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationItem> {

  private List<InboxMessage> messages = new ArrayList<>();

  public void setData(List<InboxMessage> messages) {
    this.messages = messages;
    notifyDataSetChanged();
  }

  @NonNull @Override
  public NotificationItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
    return new NotificationItem(view);
  }

  @Override public void onBindViewHolder(@NonNull NotificationItem holder, int position) {
    InboxMessage inboxMessage = messages.get(position);
    holder.notificationTitle.setText(inboxMessage.getTextContent().getTitle());
    holder.notificationMessage.setText(inboxMessage.getTextContent().getMessage());

    if (MoEInboxHelper.getInstance().hasCouponCode(inboxMessage)) {
      holder.notifCouponCode.setText(MoEInboxHelper.getInstance().getCouponCode(inboxMessage));
      holder.notifCouponCode.setVisibility(View.VISIBLE);
    } else {
      holder.notifCouponCode.setVisibility(View.GONE);
    }

    holder.unClickedIndicator.setVisibility(
        inboxMessage.isClicked() ? View.INVISIBLE : View.VISIBLE);

    holder.notificationDel.setOnClickListener(
        v -> {
          //Delete the InboxMessage from inbox
          MoEInboxHelper.getInstance().deleteMessage(holder.itemView.getContext(), messages.get(position));
          messages.remove(position);
          notifyDataSetChanged();
        }
    );

    holder.itemView.setOnClickListener(v -> {
      // mark the inbox message clicked and track the click event
      MoEInboxHelper.getInstance()
          .trackMessageClicked(holder.itemView.getContext(), inboxMessage);
      notifyDataSetChanged();
    });
  }

  @Override public int getItemCount() {
    return messages.size();
  }

  public static class NotificationItem extends RecyclerView.ViewHolder {

    public TextView notificationTitle;
    public TextView notificationMessage;
    public ImageButton notificationDel;
    public Button notifCouponCode;
    public View unClickedIndicator;

    public NotificationItem(@NonNull View itemView) {
      super(itemView);
      notificationTitle = itemView.findViewById(R.id.notificationTitle);
      notificationMessage = itemView.findViewById(R.id.notificationMessage);
      notificationDel = itemView.findViewById(R.id.inboxDelete);
      notifCouponCode = itemView.findViewById(R.id.notifCouponCode);
      unClickedIndicator = itemView.findViewById(R.id.unClickedIndicator);
    }
  }
}
