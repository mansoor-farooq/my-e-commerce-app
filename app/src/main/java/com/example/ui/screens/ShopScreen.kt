package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.CatalogRepository
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.ShopSortOption
import com.example.model.StockStatus
import com.example.ui.components.ProductImageTile
import com.example.ui.components.formatRs
import com.example.ui.state.CommerceUiState
import com.example.ui.theme.BorderCard
import com.example.ui.theme.PlusJakartaSansFamily
import com.example.ui.theme.PriceTypography
import com.example.ui.theme.PrimaryDeepForest
import com.example.ui.theme.PrimaryEmerald
import com.example.ui.theme.SecondaryEmerald
import com.example.ui.theme.SecondaryForest
import com.example.ui.theme.SurfaceCanvas
import com.example.ui.theme.SurfaceStepper
import com.example.ui.theme.SurfaceStepperButton
import com.example.ui.theme.SurfaceSubtleMint
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TertiaryRuby
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun ShopScreen(
    uiState: CommerceUiState,
    onSearchQueryChange: (String) -> Unit,
    onSelectCategory: (ProductCategory) -> Unit,
    onSelectSortOption: (ShopSortOption) -> Unit,
    onToggleOnSaleOnly: () -> Unit,
    onOpenProductDetail: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    onIncrementCart: (String) -> Unit,
    onDecrementCart: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onNavigateToAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cartQuantities = uiState.cartItems.associate { it.product.id to it.quantity }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceCanvas),
    ) {
        val columnCount = when {
            maxWidth >= 840.dp -> 4
            maxWidth >= 600.dp -> 3
            else -> 2
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(columnCount),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item(span = { GridItemSpan(columnCount) }) {
                ShopHeaderSection(
                    addressLabel = uiState.selectedAddress.label,
                    addressStreet = uiState.selectedAddress.street,
                    onAddressClick = onNavigateToAccount,
                    modifier = Modifier.statusBarsPadding(),
                )
            }

            item(span = { GridItemSpan(columnCount) }) {
                OrganicSearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = onSearchQueryChange,
                )
            }

            if (uiState.isCategorizedBrowseMode) {
                item(span = { GridItemSpan(columnCount) }) {
                    SeasonalHarvestBanner(
                        onExploreDeals = {
                            if (!uiState.onSaleOnly) onToggleOnSaleOnly()
                        },
                    )
                }
            }

            item(span = { GridItemSpan(columnCount) }) {
                CategoryFilterRow(
                    selectedCategory = uiState.selectedCategory,
                    categoryCounts = uiState.categoryCounts,
                    onSelectCategory = onSelectCategory,
                )
            }

            item(span = { GridItemSpan(columnCount) }) {
                SortAndDealsBar(
                    sortOption = uiState.sortOption,
                    onSaleOnly = uiState.onSaleOnly,
                    resultCount = uiState.filteredProducts.size,
                    onSelectSortOption = onSelectSortOption,
                    onToggleOnSaleOnly = onToggleOnSaleOnly,
                )
            }

            if (uiState.isCategorizedBrowseMode) {
                uiState.categorizedGroups.forEach { group ->
                    item(
                        key = "header_${group.category.name}",
                        span = { GridItemSpan(columnCount) },
                    ) {
                        CategorySectionHeader(
                            category = group.category,
                            itemCount = group.products.size,
                            onSeeAllClick = { onSelectCategory(group.category) },
                        )
                    }

                    items(
                        items = group.products,
                        key = { "${group.category.name}_${it.id}" },
                    ) { product ->
                        ProductGridCard(
                            product = product,
                            cartQuantity = cartQuantities[product.id] ?: 0,
                            isFavorite = product.id in uiState.favoriteIds,
                            onClick = { onOpenProductDetail(product) },
                            onAdd = { onAddToCart(product) },
                            onIncrement = { onIncrementCart(product.id) },
                            onDecrement = { onDecrementCart(product.id) },
                            onToggleFavorite = { onToggleFavorite(product.id) },
                        )
                    }
                }
            } else if (uiState.filteredProducts.isEmpty()) {
                item(span = { GridItemSpan(columnCount) }) {
                    EmptySearchState(
                        query = uiState.searchQuery,
                        onReset = {
                            onSearchQueryChange("")
                            onSelectCategory(ProductCategory.ALL)
                            onSelectSortOption(ShopSortOption.FEATURED)
                            if (uiState.onSaleOnly) onToggleOnSaleOnly()
                        },
                    )
                }
            } else {
                item(span = { GridItemSpan(columnCount) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(
                                text = if (uiState.selectedCategory == ProductCategory.ALL) {
                                    "Filtered Results"
                                } else {
                                    uiState.selectedCategory.label
                                },
                                style = MaterialTheme.typography.headlineMedium,
                                color = TextPrimary,
                            )
                            Text(
                                text = uiState.selectedCategory.sectionSubtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                            )
                        }
                        Text(
                            text = "${uiState.filteredProducts.size} items",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary,
                        )
                    }
                }

                items(
                    items = uiState.filteredProducts,
                    key = { it.id },
                ) { product ->
                    ProductGridCard(
                        product = product,
                        cartQuantity = cartQuantities[product.id] ?: 0,
                        isFavorite = product.id in uiState.favoriteIds,
                        onClick = { onOpenProductDetail(product) },
                        onAdd = { onAddToCart(product) },
                        onIncrement = { onIncrementCart(product.id) },
                        onDecrement = { onDecrementCart(product.id) },
                        onToggleFavorite = { onToggleFavorite(product.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ShopHeaderSection(
    addressLabel: String,
    addressStreet: String,
    onAddressClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(SecondaryEmerald),
                )
                Text(
                    text = "FRESH ORGANIC MARKET",
                    style = MaterialTheme.typography.labelSmall.copy(color = SecondaryForest),
                )
            }
            Text(
                text = "FreshMart",
                style = MaterialTheme.typography.displayLarge,
                color = TextPrimary,
            )
        }

        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(9999.dp))
                .clickable(onClick = onAddressClick)
                .testTag("delivery_location_chip"),
            shape = RoundedCornerShape(9999.dp),
            color = SurfaceWhite,
            border = BorderStroke(1.dp, BorderCard),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Delivery address",
                    tint = PrimaryEmerald,
                    modifier = Modifier.size(16.dp),
                )
                Column {
                    Text(
                        text = "$addressLabel · 18 mins",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                    Text(
                        text = addressStreet.substringBefore(","),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun OrganicSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, BorderCard),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search groceries",
                tint = TextSecondary,
                modifier = Modifier.size(20.dp),
            )
            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        text = "Search avocados, sourdough, farm eggs...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextTertiary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                    cursorBrush = SolidColor(PrimaryEmerald),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("shop_search_input"),
                )
            }
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.size(28.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear search",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun SeasonalHarvestBanner(
    onExploreDeals: () -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(152.dp)
            .shadow(
                elevation = 6.dp,
                shape = shape,
                ambientColor = PrimaryEmerald.copy(alpha = 0.12f),
            )
            .clip(shape)
            .background(PrimaryDeepForest)
            .clickable(onClick = onExploreDeals)
            .testTag("harvest_hero_banner"),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(CatalogRepository.HERO_BANNER_IMAGE_URL)
                .crossfade(true)
                .build(),
            contentDescription = "Organic harvest market",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            PrimaryDeepForest.copy(alpha = 0.92f),
                            PrimaryEmerald.copy(alpha = 0.78f),
                            PrimaryEmerald.copy(alpha = 0.25f),
                        ),
                    ),
                ),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Surface(
                shape = RoundedCornerShape(9999.dp),
                color = SecondaryEmerald,
            ) {
                Text(
                    text = "MORNING HARVEST · UP TO 20% OFF",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Farm-to-Kitchen\nin 18 Minutes",
                    style = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
                )
                Text(
                    text = "Tap to view today's discounted organic picks",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.86f),
                    ),
                )
            }
        }
    }
}

@Composable
private fun CategoryFilterRow(
    selectedCategory: ProductCategory,
    categoryCounts: Map<ProductCategory, Int>,
    onSelectCategory: (ProductCategory) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 2.dp),
    ) {
        items(ProductCategory.entries) { category ->
            val selected = category == selectedCategory
            val count = categoryCounts[category] ?: 0
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .clickable { onSelectCategory(category) }
                    .testTag("category_chip_${category.name}"),
                shape = RoundedCornerShape(9999.dp),
                color = if (selected) PrimaryEmerald else SurfaceWhite,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (selected) PrimaryEmerald else BorderCard,
                ),
            ) {
                Text(
                    text = "${category.label} ($count)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = if (selected) Color.White else TextSecondary,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                    ),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                )
            }
        }
    }
}

@Composable
private fun SortAndDealsBar(
    sortOption: ShopSortOption,
    onSaleOnly: Boolean,
    resultCount: Int,
    onSelectSortOption: (ShopSortOption) -> Unit,
    onToggleOnSaleOnly: () -> Unit,
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box {
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { sortMenuExpanded = true }
                        .testTag("sort_dropdown_button"),
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceWhite,
                    border = BorderStroke(1.dp, BorderCard),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SwapVert,
                            contentDescription = "Sort products",
                            tint = PrimaryEmerald,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = sortOption.label,
                            style = MaterialTheme.typography.labelMedium.copy(color = TextPrimary),
                        )
                    }
                }

                DropdownMenu(
                    expanded = sortMenuExpanded,
                    onDismissRequest = { sortMenuExpanded = false },
                    containerColor = SurfaceWhite,
                ) {
                    ShopSortOption.entries.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option.label,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (option == sortOption) PrimaryEmerald else TextPrimary,
                                        fontWeight = if (option == sortOption) FontWeight.Bold else FontWeight.Normal,
                                    ),
                                )
                            },
                            onClick = {
                                onSelectSortOption(option)
                                sortMenuExpanded = false
                            },
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onToggleOnSaleOnly)
                    .testTag("on_sale_filter_chip"),
                shape = RoundedCornerShape(10.dp),
                color = if (onSaleOnly) TertiaryRuby else SurfaceWhite,
                border = BorderStroke(1.dp, if (onSaleOnly) TertiaryRuby else BorderCard),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocalOffer,
                        contentDescription = null,
                        tint = if (onSaleOnly) Color.White else TertiaryRuby,
                        modifier = Modifier.size(14.dp),
                    )
                    Text(
                        text = "On Sale",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (onSaleOnly) Color.White else TextPrimary,
                        ),
                    )
                }
            }
        }

        Text(
            text = "$resultCount products",
            style = MaterialTheme.typography.labelMedium,
            color = TextSecondary,
        )
    }
}

@Composable
private fun CategorySectionHeader(
    category: ProductCategory,
    itemCount: Int,
    onSeeAllClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = category.label,
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
            )
            Text(
                text = "${category.sectionSubtitle} · $itemCount items",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
            )
        }

        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(9999.dp))
                .clickable(onClick = onSeeAllClick)
                .testTag("see_all_${category.name}"),
            shape = RoundedCornerShape(9999.dp),
            color = SurfaceSubtleMint,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "See all",
                    style = MaterialTheme.typography.labelMedium.copy(color = PrimaryEmerald),
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View all ${category.label}",
                    tint = PrimaryEmerald,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
}

@Composable
private fun ProductGridCard(
    product: Product,
    cartQuantity: Int,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onAdd: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onToggleFavorite: () -> Unit,
) {
    val cardShape = RoundedCornerShape(16.dp)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = cardShape,
                ambientColor = PrimaryEmerald.copy(alpha = 0.08f),
                spotColor = PrimaryEmerald.copy(alpha = 0.05f),
            )
            .clip(cardShape)
            .clickable(onClick = onClick)
            .testTag("product_card_${product.id}"),
        shape = cardShape,
        color = SurfaceWhite,
        border = BorderStroke(1.dp, BorderCard),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                ProductImageTile(
                    imageUrl = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(136.dp),
                    cornerRadius = 12.dp,
                )
                product.badgeText?.let { badge ->
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        shape = RoundedCornerShape(9999.dp),
                        color = if (product.isDiscountBadge) TertiaryRuby else SurfaceSubtleMint,
                    ) {
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (product.isDiscountBadge) Color.White else PrimaryEmerald,
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(SurfaceWhite.copy(alpha = 0.9f))
                        .clickable(onClick = onToggleFavorite)
                        .testTag("fav_btn_${product.id}"),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Save favorite",
                        tint = if (isFavorite) TertiaryRuby else TextTertiary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = product.brandTag,
                    style = MaterialTheme.typography.labelSmall.copy(color = SecondaryForest),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        lineHeight = MaterialTheme.typography.titleMedium.lineHeight,
                    ),
                    color = TextPrimary,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = if (product.pricing.unitPriceHint.isNotEmpty()) {
                        "${product.unitWeight} · ${product.pricing.unitPriceHint}"
                    } else {
                        product.unitWeight
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (product.stockStatus == StockStatus.LOW_STOCK) {
                    Text(
                        text = product.stockStatus.label ?: "",
                        style = MaterialTheme.typography.labelSmall.copy(color = TertiaryRuby),
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
                        text = formatRs(product.priceRs),
                        style = PriceTypography.medium,
                    )
                    product.originalPriceRs?.let { orig ->
                        Text(
                            text = formatRs(orig),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                textDecoration = TextDecoration.LineThrough,
                            ),
                        )
                    }
                }

                if (cartQuantity == 0) {
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(onClick = onAdd)
                            .testTag("add_btn_${product.id}"),
                        shape = RoundedCornerShape(10.dp),
                        color = PrimaryEmerald,
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add ${product.name}",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp),
                            )
                            Text(
                                text = "Add",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                ),
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceStepper)
                            .border(1.dp, BorderCard, RoundedCornerShape(10.dp))
                            .padding(3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(SurfaceStepperButton)
                                .clickable(onClick = onDecrement),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Remove,
                                contentDescription = "Decrease",
                                tint = TextPrimary,
                                modifier = Modifier.size(14.dp),
                            )
                        }
                        Text(
                            text = cartQuantity.toString(),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = PlusJakartaSansFamily,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                            ),
                            modifier = Modifier.padding(horizontal = 3.dp),
                        )
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(PrimaryEmerald)
                                .clickable(onClick = onIncrement),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Increase",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptySearchState(
    query: String,
    onReset: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 24.dp),
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
            text = if (query.isNotBlank()) "No matches for \"$query\"" else "No products in this filter",
            style = MaterialTheme.typography.headlineSmall,
            color = TextPrimary,
        )
        Text(
            text = "Try another search term or explore all fresh seasonal produce.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onReset),
            shape = RoundedCornerShape(12.dp),
            color = PrimaryEmerald,
        ) {
            Text(
                text = "Reset filters",
                style = MaterialTheme.typography.labelLarge.copy(color = Color.White),
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            )
        }
    }
}
