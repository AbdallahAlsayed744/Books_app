package com.hyperdesign.presentation

import com.hyperdesign.presentation.mvi.Effect

sealed interface SettingsEffect : Effect {
    data class ApplyLanguage(val tag: String) : SettingsEffect
}
