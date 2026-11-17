package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.CartItem
import com.example.ui.components.OrganicCard
import com.example.ui.components.PrimaryEmeraldButton
import com.example.ui.components.ProductImageTile
import com.example.ui.components.QuantityStepperCapsule
import com.example.ui.components.ReceiptSummaryCard
import com.example.ui.components.formatRs
import com.example.ui.state.CommerceUiState
import com.example.ui.theme.BorderCard
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.PriceTypography
import com.example.ui.theme.PrimaryEmerald
import com.example.ui.theme.SecondaryEmerald
import com.example.ui.theme.SecondaryForest
import com.example.ui.theme.SurfaceCanvas
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceSubtleMint
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TertiaryRuby
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun CartScreen(
    uiState: CommerceUiState,
    onIncrementItem: (String) -> Unit,
    onDecrementItem: (String) -> Unit,
    onRemoveItem: (String) -> Unit,
    onClearCart: () -> Unit,
    onTogglePromo: () -> Unit,
    onCheckoutClick: () -> Unit,
    onBrowseShop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceCanvas),
    ) {
        if (uiState.cartItems.isEmpty()) {
            EmptyCartView(
                onBrowseShop = onBrowseShop,
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 24.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item {
                    CartHeaderRow(
                        totalItems = uiState.totalCartCount,
                        onClearCart = onClearCart,
                        modifier = Modifier.statusBarsPadding(),
                    )
                }

                item {
                    FreeDeliveryProgressCard(
                        subtotalRs = uiState.subtotalRs,
                        thresholdRs = uiState.freeDeliveryThresholdRs,
                    )
                }

                items(
                    items = uiState.cartItems,
                    key = { it.product.id },
                ) { cartItem ->
                    CartItemCard(
                        item = cartItem,
                        onIncrement = { onIncrementItem(cartItem.product.id) },
                        onDecrement = { onDecrementItem(cartItem.product.id) },
                        onRemove = { onRemoveItem(cartItem.product.id) },
                    )
                }

                item {
                    PromoVoucherCard(
                        promoApplied = uiState.promoApplied,
                        promoDiscountRs = uiState.promoDiscountRs,
                        onTogglePromo = onTogglePromo,
                    )
                }

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

            // Tier 2 Pinned Checkout Summary Footer
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
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    ) {
                        PrimaryEmeraldButton(
                            text = "Continue to checkout",
                            leadingIcon = Icons.Filled.Lock,
                            trailingText = formatRs(uiState.grandTotalRs),
                            onClick = onCheckoutClick,
                            modifier = Modifier.testTag("cart_checkout_button"),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CartHeaderRow(
    totalItems: Int,
    onClearCart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "My Basket",
                style = MaterialTheme.typography.displayLarge,
                color = TextPrimary,
            )
            Surface(
                shape = RoundedCornerShape(9999.dp),
                color = SurfaceSubtleMint,
            ) {
                Text(
                    text = "$totalItems ${if (totalItems == 1) "item" else "items"}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = PrimaryEmerald,
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                )
            }
        }

        TextButton(
            onClick = onClearCart,
            modifier = Modifier.testTag("clear_cart_button"),
        ) {
            Text(
                text = "Clear",
                style = MaterialTheme.typography.labelLarge.copy(color = TertiaryRuby),
            )
        }
    }
}

@Composable
private fun FreeDeliveryProgressCard(
    subtotalRs: Int,
    thresholdRs: Int,
) {
    val qualified = subtotalRs >= thresholdRs
    val progress = (subtotalRs.toFloat() / thresholdRs.toFloat()).coerceIn(0f, 1f)
    val remaining = (thresholdRs - subtotalRs).coerceAtLeast(0)

    OrganicCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(14.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = if (qualified) SecondaryEmerald else TextTertiary,
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    text = if (qualified) {
                        "You unlocked Free Carbon-Neutral Express Delivery!"
                    } else {
                        "Add ${formatRs(remaining)} more for Free Express Delivery"
                    },
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = if (qualified) SecondaryForest else TextPrimary,
                    ),
                )
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(9999.dp)),
                color = SecondaryEmerald,
                trackColor = SurfaceContainerLow,
                strokeCap = StrokeCap.Round,
            )
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit,
) {
    OrganicCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("cart_item_card_${item.product.id}"),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProductImageTile(
                imageUrl = item.product.imageUrl,
                contentDescription = item.product.name,
                modifier = Modifier.size(80.dp),
                cornerRadius = 12.dp,
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Text(
                            text = item.product.brandTag,
                            style = MaterialTheme.typography.labelSmall.copy(color = SecondaryForest),
                        )
                        Text(
                            text = item.product.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = TextPrimary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = item.product.unitWeight,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                        )
                    }

                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("remove_cart_item_${item.product.id}"),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Remove ${item.product.name}",
                            tint = TextTertiary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = formatRs(item.lineTotalRs),
                            style = PriceTypography.medium,
                        )
                        if (item.lineOriginalRs > item.lineTotalRs) {
                            Text(
                                text = formatRs(item.lineOriginalRs),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    textDecoration = TextDecoration.LineThrough,
                                ),
                            )
                        }
                    }

                    QuantityStepperCapsule(
                        quantity = item.quantity,
                        onDecrement = onDecrement,
                        onIncrement = onIncrement,
                        testTagPrefix = "cart_stepper_${item.product.id}",
                    )
                }
            }
        }
    }
}

@Composable
private fun PromoVoucherCard(
    promoApplied: Boolean,
    promoDiscountRs: Int,
    onTogglePromo: () -> Unit,
) {
    val containerColor by animateColorAsState(
        targetValue = if (promoApplied) SurfaceSubtleMint else SurfaceWhite,
        label = "promoBg",
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onTogglePromo)
            .testTag("promo_voucher_card"),
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = BorderStroke(
            width = 1.dp,
            color = if (promoApplied) SecondaryEmerald else BorderCard,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (promoApplied) PrimaryEmerald else SurfaceSubtleMint),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocalOffer,
                        contentDescription = null,
                        tint = if (promoApplied) Color.White else PrimaryEmerald,
                        modifier = Modifier.size(18.dp),
                    )
                }
                Column {
                    Text(
                        text = if (promoApplied) "ORGANIC10 Applied" else "Use code ORGANIC10",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                    )
                    Text(
                        text = if (promoApplied) {
                            "Saving ${formatRs(promoDiscountRs)} on this basket"
                        } else {
                            "Tap to save 10% on organic produce"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (promoApplied) SecondaryForest else TextSecondary,
                    )
                }
            }
            Text(
                text = if (promoApplied) "Remove" else "Apply",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = if (promoApplied) TertiaryRuby else PrimaryEmerald,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}

@Composable
private fun EmptyCartView(
    onBrowseShop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 32.dp),
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
                imageVector = Icons.Filled.ShoppingBag,
                contentDescription = null,
                tint = PrimaryEmerald,
                modifier = Modifier.size(40.dp),
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Your basket is empty",
            style = MaterialTheme.typography.headlineLarge,
            color = TextPrimary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Add crisp organic greens, mountain berries, and freshly baked sourdough to get started.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.width(220.dp)) {
            PrimaryEmeraldButton(
                text = "Browse fresh harvest",
                onClick = onBrowseShop,
                modifier = Modifier.testTag("empty_cart_browse_button"),
            )
        }
    }
}
