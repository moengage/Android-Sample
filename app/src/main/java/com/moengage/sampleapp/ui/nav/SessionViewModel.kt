package com.moengage.sampleapp.ui.nav

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moengage.inapp.model.SelfHandledCampaignData
import com.moengage.sampleapp.data.CartRepository
import com.moengage.sampleapp.data.OrderRepository
import com.moengage.sampleapp.data.ProfileRepository
import com.moengage.sampleapp.domain.model.Bill
import com.moengage.sampleapp.domain.model.CartLine
import com.moengage.sampleapp.domain.model.CupPreference
import com.moengage.sampleapp.domain.model.Fulfilment
import com.moengage.sampleapp.domain.model.LocationState
import com.moengage.sampleapp.domain.model.MenuCategory
import com.moengage.sampleapp.domain.model.NotificationPreference
import com.moengage.sampleapp.domain.model.Order
import com.moengage.sampleapp.domain.model.OrderStage
import com.moengage.sampleapp.domain.model.PromoPayload
import com.moengage.sampleapp.ordertracking.primeOrderTracking
import com.moengage.sampleapp.ordertracking.stopOrderTracking
import com.moengage.sampleapp.push.BrewNotifications
import com.moengage.sampleapp.sdkhelper.MoEngageSDKHelper
import com.moengage.sampleapp.ui.inapp.toPromoPayload
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

/** The dark promo card on Menu home, hydrated from a self-handled in-app campaign. */
data class PromoState(val payload: PromoPayload, val campaign: SelfHandledCampaignData?)

/** The simulated push shade shown on Order status (demo only — real pushes come from FCM). */
data class SimulatedPush(
    val title: String,
    val body: String,
    val timestamp: String = "Brew Bar · now",
    val deeplink: String,
)

data class SessionState(
    val category: MenuCategory = MenuCategory.Coffee,
    val pushGranted: Boolean = false,
    /** True once the user has denied the OS prompt — Profile shows "Blocked at OS level". */
    val pushBlocked: Boolean = false,
    val promo: PromoState? = null,
    /**
     * True when the promo card was rendered from DemoTools rather than a live campaign, so
     * the Menu's own fetch does not wipe it on the way in.
     */
    val promoForcedByDemo: Boolean = false,
    val seenInAppThisSession: Boolean = false,
    val cart: List<CartLine> = CartRepository.seedLines(),
    val fulfilment: Fulfilment = Fulfilment.Pickup,
    val cupPreference: CupPreference = CupPreference.OwnCup,
    val paymentMethodId: String = com.moengage.sampleapp.data.PaymentRepository.DEFAULT_METHOD,
    val notificationPreferences: List<NotificationPreference> = ProfileRepository.notificationPreferences,
    /** Owned by the inbox SDK — see [SessionViewModel.refreshUnreadCount]. 0 until it answers. */
    val unreadCount: Int = 0,
    val lastOrder: Order = OrderRepository.latest(),
    val demoToolsVisible: Boolean = false,
    val simulatedPush: SimulatedPush? = null,
    /** Screen 3's simulated OS dialog, used when the runtime permission is unavailable. */
    /** Location grants and geofence monitoring, as Profile's location card reads them. */
    val location: LocationState = LocationState(),
    val permissionDialogVisible: Boolean = false,
) {
    val bill: Bill
        get() {
            val itemTotal = cart.sumOf { it.amount * it.quantity }
            val taxes = (itemTotal * CartRepository.TAX_RATE / 100f).roundToInt()
            val discount = (itemTotal * CartRepository.COUPON_PERCENT / 100f).roundToInt()
            return Bill(
                itemTotal = itemTotal,
                taxes = taxes,
                couponCode = CartRepository.COUPON_CODE,
                discount = discount,
                // The design's bill totals ₹476 with "bring my own cup" selected, i.e. the
                // cup preference is recorded but not billed. Kept explicit rather than silent.
                cupDiscount = 0,
            )
        }
}

/**
 * Shared state across the whole flow: permission mirror, the live self-handled campaign,
 * the cart, and the loyalty balance. Feature screens keep their own local UI state.
 */
class SessionViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(SessionState())
    val state: StateFlow<SessionState> = _state.asStateFlow()

    private val context get() = getApplication<Application>().applicationContext

    // ── Navigation-driven ───────────────────────────────────────────────────────────

    fun selectCategory(category: MenuCategory) {
        if (_state.value.category == category) return
        _state.update { it.copy(category = category) }
        MoEngageSDKHelper.trackCategoryBrowsed(context, category)
    }

    fun onMenuViewed() {
        MoEngageSDKHelper.trackMenuViewed(context, _state.value.category)
    }

    // ── Identity ────────────────────────────────────────────────────────────────────

    fun onLoginSucceeded() {
        MoEngageSDKHelper.onLoginSucceeded(context)
        MoEngageSDKHelper.syncTasteProfile(context)
    }

    fun logout() {
        MoEngageSDKHelper.logout(context)
        _state.value = SessionState()
    }

    // ── Push permission ─────────────────────────────────────────────────────────────

    /**
     * Re-reads the OS notification state into [SessionState.pushGranted] and returns it, so callers
     * can decide whether there is anything left to ask the user for.
     *
     * Deliberately does not touch [SessionState.pushBlocked]: "not granted" here could equally mean
     * never asked, and only an actual denial should put the Profile row into its blocked state.
     * Nothing is reported to MoEngage either — [onPushPermissionResult] does that, and only for a
     * real answer to a real prompt.
     */
    fun refreshPushPermissionState(): Boolean {
        val enabled = BrewNotifications.areEnabled(context)
        _state.update { it.copy(pushGranted = enabled) }
        return enabled
    }

    fun showPermissionDialog(visible: Boolean) {
        _state.update { it.copy(permissionDialogVisible = visible) }
    }

    /**
     * The answer to the OS prompt, from either the runtime launcher (API 33+) or the stand-in
     * dialog below it. Also mirrored to MoEngage so opt-in is segmentable.
     *
     * A denial sets [SessionState.pushBlocked], which is what turns the Profile row into the
     * "Blocked at OS level → Open settings" state.
     */
    fun onPushPermissionResult(granted: Boolean) {
        _state.update { it.copy(pushGranted = granted, pushBlocked = !granted) }
        MoEngageSDKHelper.recordPushPermissionResponse(context, granted)
        if (granted) {
            setNotificationPreference(ORDER_UPDATES_KEY, true)
        }
    }

    fun setNotificationPreference(key: String, enabled: Boolean) {
        _state.update { state ->
            state.copy(
                notificationPreferences = state.notificationPreferences.map {
                    if (it.key == key) it.copy(enabled = enabled) else it
                },
            )
        }
        MoEngageSDKHelper.setNotificationPreference(context, key, enabled)
    }

    fun openNotificationSettings() = MoEngageSDKHelper.openNotificationSettings(context)

    // ── Location & geofence ─────────────────────────────────────────────────────────

    /**
     * Re-reads the three location grants into state and starts geofence monitoring the moment they
     * satisfy the SDK.
     *
     * Called on every app open and again whenever the app returns to the foreground, so a grant
     * made on the system settings page takes effect without a restart.
     */
    fun refreshLocationPermissions() {
        _state.update { state ->
            state.copy(
                location = state.location.copy(
                    approximate = hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION),
                    precise = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION),
                    background = hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    backgroundRequired = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q,
                ),
            )
        }
        startGeofenceMonitoringIfPermitted()
    }

    /**
     * True when the app open should raise the OS prompt: the foreground grant is the only one the
     * prompt can still deliver, since Android 11+ auto-denies a background request without showing
     * anything. Once precise is granted, "Allow all the time" is the settings link's job.
     *
     * Reads the state [refreshLocationPermissions] just wrote, so call it after a refresh.
     */
    fun needsLocationRequest(): Boolean = !_state.value.location.precise

    /** True when foreground location is granted but background is not, so a fence needs the app open. */
    fun needsBackgroundLocation(): Boolean = with(_state.value.location) {
        backgroundRequired && precise && !background
    }

    /**
     * The answer to the foreground-location prompt, `granted` being precise specifically — an
     * "Approximate" grant does not satisfy the SDK.
     *
     * Background location is a separate, later request on Android 10+; the caller chains it when
     * [needsBackgroundLocation] says so, and its answer arrives at [onBackgroundLocationResult].
     */
    fun onLocationPermissionResult(granted: Boolean) {
        if (!granted) Timber.d("location denied — geofence monitoring not started")
        onLocationPromptAnswered()
    }

    /** The answer to the follow-up background-location prompt on Android 10+. */
    fun onBackgroundLocationResult(granted: Boolean) {
        if (!granted) Timber.d("background location denied")
        onLocationPromptAnswered()
    }

    private fun onLocationPromptAnswered() {
        _state.update { it.copy(location = it.location.copy(asked = true)) }
        refreshLocationPermissions()
    }

    /**
     * Starts monitoring once the SDK's own precondition holds, and only once.
     *
     * `MoEGeofenceHelper.startGeofenceMonitoring` checks ACCESS_BACKGROUND_LOCATION on Android 10+
     * (ACCESS_FINE_LOCATION below it) and throws `PermissionMissingError` from its own worker
     * thread when it is missing — an uncatchable crash rather than a no-op, so the same check has
     * to happen here before the call.
     */
    private fun startGeofenceMonitoringIfPermitted() {
        val location = _state.value.location
        if (!location.satisfiesGeofence()) {
            _state.update { it.copy(location = it.location.copy(monitoring = false)) }
            return
        }
        if (location.monitoring) return
        MoEngageSDKHelper.startGeofenceMonitoring(context)
        _state.update { it.copy(location = it.location.copy(monitoring = true)) }
        Timber.d("location granted — geofence monitoring started")
    }

    /** Mirrors the geofence module's `Evaluator.hasPermissionForSettingFences`. */
    private fun LocationState.satisfiesGeofence(): Boolean = if (backgroundRequired) background else precise

    fun stopGeofenceMonitoring() {
        MoEngageSDKHelper.stopGeofenceMonitoring(context)
        _state.update { it.copy(location = it.location.copy(monitoring = false)) }
    }

    /**
     * App details settings. Android 11+ never grants background location from a prompt, so the
     * only route to "Allow all the time" is the settings page.
     */
    fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null),
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun hasPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    // ── In-app ──────────────────────────────────────────────────────────────────────

    /** Menu home asks for the self-handled campaign that backs the promo card. */
    fun refreshSelfHandledCampaign() {
        MoEngageSDKHelper.fetchSelfHandledInApp(context) { data ->
            if (data == null) {
                // Campaign paused or nothing targeted: leave the card absent, layout unaffected.
                // A DemoTools-forced card survives, so the demo affordance is not undone here.
                _state.update { if (it.promoForcedByDemo) it else it.copy(promo = null) }
                return@fetchSelfHandledInApp
            }
            val payload = data.toPromoPayload()
            _state.update {
                it.copy(promo = PromoState(payload, data), promoForcedByDemo = false)
            }
            MoEngageSDKHelper.selfHandledShown(context, data)
        }
    }

    /** Used by DemoTools so the card renders without a live campaign. */
    fun showFallbackPromo() {
        _state.update {
            it.copy(
                promo = PromoState(PromoPayload.FALLBACK, campaign = null),
                promoForcedByDemo = true,
            )
        }
    }

    fun onPromoClicked(): String? {
        val promo = _state.value.promo ?: return null
        promo.campaign?.let { MoEngageSDKHelper.selfHandledClicked(context, it) }
        MoEngageSDKHelper.trackInAppCtaClicked(context, promo.payload.campaignId, promo.payload.title)
        return promo.payload.deeplink
    }

    fun onPromoDismissed() {
        val promo = _state.value.promo ?: return
        promo.campaign?.let { MoEngageSDKHelper.selfHandledDismissed(context, it) }
        _state.update { it.copy(promo = null, promoForcedByDemo = false) }
    }

    /** The native in-app modal is a once-per-session moment on first arrival at Menu. */
    fun maybeShowNativeInApp() {
        if (_state.value.seenInAppThisSession) return
        _state.update { it.copy(seenInAppThisSession = true) }
        viewModelScope.launch {
            delay(NATIVE_INAPP_DELAY_MS.milliseconds)
            MoEngageSDKHelper.showInApp(context)
        }
    }

    fun showNativeInAppNow() = MoEngageSDKHelper.showInApp(context)

    // ── Cart & checkout ─────────────────────────────────────────────────────────────

    fun setFulfilment(fulfilment: Fulfilment) = _state.update { it.copy(fulfilment = fulfilment) }

    fun setCupPreference(preference: CupPreference) = _state.update { it.copy(cupPreference = preference) }

    fun setPaymentMethod(id: String) = _state.update { it.copy(paymentMethodId = id) }

    fun addLine(line: CartLine) {
        _state.update { it.copy(cart = it.cart + line) }
    }

    fun onCartViewed() {
        val state = _state.value
        MoEngageSDKHelper.trackCartViewed(context, state.cart, state.bill.toPay)
    }

    fun onCheckoutStarted() {
        val state = _state.value
        MoEngageSDKHelper.trackCheckoutStarted(
            context,
            state.bill.toPay,
            state.fulfilment,
            state.bill.couponCode,
        )
    }

    /**
     * Screen 8 → 9. Fires `Order_Placed` and — when push is granted —
     * schedules the demo notification that lands on Order status.
     */
    fun placeOrder(): Order {
        val state = _state.value
        val order = state.lastOrder.copy(
            amount = state.bill.toPay,
            mode = state.fulfilment,
            stage = OrderStage.Brewing,
            lines = state.cart.map {
                com.moengage.sampleapp.domain.model.OrderLine(it.name, it.amount)
            },
        )
        _state.update { it.copy(lastOrder = order) }
        MoEngageSDKHelper.trackOrderPlaced(context, order)
        primeOrderTracking(context, order.id)
        if (state.pushGranted) schedulePushPreview(order)
        return order
    }

    fun onOrderPickedUp(orderId: String) {
        MoEngageSDKHelper.trackOrderPickedUp(context, orderId)
        stopOrderTracking(context, orderId)
    }

    fun onReorderTapped(item: String, orderId: String) {
        MoEngageSDKHelper.trackReorderTapped(context, item, orderId)
    }

    fun onRewardRedeemed(reward: String) {
        MoEngageSDKHelper.trackRewardRedeemed(context, reward)
    }

    // ── Simulated push (demo only) ──────────────────────────────────────────────────

    /**
     * Real campaigns arrive through FCM and the system shade. This mirrors the prototype's
     * in-app shade so the flow demos on an emulator with no campaign configured.
     */
    private fun schedulePushPreview(order: Order) {
        viewModelScope.launch {
            delay(PUSH_PREVIEW_DELAY_MS)
            showPushPreview(order)
        }
    }

    fun showPushPreview(order: Order = _state.value.lastOrder) {
        _state.update {
            it.copy(
                simulatedPush = SimulatedPush(
                    title = "Your flat white is at the bar",
                    body = "Order #${order.id} · tap to see your order.",
                    deeplink = "brewbar://status/${order.id}",
                ),
            )
        }
    }

    fun dismissPushPreview() = _state.update { it.copy(simulatedPush = null) }

    fun onNotificationOpened(campaignId: String?, deeplink: String?) {
        MoEngageSDKHelper.trackNotificationOpened(context, campaignId, deeplink)
    }

    // ── Inbox badge ─────────────────────────────────────────────────────────────────

    /**
     * Drives the bell badge on Menu home from `MoEInboxHelper.getUnClickedMessagesCountAsync`.
     * Applied unconditionally, including 0 — the SDK clearing the badge is a real answer.
     */
    fun refreshUnreadCount() {
        MoEngageSDKHelper.fetchUnreadCount(context) { count ->
            _state.update { it.copy(unreadCount = count.toInt().coerceAtLeast(0)) }
        }
    }

    fun clearUnread() = _state.update { it.copy(unreadCount = 0) }

    fun consumeUnread() = _state.update { it.copy(unreadCount = (it.unreadCount - 1).coerceAtLeast(0)) }

    // ── Demo tools ──────────────────────────────────────────────────────────────────

    fun setDemoToolsVisible(visible: Boolean) = _state.update { it.copy(demoToolsVisible = visible) }

    companion object {
        /** Prototype timing: the native in-app lands 2 seconds after Menu appears. */
        const val NATIVE_INAPP_DELAY_MS = 120L

        /** Prototype timing: the push lands ~1400 ms after payment. */
        const val PUSH_PREVIEW_DELAY_MS = 1400L

        /** Matches the [com.moengage.sampleapp.data.ProfileRepository] preference gated on push permission. */
        private const val ORDER_UPDATES_KEY = "order_updates"
    }
}
