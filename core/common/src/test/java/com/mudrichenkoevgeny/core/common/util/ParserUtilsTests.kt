package com.mudrichenkoevgeny.core.common.util

import junit.framework.TestCase.assertEquals
import org.junit.Test

class ParserUtilsTests {

    @Test
    fun `GIVEN correct message with 1 argument WHEN getArgsFromMessage invoked THEN 1 argument returned`() {
        // GIVEN
        val argument = "name@mail.com"
        val message = "User $argument is blocked"
        val argumentMask = "[arg]"
        val messageMask = "User $argumentMask is blocked"
        // WHEN
        val parsedArguments = getArgsFromMessage(
            message = message,
            argumentMask = argumentMask,
            messageMask = messageMask
        )
        // THEN
        assertEquals(argument, parsedArguments.firstOrNull())
    }

    @Test
    fun `GIVEN correct message with 2 arguments in middle of message WHEN getArgsFromMessage invoked THEN 2 arguments returned`() {
        // GIVEN
        val arguments = listOf("name@mail.com", "support@mail.com")
        val message = "User ${arguments[0]} is blocked. Contact us: ${arguments[1]}."
        val argumentMask = "[arg]"
        val messageMask = "User $argumentMask is blocked. Contact us: $argumentMask."
        // WHEN
        val parsedArguments = getArgsFromMessage(
            message = message,
            argumentMask = argumentMask,
            messageMask = messageMask
        )
        // THEN
        assertEquals(arguments, parsedArguments)
    }

    @Test
    fun `GIVEN correct message with 2 arguments at start and in the end of message WHEN getArgsFromMessage invoked THEN 2 arguments returned`() {
        // GIVEN
        val arguments = listOf("name@mail.com", "support@mail.com")
        val message = "${arguments[0]} user is blocked. Contact us: ${arguments[1]}"
        val argumentMask = "[arg]"
        val messageMask = "$argumentMask user is blocked. Contact us: $argumentMask"
        // WHEN
        val parsedArguments = getArgsFromMessage(
            message = message,
            argumentMask = argumentMask,
            messageMask = messageMask
        )
        // THEN
        assertEquals(arguments, parsedArguments)
    }

    @Test
    fun `GIVEN correct message with 3 arguments WHEN getArgsFromMessage invoked THEN 3 arguments returned`() {
        // GIVEN
        val arguments = listOf("name@mail.com", "John Doe", "support@mail.com")
        val message = "User ${arguments[0]} (${arguments[1]}) is blocked. Contact us: ${arguments[2]}."
        val argumentMask = "[arg]"
        val messageMask = "User $argumentMask ($argumentMask) is blocked. Contact us: $argumentMask."
        // WHEN
        val parsedArguments = getArgsFromMessage(
            message = message,
            argumentMask = argumentMask,
            messageMask = messageMask
        )
        // THEN
        assertEquals(arguments, parsedArguments)
    }

    @Test
    fun `GIVEN incorrect message with 1 argument WHEN getArgsFromMessage invoked THEN 1 argument returned`() {
        // GIVEN
        val arguments = listOf("name@mail.com", "support@mail.com")
        val message = "User blocked: ${arguments[0]}. Contact us by email"
        val argumentMask = "[arg]"
        val messageMask = "User blocked: $argumentMask. $argumentMask - support email"
        // WHEN
        val parsedArguments = getArgsFromMessage(
            message = message,
            argumentMask = argumentMask,
            messageMask = messageMask
        )
        // THEN
        assertEquals(listOf(arguments.firstOrNull()), parsedArguments)
    }

    @Test
    fun `GIVEN incorrect message without arguments WHEN getArgsFromMessage invoked THEN 0 argument returned`() {
        // GIVEN
        val message = "User blocked. Contact us by email"
        val argumentMask = "[arg]"
        val messageMask = "User blocked: $argumentMask. $argumentMask - support email"
        // WHEN
        val parsedArguments = getArgsFromMessage(
            message = message,
            argumentMask = argumentMask,
            messageMask = messageMask
        )
        // THEN
        assertEquals(emptyList<String>(), parsedArguments)
    }
}