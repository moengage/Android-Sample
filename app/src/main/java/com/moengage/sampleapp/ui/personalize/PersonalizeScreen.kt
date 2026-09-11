package com.moengage.sampleapp.ui.personalize

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.domain.model.PersonalizedOffer
import com.moengage.sampleapp.ui.components.BrewCard
import com.moengage.sampleapp.ui.components.IconTile
import com.moengage.sampleapp.ui.components.RemoteImage
import com.moengage.sampleapp.ui.components.TabPill
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/**
 * Screen 14 — Personalize.
 *
 * A `fetchExperience` call answers in one of two shapes, and this screen renders both: a campaign
 * whose payload carries `offerings` (a personalized card list — the **Loyalty offer program** and
 * **Coupons/offers** tabs), or one that carries none — no eligible campaign, an empty Decisioning
 * result, or nothing configured yet — which falls back to [NoOffersPanel], rendered from the
 * experience's own `title`/`message` KV pairs, rather than a blank screen. The **Rewards** tab is
 * deliberately always this second shape: it has no Offering and no Decision Policy behind it, just
 * a dashboard-authored spend-nudge message ("Place an order of ₹5,000 or more within 3 months to
 * earn rewards."). The toggle re-fetches a different experience key so every path is exercised
 * against the real SDK response, not local fixtures.
 *
 * MoEngage moments: an experience impression fires once per successful fetch, an offering
 * impression fires once per card rendered, and tapping a card fires the offering click.
 */
@Composable
fun PersonalizeScreen(
    demoState: OfferDemoState,
    offers: List<PersonalizedOffer>,
    fallbackCopy: ExperienceCopy,
    loading: Boolean,
    fetched: Boolean,
    onBack: () -> Unit,
    onDemoStateChanged: (OfferDemoState) -> Unit,
    onOfferClick: (PersonalizedOffer) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(BrewColors.PageBackground)) {
        PersonalizeHeader(demoState, onBack, onDemoStateChanged)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(
                    start = Sizes.screenPadding,
                    end = Sizes.screenPadding,
                    top = Space.x18,
                    bottom = Space.x28,
                ),
            verticalArrangement = Arrangement.spacedBy(Space.x14),
        ) {
            when {
                loading && !fetched -> LoadingPanel()
                offers.isNotEmpty() -> {
                    Text("Picked for you", style = BrewType.cardTitle, color = BrewColors.TextPrimary)
                    offers.forEach { offer -> OfferCard(offer, onClick = { onOfferClick(offer) }) }
                }
                else -> NoOffersPanel(fallbackCopy, demoState)
            }
        }
    }
}

@Composable
private fun PersonalizeHeader(
    demoState: OfferDemoState,
    onBack: () -> Unit,
    onDemoStateChanged: (OfferDemoState) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrewColors.PrimaryDarkSurface)
            .padding(Sizes.screenPadding),
        verticalArrangement = Arrangement.spacedBy(Space.x16),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Space.x12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(Sizes.backTile)
                    .clip(BrewShapes.input)
                    .border(1.dp, BrewColors.OnDarkTrack, BrewShapes.input)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = BrewColors.OnDarkPrimary,
                    modifier = Modifier.size(18.dp),
                )
            }
            Text("Personalize", style = BrewType.cardTitle, color = BrewColors.OnDarkPrimary)
        }
        Text(
            "Personalized offers picked just for you, based on your orders and " +
                "loyalty tier — pick a tab below to preview each one.",
            style = BrewType.caption,
            color = BrewColors.OnDarkSecondary,
        )
        // Forces the two SDK-level response shapes for the demo rather than waiting on a real
        // Decisioning outcome — see the README's Personalize row for the dashboard side of this.
        // Horizontally scrollable so each label keeps its natural single-line width instead of
        // being squeezed by its siblings on a narrow screen.
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(Space.x8),
        ) {
            OfferDemoState.entries.forEach { state ->
                TabPill(
                    label = state.label,
                    selected = state == demoState,
                    onClick = { onDemoStateChanged(state) },
                )
            }
        }
    }
}

@Composable
private fun LoadingPanel() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = Space.x28),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = BrewColors.Primary)
    }
}

@Composable
private fun OfferCard(offer: PersonalizedOffer, onClick: () -> Unit) {
    BrewCard(contentPadding = PaddingValues(Space.x14), onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.x12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val image = offer.imageUrl
            if (image != null) {
                RemoteImage(url = image, contentDescription = offer.title, modifier = Modifier.size(48.dp))
            } else {
                IconTile(icon = Icons.Filled.LocalOffer, size = 48.dp)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Space.x4)) {
                Text(offer.title, style = BrewType.bodyMedium, color = BrewColors.TextPrimary)
                if (offer.subtitle.isNotBlank()) {
                    Text(offer.subtitle, style = BrewType.caption, color = BrewColors.TextSecondary)
                }
            }
            Text(offer.ctaLabel, style = BrewType.captionMedium, color = BrewColors.Link)
        }
    }
}

/**
 * The no-offerings state: no eligible campaign, an empty Decisioning result, or nothing configured
 * for the experience key yet. The **Rewards** tab lives here permanently — by design it has no
 * Offering and no Decision Policy, just this message.
 *
 * The copy comes from the experience's own top-level `title`/`message` content-payload KV pairs, so
 * the dashboard owns this panel's wording the same way it owns the offer cards — a campaign that
 * returns no offerings can still say *why*. The strings below are the app's own defaults for when
 * the campaign carries neither (or never landed at all) — themed per [demoState] since "no offer
 * found" and "here's how to earn rewards" read very differently to a user. A developer wiring this
 * up for real should treat a never-landed campaign exactly the same way: show the app's own
 * default, not an empty screen.
 */
@Composable
private fun NoOffersPanel(copy: ExperienceCopy, demoState: OfferDemoState) {
    val isRewards = demoState == OfferDemoState.WITHOUT_OFFERINGS
    BrewCard(contentPadding = PaddingValues(Space.x18)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Space.x10),
        ) {
            IconTile(
                icon = if (isRewards) Icons.Filled.CardGiftcard else Icons.Filled.SearchOff,
                size = Sizes.iconTileLarge,
                background = BrewColors.NeutralFill,
                tint = BrewColors.TextTertiary,
            )
            Text(
                copy.title ?: if (isRewards) {
                    "Unlock rewards on your next order"
                } else {
                    "No personalized offer right now"
                },
                style = BrewType.bodyMedium,
                color = BrewColors.TextPrimary,
            )
            Text(
                copy.message ?: if (isRewards) {
                    "Place an order of ₹5,000 or more within 3 months to earn rewards."
                } else {
                    "Nothing matched for this user — check the menu instead. New offers show up " +
                        "the moment a Decisioning campaign has one for them."
                },
                style = BrewType.caption,
                color = BrewColors.TextSecondary,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun PersonalizeWithOffersPreview() {
    BrewBarTheme {
        PersonalizeScreen(
            demoState = OfferDemoState.WITH_OFFERINGS,
            offers = previewOffers,
            fallbackCopy = ExperienceCopy(title = null, message = null),
            loading = false,
            fetched = true,
            onBack = {},
            onDemoStateChanged = {},
            onOfferClick = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892, name = "Personalize · rewards")
@Composable
private fun PersonalizeRewardsPreview() {
    BrewBarTheme {
        PersonalizeScreen(
            demoState = OfferDemoState.WITHOUT_OFFERINGS,
            offers = emptyList(),
            fallbackCopy = ExperienceCopy(title = null, message = null),
            loading = false,
            fetched = true,
            onBack = {},
            onDemoStateChanged = {},
            onOfferClick = {},
        )
    }
}

// Single offering: whichever tier this user's segment qualifies for — never more than one card,
// since the Decision Policy behind EXPERIENCE_KEY_WITH_OFFERINGS resolves to exactly one Offering.
private val previewOffers = listOf(
    PersonalizedOffer(
        offeringKey = "loyalty_upgrade_silver",
        title = "You've been upgraded to Silver — free!",
        subtitle = "Unlocked from your order history this quarter",
        imageUrl = null,
        ctaLabel = "View benefits",
        deeplink = "brewbar://loyalty/silver",
    ),
)
