package com.moengage.sample.java.inbox.custom_adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.moengage.inbox.core.MoEInboxHelper;
import com.moengage.inbox.core.model.InboxMessage;
import com.moengage.inbox.ui.adapter.InboxListAdapter;
import com.moengage.inbox.ui.adapter.ViewHolder;
import com.moengage.sample.java.R;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

/**
 * Custom {@link ViewHolder} class
 */
public class CustomInboxViewHolder extends ViewHolder {

  private static final String TAG = "CustomInboxViewHolder";

  private final View view;
  private final TextView messageView;
  private final TextView titleView;
  private final Button couponCode;
  private final ImageButton deleteButton;

  public CustomInboxViewHolder(@NonNull View view) {
    super(view);
    this.view = view;
    messageView = view.findViewById(R.id.inboxMessage);
    titleView = view.findViewById(R.id.inboxTitle);
    couponCode = view.findViewById(R.id.couponCode);
    deleteButton = view.findViewById(R.id.inboxDelete);
  }

  public void onBind(int position, @NotNull InboxMessage inboxMessage,
      @NotNull InboxListAdapter inboxListAdapter) {
    try {
      Timber.v("%s onBind() : ", TAG);
      // checks if the [InboxMessage] has coupon code
      if (MoEInboxHelper.getInstance().hasCouponCode(inboxMessage)) {
        // fetches the coupon code
        couponCode.setText(MoEInboxHelper.getInstance().getCouponCode(inboxMessage));
        couponCode.setVisibility(View.VISIBLE);
      }
      titleView.setText(inboxMessage.getTextContent().getTitle());
      messageView.setText(inboxMessage.getTextContent().getMessage());

      deleteButton.setOnClickListener(v -> {
        // Deletes the InboxMessage item from Notification Center and removes the item
        // from the list
        inboxListAdapter.deleteItem(position, inboxMessage);
      });

      view.setOnClickListener(v -> {
        //Notifies the InboxMessage clicked event to MoEngage SDK
        //NOTE: When you are using custom InboxAdapter and want the SDK to handle the
        // item click action, `InboxListAdapter#onItemClicked` must be invoked.
        inboxListAdapter.onItemClicked(position, inboxMessage);
      });
    } catch (Exception e) {
      Timber.e("%s onBind() : ", TAG);
    }
  }
}
