package com.sophia.memo_project.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sophia.memo_project.adapter.MemoAdapter
import com.sophia.memo_project.databinding.FragMemoListBinding
import com.sophia.memo_project.viewmodel.MemoViewModel
import com.sophia.memo_project.viewmodel.ViewModelFactory

class ListFragment: Fragment(), MemoAdapter.OnItemClickListener {

    private val viewmodel by activityViewModels<MemoViewModel> { ViewModelFactory(requireActivity().application) }

    private var _binding: FragMemoListBinding? = null
    val binding: FragMemoListBinding
    get() = _binding!!

    private lateinit var adapter: MemoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragMemoListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerview()
        setObserve()
        setListener()
        searchText()
        searchObserver()
    }

    private fun searchText() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
               viewmodel.searchDatabase(s.toString())
            }

        })
    }

    private fun searchObserver() {
        viewmodel.allProducts.observe(
            viewLifecycleOwner, {
                products ->
                (binding.recyclerView.adapter as MemoAdapter).submitList(products)
            }
        )
    }

    private fun initRecyclerview() {
        adapter = MemoAdapter(this)
        binding.recyclerView.let {
            it.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            it.adapter = adapter
        }
    }

    private fun setObserve() {
        viewmodel.listLiveData.observe(
            viewLifecycleOwner, {
                (binding.recyclerView.adapter as MemoAdapter).submitList(it)
            }
        )
    }

    private fun setListener() {
        binding.fadBtnCreateNote.setOnClickListener {
            viewmodel.replaceFragment(MemoViewModel.FragmentType.EDITER)
        }
    }

    override fun setOnClickListener(position: Int) {
        viewmodel.setEditMemo(position)
        viewmodel.replaceFragment(MemoViewModel.FragmentType.EDITER)
    }

    override fun setOnLongClickListener(position: Int) {
        viewmodel.deleteMemo(position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}