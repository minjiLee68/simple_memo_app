package com.sophia.memo_project.fragment

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sophia.memo_project.R
import pub.devrel.easypermissions.EasyPermissions
import com.sophia.memo_project.databinding.FragNewMemoBinding
import com.sophia.memo_project.entity.MemoEntity
import com.sophia.memo_project.viewmodel.MemoViewModel
import com.sophia.memo_project.viewmodel.ViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.lang.Exception

class EditorFragment: Fragment() {

    private lateinit var note: MemoEntity
    private lateinit var callback: OnBackPressedCallback

    private val viewmodel by activityViewModels<MemoViewModel> { ViewModelFactory(requireActivity().application) }

    private var _binding: FragNewMemoBinding? = null
    val binding: FragNewMemoBinding
    get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragNewMemoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.etNoteTitle.text.clear()
                binding.etNoteSubTitle.text.clear()
                binding.etNoteDesc.text.clear()
                viewmodel.replaceFragment(MemoViewModel.FragmentType.LIST)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        note = MemoEntity()

        setObserve()
        setListener()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListener() {
        binding.imgDone.setOnClickListener {
            viewmodel.saveMemo(
                binding.etNoteTitle.text.toString(),
                binding.etNoteSubTitle.text.toString(),
                binding.etNoteDesc.text.toString(),
            )
            binding.etNoteTitle.text.clear()
            binding.etNoteSubTitle.text.clear()
            binding.etNoteDesc.text.clear()
            viewmodel.replaceFragment(MemoViewModel.FragmentType.LIST)
        }
        binding.imgBack.setOnClickListener {
            viewmodel.replaceFragment(MemoViewModel.FragmentType.LIST)
        }
    }

    private fun setObserve() {
        viewmodel.editLiveData.observe(
            viewLifecycleOwner, {
                binding.run {
                    etNoteTitle.setText(it?.title)
                    etNoteSubTitle.setText(it?.subtitle)
                    etNoteDesc.setText(it?.content)
                }
            }
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

//    override fun onDestroy() {
//        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
//        super.onDestroy()
//    }
}