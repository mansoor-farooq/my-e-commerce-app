package com.example.model

enum class ProductCategory(val label: String, val sectionSubtitle: String) {
    ALL("All", "Everything in store"),
    PRODUCE("Produce", "Harvested within 24 hours"),
    BERRIES("Fruits & Berries", "Orchard-picked sweetness"),
    DAIRY_EGGS("Dairy & Eggs", "Pasture-raised & A2 fresh"),
    BAKERY("Bakery", "Stone-milled & baked daily"),
    PANTRY("Pantry", "Cold-pressed oils & raw staples"),
    BEVERAGES("Beverages", "Single-estate brews & plant milks"),
}

enum class ShopSortOption(val label: String) {
    FEATURED("Featured"),
    PRICE_LOW_HIGH("Price: Low to High"),
    PRICE_HIGH_LOW("Price: High to Low"),
    BIGGEST_SAVINGS("Best Deals"),
    TOP_RATED("Top Rated"),
}

enum class StockStatus(val label: String?) {
    IN_STOCK(null),
    LOW_STOCK("Only a few left"),
    OUT_OF_STOCK("Out of stock"),
}

data class GroceryPricing(
    val currentPriceRs: Int,
    val originalPriceRs: Int? = null,
    val unitPriceHint: String = "",
) {
    val discountPercent: Int?
        get() = originalPriceRs?.let { orig ->
            if (orig > currentPriceRs) (((orig - currentPriceRs) * 100f) / orig).toInt() else null
        }

    val isOnSale: Boolean
        get() = originalPriceRs != null && originalPriceRs > currentPriceRs
}

data class Product(
    val id: String,
    val name: String,
    val brandTag: String,
    val unitWeight: String,
    val pricing: GroceryPricing,
    val category: ProductCategory,
    val imageUrl: String,
    val badgeText: String? = null,
    val isDiscountBadge: Boolean = false,
    val stockStatus: StockStatus = StockStatus.IN_STOCK,
    val dietaryTags: List<String> = emptyList(),
    val originFarm: String,
    val description: String,
    val nutritionHighlight: String,
    val rating: Double = 4.8,
    val reviewCount: Int = 124,
) {
    val priceRs: Int get() = pricing.currentPriceRs
    val originalPriceRs: Int? get() = pricing.originalPriceRs
    val discountPercent: Int? get() = pricing.discountPercent
}

typealias GroceryItem = Product

data class CategorizedProductGroup(
    val category: ProductCategory,
    val products: List<Product>,
)

data class CartItem(
    val product: Product,
    val quantity: Int,
) {
    val lineTotalRs: Int get() = product.priceRs * quantity
    val lineOriginalRs: Int get() = (product.originalPriceRs ?: product.priceRs) * quantity
}

data class DeliveryAddress(
    val id: String,
    val label: String,
    val street: String,
    val apartment: String,
    val city: String,
    val instructions: String,
)

data class DeliverySlot(
    val id: String,
    val title: String,
    val etaLabel: String,
    val feeRs: Int,
)

data class PaymentMethod(
    val id: String,
    val title: String,
    val subtitle: String,
)

data class OrderRecord(
    val orderId: String,
    val dateLabel: String,
    val itemCount: Int,
    val totalRs: Int,
    val status: String,
    val itemsSummary: String,
    val productIds: List<String>,
)

data class SourcingPreferences(
    val zeroPlasticPackaging: Boolean = true,
    val localFarmPriority: Boolean = true,
    val orderNotifications: Boolean = true,
)

enum class AppTab(val route: String, val label: String) {
    SHOP("shop", "Shop"),
    CART("cart", "Cart"),
    ACCOUNT("account", "Account"),
}
