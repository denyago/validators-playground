package name.denyago.validationplayground.common

import jakarta.validation.Valid
import name.denyago.validationplayground.jakarta.OrderRequestJakarta
import name.denyago.validationplayground.jakarta.toDomain
import name.denyago.validationplayground.tribune.OrderRequestTribune
import name.denyago.validationplayground.tribune.errorHandlingMapper
import name.denyago.validationplayground.tribune.toDomain
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OrdersController(
    private val orderRepository: OrderRepository,
) {
    @PostMapping("/orders-jakarta")
    fun createOrder(
        @Valid @RequestBody orderRequest: OrderRequestJakarta,
    ): ResponseEntity<Order> {
        return ResponseEntity.ok(orderRepository.save(orderRequest.toDomain()))
    }

    @PostMapping("/orders-tribune")
    fun createOrderTribune(
        @RequestBody orderRequest: OrderRequestTribune,
    ): ResponseEntity<Any> =
        orderRequest.toDomain().fold(
            { errorHandlingMapper(it) },
            { ResponseEntity.ok(orderRepository.save(it)) },
        )

    @GetMapping("/orders")
    fun getOrders(): ResponseEntity<List<Order>> {
        return ResponseEntity.ok(orderRepository.findAll())
    }
}
