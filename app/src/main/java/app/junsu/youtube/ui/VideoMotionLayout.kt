package app.junsu.youtube.ui

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import app.junsu.youtube.R

class VideoMotionLayout(
    context: Context,
    attributeSet: AttributeSet?,
) : MotionLayout(
    context, attributeSet,
) {
    private var motionTouchStarted: Boolean = false
    private val hitRect: Rect = Rect()
    private val motionLayoutPlayer: View by lazy {
        findViewById(R.id.player_container_player)
    }
    private val gestureListener: GestureDetector.SimpleOnGestureListener by lazy {
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float,
            ): Boolean {
                motionLayoutPlayer.getHitRect(hitRect)
                return hitRect.contains(e1.x.toInt(), e1.y.toInt())
            }
        }
    }
    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(context, gestureListener)
    }

    init {
        setTransitionListener(
            object : TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                ) {
                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float,
                ) {
                }

                override fun onTransitionCompleted(
                    motionLayout: MotionLayout?,
                    currentId: Int,
                ) {
                    motionTouchStarted = false
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float,
                ) {
                }
            },
        )
    }

    override fun onTouchEvent(
        event: MotionEvent,
    ): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                motionTouchStarted = false
                return super.onTouchEvent(event)
            }
        }
        if (motionTouchStarted.not()) {
            motionLayoutPlayer.getHitRect(hitRect)
            motionTouchStarted = hitRect.contains(event.x.toInt(), event.y.toInt())
        }

        return super.onTouchEvent(event) && motionTouchStarted
    }

    override fun onInterceptTouchEvent(
        event: MotionEvent,
    ): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
}
