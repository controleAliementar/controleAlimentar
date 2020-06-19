package com.example.controlealimentar.util

import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.NonNull
import com.example.controlealimentar.R
import kotlinx.android.synthetic.main.popup_layout.view.*


class PopUps {

    lateinit var dialog: Dialog

    fun show(context: Context): Dialog {
        return show(context, null)
    }

    fun show(context: Context?, title:CharSequence?): Dialog {

        val inflator = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflator.inflate(R.layout.popup_layout, null)
        if (title != null) {
            view.cp_title.text = title
        }
        view.popup_view.setBackgroundColor(Color.parseColor("#60000000")) //Background Color
        view.cp_cardview.setCardBackgroundColor(Color.WHITE) //Box Color white
        view.cp_title.setTextColor(Color.BLACK) //Text Color

        view.ib_close.setOnClickListener {
            dialog.dismiss()
        }

        dialog = Dialog(context, R.style.CustomProgressBarTheme)
        dialog.setContentView(view)
        dialog.show()

        return dialog
    }

    fun setColorFilter(@NonNull drawable: Drawable, color:Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}