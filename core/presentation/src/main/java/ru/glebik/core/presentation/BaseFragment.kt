package ru.glebik.core.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import javax.inject.Inject


abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        initDagger()
        super.onCreate(savedInstanceState)
    }

    abstract fun initDagger()

}