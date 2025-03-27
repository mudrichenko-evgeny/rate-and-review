package com.mudrichenkoevgeny.feature.user.result

sealed class UserInputError {
    object IncorrectEmail : UserInputError()
    class ShortPassword(val minimumLength: Int, val currentLength: Int) : UserInputError()
    class ShortUserName(val minimumLength: Int, val currentLength: Int) : UserInputError()
    sealed class IncorrectImage() : UserInputError() {
        object Unknown : IncorrectImage()
        class FileSizeTooLarge(val maxSizeMb: Double, val currentSizeMb: Double) : IncorrectImage()
    }
}

fun List<UserInputError>.getIncorrectEmailError(): UserInputError.IncorrectEmail? = getError()

fun List<UserInputError>.getShortPasswordError(): UserInputError.ShortPassword? = getError()

fun List<UserInputError>.getShortUserNameError(): UserInputError.ShortUserName? = getError()

fun List<UserInputError>.getIncorrectImageError(): UserInputError.IncorrectImage? = getError()

fun List<UserInputError>.addError(userInputError: UserInputError): List<UserInputError> {
    val filteredList = this.filterNot { it::class == userInputError::class }
    return filteredList + userInputError
}

private inline fun <reified T : UserInputError> List<UserInputError>.getError(): T? {
    return this.firstOrNull { it is T } as? T
}