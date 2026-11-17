package com.example.ui.state

import androidx.lifecycle.ViewModel
import com.example.data.CatalogRepository
import com.example.model.CartItem
import com.example.model.CategorizedProductGroup
import com.example.model.DeliveryAddress
import com.example.model.DeliverySlot
import com.example.model.OrderRecord
import com.example.model.PaymentMethod
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.ShopSortOption
import com.example.model.SourcingPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.max
import kotlin.random.Random

data class CommerceUiState(
    val products: List<Product> = CatalogRepository.products,
    val selectedCategory: ProductCategory = ProductCategory.ALL,
    val sortOption: ShopSortOption = ShopSortOption.FEATURED,
    val onSaleOnly: Boolean = false,
    val searchQuery: String = "",
    val cartItems: List<CartItem> = CatalogRepository.initialCart,
    val favoriteIds: Set<String> = CatalogRepository.initialFavorites,
    val addresses: List<DeliveryAddress> = CatalogRepository.addresses,
    val selectedAddressId: String = CatalogRepository.addresses.first().id,
    val deliverySlots: List<DeliverySlot> = CatalogRepository.deliverySlots,
    val selectedSlotId: String = CatalogRepository.deliverySlots.first().id,
    val paymentMethods: List<PaymentMethod> = CatalogRepository.paymentMethods,
    val selectedPaymentId: String = CatalogRepository.paymentMethods.first().id,
    val promoApplied: Boolean = false,
    val orders: List<OrderRecord> = CatalogRepository.initialOrders,
    val sourcingPreferences: SourcingPreferences = SourcingPreferences(),
    val selectedProductForDetail: Product? = null,
    val confirmedOrder: OrderRecord? = null,
    val isSignedIn: Boolean = true,
    val userName: String = "Elena Vance",
    val userEmail: String = "elena.vance@organicmail.com",
    val ecoPoints: Int = 640,
    val statusMessage: String? = null,
) {
    val categoryCounts: Map<ProductCategory, Int>
        get() = buildMap {
            put(ProductCategory.ALL, products.size)
            ProductCategory.entries
                .filter { it != ProductCategory.ALL }
                .forEach { cat ->
                    put(cat, products.count { it.category == cat })
                }
        }

    val filteredProducts: List<Product>
        get() {
            val q = searchQuery.trim()
            val matching = products.filter { product ->
                val matchesCategory =
                    selectedCategory == ProductCategory.ALL || product.category == selectedCategory
                val matchesSale = !onSaleOnly || product.pricing.isOnSale
                val matchesQuery = q.isEmpty() ||
                    product.name.contains(q, ignoreCase = true) ||
                    product.brandTag.contains(q, ignoreCase = true) ||
                    product.originFarm.contains(q, ignoreCase = true) ||
                    product.category.label.contains(q, ignoreCase = true)
                matchesCategory && matchesSale && matchesQuery
            }
            return when (sortOption) {
                ShopSortOption.FEATURED -> matching
                ShopSortOption.PRICE_LOW_HIGH -> matching.sortedBy { it.priceRs }
                ShopSortOption.PRICE_HIGH_LOW -> matching.sortedByDescending { it.priceRs }
                ShopSortOption.BIGGEST_SAVINGS -> matching.sortedByDescending { it.discountPercent ?: 0 }
                ShopSortOption.TOP_RATED -> matching.sortedByDescending { it.rating }
            }
        }

    val isCategorizedBrowseMode: Boolean
        get() = selectedCategory == ProductCategory.ALL &&
            searchQuery.isBlank() &&
            sortOption == ShopSortOption.FEATURED &&
            !onSaleOnly

    val categorizedGroups: List<CategorizedProductGroup>
        get() = ProductCategory.entries
            .filter { it != ProductCategory.ALL }
            .mapNotNull { cat ->
                val items = products.filter { it.category == cat }
                if (items.isEmpty()) null else CategorizedProductGroup(cat, items)
            }

    val totalCartCount: Int get() = cartItems.sumOf { it.quantity }

    val subtotalRs: Int get() = cartItems.sumOf { it.lineTotalRs }

    val itemSavingsRs: Int
        get() = cartItems.sumOf { max(0, it.lineOriginalRs - it.lineTotalRs) }

    val promoDiscountRs: Int
        get() = if (promoApplied && subtotalRs > 0) (subtotalRs * 0.10f).toInt() else 0

    val selectedAddress: DeliveryAddress
        get() = addresses.firstOrNull { it.id == selectedAddressId } ?: addresses.first()

    val selectedSlot: DeliverySlot
        get() = deliverySlots.firstOrNull { it.id == selectedSlotId } ?: deliverySlots.first()

    val selectedPayment: PaymentMethod
        get() = paymentMethods.firstOrNull { it.id == selectedPaymentId } ?: paymentMethods.first()

    val freeDeliveryThresholdRs: Int get() = 1200

    val qualifiesFreeDelivery: Boolean get() = subtotalRs >= freeDeliveryThresholdRs

    val deliveryFeeRs: Int
        get() = when {
            cartItems.isEmpty() -> 0
            qualifiesFreeDelivery -> 0
            else -> selectedSlot.feeRs
        }

    val grandTotalRs: Int
        get() = max(0, subtotalRs - promoDiscountRs + deliveryFeeRs)

    val favoriteProducts: List<Product>
        get() = products.filter { it.id in favoriteIds }
}

class CommerceViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CommerceUiState())
    val uiState: StateFlow<CommerceUiState> = _uiState.asStateFlow()

    fun selectCategory(category: ProductCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun selectSortOption(option: ShopSortOption) {
        _uiState.update { it.copy(sortOption = option) }
    }

    fun toggleOnSaleOnly() {
        _uiState.update { it.copy(onSaleOnly = !it.onSaleOnly) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun openProductDetail(product: Product) {
        _uiState.update { it.copy(selectedProductForDetail = product) }
    }

    fun dismissProductDetail() {
        _uiState.update { it.copy(selectedProductForDetail = null) }
    }

    fun addToCart(product: Product, amount: Int = 1) {
        _uiState.update { state ->
            val existing = state.cartItems.find { it.product.id == product.id }
            val updated = if (existing != null) {
                state.cartItems.map { item ->
                    if (item.product.id == product.id) {
                        item.copy(quantity = item.quantity + amount)
                    } else {
                        item
                    }
                }
            } else {
                state.cartItems + CartItem(product = product, quantity = amount)
            }
            state.copy(
                cartItems = updated,
                statusMessage = "Added ${product.name} to basket",
            )
        }
    }

    fun incrementCartItem(productId: String) {
        _uiState.update { state ->
            val updated = state.cartItems.map { item ->
                if (item.product.id == productId) item.copy(quantity = item.quantity + 1) else item
            }
            state.copy(cartItems = updated)
        }
    }

    fun decrementCartItem(productId: String) {
        _uiState.update { state ->
            val updated = state.cartItems.mapNotNull { item ->
                if (item.product.id == productId) {
                    if (item.quantity > 1) item.copy(quantity = item.quantity - 1) else null
                } else {
                    item
                }
            }
            state.copy(cartItems = updated)
        }
    }

    fun removeFromCart(productId: String) {
        _uiState.update { state ->
            val removedName = state.cartItems.find { it.product.id == productId }?.product?.name
            state.copy(
                cartItems = state.cartItems.filterNot { it.product.id == productId },
                statusMessage = removedName?.let { "Removed $it from basket" },
            )
        }
    }

    fun clearCart() {
        _uiState.update { it.copy(cartItems = emptyList(), promoApplied = false) }
    }

    fun toggleFavorite(productId: String) {
        _uiState.update { state ->
            val next = if (productId in state.favoriteIds) {
                state.favoriteIds - productId
            } else {
                state.favoriteIds + productId
            }
            state.copy(favoriteIds = next)
        }
    }

    fun togglePromoCode() {
        _uiState.update { state ->
            val next = !state.promoApplied
            state.copy(
                promoApplied = next,
                statusMessage = if (next) "Applied ORGANIC10 (10% off)" else "Promo removed",
            )
        }
    }

    fun selectAddress(addressId: String) {
        _uiState.update { it.copy(selectedAddressId = addressId) }
    }

    fun selectDeliverySlot(slotId: String) {
        _uiState.update { it.copy(selectedSlotId = slotId) }
    }

    fun selectPaymentMethod(paymentId: String) {
        _uiState.update { it.copy(selectedPaymentId = paymentId) }
    }

    fun updateSourcingPreferences(
        zeroPlastic: Boolean = _uiState.value.sourcingPreferences.zeroPlasticPackaging,
        localFarm: Boolean = _uiState.value.sourcingPreferences.localFarmPriority,
        notifications: Boolean = _uiState.value.sourcingPreferences.orderNotifications,
    ) {
        _uiState.update {
            it.copy(
                sourcingPreferences = SourcingPreferences(
                    zeroPlasticPackaging = zeroPlastic,
                    localFarmPriority = localFarm,
                    orderNotifications = notifications,
                )
            )
        }
    }

    fun placeOrder() {
        _uiState.update { state ->
            if (state.cartItems.isEmpty()) return@update state
            val orderNum = "FM-${Random.nextInt(85000, 99999)}"
            val count = state.totalCartCount
            val firstTwo = state.cartItems.take(2).joinToString(", ") { it.product.name }
            val extra = state.cartItems.size - 2
            val summary = if (extra > 0) "$firstTwo +$extra more" else firstTwo
            val newOrder = OrderRecord(
                orderId = orderNum,
                dateLabel = "Just now · ${state.selectedSlot.etaLabel}",
                itemCount = count,
                totalRs = state.grandTotalRs,
                status = "Confirmed",
                itemsSummary = summary,
                productIds = state.cartItems.map { it.product.id },
            )
            state.copy(
                orders = listOf(newOrder) + state.orders,
                cartItems = emptyList(),
                promoApplied = false,
                confirmedOrder = newOrder,
                ecoPoints = state.ecoPoints + (count * 15),
            )
        }
    }

    fun dismissOrderConfirmation() {
        _uiState.update { it.copy(confirmedOrder = null) }
    }

    fun reorderFromHistory(order: OrderRecord) {
        _uiState.update { state ->
            val itemsToAdd = state.products.filter { it.id in order.productIds }
            var currentCart = state.cartItems
            itemsToAdd.forEach { product ->
                val existing = currentCart.find { it.product.id == product.id }
                currentCart = if (existing != null) {
                    currentCart.map {
                        if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
                    }
                } else {
                    currentCart + CartItem(product = product, quantity = 1)
                }
            }
            state.copy(
                cartItems = currentCart,
                statusMessage = "Added ${itemsToAdd.size} items from ${order.orderId} to basket",
            )
        }
    }

    fun signOut() {
        _uiState.update {
            it.copy(
                isSignedIn = false,
                statusMessage = "Signed out of Elena Vance",
            )
        }
    }

    fun signInDemo() {
        _uiState.update {
            it.copy(
                isSignedIn = true,
                statusMessage = "Welcome back, Elena!",
            )
        }
    }

    fun clearStatusMessage() {
        _uiState.update { it.copy(statusMessage = null) }
    }
}
