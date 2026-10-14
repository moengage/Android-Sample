package com.moengage.sampleapp.ui.recommendations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.moengage.sampleapp.domain.model.RecommendedProduct
import com.moengage.sampleapp.sdkhelper.MoEngageSDKHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RecommendationsUiState(
    val products: List<RecommendedProduct> = emptyList(),
    /** False until a fetch answers — before that an empty list would misread as "nothing found". */
    val fetched: Boolean = false,
)

/**
 * State for the "Recommendations for you" row. User-scoped, not category-scoped: no `itemId`, and
 * one fetch per Menu arrival regardless of tab switches.
 */
class RecommendationsViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(RecommendationsUiState())
    val state: StateFlow<RecommendationsUiState> = _state.asStateFlow()

    private val context get() = getApplication<Application>().applicationContext

    fun onScreenEntered() {
        if (_state.value.fetched) return

        MoEngageSDKHelper.fetchRecommendations(
            context = context,
            recommendationId = RecommendationsContract.RECOMMENDATION_ID,
            includedFields = RecommendationsContract.INCLUDED_FIELDS,
        ) { items ->
            _state.value = RecommendationsUiState(
                products = items.toRecommendedProducts(),
                fetched = true,
            )
        }
    }
}
