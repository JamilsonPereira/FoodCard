package br.com.jptech.coreapi

import java.util.*

class FoodCartCreateEvent(
    val foodCartId: UUID
)

data class ProductSelectedEvent(
    val foodCartId: UUID,
    val productId: UUID,
    val quantity: Int
)
data class ProductDeselectedEvent(
    val foodCartId: UUID,
    val productId: UUID,
    val quantity: Int
)
data class OrderConfirmedEvent(
    val foodCartId: UUID
)