package app.junsu.youtube

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import app.junsu.youtube.databinding.FragmentPlayerBinding
import kotlin.math.abs

class PlayerFragment : Fragment(R.layout.fragment_player) {
    private lateinit var binding: FragmentPlayerBinding
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlayerBinding.bind(view)

        binding.motionLayoutPlayer.setTransitionListener(
            object : MotionLayout.TransitionListener {
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
                    binding.let {
                        (activity as MainActivity).also { mainActivity ->
                            mainActivity.findViewById<MotionLayout>(
                                R.id.motion_layout_main,
                            ).progress = abs(progress)
                        }
                    }
                }

                override fun onTransitionCompleted(
                    motionLayout: MotionLayout?,
                    currentId: Int,
                ) {
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
}
