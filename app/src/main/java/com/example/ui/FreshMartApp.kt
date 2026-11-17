package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.model.AppTab
import com.example.ui.components.FreshMartBottomBar
import com.example.ui.components.FreshMartNavRail
import com.example.ui.components.ProductDetailSheet
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.state.CommerceViewModel
import com.example.ui.theme.PrimaryDeepForest
import com.example.ui.theme.SurfaceCanvas

const val ROUTE_CHECKOUT = "checkout"

@Composable
fun FreshMartApp(
    viewModel: CommerceViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: AppTab.SHOP.route
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.statusMessage) {
        val message = uiState.statusMessage
        if (message != null) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short,
            )
            viewModel.clearStatusMessage()
        }
    }

    val currentTab = AppTab.entries.firstOrNull { it.route == currentRoute } ?: AppTab.SHOP
    val showNavigationChrome = currentRoute != ROUTE_CHECKOUT

    val onSelectTab: (AppTab) -> Unit = { tab ->
        navController.navigate(tab.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas),
    ) {
        val useNavRail = maxWidth >= 840.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = SurfaceCanvas,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = PrimaryDeepForest,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
            },
            bottomBar = {
                if (showNavigationChrome && !useNavRail) {
                    FreshMartBottomBar(
                        currentTab = currentTab,
                        cartCount = uiState.totalCartCount,
                        onSelectTab = onSelectTab,
                    )
                }
            },
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                if (showNavigationChrome && useNavRail) {
                    FreshMartNavRail(
                        currentTab = currentTab,
                        cartCount = uiState.totalCartCount,
                        onSelectTab = onSelectTab,
                        modifier = Modifier.fillMaxHeight(),
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .widthIn(max = if (useNavRail) 1040.dp else 680.dp),
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = AppTab.SHOP.route,
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            composable(AppTab.SHOP.route) {
                                ShopScreen(
                                    uiState = uiState,
                                    onSearchQueryChange = viewModel::updateSearchQuery,
                                    onSelectCategory = viewModel::selectCategory,
                                    onSelectSortOption = viewModel::selectSortOption,
                                    onToggleOnSaleOnly = viewModel::toggleOnSaleOnly,
                                    onOpenProductDetail = viewModel::openProductDetail,
                                    onAddToCart = { viewModel.addToCart(it) },
                                    onIncrementCart = viewModel::incrementCartItem,
                                    onDecrementCart = viewModel::decrementCartItem,
                                    onToggleFavorite = viewModel::toggleFavorite,
                                    onNavigateToAccount = { onSelectTab(AppTab.ACCOUNT) },
                                )
                            }

                            composable(AppTab.CART.route) {
                                CartScreen(
                                    uiState = uiState,
                                    onIncrementItem = viewModel::incrementCartItem,
                                    onDecrementItem = viewModel::decrementCartItem,
                                    onRemoveItem = viewModel::removeFromCart,
                                    onClearCart = viewModel::clearCart,
                                    onTogglePromo = viewModel::togglePromoCode,
                                    onCheckoutClick = {
                                        navController.navigate(ROUTE_CHECKOUT)
                                    },
                                    onBrowseShop = { onSelectTab(AppTab.SHOP) },
                                )
                            }

                            composable(AppTab.ACCOUNT.route) {
                                AccountScreen(
                                    uiState = uiState,
                                    onSelectAddress = viewModel::selectAddress,
                                    onSelectPayment = viewModel::selectPaymentMethod,
                                    onToggleZeroPlastic = {
                                        viewModel.updateSourcingPreferences(zeroPlastic = it)
                                    },
                                    onToggleLocalFarm = {
                                        viewModel.updateSourcingPreferences(localFarm = it)
                                    },
                                    onToggleNotifications = {
                                        viewModel.updateSourcingPreferences(notifications = it)
                                    },
                                    onReorder = { order ->
                                        viewModel.reorderFromHistory(order)
                                        onSelectTab(AppTab.CART)
                                    },
                                    onAddToCart = { viewModel.addToCart(it) },
                                    onSignOut = viewModel::signOut,
                                    onSignInDemo = viewModel::signInDemo,
                                )
                            }

                            composable(ROUTE_CHECKOUT) {
                                CheckoutScreen(
                                    uiState = uiState,
                                    onSelectAddress = viewModel::selectAddress,
                                    onSelectSlot = viewModel::selectDeliverySlot,
                                    onSelectPayment = viewModel::selectPaymentMethod,
                                    onPlaceOrder = viewModel::placeOrder,
                                    onDoneAfterOrder = {
                                        viewModel.dismissOrderConfirmation()
                                        navController.popBackStack(AppTab.SHOP.route, inclusive = false)
                                        onSelectTab(AppTab.SHOP)
                                    },
                                    onViewOrdersAfterOrder = {
                                        viewModel.dismissOrderConfirmation()
                                        navController.popBackStack(AppTab.SHOP.route, inclusive = false)
                                        onSelectTab(AppTab.ACCOUNT)
                                    },
                                    onNavigateBack = { navController.popBackStack() },
                                )
                            }
                        }
                    }
                }
            }
        }

        uiState.selectedProductForDetail?.let { product ->
            val qtyInCart = uiState.cartItems
                .firstOrNull { it.product.id == product.id }
                ?.quantity ?: 0
            ProductDetailSheet(
                product = product,
                isFavorite = product.id in uiState.favoriteIds,
                inCartQuantity = qtyInCart,
                onDismiss = viewModel::dismissProductDetail,
                onToggleFavorite = { viewModel.toggleFavorite(product.id) },
                onAddToCart = { amount -> viewModel.addToCart(product, amount) },
            )
        }
    }
}
