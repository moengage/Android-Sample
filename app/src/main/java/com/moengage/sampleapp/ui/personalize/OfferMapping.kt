package com.moengage.sampleapp.ui.personalize

import com.moengage.campaigns.personalize.model.ExperienceCampaign
import com.moengage.sampleapp.domain.model.PersonalizedOffer
import org.json.JSONObject

/**
 * Every dashboard-linkage constant Brew Bar's Personalize demo depends on, in one place, so the
 * three demo states in `PersonalizeViewModel.kt` and the payload parsing below can't drift out of
 * sync with what's actually configured on the dashboard. See the README's Personalize row for the
 * full Experience → Decision Policy → Offering(s) setup each key expects.
 *
 * The three keys back three distinct business scenarios, not just three SDK response shapes:
 *  - **Loyalty offer program** (single offering) — a free Bronze/Silver/Diamond tier upgrade,
 *    the one tier this user's segment qualifies for — [EXPERIENCE_KEY_WITH_OFFERINGS].
 *  - **Coupons/offers** (multiple offerings) — [EXPERIENCE_KEY_MULTIPLE_OFFERINGS]. Unchanged.
 *  - **Rewards** (no offering, no Decision Policy — a spend-nudge message) —
 *    [EXPERIENCE_KEY_REWARDS].
 */
internal object PersonalizeContract {
    /** Key of the String content-payload KV pair carrying the experience's offerings. */
    const val OFFER_PAYLOAD_KEY = "offer_payload"

    /**
     * The two plain String content-payload KV pairs an experience carries alongside
     * [OFFER_PAYLOAD_KEY] for the copy the app should show when the payload has no offerings —
     * authored on the dashboard so the fallback panel is campaign-driven, not hardcoded.
     */
    const val TITLE_KEY = "title"
    const val MESSAGE_KEY = "message"

    /**
     * Loyalty offer program (single offering) — a free tier upgrade (Bronze/Silver/Diamond),
     * decided by the user's own standing rather than authored per-user. `BrewBar Loyalty Tier
     * Upgrade` experience → `brewbar_loyalty_policy`, which carries all three tier Offerings
     * (`BrewBar_LoyaltyUpgradeBronze`, `BrewBar_LoyaltyUpgradeSilver`,
     * `BrewBar_LoyaltyUpgradeDiamond`) each targeted at the audience segment eligible for that
     * tier, with "No. of offerings in output" set to 1 — so Decisioning always resolves to the
     * single tier this user qualifies for, never more than one at a time.
     */
    const val EXPERIENCE_KEY_WITH_OFFERINGS = "brewbar_home_offer_with"

    /**
     * Coupons/offers (multiple offerings) — no changes. `BrewBar Multi Offer Bundle` experience →
     * `brewbar_multi_offer_policy` → 3 offerings (`BrewBar_FreeAlmondCroissant`,
     * `BrewBar_IcedLatte10Off`, `BrewBar_LoyaltyBonusStar`).
     */
    const val EXPERIENCE_KEY_MULTIPLE_OFFERINGS = "brewbar_multi_offer"

    /**
     * Rewards — "Place an order of ₹5,000 or more within 3 months to earn rewards." Deliberately
     * the no-offerings shape: no Offering, no Decision Policy, just a spend-nudge message.
     * `BrewBar Rewards Nudge` experience, no Decision Policy attached, content payload carries
     * only [TITLE_KEY] / [MESSAGE_KEY] (and, for parity, [OFFER_PAYLOAD_KEY] = `{"offerings": []}`).
     */
    const val EXPERIENCE_KEY_REWARDS = "brewbar_home_rewards"
}

/**
 * A [PersonalizedOffer] plus the raw JSON object it came from — what
 * `MoEngageSDKHelper.personalizeOfferingShown`/`personalizeOfferingClicked` expect, since the SDK
 * attributes an offering-level impression/click to the exact payload extracted from the campaign,
 * not to the app's own mapped shape.
 */
internal data class OfferWithPayload(val offer: PersonalizedOffer, val rawPayload: Map<String, Any>)

/**
 * The experience's own authored copy, read from the top-level [PersonalizeContract.TITLE_KEY] and
 * [PersonalizeContract.MESSAGE_KEY] KV pairs rather than from inside `offer_payload`. Drives the
 * "without offerings" panel: either field is null when the campaign does not carry it (or carries
 * it blank), which leaves the screen's own default copy in place for that half instead of
 * rendering an empty line.
 */
data class ExperienceCopy(val title: String?, val message: String?)

/** Reads [ExperienceCopy] off a fetched campaign's payload. Both fields null when nothing is set. */
internal fun ExperienceCampaign.toCopy(): ExperienceCopy = ExperienceCopy(
    title = payload.nonBlankString(PersonalizeContract.TITLE_KEY),
    message = payload.nonBlankString(PersonalizeContract.MESSAGE_KEY),
)

private fun Map<String, Any>.nonBlankString(key: String): String? = (this[key] as? String)?.takeIf { it.isNotBlank() }

/**
 * Maps a fetched experience campaign onto Brew Bar's own offers, following the same "app owns the
 * contract" rule as `PromoMapping.kt`.
 *
 * [PersonalizeContract.OFFER_PAYLOAD_KEY] is expected to hold a JSON string shaped `{"offerings": [...]}`, where
 * `offerings` is Decisioning's own per-offering result passed through untouched — one entry per
 * offering the policy returned (however many "No. of offerings in output" is set to), each shaped
 * `{"offering_content": {"payload": {"title": …, "subtitle": …, "offering_key": …, "image_url": …,
 * "cta_label": …, "deeplink": …}}, "offering_context": {"moe_offering_id": …, …}}`. `offering_content
 * .payload` is Brew Bar's own authored fields (set on the Offering's Content JSON); `offering_context`
 * is the SDK-required tracking block Decisioning attaches automatically — passed straight through as
 * `rawPayload["offering_context"]`, which is what makes offering-level impression/click tracking work.
 *
 * A missing key, a blank value, invalid JSON, or an empty/absent `offerings` array all return an
 * empty list — the "without offerings" case — rather than throwing, so a half-configured campaign,
 * or an experience deliberately configured to carry no offerings, renders the screen's fallback
 * instead of crashing it.
 */
internal fun ExperienceCampaign.toOffers(): List<OfferWithPayload> {
    val raw = payload[PersonalizeContract.OFFER_PAYLOAD_KEY] as? String
    if (raw.isNullOrBlank()) return emptyList()
    val json = runCatching { JSONObject(raw) }.getOrNull() ?: return emptyList()
    val offerings = json.optJSONArray("offerings") ?: return emptyList()

    return buildList {
        for (index in 0 until offerings.length()) {
            val decision = offerings.optJSONObject(index) ?: continue
            val content = decision.optJSONObject("offering_content")?.optJSONObject("payload") ?: continue
            val offeringContext = decision.optJSONObject("offering_context")
            val title = content.optString("title").takeIf { it.isNotBlank() } ?: continue

            val offeringKey = content.optString("offering_key").takeIf { it.isNotBlank() }
                ?: offeringContext?.optString("moe_offering_id")?.takeIf { it.isNotBlank() }
                ?: "offer_$index"

            val rawPayload = content.toMap() + mapOfNotNull("offering_context" to offeringContext?.toMap())

            add(
                OfferWithPayload(
                    offer = PersonalizedOffer(
                        offeringKey = offeringKey,
                        title = title,
                        subtitle = content.optString("subtitle"),
                        imageUrl = content.optString("image_url").takeIf { it.isNotBlank() },
                        ctaLabel = content.optString("cta_label").takeIf { it.isNotBlank() } ?: "View",
                        deeplink = content.optString("deeplink").takeIf { it.isNotBlank() },
                    ),
                    rawPayload = rawPayload,
                ),
            )
        }
    }
}

private fun JSONObject.toMap(): Map<String, Any> = keys().asSequence().associateWith { key -> get(key) }

/** [Pair.second] is only added when non-null — lets a single nullable entry fold into `+` cleanly. */
private fun mapOfNotNull(pair: Pair<String, Any?>): Map<String, Any> =
    pair.second?.let { mapOf(pair.first to it) } ?: emptyMap()
