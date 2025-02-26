package com.mustafacanyucel.fireflyiiishortcuts.services.firefly

import com.mustafacanyucel.fireflyiiishortcuts.services.auth.Oauth2Manager
import javax.inject.Inject

class FireflyRepository @Inject constructor(
    private val authManager: Oauth2Manager
) {
}