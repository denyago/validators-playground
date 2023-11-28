package name.denyago.validationplayground.common

import org.springframework.stereotype.Repository

@Repository
class OrderRepository {
    private val orders = mutableListOf<Order>()

    fun save(order: Order): Order {
        orders.add(order)
        return order
    }

    fun findAll(): List<Order> = orders.toList()

    fun deleteAll() = orders.clear()
}
