package com.moengage.sampleapp.sdkhelper

import android.content.Context
import com.moengage.campaigns.personalize.MoEPersonalizeHelper
import com.moengage.campaigns.personalize.model.ExperienceCampaign
import com.moengage.campaigns.personalize.model.ExperienceCampaignsMetadata
import com.moengage.campaigns.personalize.model.ExperienceCampaignsResult
import timber.log.Timber

/**
 * Personalize (`personalization-core`) — the "with offerings" / "without offerings" demo behind
 * the Personalize screen. Reached through [MoEngageSDKHelper].
 *
 * An experience campaign's `payload` is author-defined JSON, the same contract as self-handled
 * in-app and cards: the SDK only hands back whatever the dashboard's content-payload KV pairs put
 * there — rendering is the app's job. Brew Bar's experiences carry one String KV pair,
 * `offer_payload`, holding a JSON-encoded `{"offerings": [...]}` when Decisioning found something
 * to show, or blank when it did not (see `OfferMapping.kt` for the parse).
 *
 * `attributes` on [fetchExperience] are session-scoped — sent with the request, never written to
 * the user's profile — which is what lets the demo toggle steer which audience/variation answers
 * without touching real user data.
 */
internal object MoEngagePersonalize {

    private val EMPTY_RESULT = ExperienceCampaignsResult(experiences = emptyList(), failures = emptyList())

    /**
     * `offeringShown`/`offeringClicked` validate the offering payload on a background thread and
     * throw `UnsupportedOperationException` when it fails — off the calling thread, so a plain
     * try/catch around the call site can't stop it from crashing the app. The SDK requires a
     * nested `offering_context` map carrying all five of these keys; a live Decisioning-backed
     * offering already includes it, so this only bites a still-being-authored campaign.
     */
    private val REQUIRED_OFFERING_CONTEXT_KEYS = listOf(
        "moe_offering_id",
        "moe_offering_name",
        "moe_offering_type",
        "moe_decision_policy_id",
        "moe_decision_policy_name",
    )

    private fun hasValidOfferingContext(offeringPayload: Map<String, Any>): Boolean {
        val offeringContext = offeringPayload["offering_context"] as? Map<*, *> ?: return false
        return REQUIRED_OFFERING_CONTEXT_KEYS.all { offeringContext.containsKey(it) }
    }

    /**
     * Syncs this workspace's experience-key metadata. `fetchExperience` validates its key against
     * a locally-cached list *before* ever calling the network — a key that is genuinely configured
     * on the dashboard still comes back `INVALID_EXPERIENCE_KEY` on a device that has never synced
     * this. Call once before the first fetch; the SDK rate-limits the actual network call itself
     * (`personalizationConfig.experienceMetaSyncInterval`), so calling this on every screen entry
     * is safe.
     */
    fun fetchExperiencesMeta(context: Context, onResult: (ExperienceCampaignsMetadata?) -> Unit) {
        try {
            MoEPersonalizeHelper.fetchExperiencesMeta(
                context = context,
                status = emptyList(),
                onSuccess = { metadata -> onResult(metadata) },
                onFailure = { reason, message ->
                    Timber.w("MoEngage Personalize meta fetch failed: $reason — $message")
                    onResult(null)
                },
            )
        } catch (throwable: Throwable) {
            Timber.w(throwable, "MoEngage call skipped")
            onResult(null)
        }
    }

    /**
     * Fetches one experience campaign. An empty result — no experiences, no failures — means
     * nothing is configured for this key yet, same as an SDK-not-initialised sample app: the
     * screen should fall back gracefully rather than treat it as an error.
     */
    fun fetchExperience(
        context: Context,
        experienceKey: String,
        attributes: Map<String, String>,
        onResult: (ExperienceCampaignsResult) -> Unit,
    ) {
        try {
            MoEPersonalizeHelper.fetchExperience(
                context = context,
                experienceKey = experienceKey,
                attributes = attributes,
                onSuccess = { result -> onResult(result) },
                onFailure = { reason, message ->
                    Timber.w("MoEngage Personalize fetch failed: $reason — $message")
                    onResult(EMPTY_RESULT)
                },
            )
        } catch (throwable: Throwable) {
            Timber.w(throwable, "MoEngage call skipped")
            onResult(EMPTY_RESULT)
        }
    }

    /** Impression on the whole experience campaign. Report once per successful render. */
    fun experienceShown(context: Context, campaign: ExperienceCampaign) = guarded {
        MoEPersonalizeHelper.experienceShown(context, campaign)
    }

    /** Click on the experience campaign as a whole. */
    fun experienceClicked(context: Context, campaign: ExperienceCampaign) = guarded {
        MoEPersonalizeHelper.experienceClicked(context, campaign)
    }

    /**
     * Impression on one offering extracted from the experience's payload. Skipped — not
     * crashed — when the offering carries no valid `offering_context`, which is exactly the
     * state a campaign is in before its Decisioning wiring is finished.
     */
    fun offeringShown(context: Context, offeringPayload: Map<String, Any>) {
        if (!hasValidOfferingContext(offeringPayload)) {
            Timber.w("MoEngage Personalize: offering has no valid offering_context, skipping offeringShown")
            return
        }
        guarded { MoEPersonalizeHelper.offeringShown(context, offeringPayload) }
    }

    /** Click on one offering, attributed to both the offering and its parent campaign. Same guard as [offeringShown]. */
    fun offeringClicked(context: Context, campaign: ExperienceCampaign, offeringPayload: Map<String, Any>) {
        if (!hasValidOfferingContext(offeringPayload)) {
            Timber.w("MoEngage Personalize: offering has no valid offering_context, skipping offeringClicked")
            return
        }
        guarded { MoEPersonalizeHelper.offeringClicked(context, campaign, offeringPayload) }
    }
}
