package com.androiddevs.mvvmnewsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.BeritaApplication
import com.androiddevs.mvvmnewsapp.models.Artikel
import com.androiddevs.mvvmnewsapp.models.ResponseBerita
import com.androiddevs.mvvmnewsapp.repository.BeritaRepository
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class BeritaViewModel(app: Application, val beritaRepository: BeritaRepository) : AndroidViewModel(app) {

    val beritaBaru: MutableLiveData<Resource<ResponseBerita>> = MutableLiveData()
    var beritaBaruPage = 1
    var beritaBaruResponse: ResponseBerita? = null

    val cariBerita: MutableLiveData<Resource<ResponseBerita>> = MutableLiveData()
    var cariBeritaPage = 1
    var cariBeritaResponse: ResponseBerita? = null

    init {
        getBeritaBaru("id")
    }

    fun getBeritaBaru(countryCode: String) = viewModelScope.launch {
        safeBreakingBeritaCall(countryCode)
    }

    fun cariBerita(searchQuery: String) = viewModelScope.launch {
        safeCariBeritaCall(searchQuery)
    }

    private fun handleBeritaBaruResponse(response: Response<ResponseBerita>) : Resource<ResponseBerita>{
        if (response.isSuccessful){
            response.body()?.let { hasilResponse ->
                beritaBaruPage++
                if (beritaBaruResponse == null){
                    beritaBaruResponse = hasilResponse
                } else {
                    val artikelLama = beritaBaruResponse?.articles
                    val artikelBaru = hasilResponse.articles
                    artikelLama?.addAll(artikelBaru)
                }
                return Resource.Success(beritaBaruResponse ?: hasilResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleCariBeritaResponse(response: Response<ResponseBerita>) : Resource<ResponseBerita>{
        if (response.isSuccessful){
            response.body()?.let { hasilResponse ->
                cariBeritaPage++
                if (cariBeritaResponse == null){
                    cariBeritaResponse = hasilResponse
                } else {
                    val artikelLama = cariBeritaResponse?.articles
                    val artikelBaru = hasilResponse.articles
                    artikelLama?.addAll(artikelBaru)
                }
                return Resource.Success(cariBeritaResponse ?: hasilResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun simpanArtikel(artikel: Artikel) = viewModelScope.launch {
        beritaRepository.simpanBerita(artikel)
    }

    fun getSimpanBeritaDb() = beritaRepository.getSimpanBeritaDb()

    fun hapusArtikel(artikel: Artikel) = viewModelScope.launch {
        beritaRepository.hapusArtikel(artikel)
    }

    private suspend fun safeCariBeritaCall(searchQuery: String) {
        cariBerita.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = beritaRepository.cariBerita(searchQuery, cariBeritaPage)
                cariBerita.postValue(handleCariBeritaResponse(response))
            } else {
                cariBerita.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t: Throwable){
            when(t) {
                is IOException -> cariBerita.postValue(Resource.Error("Network Failure"))
                else -> cariBerita.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    private suspend fun safeBreakingBeritaCall(countryCode: String) {
        beritaBaru.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = beritaRepository.getBeritaTerbaru(countryCode, beritaBaruPage)
                beritaBaru.postValue(handleBeritaBaruResponse(response))
            } else {
                beritaBaru.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t: Throwable){
            when(t) {
                is IOException -> beritaBaru.postValue(Resource.Error("Network Failure"))
                else -> beritaBaru.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    //fungsi cek koneksi internet
    private fun hasInternetConnection(): Boolean {

        val connectivityManager = getApplication<BeritaApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false

    }
}