package com.moengage.sample.java.inbox.custom_adapter;

import android.view.View;
import androidx.annotation.NonNull;
import com.moengage.inbox.core.MoEInboxHelper;
import com.moengage.inbox.core.model.InboxMessage;
import com.moengage.inbox.ui.adapter.InboxListAdapter;
import com.moengage.inbox.ui.adapter.ViewHolder;
import com.moengage.sample.java.databinding.CustomInboxItemViewBinding;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

/**
 * Custom {@link ViewHolder} class
 */
public class CustomInboxViewHolder extends ViewHolder {

  private static final String TAG = "CustomInboxViewHolder";

  public CustomInboxItemViewBinding binding;

  public CustomInboxViewHolder(@NonNull CustomInboxItemViewBinding binding) {
    super(binding.getRoot());
    this.binding = binding;
  }

  public void onBind(int position, @NotNull InboxMessage inboxMessage,
      @NotNull InboxListAdapter inboxListAdapter) {
    try {
      Timber.v("%s onBind() : ", TAG);
      // checks if the [InboxMessage] has coupon code
      if (MoEInboxHelper.getInstance().hasCouponCode(inboxMessage)) {
        // fetches the coupon code
        binding.couponCode.setText(MoEInboxHelper.getInstance().getCouponCode(inboxMessage));
        binding.couponCode.setVisibility(View.VISIBLE);
      }
      binding.inboxTitle.setText(inboxMessage.getTextContent().getTitle());
      binding.inboxMessage.setText(inboxMessage.getTextContent().getMessage());

      binding.inboxDelete.setOnClickListener(v -> {
        // Deletes the InboxMessage item from Notification Center and removes the item
        // from the list
        inboxListAdapter.deleteItem(position, inboxMessage);
      });

      binding.getRoot().setOnClickListener(v -> {
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
