package com.example.presentation.ui.animations

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation

class RefreshButtonAnimation : RotateAnimation(
    0.0f, 360.0f,
    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
    0.5f
) {
    init {
        repeatCount = Animation.INFINITE
        interpolator = LinearInterpolator()
        duration = 500
    }
}