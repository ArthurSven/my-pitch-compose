package com.devapps.mypitch.di

import android.content.Context
import com.devapps.mypitch.data.auth.GoogleAuthClient
import com.devapps.mypitch.ui.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.identity.SignInClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val appModule = module {

    single<SignInClient> {
        com.google.android.gms.auth.api.identity.Identity.getSignInClient(androidContext())
    }

    // Provide GoogleAuthClient with a dynamic Context
    single {
        GoogleAuthClient(
            context = androidContext(),
            oneTapClient = get() // Retrieve the SignInClient from the Koin container
        )
    }

    // Provide AuthViewModel with a dynamic Context
    viewModel {
        AuthViewModel(
            googleAuthClient = get()// Pass the context to GoogleAuthClient
        )
    }
}