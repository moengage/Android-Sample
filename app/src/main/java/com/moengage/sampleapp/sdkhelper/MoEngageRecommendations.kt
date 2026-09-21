package com.moengage.sampleapp.sdkhelper

import android.content.Context
import timber.log.Timber

/**
 * Recommendations — the Menu-home row. Reached through [MoEngageSDKHelper].
 *
 * The module is unreleased, so this is a stub; see the TODO for what to swap in.
 */
internal object MoEngageRecommendations {

    /**
     * Empty list for both "nothing found" and any failure — the row renders the same either way.
     *
     * Not [guarded]: a dropped callback would leave the row loading forever.
     */
    fun fetchRecommendations(
        context: Context,
        recommendationId: String,
        includedFields: Set<String>,
        onResult: (List<Map<String, Any>>) -> Unit,
    ) {
        try {
            // TODO: MOEN-46606 — once com.moengage:recommendations ships, replace with:
            //   MoERecommendationsHelper.fetchRecommendations(context, recommendationId,
            //       includedFields = includedFields)
            //       .onSuccess { onResult(it.items) }
            //       .onFailure { Timber.w("recommendations failed: ${it.reason}"); onResult(emptyList()) }
            // Keep this try/catch: fetchRecommendations throws SdkNotInitializedException
            // synchronously when the integration validator is enabled.
            onResult(emptyList())
        } catch (throwable: Throwable) {
            Timber.w(throwable, "MoEngage call skipped")
            onResult(emptyList())
        }
    }
}
