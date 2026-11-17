package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.OrderRecord
import com.example.ui.components.OrganicCard
import com.example.ui.components.PrimaryEmeraldButton
import com.example.ui.components.ReceiptSummaryCard
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
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CheckoutScreen(
    uiState: CommerceUiState,
    onSelectAddress: (String) -> Unit,
    onSelectSlot: (String) -> Unit,
    onSelectPayment: (String) -> Unit,
    onPlaceOrder: () -> Unit,
    onDoneAfterOrder: () -> Unit,
    onViewOrdersAfterOrder: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val confirmed = uiState.confirmedOrder

    BackHandler {
        if (confirmed != null) {
            onDoneAfterOrder()
        } else {
            onNavigateBack()
        }
    }

    if (confirmed != null) {
        OrderConfirmationView(
            order = confirmed,
            addressStreet = uiState.selectedAddress.street,
            onContinueShopping = onDoneAfterOrder,
            onViewOrders = onViewOrdersAfterOrder,
            modifier = modifier,
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceCanvas),
    ) {
        // Top App Bar
        Surface(
            color = SurfaceWhite,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("checkout_back_button"),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to basket",
                            tint = TextPrimary,
                        )
                    }
                    Text(
                        text = "Checkout",
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextPrimary,
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = BorderSubtle)
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Delivery Address Selection
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionTitle(icon = Icons.Filled.LocationOn, title = "Delivery Address")
                    uiState.addresses.forEach { address ->
                        val selected = address.id == uiState.selectedAddressId
                        SelectableOptionCard(
                            selected = selected,
                            onClick = { onSelectAddress(address.id) },
                            testTag = "checkout_addr_${address.id}",
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Text(
                                        text = address.label,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimary,
                                    )
                                    if (selected) {
                                        Surface(
                                            shape = RoundedCornerShape(9999.dp),
                                            color = SurfaceSubtleMint,
                                        ) {
                                            Text(
                                                text = "SELECTED",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = PrimaryEmerald,
                                                ),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "${address.apartment}, ${address.street}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                )
                                Text(
                                    text = address.instructions,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                )
                            }
                        }
                    }
                }
            }

            // Delivery Slot Selection
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionTitle(icon = Icons.Filled.Schedule, title = "Delivery Window")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        uiState.deliverySlots.forEach { slot ->
                            val selected = slot.id == uiState.selectedSlotId
                            val feeLabel = if (uiState.qualifiesFreeDelivery || slot.feeRs == 0) {
                                "Free"
                            } else {
                                formatRs(slot.feeRs)
                            }
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { onSelectSlot(slot.id) }
                                    .testTag("checkout_slot_${slot.id}"),
                                shape = RoundedCornerShape(14.dp),
                                color = if (selected) SurfaceSubtleMint else SurfaceWhite,
                                border = BorderStroke(
                                    width = if (selected) 1.5.dp else 1.dp,
                                    color = if (selected) PrimaryEmerald else BorderCard,
                                ),
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    Text(
                                        text = slot.title,
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            color = if (selected) PrimaryEmerald else TextPrimary,
                                            fontWeight = FontWeight.Bold,
                                        ),
                                    )
                                    Text(
                                        text = slot.etaLabel,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                    )
                                    Text(
                                        text = feeLabel,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontFamily = PlusJakartaSansFamily,
                                            color = if (feeLabel == "Free") SecondaryForest else TextPrimary,
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Payment Method Selection
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionTitle(icon = Icons.Filled.CreditCard, title = "Payment Method")
                    uiState.paymentMethods.forEach { method ->
                        val selected = method.id == uiState.selectedPaymentId
                        SelectableOptionCard(
                            selected = selected,
                            onClick = { onSelectPayment(method.id) },
                            testTag = "checkout_pay_${method.id}",
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = method.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary,
                                )
                                Text(
                                    text = method.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                )
                            }
                        }
                    }
                }
            }

            // Receipt Summary
            item {
                ReceiptSummaryCard(
                    subtotalRs = uiState.subtotalRs,
                    deliveryFeeRs = uiState.deliveryFeeRs,
                    itemSavingsRs = uiState.itemSavingsRs,
                    promoDiscountRs = uiState.promoDiscountRs,
                    totalRs = uiState.grandTotalRs,
                )
            }
        }

        // Pinned Place Order CTA
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp,
                    ambientColor = PrimaryEmerald.copy(alpha = 0.08f),
                ),
            color = SurfaceWhite,
        ) {
            Column {
                HorizontalDivider(thickness = 1.dp, color = BorderSubtle)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    PrimaryEmeraldButton(
                        text = "Place order",
                        leadingIcon = Icons.Filled.Lock,
                        trailingText = formatRs(uiState.grandTotalRs),
                        onClick = onPlaceOrder,
                        modifier = Modifier.testTag("place_order_button"),
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryEmerald,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
        )
    }
}

@Composable
private fun SelectableOptionCard(
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        color = if (selected) SurfaceSubtleMint.copy(alpha = 0.55f) else SurfaceWhite,
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) PrimaryEmerald else BorderCard,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(modifier = Modifier.weight(1f)) {
                content()
            }
            Icon(
                imageVector = if (selected) Icons.Filled.RadioButtonChecked else Icons.Filled.RadioButtonUnchecked,
                contentDescription = if (selected) "Selected" else "Not selected",
                tint = if (selected) PrimaryEmerald else TextSecondary,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun OrderConfirmationView(
    order: OrderRecord,
    addressStreet: String,
    onContinueShopping: () -> Unit,
    onViewOrders: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(SurfaceSubtleMint),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Order confirmed",
                tint = SecondaryEmerald,
                modifier = Modifier.size(48.dp),
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Order Confirmed!",
            style = MaterialTheme.typography.displayLarge,
            color = TextPrimary,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Order ${order.orderId} is being hand-packed fresh",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
        )
        Spacer(modifier = Modifier.height(24.dp))

        OrganicCard(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Estimated Arrival", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    Text(
                        order.dateLabel.substringAfter("· ").trim(),
                        style = MaterialTheme.typography.labelLarge.copy(color = SecondaryForest),
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Delivering To", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    Text(
                        addressStreet.substringBefore(","),
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Total Paid", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    Text(
                        formatRs(order.totalRs),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontFamily = PlusJakartaSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                        ),
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = BorderSubtle)
                Text(
                    text = order.itemsSummary,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
        PrimaryEmeraldButton(
            text = "Continue shopping",
            onClick = onContinueShopping,
            modifier = Modifier.testTag("order_confirmed_shop_btn"),
        )
        Spacer(modifier = Modifier.height(12.dp))
        SecondaryOutlinedButton(
            text = "View my orders",
            onClick = onViewOrders,
            modifier = Modifier.testTag("order_confirmed_orders_btn"),
        )
    }
}
