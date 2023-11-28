package name.denyago.validationplayground.tribune

import arrow.core.NonEmptyList
import org.springframework.http.ResponseEntity

fun errorHandlingMapper(errors: NonEmptyList<Pair<String, String>>): ResponseEntity<Any> {
    val errorsMap = errors.groupBy { it.first }.mapValues { (_, value) -> value.map { it.second } }
    return ResponseEntity.badRequest().body(errorsMap)
}
