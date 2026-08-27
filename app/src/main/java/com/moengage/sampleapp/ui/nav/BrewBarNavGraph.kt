package com.moengage.sampleapp.ui.nav

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.moengage.sampleapp.data.MenuRepository
import com.moengage.sampleapp.data.OrderRepository
import com.moengage.sampleapp.domain.model.CartLine
import com.moengage.sampleapp.domain.model.MenuCategory
import com.moengage.sampleapp.sdkhelper.MoEngageSDKHelper
import com.moengage.sampleapp.ui.components.BottomNavBar
import com.moengage.sampleapp.ui.components.BottomTab
import com.moengage.sampleapp.ui.inapp.DemoActions
import com.moengage.sampleapp.ui.inapp.DemoToolsSheet
import com.moengage.sampleapp.ui.inbox.InboxScreen
import com.moengage.sampleapp.ui.inbox.InboxViewModel
import com.moengage.sampleapp.ui.login.LoginScreen
import com.moengage.sampleapp.ui.menu.CategoryListScreen
import com.moengage.sampleapp.ui.menu.ItemDetailScreen
import com.moengage.sampleapp.ui.menu.MenuHomeScreen
import com.moengage.sampleapp.ui.order.CartScreen
import com.moengage.sampleapp.ui.order.OrderStatusScreen
import com.moengage.sampleapp.ui.order.PaymentScreen
import com.moengage.sampleapp.ui.orders.OrdersScreen
import com.moengage.sampleapp.ui.permission.PermissionDialogOverlay
import com.moengage.sampleapp.ui.permission.PushOptInScreen
import com.moengage.sampleapp.ui.profile.ProfileScreen
import com.moengage.sampleapp.ui.selfhandledcards.SelfHandledCardsScreen
import com.moengage.sampleapp.ui.selfhandledcards.SelfHandledCardsViewModel
import com.moengage.sampleapp.ui.splash.SplashScreen
import com.moengage.sampleapp.ui.theme.BrewColors

/**
 * The whole app: one NavHost with the 13 destinations, the bottom nav on Menu and Profile,
 * and the two overlays (simulated OS permission dialog, DemoTools sheet).
 */
@Composable
fun BrewBarNavGraph(
    navController: NavHostController = rememberNavController(),
    /** A deep link handed in from a notification tap, consumed once. */
    pendingDeeplink: String? = null,
    onDeeplinkConsumed: () -> Unit = {},
) {
    val context = LocalContext.current
    val session: SessionViewModel = viewModel()
    val state by session.state.collectAsStateWithLifecycle()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Android 13+ runtime permission. Below 33 the SDK reports the manifest state instead.
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        session.onPushPermissionResult(granted)
    }

    // Android 10+ only: the geofence module needs ACCESS_BACKGROUND_LOCATION before it will
    // register a fence, and the OS refuses to grant it in the same prompt as the foreground pair.
    val backgroundLocationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        session.onBackgroundLocationResult(granted)
    }

    // Foreground location for geofence campaigns, chaining into the background request when the
    // OS version needs one. Fine specifically: an "Approximate" grant does not satisfy the SDK.
    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { grants ->
        val fineGranted = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true
        session.onLocationPermissionResult(fineGranted)
        if (fineGranted && session.needsBackgroundLocation()) {
            backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

    val requestLocation: () -> Unit = {
        locationLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ),
        )
    }

    val requestPush: () -> Unit = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // Pre-13 there is no OS prompt. Show the design's stand-in dialog so the moment
            // is still visible, then mirror the answer through the SDK.
            session.showPermissionDialog(true)
        }
    }

    // Location is read on every app open and asked for when the foreground grant is missing —
    // geofence campaigns are the point of the demo, and a permanently denied prompt no-ops rather
    // than re-showing. refreshLocationPermissions() is idempotent, so the ON_START effect below
    // repeating it costs nothing.
    LaunchedEffect(Unit) {
        session.refreshLocationPermissions()
        if (session.needsLocationRequest()) requestLocation()
    }

    // A grant made on the settings page only reaches us on the way back in, and it is what turns
    // geofence monitoring on — refreshLocationPermissions() starts it as soon as it qualifies.
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        session.refreshLocationPermissions()
    }

    // In-app campaigns and inbox rows both route through here.
    LaunchedEffect(Unit) {
        MoEngageSDKHelper.setNavigationSink { link ->
            Routes.fromDeeplink(link)?.let(navController::navigate)
        }
    }

    LaunchedEffect(pendingDeeplink) {
        val link = pendingDeeplink ?: return@LaunchedEffect
        Routes.fromDeeplink(link)?.let { route ->
            navController.navigate(route)
            session.onNotificationOpened(campaignId = null, deeplink = link)
        }
        onDeeplinkConsumed()
    }

    // Scope in-app eligibility to the visible screen so a Menu campaign doesn't fire on Cart.
    LaunchedEffect(currentRoute) {
        MoEngageSDKHelper.setInAppContext(Routes.inAppContext(currentRoute))
    }

    val bottomTab = when {
        currentRoute == Routes.MENU -> BottomTab.Menu
        currentRoute == Routes.PROFILE -> BottomTab.Profile
        else -> null
    }

    Box(Modifier.fillMaxSize().background(BrewColors.PageBackground)) {
        Column(Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Routes.SPLASH,
                modifier = Modifier.weight(1f),
            ) {
                composable(Routes.SPLASH) {
                    SplashScreen(onGetStarted = { navController.navigate(Routes.LOGIN) })
                }

                composable(Routes.LOGIN) {
                    LoginScreen(
                        onBack = { navController.popBackStack() },
                        onVerified = {
                            session.onLoginSucceeded()
                            // Nothing to opt into if notifications are already on — from an earlier
                            // run, or switched on in system settings. Go straight to the menu.
                            if (session.refreshPushPermissionState()) {
                                navController.navigateToMenu()
                            } else {
                                navController.navigate(Routes.PERMISSION)
                            }
                        },
                    )
                }

                composable(Routes.PERMISSION) {
                    // Also covers reaching this route any other way, and the case where the grant
                    // happened in system settings while the app was backgrounded.
                    LaunchedEffect(Unit) { session.refreshPushPermissionState() }
                    PushOptInScreen(
                        onEnable = requestPush,
                        onSkip = { navController.navigateToMenu() },
                    )
                    // Once the OS has answered, continue into the app.
                    LaunchedEffect(state.pushGranted, state.pushBlocked) {
                        if (state.pushGranted || state.pushBlocked) navController.navigateToMenu()
                    }
                }

                composable(Routes.MENU) {
                    LaunchedEffect(Unit) {
                        session.onMenuViewed()
                        session.refreshSelfHandledCampaign()
                        session.refreshUnreadCount()
                        session.maybeShowNativeInApp()
                    }
                    MenuHomeScreen(
                        category = state.category,
                        unreadCount = state.unreadCount,
                        promo = state.promo,
                        onCategorySelected = session::selectCategory,
                        onItemClick = { navController.navigate(Routes.item(it.id)) },
                        onFullMenu = { navController.navigate(Routes.category(state.category.id)) },
                        onReorderUsual = {
                            session.onReorderTapped(
                                MenuRepository.usual.summary,
                                state.lastOrder.id,
                            )
                            navController.navigate(Routes.CART)
                        },
                        onInboxClick = { navController.navigate(Routes.INBOX) },
                        onPromoClick = {
                            session.onPromoClicked()
                                ?.let { Routes.fromDeeplink(it) }
                                ?.let(navController::navigate)
                        },
                        onPromoDismiss = session::onPromoDismissed,
                        onDemoTools = { session.setDemoToolsVisible(true) },
                    )
                }

                composable(
                    route = Routes.CATEGORY,
                    arguments = listOf(navArgument("categoryId") { type = NavType.StringType }),
                ) { entry ->
                    val category = MenuCategory.fromId(entry.arguments?.getString("categoryId"))
                    LaunchedEffect(category) { session.selectCategory(category) }
                    CategoryListScreen(
                        category = category,
                        onBack = { navController.popBackStack() },
                        onItemClick = { navController.navigate(Routes.item(it.id)) },
                        onAdd = { navController.navigate(Routes.item(it.id)) },
                        onDemoTools = { session.setDemoToolsVisible(true) },
                    )
                }

                composable(
                    route = Routes.ITEM,
                    arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
                ) { entry ->
                    val item = MenuRepository.item(entry.arguments?.getString("itemId").orEmpty())
                    LaunchedEffect(item.id) {
                        MoEngageSDKHelper.trackItemViewed(context, item)
                        // Item detail is an in-app campaign target too.
                        MoEngageSDKHelper.showInApp(context)
                    }
                    ItemDetailScreen(
                        item = item,
                        onBack = { navController.popBackStack() },
                        onAdd = { selection ->
                            MoEngageSDKHelper.trackAddToCart(
                                context = context,
                                item = item,
                                size = selection.size,
                                milk = selection.milk,
                                addOns = selection.addOns,
                                amount = selection.amount,
                            )
                            session.addLine(
                                CartLine(
                                    id = "${item.id}-${selection.size}-${selection.milk}",
                                    itemId = item.id,
                                    name = "${item.name} · ${selection.size.lowercase()}",
                                    options = buildList {
                                        add("${selection.milk} milk")
                                        addAll(selection.addOns)
                                    }.joinToString(" · "),
                                    amount = selection.amount,
                                    quantity = selection.quantity,
                                    image = item.image,
                                ),
                            )
                            navController.navigate(Routes.CART)
                        },
                    )
                }

                composable(Routes.CART) {
                    LaunchedEffect(Unit) { session.onCartViewed() }
                    CartScreen(
                        lines = state.cart,
                        bill = state.bill,
                        fulfilment = state.fulfilment,
                        cupPreference = state.cupPreference,
                        onBack = { navController.popBackStack() },
                        onFulfilmentChange = session::setFulfilment,
                        onCupPreferenceChange = session::setCupPreference,
                        onAddAnother = {
                            navController.navigate(Routes.category(state.category.id))
                        },
                        onProceed = { navController.navigate(Routes.PAYMENT) },
                    )
                }

                composable(Routes.PAYMENT) {
                    LaunchedEffect(Unit) { session.onCheckoutStarted() }
                    PaymentScreen(
                        bill = state.bill,
                        fulfilment = state.fulfilment,
                        itemsCount = state.cart.size,
                        selectedMethodId = state.paymentMethodId,
                        onBack = { navController.popBackStack() },
                        onMethodSelected = session::setPaymentMethod,
                        onPay = {
                            val order = session.placeOrder()
                            navController.navigate(Routes.status(order.id)) {
                                popUpTo(Routes.MENU)
                            }
                        },
                    )
                }

                composable(
                    route = Routes.STATUS,
                    arguments = listOf(navArgument("orderId") { type = NavType.StringType }),
                ) { entry ->
                    val orderId = entry.arguments?.getString("orderId").orEmpty()
                    val order = if (orderId == state.lastOrder.id) {
                        state.lastOrder
                    } else {
                        OrderRepository.byId(orderId)
                    }
                    OrderStatusScreen(
                        order = order,
                        simulatedPush = state.simulatedPush,
                        onMyOrders = { navController.navigate(Routes.ORDERS) },
                        onBackToMenu = { navController.navigateToMenu() },
                        onPushTapped = { push ->
                            session.dismissPushPreview()
                            session.onNotificationOpened(null, push.deeplink)
                            session.onOrderPickedUp(order.id)
                            Routes.fromDeeplink(push.deeplink)?.let(navController::navigate)
                        },
                        onPushDismissed = session::dismissPushPreview,
                    )
                }

                composable(Routes.ORDERS) {
                    // Order history is the nudge campaign target — the in-app context is already
                    // scoped to "orders" by the navigation effect above, so the SDK only matches
                    // nudges configured for this screen.
                    LaunchedEffect(Unit) {
                        MoEngageSDKHelper.showNudge(context)
                    }
                    OrdersScreen(
                        orders = OrderRepository.all(),
                        onBack = { navController.popBackStack() },
                        onTrack = { navController.navigate(Routes.status(it.id)) },
                        onReorder = { order ->
                            session.onReorderTapped(
                                order.lines.first().name,
                                order.id,
                            )
                            navController.navigate(Routes.CART)
                        },
                        onSubscribe = { navController.navigate(Routes.REWARDS) },
                    )
                }

                composable(Routes.REWARDS) {
                    val cards: SelfHandledCardsViewModel = viewModel()
                    val cardsState by cards.state.collectAsStateWithLifecycle()
                    // The cards section has a lifecycle the SDK needs both ends of, so this is
                    // a DisposableEffect rather than the LaunchedEffect the inbox gets away with.
                    DisposableEffect(Unit) {
                        cards.onScreenEntered()
                        onDispose { cards.onScreenLeft() }
                    }
                    SelfHandledCardsScreen(
                        coupons = cardsState.coupons,
                        onBack = { navController.popBackStack() },
                        onRedeem = { coupon ->
                            session.onRewardRedeemed(coupon.title)
                            cards.onRedeem(coupon)
                                ?.let { Routes.fromDeeplink(it) }
                                ?.let(navController::navigate)
                        },
                    )
                }

                composable(Routes.INBOX) {
                    val inbox: InboxViewModel = viewModel()
                    val inboxState by inbox.state.collectAsStateWithLifecycle()
                    LaunchedEffect(Unit) { inbox.refresh() }
                    InboxScreen(
                        messages = inboxState.messages,
                        onBack = { navController.popBackStack() },
                        onMessageClick = { message ->
                            session.consumeUnread()
                            inbox.onMessageClicked(message)
                                ?.let { Routes.fromDeeplink(it) }
                                ?.let(navController::navigate)
                        },
                        onMarkAllRead = {
                            inbox.markAllRead()
                            session.clearUnread()
                        },
                    )
                }

                composable(Routes.PROFILE) {
                    ProfileScreen(
                        pushGranted = state.pushGranted,
                        pushBlocked = state.pushBlocked,
                        preferences = state.notificationPreferences,
                        onPreferenceChange = session::setNotificationPreference,
                        onRequestPush = requestPush,
                        onOpenSystemSettings = session::openNotificationSettings,
                        location = state.location,
                        onRequestLocation = requestLocation,
                        onOpenAppSettings = session::openAppSettings,
                        onLogout = {
                            session.logout()
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        },
                        onDemoTools = { session.setDemoToolsVisible(true) },
                    )
                }
            }

            if (bottomTab != null) {
                BottomNavBar(
                    selected = bottomTab,
                    onSelect = { tab ->
                        when (tab) {
                            BottomTab.Menu -> navController.navigateToMenu()
                            BottomTab.Orders -> navController.navigate(Routes.ORDERS)
                            BottomTab.SelfHandledCards -> navController.navigate(Routes.REWARDS)
                            BottomTab.Profile -> navController.navigate(Routes.PROFILE)
                        }
                    },
                )
            }
        }

        if (state.permissionDialogVisible) {
            PermissionDialogOverlay(
                onAllow = {
                    session.showPermissionDialog(false)
                    session.onPushPermissionResult(true)
                },
                onDeny = {
                    session.showPermissionDialog(false)
                    session.onPushPermissionResult(false)
                },
            )
        }

        if (state.demoToolsVisible) {
            DemoToolsSheet(
                actions = DemoActions(
                    onRequestPermission = {
                        session.setDemoToolsVisible(false)
                        requestPush()
                    },
                    onShowNativeInApp = {
                        session.setDemoToolsVisible(false)
                        session.showNativeInAppNow()
                    },
                    onRenderSelfHandled = {
                        session.setDemoToolsVisible(false)
                        session.showFallbackPromo()
                        navController.navigateToMenu()
                    },
                    onStartGeofence = {
                        session.setDemoToolsVisible(false)
                        requestLocation()
                    },
                ),
                onDismiss = { session.setDemoToolsVisible(false) },
            )
        }
    }
}

/** Menu is the app's home: reaching it clears everything above it. */
private fun NavHostController.navigateToMenu() {
    navigate(Routes.MENU) {
        popUpTo(Routes.MENU) { inclusive = true }
        launchSingleTop = true
    }
}
