package com.moengage.sampleapp.sdkhelper

import android.content.Context
import com.moengage.core.model.MoERequestFailureReason
import com.moengage.recommendations.MoERecommendationsHelper
import com.moengage.recommendations.model.RecommendationsFailureReason
import timber.log.Timber

/** Recommendations — the Menu-home row. Reached through [MoEngageSDKHelper]. */
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
            MoERecommendationsHelper.fetchRecommendations(
                context = context,
                recommendationId = recommendationId,
                includedFields = includedFields,
            )
                .onSuccess { recommendedItems -> onResult(recommendedItems.items) }
                .onFailure { failure ->
                    when (failure.reason) {
                        // Recommendations-specific reason.
                        RecommendationsFailureReason.RATE_LIMIT_EXCEEDED ->
                            Timber.w("MoEngage Recommendations rate limited, retry later")
                        // Reason shared across MoEngage modules.
                        MoERequestFailureReason.NetworkError ->
                            Timber.w("MoEngage Recommendations failed, device is offline")
                        else -> Timber.w("MoEngage Recommendations failed: ${failure.reason} — ${failure.message}")
                    }
                    onResult(emptyList())
                }
        } catch (throwable: Throwable) {
            // Thrown synchronously (SdkNotInitializedException) when the integration validator is on.
            Timber.w(throwable, "MoEngage call skipped")
            onResult(emptyList())
        }
    }
}
