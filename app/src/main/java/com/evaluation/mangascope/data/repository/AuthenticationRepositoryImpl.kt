package com.evaluation.mangascope.data.repository

import com.evaluation.mangascope.data.local.dao.UserDao
import com.evaluation.mangascope.data.local.entity.UserEntity
import com.evaluation.mangascope.domain.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val userDao: UserDao,
) : AuthenticationRepository {
    override suspend fun signIn(
        email: String,
        password: String,
    ) {
        userDao.insertUser(UserEntity(email = email, password = password))
    }

    override suspend fun isUserSignedIn(email: String): Boolean = userDao.getUserByEmail(email) != null
}
