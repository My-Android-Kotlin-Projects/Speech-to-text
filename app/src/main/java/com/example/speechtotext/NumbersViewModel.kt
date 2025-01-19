package com.example.speechtotext

import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechtotext.db.NumbersModel
import kotlinx.coroutines.launch

class NumbersViewModel: ViewModel() {
    private val numbersDao = MainApplication.numbersDB.numbersDao()
    val numbers: LiveData<List<NumbersModel>> get() = numbersDao.getNumber()

    fun insertNumber(number: Int) {
        viewModelScope.launch {
            numbersDao.insertNumber(NumbersModel(number = number))
        }
    }

    fun deleteNumber(id: Int) {
        viewModelScope.launch {
            numbersDao.deleteNumber(id)
        }
    }

    fun deleteAllNumbers() {
        viewModelScope.launch {
            numbersDao.deleteAllNumbers()
        }
    }
    fun getSum(): LiveData<Int> {
        val result = MediatorLiveData<Int>()
        result.addSource(numbersDao.getSum()){value ->
            result.value = value ?: 0
        }
        return result
    }
}