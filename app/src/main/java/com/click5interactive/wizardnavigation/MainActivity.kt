package com.click5interactive.wizardnavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import com.google.android.material.appbar.AppBarLayout


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {

            val intent = Intent(this, MyWizardActivity::class.java)
            intent.putExtra("INITIAL_STEP", 4)

            startActivity(intent)

        }
    }
}
