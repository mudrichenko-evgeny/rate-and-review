package com.mudrichenkoevgeny.feature.user.util

import android.util.Patterns
import com.mudrichenkoevgeny.feature.user.result.UserInputError
import java.io.File

private const val MB_TO_BYTES = 1024 * 1024

const val PASSWORD_MINIMUM_LENGTH = 6
const val USER_NAME_MINIMUM_LENGTH = 4
const val AVATAR_FILE_SIZE_MAX_MB = 5.0

fun checkEmailForErrors(email: String): UserInputError? {
    if (!Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()) {
        return UserInputError.IncorrectEmail
    }
    return null
}

fun checkPasswordForErrors(password: String): UserInputError? {
    if (password.length < PASSWORD_MINIMUM_LENGTH) {
        return UserInputError.ShortPassword(
            minimumLength = PASSWORD_MINIMUM_LENGTH,
            currentLength = password.length
        )
    }
    return null
}

fun checkUserNameForErrors(name: String): UserInputError? {
    if (name.length < USER_NAME_MINIMUM_LENGTH) {
        return UserInputError.ShortUserName(
            minimumLength = USER_NAME_MINIMUM_LENGTH,
            currentLength = name.length
        )
    }
    return null
}

fun checkAvatarFileSize(file: File?): UserInputError? {
    if (file == null) {
        return null
    }
    val fileSizeMb = file.length().toDouble() / MB_TO_BYTES
    if (fileSizeMb > AVATAR_FILE_SIZE_MAX_MB) {
        return UserInputError.IncorrectImage.FileSizeTooLarge(
            maxSizeMb = AVATAR_FILE_SIZE_MAX_MB,
            currentSizeMb = fileSizeMb
        )
    }
    return null
}