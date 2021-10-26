package com.sophia.memo_project.viewmodel

import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.sophia.memo_project.entity.MemoEntity
import com.sophia.memo_project.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MemoViewModel(private val repository: AppRepository) : ViewModel() {

    enum class FragmentType {
        LIST, EDITER
    }

    private var _fragLiveData = MutableLiveData(FragmentType.LIST)
    val fragLiveData: LiveData<FragmentType>
        get() = _fragLiveData

    fun replaceFragment(fragmentType: FragmentType) {
        _fragLiveData.value = fragmentType
    }

    val listLiveData = repository.getAllMemo()

    private var _editLiveData = MutableLiveData<MemoEntity?>()
    val editLiveData: LiveData<MemoEntity?>
        get() = _editLiveData

    private val searchStringLiveData = MutableLiveData("")
    val allProducts: LiveData<List<MemoEntity>> =
        Transformations.switchMap(searchStringLiveData) { string ->
            if (TextUtils.isEmpty(string)) {
                repository.getAllMemo()
            } else {
                repository.getSearchTitle(string)
            }
        }

    fun setEditMemo(position: Int) {
        val memo = listLiveData.value?.get(position) ?: throw Exception("")
        _editLiveData.value = memo
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertMemo(title: String, subtitle: String, content: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE
            val memoDate = current.format(formatter)

            val memo = MemoEntity(title, subtitle, content, memoDate)

            repository.inserMemo(memo)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateMemo(title: String, subtitle: String, content: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE
            val memoDate = current.format(formatter)

            val memo = editLiveData.value?.apply {
                this.title = title
                this.subtitle = subtitle
                this.content = content
                this.date = memoDate
            } ?: throw  Exception("")
            repository.updateMemo(memo)

            viewModelScope.launch(Dispatchers.Main) {
                _editLiveData.value = null
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveMemo(title: String, subtitle: String, content: String) {
        if (editLiveData.value == null) {
            insertMemo(title, subtitle, content)
        } else {
            updateMemo(title, subtitle, content)
        }
    }

    fun deleteMemo(position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMemo(listLiveData.value?.get(position) ?: throw Exception(""))
        }
    }

    fun searchDatabase(title: String) {
        searchStringLiveData.value = title
    }
}