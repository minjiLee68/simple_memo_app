package com.sophia.memo_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.sophia.memo_project.databinding.ActivityMainBinding
import com.sophia.memo_project.fragment.EditorFragment
import com.sophia.memo_project.fragment.ListFragment
import com.sophia.memo_project.viewmodel.MemoViewModel
import com.sophia.memo_project.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val listFragment = ListFragment()
    private val editFragment = EditorFragment()

    private val viewmodel by viewModels<MemoViewModel> { ViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragObserve()
    }

    private fun setFragObserve() {
        viewmodel.fragLiveData.observe(
            this, {
                val fragment =
                    when (it) {
                        MemoViewModel.FragmentType.LIST -> listFragment
                        MemoViewModel.FragmentType.EDITER -> editFragment
                    }
                replaceFragment(fragment)
            }
        )
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.frameLayout.id, fragment).commit()
    }
}