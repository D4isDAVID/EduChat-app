package io.github.d4isdavid.educhat.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry

private const val DURATION = 500
private val EASING = FastOutSlowInEasing

val slideInTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideInHorizontally(
        animationSpec = tween(durationMillis = DURATION, easing = EASING),
        initialOffsetX = { it },
    )
}

val slideOutTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutHorizontally(
        animationSpec = tween(durationMillis = DURATION, easing = EASING),
        targetOffsetX = { it },
    )
}

val slideUpTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideInVertically(
        animationSpec = tween(durationMillis = DURATION, easing = EASING),
        initialOffsetY = { it },
    )
}

val slideDownTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutVertically(
        animationSpec = tween(durationMillis = DURATION, easing = EASING),
        targetOffsetY = { it },
    )
}
