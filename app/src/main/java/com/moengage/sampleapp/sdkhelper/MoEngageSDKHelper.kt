package com.moengage.sampleapp.sdkhelper

import android.app.Application
import android.content.Context
import com.moengage.cards.core.model.Card
import com.moengage.inapp.model.SelfHandledCampaignData
import com.moengage.inbox.core.model.InboxMessage
import com.moengage.sampleapp.domain.model.CartLine
import com.moengage.sampleapp.domain.model.Fulfilment
import com.moengage.sampleapp.domain.model.MenuCategory
import com.moengage.sampleapp.domain.model.MenuItem
import com.moengage.sampleapp.domain.model.Order

/**
 * The one and only place Brew Bar talks to the MoEngage SDK — the entry point for the whole
 * integration. Screens and view models call [MoEngageSDKHelper]; they never import
 * `com.moengage.*` themselves.
 *
 * This file is the index: every SDK capability the app uses appears below as a one-line
 * delegation, so the surface can still be audited at a glance. The implementations live in
 * `internal` objects next to it, one per feature, and are not reachable from the rest of the app:
 *
 * - [MoEngageInitialiser] — SDK construction and configuration
 * - [MoEngageUser] — identity and user attributes, including the attribute-key dictionary
 * - [AppEvents] — the event dictionary: every event name, attribute key and `trackX` call
 * - [MoEngagePush] — push permission and push callbacks
 * - [MoEngageInApp] — native and self-handled in-app campaigns
 * - [MoEngageInbox] — the notification inbox
 * - [MoEngageGeofence] — location-triggered campaigns
 * - [MoEngageCards] — the cards feed behind Self-handled cards
 * - [guarded] — the shared "never crash the sample app" wrapper every call goes through
 */
object MoEngageSDKHelper {

    // ── Initialisation ──────────────────────────────────────────────────────────────

    /** True when `local.properties` actually carries an App ID. */
    val isConfigured: Boolean get() = MoEngageInitialiser.isConfigured

    /** Called from `BrewBarApp.onCreate()`. */
    fun initialise(application: Application) = MoEngageInitialiser.initialise(application)

    // ── Identity & user attributes ──────────────────────────────────────────────────

    /** Screen 2 — after OTP verification (or the Google button). */
    fun onLoginSucceeded(context: Context) = MoEngageUser.onLoginSucceeded(context)

    /** Screen 10 — the taste profile is mirrored onto the MoEngage user. */
    fun syncTasteProfile(context: Context) = MoEngageUser.syncTasteProfile(context)

    /** Screen 10 — the two in-app notification-category toggles. */
    fun setNotificationPreference(context: Context, key: String, enabled: Boolean) =
        MoEngageUser.setNotificationPreference(context, key, enabled)

    /** Screen 10 — "Log out". */
    fun logout(context: Context) = MoEngageUser.logout(context)

    // ── Event dictionary ────────────────────────────────────────────────────────────

    fun trackMenuViewed(context: Context, category: MenuCategory) = AppEvents.trackMenuViewed(context, category)

    fun trackCategoryBrowsed(context: Context, category: MenuCategory) =
        AppEvents.trackCategoryBrowsed(context, category)

    fun trackItemViewed(context: Context, item: MenuItem) = AppEvents.trackItemViewed(context, item)

    fun trackAddToCart(
        context: Context,
        item: MenuItem,
        size: String,
        milk: String,
        addOns: List<String>,
        amount: Int,
    ) = AppEvents.trackAddToCart(context, item, size, milk, addOns, amount)

    fun trackCartViewed(context: Context, lines: List<CartLine>, amount: Int) =
        AppEvents.trackCartViewed(context, lines, amount)

    fun trackCheckoutStarted(context: Context, amount: Int, fulfilment: Fulfilment, coupon: String?) =
        AppEvents.trackCheckoutStarted(context, amount, fulfilment, coupon)

    fun trackOrderPlaced(context: Context, order: Order) = AppEvents.trackOrderPlaced(context, order)

    fun trackOrderPickedUp(context: Context, orderId: String) = AppEvents.trackOrderPickedUp(context, orderId)

    fun trackReorderTapped(context: Context, item: String, orderId: String) =
        AppEvents.trackReorderTapped(context, item, orderId)

    fun trackRewardRedeemed(context: Context, reward: String) = AppEvents.trackRewardRedeemed(context, reward)

    /** Mirrors a notification open into a named event so the demo dashboard shows one funnel. */
    fun trackNotificationOpened(context: Context, campaignId: String?, deeplink: String?) =
        AppEvents.trackNotificationOpened(context, campaignId, deeplink)

    fun trackInAppCtaClicked(context: Context, campaignId: String?, cta: String) =
        AppEvents.trackInAppCtaClicked(context, campaignId, cta)

    // ── Push ────────────────────────────────────────────────────────────────────────

    /** Screen 3 CTA. On Android 13+ this surfaces the OS `POST_NOTIFICATIONS` dialog. */
    fun requestPushPermission(context: Context) = MoEngagePush.requestPermission(context)

    /** Reports the user's answer to the OS prompt back to MoEngage. Call for both outcomes. */
    fun recordPushPermissionResponse(context: Context, granted: Boolean) =
        MoEngagePush.recordPermissionResponse(context, granted)

    /** Profile toggle when the OS has blocked notifications. */
    fun openNotificationSettings(context: Context) = MoEngagePush.openNotificationSettings(context)

    /** Registered once from `BrewBarApp`; routes order-tracking pushes to the app. */
    fun registerPushCallbacks() = MoEngagePush.registerCallbacks()

    // ── In-app messages ─────────────────────────────────────────────────────────────

    /** InApp. Called from the screens that are campaign targets. */
    fun showInApp(context: Context) = MoEngageInApp.showInApp(context)

    /** Nudge. Called from the screens that are nudge campaign targets. */
    fun showNudge(context: Context) = MoEngageInApp.showNudge(context)

    /** Scopes which in-app contexts are eligible. Called on every navigation. */
    fun setInAppContext(screen: String) = MoEngageInApp.setContext(screen)

    /** Pull the self-handled campaign behind the Menu promo card; null means nothing targeted. */
    fun fetchSelfHandledInApp(context: Context, onResult: (SelfHandledCampaignData?) -> Unit) =
        MoEngageInApp.fetchSelfHandled(context, onResult)

    /** Impression, click and dismissal must be reported or campaign stats go wrong. */
    fun selfHandledShown(context: Context, campaign: SelfHandledCampaignData) =
        MoEngageInApp.selfHandledShown(context, campaign)

    fun selfHandledClicked(context: Context, campaign: SelfHandledCampaignData) =
        MoEngageInApp.selfHandledClicked(context, campaign)

    fun selfHandledDismissed(context: Context, campaign: SelfHandledCampaignData) =
        MoEngageInApp.selfHandledDismissed(context, campaign)

    /** Push-based counterpart to [fetchSelfHandledInApp]: campaign-triggered payloads. */
    fun setSelfHandledListener(onCampaign: (SelfHandledCampaignData?) -> Unit) =
        MoEngageInApp.setSelfHandledListener(onCampaign)

    /** Lets the in-app / inbox layers hand a deep link to the nav graph. */
    fun setNavigationSink(sink: ((String) -> Unit)?) = MoEngageInApp.setNavigationSink(sink)

    /** Registered once from `BrewBarApp`: reports CTA clicks and routes their deep links. */
    fun registerInAppCallbacks(context: Context) = MoEngageInApp.registerCallbacks(context)

    // ── Notification inbox ──────────────────────────────────────────────────────────

    fun fetchInboxMessages(context: Context, onResult: (List<InboxMessage>) -> Unit) =
        MoEngageInbox.fetchMessages(context, onResult)

    fun fetchUnreadCount(context: Context, onResult: (Long) -> Unit) = MoEngageInbox.fetchUnreadCount(context, onResult)

    /** Tapping an inbox row marks it read and reports the click. */
    fun markInboxMessageRead(context: Context, message: InboxMessage) = MoEngageInbox.markMessageRead(context, message)

    // ── Cards ───────────────────────────────────────────────────────────────────────

    /**
     * Screen 12 — announces that the cards section is visible, which is what drives the SDK's
     * sync and delivery reporting. Callback fires once the sync settles.
     */
    fun onCardSectionLoaded(context: Context, onSyncComplete: (Boolean) -> Unit) =
        MoEngageCards.onSectionLoaded(context, onSyncComplete)

    /** Required counterpart to [onCardSectionLoaded]. */
    fun onCardSectionUnloaded(context: Context) = MoEngageCards.onSectionUnloaded(context)

    /** Every delivered card, across all categories. */
    fun fetchCards(context: Context, onResult: (List<Card>) -> Unit) = MoEngageCards.fetchCards(context, onResult)

    /** The cards in one dashboard-defined category. */
    fun fetchCards(context: Context, category: String, onResult: (List<Card>) -> Unit) =
        MoEngageCards.fetchCards(context, category, onResult)

    /** Forces a server sync rather than reading the local store. */
    fun refreshCards(context: Context, onSyncComplete: (Boolean) -> Unit) =
        MoEngageCards.refresh(context, onSyncComplete)

    /** Impression, reported per card actually rendered. */
    fun cardShown(context: Context, card: Card) = MoEngageCards.cardShown(context, card)

    /** Click, attributed to the widget the user tapped. */
    fun cardClicked(context: Context, card: Card, widgetId: Int) = MoEngageCards.cardClicked(context, card, widgetId)

    // ── Geofence ────────────────────────────────────────────────────────────────────

    /** Registered once from `BrewBarApp`, so a geofence hit is visible in logcat. */
    fun registerGeofenceCallbacks() = MoEngageGeofence.registerHitListener()

    /** Starts geofence monitoring. Call only once location permission has been granted. */
    fun startGeofenceMonitoring(context: Context) = MoEngageGeofence.startMonitoring(context)

    /** Stops geofence monitoring. */
    fun stopGeofenceMonitoring(context: Context) = MoEngageGeofence.stopMonitoring(context)
}
