package com.evaluation.mangascope.core

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val MANGAVERSE_API_BASE_URL = "https://mangaverse-api.p.rapidapi.com"

    object Preferences {
        val SIGNED_USER_EMAIL = stringPreferencesKey("signed_user_email")
    }
}
