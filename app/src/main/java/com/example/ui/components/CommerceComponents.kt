package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.model.AppTab
import com.example.model.Product
import com.example.ui.theme.BorderCard
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.PlusJakartaSansFamily
import com.example.ui.theme.PriceTypography
import com.example.ui.theme.PrimaryEmerald
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.SecondaryEmerald
import com.example.ui.theme.SecondaryForest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceStepper
import com.example.ui.theme.SurfaceStepperButton
import com.example.ui.theme.SurfaceSubtleMint
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TertiaryContainerRuby
import com.example.ui.theme.TertiaryRuby
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.NumberFormat
import java.util.Locale

fun formatRs(amount: Int): String {
    val formatted = NumberFormat.getNumberInstance(Locale.US).format(amount)
    return "Rs $formatted"
}

@Composable
fun OrganicCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(cornerRadius)
    Surface(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = shape,
                ambientColor = PrimaryEmerald.copy(alpha = 0.08f),
                spotColor = PrimaryEmerald.copy(alpha = 0.06f),
            )
            .clip(shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = shape,
        color = SurfaceWhite,
        border = BorderStroke(1.dp, BorderCard),
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}

@Composable
fun ProductImageTile(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .clip(shape)
            .background(SurfaceStepper),
        contentAlignment = Alignment.Center,
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SurfaceContainerLow),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Eco,
                        contentDescription = null,
                        tint = PrimaryEmerald.copy(alpha = 0.35f),
                        modifier = Modifier.size(28.dp),
                    )
                }
            },
            error = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SurfaceSubtleMint),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Eco,
                        contentDescription = null,
                        tint = PrimaryEmerald.copy(alpha = 0.65f),
                        modifier = Modifier.size(32.dp),
                    )
                }
            },
        )
    }
}

@Composable
fun QuantityStepperCapsule(
    quantity: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
    testTagPrefix: String = "stepper",
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceStepper)
            .border(1.dp, BorderCard, RoundedCornerShape(12.dp))
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(SurfaceStepperButton)
                .clickable(onClick = onDecrement)
                .testTag("${testTagPrefix}_dec"),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = "Decrease quantity",
                tint = TextPrimary,
                modifier = Modifier.size(16.dp),
            )
        }
        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            ),
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .testTag("${testTagPrefix}_count"),
        )
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(PrimaryEmerald)
                .clickable(onClick = onIncrement)
                .testTag("${testTagPrefix}_inc"),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Increase quantity",
                tint = Color.White,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

@Composable
fun PrimaryEmeraldButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingText: String? = null,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 54.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryEmerald,
            contentColor = Color.White,
            disabledContainerColor = SurfaceContainerLow,
            disabledContentColor = TextSecondary,
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.Bold,
                ),
            )
            if (trailingText != null) {
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    shape = RoundedCornerShape(9999.dp),
                    color = Color.White.copy(alpha = 0.16f),
                ) {
                    Text(
                        text = trailingText,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = PlusJakartaSansFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun SecondaryOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 52.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.5.dp, PrimaryEmerald),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = PrimaryEmerald,
            containerColor = Color.Transparent,
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}

@Composable
fun ReceiptSummaryCard(
    subtotalRs: Int,
    deliveryFeeRs: Int,
    itemSavingsRs: Int,
    promoDiscountRs: Int,
    totalRs: Int,
    modifier: Modifier = Modifier,
) {
    OrganicCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Order Summary",
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
            )
            ReceiptRow(label = "Subtotal", value = formatRs(subtotalRs))
            ReceiptRow(
                label = "Delivery",
                value = if (deliveryFeeRs == 0) "Free" else formatRs(deliveryFeeRs),
                valueColor = if (deliveryFeeRs == 0) SecondaryForest else TextPrimary,
            )
            if (itemSavingsRs > 0) {
                ReceiptRow(
                    label = "Harvest Savings",
                    value = "-${formatRs(itemSavingsRs)}",
                    valueColor = SecondaryEmerald,
                )
            }
            if (promoDiscountRs > 0) {
                ReceiptRow(
                    label = "ORGANIC10 Promo",
                    value = "-${formatRs(promoDiscountRs)}",
                    valueColor = SecondaryEmerald,
                )
            }
            HorizontalDivider(thickness = 1.dp, color = BorderSubtle)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceSubtleMint,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary),
                    )
                    Text(
                        text = formatRs(totalRs),
                        style = PriceTypography.large,
                    )
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(
    label: String,
    value: String,
    valueColor: Color = TextPrimary,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = PlusJakartaSansFamily,
                color = valueColor,
            ),
        )
    }
}

@Composable
fun FreshMartBottomBar(
    currentTab: AppTab,
    cartCount: Int,
    onSelectTab: (AppTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                ambientColor = PrimaryEmerald.copy(alpha = 0.08f),
                spotColor = PrimaryEmerald.copy(alpha = 0.08f),
            ),
        color = SurfaceWhite,
    ) {
        Column {
            HorizontalDivider(thickness = 1.dp, color = BorderSubtle)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppTab.entries.forEach { tab ->
                    val selected = tab == currentTab
                    val pillColor by animateColorAsState(
                        targetValue = if (selected) SurfaceSubtleMint else Color.Transparent,
                        animationSpec = spring(),
                        label = "tabPill",
                    )
                    val iconTint by animateColorAsState(
                        targetValue = if (selected) PrimaryEmerald else TextSecondary,
                        animationSpec = spring(),
                        label = "tabTint",
                    )
                    val icon = when (tab) {
                        AppTab.SHOP -> if (selected) Icons.Filled.Storefront else Icons.Outlined.Storefront
                        AppTab.CART -> if (selected) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag
                        AppTab.ACCOUNT -> if (selected) Icons.Filled.Person else Icons.Outlined.Person
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onSelectTab(tab) }
                            .minimumInteractiveComponentSize()
                            .padding(vertical = 4.dp)
                            .testTag("nav_tab_${tab.route}"),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(9999.dp))
                                .background(pillColor)
                                .padding(horizontal = 18.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = tab.label,
                                    tint = iconTint,
                                    modifier = Modifier.size(22.dp),
                                )
                                if (tab == AppTab.CART && cartCount > 0) {
                                    Surface(
                                        shape = RoundedCornerShape(9999.dp),
                                        color = if (selected) PrimaryEmerald else SecondaryEmerald,
                                    ) {
                                        Text(
                                            text = cartCount.toString(),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                            ),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tab.label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                color = iconTint,
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FreshMartNavRail(
    currentTab: AppTab,
    cartCount: Int,
    onSelectTab: (AppTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(
        modifier = modifier,
        containerColor = SurfaceWhite,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(PrimaryEmerald),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Eco,
                contentDescription = "FreshMart",
                tint = Color.White,
                modifier = Modifier.size(22.dp),
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        AppTab.entries.forEach { tab ->
            val selected = tab == currentTab
            val icon = when (tab) {
                AppTab.SHOP -> if (selected) Icons.Filled.Storefront else Icons.Outlined.Storefront
                AppTab.CART -> if (selected) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag
                AppTab.ACCOUNT -> if (selected) Icons.Filled.Person else Icons.Outlined.Person
            }
            NavigationRailItem(
                selected = selected,
                onClick = { onSelectTab(tab) },
                icon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(imageVector = icon, contentDescription = tab.label)
                        if (tab == AppTab.CART && cartCount > 0) {
                            Surface(
                                shape = RoundedCornerShape(9999.dp),
                                color = SecondaryEmerald,
                            ) {
                                Text(
                                    text = cartCount.toString(),
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                )
                            }
                        }
                    }
                },
                label = { Text(tab.label) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = PrimaryEmerald,
                    selectedTextColor = PrimaryEmerald,
                    indicatorColor = PrimaryFixed,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                ),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailSheet(
    product: Product,
    isFavorite: Boolean,
    inCartQuantity: Int,
    onDismiss: () -> Unit,
    onToggleFavorite: () -> Unit,
    onAddToCart: (Int) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedQty by remember(product.id) { mutableIntStateOf(1) }

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
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                ProductImageTile(
                    imageUrl = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    cornerRadius = 16.dp,
                )
                product.badgeText?.let { badge ->
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp),
                        shape = RoundedCornerShape(9999.dp),
                        color = if (product.isDiscountBadge) TertiaryRuby else SurfaceSubtleMint,
                    ) {
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (product.isDiscountBadge) Color.White else PrimaryEmerald,
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        )
                    }
                }
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(CircleShape)
                        .background(SurfaceWhite.copy(alpha = 0.92f))
                        .testTag("detail_favorite_btn"),
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Toggle favorite",
                        tint = if (isFavorite) TertiaryRuby else TextSecondary,
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = product.brandTag,
                        style = MaterialTheme.typography.labelSmall.copy(color = SecondaryForest),
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = "${product.rating} (${product.reviewCount})",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary,
                        )
                    }
                }
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${product.unitWeight} · ${product.originFarm}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = formatRs(product.priceRs),
                    style = PriceTypography.large,
                )
                product.originalPriceRs?.let { orig ->
                    Text(
                        text = formatRs(orig),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            textDecoration = TextDecoration.LineThrough,
                        ),
                    )
                }
                product.discountPercent?.let { pct ->
                    Surface(
                        shape = RoundedCornerShape(9999.dp),
                        color = TertiaryContainerRuby,
                    ) {
                        Text(
                            text = "Save $pct%",
                            style = MaterialTheme.typography.labelSmall.copy(color = TertiaryRuby),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }
                }
                if (product.pricing.unitPriceHint.isNotEmpty()) {
                    Text(
                        text = "(${product.pricing.unitPriceHint})",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                    )
                }
            }

            if (product.dietaryTags.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    product.dietaryTags.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(9999.dp),
                            color = SurfaceContainerLow,
                            border = BorderStroke(1.dp, BorderCard),
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall.copy(color = TextPrimary),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            )
                        }
                    }
                }
            }

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary.copy(alpha = 0.85f),
            )

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceSubtleMint,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Eco,
                        contentDescription = null,
                        tint = PrimaryEmerald,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = product.nutritionHighlight,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = PrimaryEmerald,
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                }
            }

            if (inCartQuantity > 0) {
                Text(
                    text = "Already in basket: $inCartQuantity",
                    style = MaterialTheme.typography.labelMedium.copy(color = SecondaryForest),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QuantityStepperCapsule(
                    quantity = selectedQty,
                    onDecrement = { if (selectedQty > 1) selectedQty-- },
                    onIncrement = { selectedQty++ },
                    testTagPrefix = "detail_stepper",
                )
                PrimaryEmeraldButton(
                    text = "Add to basket",
                    trailingText = formatRs(product.priceRs * selectedQty),
                    onClick = {
                        onAddToCart(selectedQty)
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("detail_add_button"),
                )
            }
        }
    }
}
