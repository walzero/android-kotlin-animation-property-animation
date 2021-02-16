/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.*
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.databinding.DataBindingUtil
import com.google.samples.propertyanimation.databinding.ActivityMainBinding
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.rotateButton.setOnClickListener {
            rotater()
        }

        binding.translateButton.setOnClickListener {
            translater()
        }

        binding.scaleButton.setOnClickListener {
            scaler()
        }

        binding.fadeButton.setOnClickListener {
            fader()
        }

        binding.colorizeButton.setOnClickListener {
            colorizer()
        }

        binding.showerButton.setOnClickListener {
            shower()
        }
    }

    private fun rotater() {
        ObjectAnimator
            .ofFloat(binding.star, View.ROTATION, -360f, 0f)
            .apply {
                duration = 1000
                disableButtonDuringAnimation(binding.rotateButton)
            }.start()
    }

    private fun translater() {
        ObjectAnimator
            .ofFloat(binding.star, View.TRANSLATION_X, 200f)
            .apply {
                repeatCount = 1
                repeatMode = ObjectAnimator.REVERSE
                disableButtonDuringAnimation(binding.translateButton)
            }.start()
    }

    private fun scaler() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        ObjectAnimator
            .ofPropertyValuesHolder(binding.star, scaleX, scaleY)
            .apply {
                repeatCount = 1
                repeatMode = ObjectAnimator.REVERSE
                disableButtonDuringAnimation(binding.scaleButton)
            }.start()
    }

    private fun fader() {
        ObjectAnimator
            .ofFloat(binding.star, View.ALPHA, 0f)
            .apply {
                repeatCount = 1
                repeatMode = ObjectAnimator.REVERSE
                duration = 1000
                disableButtonDuringAnimation(binding.fadeButton)
            }.start()
    }

    private fun colorizer() {
        ObjectAnimator
//           .ofInt(binding.star.parent, "backgroundColor", Color.BLACK, Color.RED)
//           API 21, use type evaluator for earlier version
            .ofArgb(binding.star.parent, "backgroundColor", Color.BLACK, Color.RED)
            .apply {
                repeatCount = 1
                repeatMode = ObjectAnimator.REVERSE
                duration = 1000
                disableButtonDuringAnimation(binding.colorizeButton)
            }.start()
    }

    private fun shower() {
        val container = binding.star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var starW: Float = binding.star.width.toFloat()
        var starH: Float = binding.star.height.toFloat()

        val newStar = AppCompatImageView(this)
        newStar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        container.addView(newStar)
        newStar.scaleX = Random.nextFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX
        starH *= newStar.scaleY

        newStar.translationX = Random.nextFloat() * containerW - starW / 2

        val mover = ObjectAnimator
            .ofFloat(newStar, View.TRANSLATION_Y, -starH, containerH + starH)
            .apply {
                interpolator = AccelerateInterpolator(1f)
            }
        
        val rotator = ObjectAnimator
            .ofFloat(newStar, View.ROTATION, (Random.nextInt() * 1080).toFloat())
            .apply {
                interpolator = LinearInterpolator()
            }

        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        set.duration = (Random.nextInt() * 1500 + 500).toLong()
        set.doOnEnd { container.removeView(newStar) }
    }

    private fun ObjectAnimator.disableButtonDuringAnimation(view: View) {
        doOnStart { view.isEnabled = false }
        doOnEnd { view.isEnabled = true }
    }
}
