package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.data.CatalogRepository
import com.example.model.OrderRecord
import com.example.model.Product
import com.example.ui.components.OrganicCard
import com.example.ui.components.PrimaryEmeraldButton
import com.example.ui.components.ProductImageTile
import com.example.ui.components.SecondaryOutlinedButton
import com.example.ui.components.formatRs
import com.example.ui.state.CommerceUiState
import com.example.ui.theme.BorderCard
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.PlusJakartaSansFamily
import com.example.ui.theme.PrimaryEmerald
import com.example.ui.theme.SecondaryEmerald
import com.example.ui.theme.SecondaryForest
import com.example.ui.theme.SurfaceCanvas
import com.example.ui.theme.SurfaceSubtleMint
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TertiaryRuby
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

private enum class AccountSheet {
    NONE,
    ORDERS,
    FAVORITES,
    ADDRESSES,
    PAYMENTS,
}

@Composable
fun AccountScreen(
    uiState: CommerceUiState,
    onSelectAddress: (String) -> Unit,
    onSelectPayment: (String) -> Unit,
    onToggleZeroPlastic: (Boolean) -> Unit,
    onToggleLocalFarm: (Boolean) -> Unit,
    onToggleNotifications: (Boolean) -> Unit,
    onReorder: (OrderRecord) -> Unit,
    onAddToCart: (Product) -> Unit,
    onSignOut: () -> Unit,
    onSignInDemo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var activeSheet by remember { mutableStateOf(AccountSheet.NONE) }
    var showSignOutDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceCanvas),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            Text(
                text = "Account",
                style = MaterialTheme.typography.displayLarge,
                color = TextPrimary,
                modifier = Modifier.statusBarsPadding(),
            )
        }

        if (!uiState.isSignedIn) {
            item {
                OrganicCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(SurfaceSubtleMint),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Eco,
                                contentDescription = null,
                                tint = PrimaryEmerald,
                                modifier = Modifier.size(30.dp),
                            )
                        }
                        Text(
                            text = "Sign in to FreshMart",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary,
                        )
                        Text(
                            text = "Access saved addresses, organic harvest perks, and instant reordering.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                        )
                        PrimaryEmeraldButton(
                            text = "Continue as Elena Vance",
                            onClick = onSignInDemo,
                            modifier = Modifier.testTag("sign_in_demo_button"),
                        )
                    }
                }
            }
        } else {
            item {
                ProfileHeaderCard(
                    userName = uiState.userName,
                    userEmail = uiState.userEmail,
                    ordersCount = uiState.orders.size,
                    favoritesCount = uiState.favoriteIds.size,
                    ecoPoints = uiState.ecoPoints,
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "SHOPPING & DELIVERY",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary),
                        modifier = Modifier.padding(horizontal = 4.dp),
                    )
                    GroupedSettingsCard {
                        SettingsNavigationRow(
                            icon = Icons.Filled.ReceiptLong,
                            title = "My Orders",
                            subtitle = "${uiState.orders.size} past & active orders",
                            onClick = { activeSheet = AccountSheet.ORDERS },
                            testTag = "account_orders_row",
                        )
                        SettingsInsetDivider()
                        SettingsNavigationRow(
                            icon = Icons.Filled.Favorite,
                            title = "Saved Favorites",
                            subtitle = "${uiState.favoriteIds.size} organic picks saved",
                            onClick = { activeSheet = AccountSheet.FAVORITES },
                            testTag = "account_favorites_row",
                        )
                        SettingsInsetDivider()
                        SettingsNavigationRow(
                            icon = Icons.Filled.LocationOn,
                            title = "Delivery Addresses",
                            subtitle = "${uiState.selectedAddress.label} · ${uiState.selectedAddress.street.substringBefore(",")}",
                            onClick = { activeSheet = AccountSheet.ADDRESSES },
                            testTag = "account_addresses_row",
                        )
                        SettingsInsetDivider()
                        SettingsNavigationRow(
                            icon = Icons.Filled.CreditCard,
                            title = "Payment Methods",
                            subtitle = uiState.selectedPayment.title,
                            onClick = { activeSheet = AccountSheet.PAYMENTS },
                            testTag = "account_payments_row",
                        )
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "SOURCING & ALERTS",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary),
                        modifier = Modifier.padding(horizontal = 4.dp),
                    )
                    GroupedSettingsCard {
                        SettingsToggleRow(
                            icon = Icons.Filled.Recycling,
                            title = "Zero-Plastic Packaging",
                            subtitle = "Compostable paper & chilled cloth totes",
                            checked = uiState.sourcingPreferences.zeroPlasticPackaging,
                            onCheckedChange = onToggleZeroPlastic,
                        )
                        SettingsInsetDivider()
                        SettingsToggleRow(
                            icon = Icons.Filled.LocalShipping,
                            title = "Local Farm Priority",
                            subtitle = "Prefer harvests within 120 km radius",
                            checked = uiState.sourcingPreferences.localFarmPriority,
                            onCheckedChange = onToggleLocalFarm,
                        )
                        SettingsInsetDivider()
                        SettingsToggleRow(
                            icon = Icons.Filled.Notifications,
                            title = "Harvest & Order Updates",
                            subtitle = "Live dispatch & seasonal drop alerts",
                            checked = uiState.sourcingPreferences.orderNotifications,
                            onCheckedChange = onToggleNotifications,
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                SecondaryOutlinedButton(
                    text = "Sign out",
                    leadingIcon = Icons.AutoMirrored.Filled.Logout,
                    onClick = { showSignOutDialog = true },
                    modifier = Modifier.testTag("account_sign_out_button"),
                )
            }
        }
    }

    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            containerColor = SurfaceWhite,
            title = {
                Text(
                    text = "Sign out of FreshMart?",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                )
            },
            text = {
                Text(
                    text = "Your basket items stay ready whenever you sign back in.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSignOutDialog = false
                        onSignOut()
                    },
                ) {
                    Text("Sign out", color = TertiaryRuby, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) {
                    Text("Cancel", color = PrimaryEmerald, fontWeight = FontWeight.SemiBold)
                }
            },
        )
    }

    if (activeSheet != AccountSheet.NONE) {
        AccountDetailSheet(
            sheet = activeSheet,
            uiState = uiState,
            onDismiss = { activeSheet = AccountSheet.NONE },
            onSelectAddress = {
                onSelectAddress(it)
                activeSheet = AccountSheet.NONE
            },
            onSelectPayment = {
                onSelectPayment(it)
                activeSheet = AccountSheet.NONE
            },
            onReorder = {
                onReorder(it)
                activeSheet = AccountSheet.NONE
            },
            onAddToCart = onAddToCart,
        )
    }
}

@Composable
private fun ProfileHeaderCard(
    userName: String,
    userEmail: String,
    ordersCount: Int,
    favoritesCount: Int,
    ecoPoints: Int,
) {
    OrganicCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(CatalogRepository.USER_AVATAR_URL)
                        .crossfade(true)
                        .build(),
                    contentDescription = userName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .background(SurfaceSubtleMint),
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(SurfaceSubtleMint),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "EV",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = PrimaryEmerald,
                                ),
                            )
                        }
                    },
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextPrimary,
                    )
                    Text(
                        text = userEmail,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                    )
                    Surface(
                        shape = RoundedCornerShape(9999.dp),
                        color = SurfaceSubtleMint,
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Verified,
                                contentDescription = null,
                                tint = SecondaryEmerald,
                                modifier = Modifier.size(13.dp),
                            )
                            Text(
                                text = "ORGANIC PLUS MEMBER",
                                style = MaterialTheme.typography.labelSmall.copy(color = PrimaryEmerald),
                            )
                        }
                    }
                }
            }

            HorizontalDivider(thickness = 1.dp, color = BorderSubtle)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                StatPill(value = ordersCount.toString(), label = "Orders")
                StatPill(value = favoritesCount.toString(), label = "Saved")
                StatPill(value = ecoPoints.toString(), label = "Eco Points")
            }
        }
    }
}

@Composable
private fun StatPill(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = PlusJakartaSansFamily,
                color = PrimaryEmerald,
            ),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
        )
    }
}

@Composable
private fun GroupedSettingsCard(
    content: @Composable () -> Unit,
) {
    OrganicCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

@Composable
private fun SettingsNavigationRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceSubtleMint),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryEmerald,
                modifier = Modifier.size(18.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceSubtleMint),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryEmerald,
                modifier = Modifier.size(18.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryEmerald,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = BorderCard,
            ),
        )
    }
}

@Composable
private fun SettingsInsetDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 64.dp),
        thickness = 1.dp,
        color = BorderSubtle,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountDetailSheet(
    sheet: AccountSheet,
    uiState: CommerceUiState,
    onDismiss: () -> Unit,
    onSelectAddress: (String) -> Unit,
    onSelectPayment: (String) -> Unit,
    onReorder: (OrderRecord) -> Unit,
    onAddToCart: (Product) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceWhite,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            val title = when (sheet) {
                AccountSheet.ORDERS -> "My Orders"
                AccountSheet.FAVORITES -> "Saved Favorites"
                AccountSheet.ADDRESSES -> "Delivery Addresses"
                AccountSheet.PAYMENTS -> "Payment Methods"
                AccountSheet.NONE -> ""
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = TextPrimary,
            )

            when (sheet) {
                AccountSheet.ORDERS -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        items(uiState.orders, key = { it.orderId }) { order ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = SurfaceCanvas,
                                border = BorderStroke(1.dp, BorderCard),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = order.orderId,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = TextPrimary,
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(9999.dp),
                                            color = SurfaceSubtleMint,
                                        ) {
                                            Text(
                                                text = order.status,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = SecondaryForest,
                                                ),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${order.dateLabel} · ${order.itemCount} items",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                    )
                                    Text(
                                        text = order.itemsSummary,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextPrimary,
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = formatRs(order.totalRs),
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontFamily = PlusJakartaSansFamily,
                                                fontWeight = FontWeight.Bold,
                                            ),
                                        )
                                        Surface(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp))
                                                .clickable { onReorder(order) },
                                            shape = RoundedCornerShape(10.dp),
                                            color = PrimaryEmerald,
                                        ) {
                                            Text(
                                                text = "Reorder",
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    color = Color.White,
                                                ),
                                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                AccountSheet.FAVORITES -> {
                    val favs = uiState.favoriteProducts
                    if (favs.isEmpty()) {
                        Text(
                            text = "Tap the heart icon on any item in Shop to save your organic staples here.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                        )
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(favs, key = { it.id }) { product ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = SurfaceCanvas,
                                    border = BorderStroke(1.dp, BorderCard),
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    ) {
                                        ProductImageTile(
                                            imageUrl = product.imageUrl,
                                            contentDescription = product.name,
                                            modifier = Modifier.size(56.dp),
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = product.name,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = TextPrimary,
                                            )
                                            Text(
                                                text = formatRs(product.priceRs),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    color = SecondaryForest,
                                                ),
                                            )
                                        }
                                        Surface(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp))
                                                .clickable { onAddToCart(product) },
                                            shape = RoundedCornerShape(10.dp),
                                            color = PrimaryEmerald,
                                        ) {
                                            Text(
                                                text = "+ Basket",
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    color = Color.White,
                                                ),
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                AccountSheet.ADDRESSES -> {
                    uiState.addresses.forEach { addr ->
                        val selected = addr.id == uiState.selectedAddressId
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onSelectAddress(addr.id) },
                            shape = RoundedCornerShape(14.dp),
                            color = if (selected) SurfaceSubtleMint else SurfaceCanvas,
                            border = BorderStroke(1.dp, if (selected) PrimaryEmerald else BorderCard),
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                            ) {
                                Text(
                                    text = addr.label,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary,
                                )
                                Text(
                                    text = "${addr.apartment}, ${addr.street}, ${addr.city}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary,
                                )
                            }
                        }
                    }
                }

                AccountSheet.PAYMENTS -> {
                    uiState.paymentMethods.forEach { pay ->
                        val selected = pay.id == uiState.selectedPaymentId
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onSelectPayment(pay.id) },
                            shape = RoundedCornerShape(14.dp),
                            color = if (selected) SurfaceSubtleMint else SurfaceCanvas,
                            border = BorderStroke(1.dp, if (selected) PrimaryEmerald else BorderCard),
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                            ) {
                                Text(
                                    text = pay.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary,
                                )
                                Text(
                                    text = pay.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                )
                            }
                        }
                    }
                }

                AccountSheet.NONE -> Unit
            }
        }
    }
}
