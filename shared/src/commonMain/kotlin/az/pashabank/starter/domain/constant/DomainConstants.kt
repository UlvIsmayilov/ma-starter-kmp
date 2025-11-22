package az.pashabank.starter.domain.constant

enum class AppLanguage {
    AZ, EN;

    companion object {
        fun of(string: String): AppLanguage = values().firstOrNull { it.name == string } ?: EN
    }
}

fun String.toAppLanguage() = AppLanguage.of(uppercase())