package com.moengage.sampleapp.sdkhelper

import android.content.Context
import com.moengage.inapp.MoEInAppHelper
import com.moengage.inapp.listeners.InAppLifeCycleListener
import com.moengage.inapp.model.InAppData
import com.moengage.inapp.model.SelfHandledCampaignData
import com.moengage.inapp.model.actions.CustomAction
import com.moengage.inapp.model.actions.NavigationAction
import com.moengage.inapp.model.enums.InAppPosition
import timber.log.Timber

/** Native and self-handled in-app campaigns. Reached through [MoEngageSDKHelper]. */
internal object MoEngageInApp {

    /** Set by [setSelfHandledListener] so the Menu screen can render campaign-driven promos. */
    private var selfHandledSink: ((SelfHandledCampaignData?) -> Unit)? = null

    /** Set by [setNavigationSink] so in-app / inbox deep links can drive Navigation-Compose. */
    private var navigationSink: ((String) -> Unit)? = null

    /** Native in-app. Called from the screens that are campaign targets. */
    fun showInApp(context: Context) = guarded {
        MoEInAppHelper.getInstance().showInApp(context)
    }

    /**
     * Nudges. Called from the screens that are nudge campaign targets.
     *
     * Distinct from [showInApp]: a nudge is anchored to a position rather than shown as a modal,
     * and the SDK matches it against nudge campaigns only. [InAppPosition.ANY] lets the campaign's
     * own configured position win, which is what a sample app wants.
     */
    fun showNudge(context: Context) = guarded {
        MoEInAppHelper.getInstance().showNudge(context, InAppPosition.ANY)
    }

    /**
     * Scopes which in-app contexts are eligible, so a Menu campaign does not fire on Cart.
     * Called on every navigation.
     */
    fun setContext(screen: String) = guarded {
        MoEInAppHelper.getInstance().setInAppContext(setOf(screen))
    }

    /**
     * Ask for the self-handled campaign that backs the dark promo card on Menu home.
     * A null payload means nothing is targeted (or the campaign is paused) — the caller
     * leaves the promo slot empty rather than rendering a placeholder.
     *
     * The listener overload is the one the integration guide documents; the SDK also exposes
     * a success/failure-callback variant that is not reachable from Kotlin on this version.
     */
    @Suppress("DEPRECATION")
    fun fetchSelfHandled(context: Context, onResult: (SelfHandledCampaignData?) -> Unit) = guarded {
        MoEInAppHelper.getInstance().getSelfHandledInApp(
            context,
        ) { data -> onResult(data) }
    }

    /** Impression, click and dismissal must be reported or campaign stats go wrong. */
    fun selfHandledShown(context: Context, campaign: SelfHandledCampaignData) = guarded {
        MoEInAppHelper.getInstance().selfHandledShown(context, campaign)
    }

    fun selfHandledClicked(context: Context, campaign: SelfHandledCampaignData) = guarded {
        MoEInAppHelper.getInstance().selfHandledClicked(context, campaign)
    }

    fun selfHandledDismissed(context: Context, campaign: SelfHandledCampaignData) = guarded {
        MoEInAppHelper.getInstance().selfHandledDismissed(context, campaign)
    }

    /**
     * Registered once from `BrewBarApp`. Campaign-triggered self-handled payloads arrive here
     * (as opposed to the pull-based [fetchSelfHandled]) and are forwarded to Menu home.
     */
    fun setSelfHandledListener(onCampaign: (SelfHandledCampaignData?) -> Unit) = guarded {
        selfHandledSink = onCampaign
        MoEInAppHelper.getInstance().setSelfHandledListener { data -> selfHandledSink?.invoke(data) }
    }

    /** Lets the in-app / inbox layers hand a deep link to the nav graph. */
    fun setNavigationSink(sink: ((String) -> Unit)?) {
        navigationSink = sink
    }

    /**
     * Registered once from `BrewBarApp`: fires [AppEvents.Events.INAPP_CTA_CLICKED] and routes
     * the CTA. Returning `true` tells the SDK the app handled navigation itself.
     */
    fun registerCallbacks(context: Context) = guarded {
        MoEInAppHelper.getInstance().setClickActionListener { clickData ->
            val campaignId = clickData.campaignData.campaignId
            val action = clickData.action
            val (cta, deeplink) = when (action) {
                is NavigationAction -> action.navigationUrl to action.navigationUrl
                is CustomAction -> action.keyValuePairs?.keys?.joinToString(",").orEmpty() to null
                else -> action.actionType.name to null
            }
            AppEvents.trackInAppCtaClicked(context, campaignId, cta)
            deeplink?.let { url ->
                navigationSink?.let { sink ->
                    sink(url)
                    true
                } ?: false
            } ?: false
        }
        MoEInAppHelper.getInstance().addInAppLifeCycleListener(
            object : InAppLifeCycleListener {
                override fun onShown(inAppData: InAppData) {
                    Timber.d("in-app shown: %s", inAppData.campaignData.campaignName)
                }

                override fun onDismiss(inAppData: InAppData) {
                    Timber.d("in-app dismissed: %s", inAppData.campaignData.campaignName)
                }
            },
        )
    }
}
