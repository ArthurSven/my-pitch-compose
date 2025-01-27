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

    // Provide the GoogleAuthClient
    factory { (context: Context) ->
        GoogleAuthClient(
            context = context,
            oneTapClient = get() // This will retrieve the SignInClient from the Koin container
        )
    }

    // Provide the AuthViewModel
    viewModel { (context: Context) ->
        AuthViewModel(
            googleAuthClient = get { parametersOf(context) } // Pass the context to create GoogleAuthClient
        )
    }
}