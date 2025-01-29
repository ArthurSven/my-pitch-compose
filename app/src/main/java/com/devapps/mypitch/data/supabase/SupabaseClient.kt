package com.devapps.mypitch.data.supabase

import com.devapps.mypitch.Constants.API_KEY
import com.devapps.mypitch.Constants.BASE_URL
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    BASE_URL,
    API_KEY
) {
    install(Postgrest)
}