package com.evaluation.mangascope.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.evaluation.mangascope.core.Constants
import com.evaluation.mangascope.data.local.dao.UserDao
import com.evaluation.mangascope.data.local.entity.UserEntity
import com.evaluation.mangascope.domain.model.preferences.SignInPreferences
import com.evaluation.mangascope.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sign_in_preferences")

class AuthenticationRepositoryImpl(
    private val userDao: UserDao,
    private val dataStore: DataStore<Preferences>,
) : AuthenticationRepository {
    override val signInPreferencesFlow: Flow<SignInPreferences>
        get() =
            dataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }.map { preferences ->
                    val email = preferences[Constants.Preferences.SIGNED_USER_EMAIL]
                    SignInPreferences(email)
                }

    override suspend fun signIn(
        email: String,
        password: String,
    ) {
        userDao.insertUser(UserEntity(email = email, password = password))
        dataStore.edit { preferences ->
            preferences[Constants.Preferences.SIGNED_USER_EMAIL] = email
        }
    }

    override suspend fun getUserByEmail(email: String): UserEntity? = userDao.getUserByEmail(email)
}
