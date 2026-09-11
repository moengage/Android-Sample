package com.moengage.sampleapp.ui.personalize

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.moengage.campaigns.personalize.model.ExperienceCampaign
import com.moengage.sampleapp.domain.model.PersonalizedOffer
import com.moengage.sampleapp.sdkhelper.MoEngageSDKHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * The three business scenarios the SDK's Personalize API demo covers — each backed by its own
 * dashboard campaign, so the toggle is picking which experience key to fetch, not which audience
 * of a shared one. See the campaign spec (`PersonalizeContract`) for the exact payload each key
 * should carry.
 *
 *  - [WITH_OFFERINGS] — Loyalty offer program: single offering.
 *  - [MULTIPLE_OFFERINGS] — Coupons/offers: multiple offerings. No changes.
 *  - [WITHOUT_OFFERINGS] — Rewards: no offering, no Decision Policy, just a spend-nudge message
 *    ("Place an order of ₹5,000 or more within 3 months to earn rewards.").
 */
enum class OfferDemoState(val experienceKey: String, val label: String) {
    WITH_OFFERINGS(PersonalizeContract.EXPERIENCE_KEY_WITH_OFFERINGS, "Loyalty offer program"),
    MULTIPLE_OFFERINGS(PersonalizeContract.EXPERIENCE_KEY_MULTIPLE_OFFERINGS, "Coupons/offers"),
    WITHOUT_OFFERINGS(PersonalizeContract.EXPERIENCE_KEY_REWARDS, "Rewards"),
}

data class PersonalizeUiState(
    val demoState: OfferDemoState = OfferDemoState.WITH_OFFERINGS,
    val offers: List<PersonalizedOffer> = emptyList(),
    /** Dashboard-authored copy for the no-offerings panel; null where the campaign carries none. */
    val fallbackCopy: ExperienceCopy = ExperienceCopy(title = null, message = null),
    val loading: Boolean = true,
    /** True once a fetch has come back — before that, an empty list would misread as "without offerings". */
    val fetched: Boolean = false,
)

/**
 * Screen 14 — Personalize. Demonstrates the shapes `fetchExperience` can answer with across three
 * business scenarios — Loyalty offer program (single offering), Coupons/offers (multiple
 * offerings), and Rewards (no offering at all, just a spend-nudge message) — see [OfferDemoState].
 * The toggle re-fetches a different experience key rather than switching on canned local data, so
 * the screen is exercising the real SDK round trip in every state.
 */
class PersonalizeViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(PersonalizeUiState())
    val state: StateFlow<PersonalizeUiState> = _state.asStateFlow()

    private var currentCampaign: ExperienceCampaign? = null
    private var offerPayloads: Map<String, Map<String, Any>> = emptyMap()

    /** Offering keys already reported shown for the state currently on screen. */
    private val reportedShown = mutableSetOf<String>()

    private val context get() = getApplication<Application>().applicationContext

    fun onScreenEntered() = fetch(_state.value.demoState)

    fun onDemoStateChanged(demoState: OfferDemoState) {
        if (demoState == _state.value.demoState) return
        reportedShown.clear()
        fetch(demoState)
    }

    /**
     * The offer card was tapped. Reports the click against both the offering and its parent
     * campaign, and returns the offer's own deep link to follow.
     */
    fun onOfferClicked(offer: PersonalizedOffer): String? {
        val campaign = currentCampaign ?: return null
        offerPayloads[offer.offeringKey]?.let { rawPayload ->
            MoEngageSDKHelper.personalizeOfferingClicked(context, campaign, rawPayload)
        }
        return offer.deeplink
    }

    private fun fetch(demoState: OfferDemoState) {
        _state.value = _state.value.copy(demoState = demoState, loading = true)
        val experienceKey = demoState.experienceKey
        // The SDK validates fetchExperience's key against a locally-cached list before ever
        // calling the network — sync it first or a dashboard-configured key still comes back
        // INVALID_EXPERIENCE_KEY on a device that has never synced this.
        MoEngageSDKHelper.fetchPersonalizeExperiencesMeta(context) {
            fetchExperience(demoState, experienceKey)
        }
    }

    private fun fetchExperience(demoState: OfferDemoState, experienceKey: String) {
        MoEngageSDKHelper.fetchPersonalizeExperience(
            context = context,
            experienceKey = experienceKey,
            attributes = emptyMap(),
        ) { result ->
            val campaign = result.experiences.firstOrNull { it.experienceKey == experienceKey }
            currentCampaign = campaign
            val mapped = campaign?.toOffers().orEmpty()
            offerPayloads = mapped.associate { it.offer.offeringKey to it.rawPayload }

            _state.value = PersonalizeUiState(
                demoState = demoState,
                offers = mapped.map { it.offer },
                fallbackCopy = campaign?.toCopy() ?: ExperienceCopy(title = null, message = null),
                loading = false,
                fetched = true,
            )

            // Impressions only make sense once something has actually rendered.
            campaign?.let { MoEngageSDKHelper.personalizeExperienceShown(context, it) }
            mapped.forEach(::reportOfferingShown)
        }
    }

    private fun reportOfferingShown(offerWithPayload: OfferWithPayload) {
        if (!reportedShown.add(offerWithPayload.offer.offeringKey)) return
        MoEngageSDKHelper.personalizeOfferingShown(context, offerWithPayload.rawPayload)
    }
}
