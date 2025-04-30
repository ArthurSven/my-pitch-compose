package com.devapps.mypitch

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class ReviewHelper(private val activity: Activity) {
    private val manager = ReviewManagerFactory.create(activity)
    private var lastPromptTime = 0L
    private val cooldownDays = 30 // Google's limit

    suspend fun requestReview() {
        if (System.currentTimeMillis() - lastPromptTime > TimeUnit.DAYS.toMillis(cooldownDays.toLong())) {
            try {
                val reviewInfo = manager.requestReviewFlow().await()
                manager.launchReview(activity, reviewInfo)
                lastPromptTime = System.currentTimeMillis()
            } catch (_: Exception) {
                // Fallback to Play Store
                activity.startActivity(
                    Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=${activity.packageName}"))
                )
            }
        }
    }
}