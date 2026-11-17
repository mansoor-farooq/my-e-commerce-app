package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.ProductCategory
import com.example.model.ShopSortOption
import com.example.ui.state.CommerceViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("FreshMart", appName)
    }

    @Test
    fun `viewModel categorizes sorts and prices groceries accurately`() {
        val vm = CommerceViewModel()
        val initialState = vm.uiState.value
        assertTrue(initialState.isCategorizedBrowseMode)
        assertTrue(initialState.categorizedGroups.size >= 5)

        val initialCount = initialState.totalCartCount
        assertTrue(initialCount > 0)

        vm.selectCategory(ProductCategory.BERRIES)
        assertFalse(vm.uiState.value.isCategorizedBrowseMode)
        assertTrue(vm.uiState.value.filteredProducts.all { it.category == ProductCategory.BERRIES })

        vm.selectSortOption(ShopSortOption.PRICE_LOW_HIGH)
        val prices = vm.uiState.value.filteredProducts.map { it.priceRs }
        assertEquals(prices.sorted(), prices)

        val berry = vm.uiState.value.filteredProducts.first()
        vm.addToCart(berry, 2)
        assertEquals(initialCount + 2, vm.uiState.value.totalCartCount)
    }
}
