package com.mudrichenkoevgeny.core.common.util

/**
 * @param message Message from Rest Api. Example: User name@mail.com is blocked
 * @param argumentMask Mask for arguments
 * @param messageMask Mask for Rest Api message. Example: "User argumentMask is blocked"
 * @param args Intermediate result of the recursive function
 * @return List of arguments from message. Example: listOf(name@mail.com)
 */
fun getArgsFromMessage(
    message: String,
    argumentMask: String,
    messageMask: String,
    args: MutableList<String> = mutableListOf()
): List<String> {
    var maskStartIndex = messageMask.indexOf(argumentMask)
    if (maskStartIndex == -1) {
        return args
    }

    var newMessageMask = messageMask.substring(maskStartIndex + argumentMask.length)

    var nextMaskStartIndex = newMessageMask.indexOf(argumentMask)
    val isLastArgument = nextMaskStartIndex == -1
    val partOfTextAfterArgument = if (isLastArgument) {
        newMessageMask
    } else {
        val newMessageMaskEndIndex = nextMaskStartIndex
        if (newMessageMask.lastIndex < newMessageMaskEndIndex) {
            null
        } else {
            newMessageMask.substring(0, newMessageMaskEndIndex)
        }
    } ?: return args

    val newMessageMaskEndIndex = if (isLastArgument) {
        newMessageMask.lastIndex
    } else {
        nextMaskStartIndex
    }

    if (newMessageMask.lastIndex < newMessageMaskEndIndex) {
        return args
    }

    val argumentEndIndex = when {
        partOfTextAfterArgument.isEmpty() -> message.lastIndex + 1
        isLastArgument -> message.lastIndexOf(partOfTextAfterArgument)
        else -> message.indexOf(partOfTextAfterArgument)
    }

    if (maskStartIndex > argumentEndIndex
        || maskStartIndex > message.lastIndex
        || argumentEndIndex > message.lastIndex + 1
    ) {
        return args
    }
    args.add(message.substring(maskStartIndex, argumentEndIndex))

    return if (isLastArgument) {
        args
    } else {
        getArgsFromMessage(
            message = message.substring(startIndex = argumentEndIndex),
            argumentMask = argumentMask,
            messageMask = newMessageMask,
            args = args
        )
    }
}