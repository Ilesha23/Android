package com.iyakovlev.task2.presentation.activity.auth.password_validator

class PasswordValidator(private val password: String) {

    fun validate(): String {
        val errors = mutableListOf<String>()

        if (password.length < 8 || password.length > 16) {
            errors.add(LENGTH)
        }

        if (!password.matches(Regex(PASSWORD_REGEX))) {
            errors.add(SYMBOLS_INVALID)
        }

        if (!password.any { it.isDigit() }) {
            errors.add(NUMBERS)
        }

        if (!password.any { it.isLowerCase() }) {
            errors.add(LETTERS_LOWERCASE)
        }

        if (!password.any { it.isUpperCase() }) {
            errors.add(LETTERS_UPPERCASE)
        }

        if (!password.any { it in PASSWORD_SYMBOLS }) {
            errors.add(SYMBOLS)
        }

        return if (errors.isEmpty()) {
            ""
        } else {
            REQUIRED + errors.joinToString(", ")
        }
    }

    companion object {
        const val REQUIRED = "Required: "
        const val LENGTH = "8-16 length"
        const val NUMBERS = "0-9"
        const val LETTERS_LOWERCASE = "a-z"
        const val LETTERS_UPPERCASE = "A-Z"
        const val SYMBOLS = "symbols"
        const val SYMBOLS_INVALID = "no invalid symbols"

        const val PASSWORD_REGEX = "[A-Za-z0-9!@#\$%^&*()-_+=<>?]+"
        const val PASSWORD_SYMBOLS = "!@#\$%^&*()-_+=<>?"
    }
}
