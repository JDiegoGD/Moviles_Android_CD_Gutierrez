package com.gutierrez.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.gutierrez.campusgps.data.LugaresRepository
import com.gutierrez.campusgps.data.UbicacionRepository
import com.gutierrez.campusgps.viewmodel.MapaViewModel

class CampusViewModelFactory(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MapaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapaViewModel(ubicacionRepo, lugaresRepo) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
