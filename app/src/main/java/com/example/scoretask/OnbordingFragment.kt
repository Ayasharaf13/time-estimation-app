package com.example.scoretask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [OnbordingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnbordingFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_onbording, container, false)




    return ComposeView(requireContext()).apply {
        setContent {
            // تعريف الألوان والـ Brush داخل الـ setContent
            val PurpleGradientStart = Color(0xFF4A2997)
          //  val PurpleGradientEnd = Color(0xFF331E66) // لون آخر لجعل التدرج واضحاً

            val gradientBrush = Brush.linearGradient(
                colors = listOf(PurpleGradientStart, PurpleGradientStart),
                start = Offset(0f, 0f),
                end = Offset.Infinite // استخدام Offset.Infinite يعطي نفس نتيجة Float.POSITIVE_INFINITY
            )

            // استخدام Box كخلفية للشاشة بالكامل
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradientBrush)
            ) {
                // هنا تضعين باقي محتويات الشاشة (Timeline, Today's Overview, إلخ)

            }
        }
    }
}


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OnbordingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OnbordingFragment().apply {
                arguments = Bundle().apply {


                }
            }
    }
}