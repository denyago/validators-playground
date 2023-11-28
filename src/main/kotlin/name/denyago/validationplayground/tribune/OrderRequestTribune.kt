package name.denyago.validationplayground.tribune

import arrow.core.NonEmptyList
import name.denyago.validationplayground.common.Order

data class OrderRequestTribune(
    val customerName: String?,
    val total: Double?,
    val dueDate: String?,
    val lineItems: List<LineItemRequestTribune>?,
)

data class LineItemRequestTribune(
    val name: String?,
    val price: Double?,
    val quantity: Int?,
)

fun OrderRequestTribune.toDomain(): arrow.core.Validated<NonEmptyList<Pair<String, String>>, Order> = orderParser.parse(this)
