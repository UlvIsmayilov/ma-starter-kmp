package az.pashabank.starter.domain.exceptions

fun interface ErrorMapper {
    fun mapError(e: Throwable): Throwable
}