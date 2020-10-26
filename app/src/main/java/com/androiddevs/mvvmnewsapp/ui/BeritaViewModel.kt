package com.androiddevs.mvvmnewsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.models.ResponseBerita
import com.androiddevs.mvvmnewsapp.repository.BeritaRepository
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class BeritaViewModel(val beritaRepository: BeritaRepository) :ViewModel(){

    val beritaBaru: MutableLiveData<Resource<ResponseBerita>> = MutableLiveData()
    var beritaBarupage = 1

    init {
        getBeritaBaru("id")
    }

    fun getBeritaBaru(countryCode: String) = viewModelScope.launch {
        beritaBaru.postValue(Resource.Loading())
        val response = beritaRepository.getBeritaTerbaru(countryCode, beritaBarupage)
        beritaBaru.postValue(handleBeritaBaruResponse(response))
    }

    private fun handleBeritaBaruResponse(response: Response<ResponseBerita>) : Resource<ResponseBerita>{
        if (response.isSuccessful){
            response.body()?.let { hasilResponse ->
                return Resource.Success(hasilResponse)
            }
        }

        return Resource.Error(response.message())
    }


}