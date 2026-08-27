package com.moengage.sampleapp.ui.inapp

import com.moengage.inapp.model.SelfHandledCampaignData
import com.moengage.sampleapp.domain.model.PromoPayload
import org.json.JSONObject

/**
 * Maps a self-handled in-app campaign onto the app's own [PromoPayload].
 *
 * The payload is author-defined JSON, so the contract is Brew Bar's, not the SDK's:
 * `{"title": …, "subtitle": …, "code": …, "deeplink": …}`. Anything missing — or a payload that
 * is not valid JSON at all — falls back to [PromoPayload.FALLBACK] rather than failing, so a
 * half-configured campaign still renders a card.
 */
internal fun SelfHandledCampaignData?.toPromoPayload(): PromoPayload {
    val fallback = PromoPayload.FALLBACK
    val raw = this?.campaign?.payload ?: return fallback
    val json = runCatching { JSONObject(raw) }.getOrNull() ?: return fallback
    val code = json.optString("code").takeIf { it.isNotBlank() }
    val subtitle = json.optString("subtitle").takeIf { it.isNotBlank() }
        ?: code?.let { "2–5 pm today. Code $it." }
        ?: fallback.subtitle
    return PromoPayload(
        title = json.optString("title").takeIf { it.isNotBlank() } ?: fallback.title,
        subtitle = subtitle,
        deeplink = json.optString("deeplink").takeIf { it.isNotBlank() } ?: fallback.deeplink,
        campaignId = campaignData.campaignId,
    )
}
