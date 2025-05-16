package com.devapps.mypitch.di

import android.content.Context
import com.devapps.mypitch.Constants.API_KEY
import com.devapps.mypitch.Constants.BASE_URL
import com.devapps.mypitch.data.auth.GoogleAuthClient
import com.devapps.mypitch.data.model.UserData
import com.devapps.mypitch.data.repository.FirebasePitchRepository
import com.devapps.mypitch.data.repository.PitchRepository
import com.devapps.mypitch.data.repository.SupabaseRepository
import com.devapps.mypitch.ui.viewmodels.AuthViewModel
import com.devapps.mypitch.ui.viewmodels.PitchViewModel
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
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


//    //provide supabase client
//    single<SupabaseClient> {
//        createSupabaseClient(
//            BASE_URL,
//            API_KEY
//        ) {
//            install(Postgrest)
//        }
//    }
    //firebase
    single<FirebaseFirestore> {
        FirebaseFirestore.getInstance().apply {
            // Firestore-Einstellungen konfigurieren (optional)
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        }
    }

    single<PitchRepository> {
        // Umschalten durch Ändern dieser Zeile:
        FirebasePitchRepository(firestore = get())
        // Oder für Supabase: SupabaseRepository(supabaseClient = get())
    }
//    //provide supabaseRepository
//    single {
//        SupabaseRepository(
//            supabaseClient = get()
//        )
//    }

//    single<PitchRepository> { // Bind SupabaseRepository to PitchRepository
//        SupabaseRepository(
//            supabaseClient = get()
//        )
//    }



    // Provide AuthViewModel with a dynamic Context
    viewModel {
        AuthViewModel(
            googleAuthClient = get()// Pass the context to GoogleAuthClient
        )
    }

    viewModel { (userData: UserData) ->
        PitchViewModel(
            pitchRepository = get(),
            userData = userData
        )
    }

}