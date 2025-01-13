package ru.glebik.core.presentation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @IdRes
    protected val containerId = R.id.container

    @Inject
    lateinit var router: Router

    private val navigator by lazy { UsableAppNavigator(this, containerId) }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}