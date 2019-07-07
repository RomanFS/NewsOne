package com.example.newsone

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_email.*

private val TAG = "BaseActivity"

abstract class BaseActivity(private val itemNum: Int) : AppCompatActivity() {

    fun setUpBtmNav() {
        btm_nav_view.setItemIconSizeRes(R.dimen.iconSize)
        btm_nav_view.setOnNavigationItemSelectedListener {
            val nextActivity =
                when (it.itemId) {
                    R.id.emailed_tab -> EmailActivity::class.java
                    R.id.viewed_tab -> ViewedActivity::class.java
                    R.id.shared_tab -> SharedActivity::class.java
                    R.id.favourite_tab -> FavouriteActivity::class.java
                    else -> {
                        Log.e(TAG, "unknown btm item clicked")
                        null
                    }
                }
            if (nextActivity != null) {
                val intent = Intent(this, nextActivity)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                overridePendingTransition(0,0)
                startActivity(intent)
                true
            } else {
                false
            }
        }
        btm_nav_view.menu.getItem(itemNum).isChecked = true
    }
    fun getDB(): MyDBHandler {
        // create DataBase
        return MyDBHandler(this, null, null, 1)
    }
}