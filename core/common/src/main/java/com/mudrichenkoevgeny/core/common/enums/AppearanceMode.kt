package com.mudrichenkoevgeny.core.common.enums

enum class AppearanceMode {
    LIGHT, DARK, SYSTEM;

    companion object {
        fun default(): AppearanceMode = SYSTEM
    }
}