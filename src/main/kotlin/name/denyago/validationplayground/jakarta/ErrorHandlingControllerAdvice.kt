package name.denyago.validationplayground.jakarta

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorHandlingControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun onConstraintValidationException(exception: MethodArgumentNotValidException): ResponseEntity<Map<String, List<String>>> {
        val errors =
            exception.bindingResult.fieldErrors
                .groupBy { it.field }
                .map { (field, errors) ->
                    field to errors.map { it.defaultMessage!! }
                }.toMap()
        return ResponseEntity.badRequest().body(errors)
    }
}
