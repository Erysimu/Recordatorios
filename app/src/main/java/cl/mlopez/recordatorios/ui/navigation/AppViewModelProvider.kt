package cl.mlopez.recordatorios.ui.navigation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.mlopez.recordatorios.data.ServiceLocator
import cl.mlopez.recordatorios.ui.detalle.DetalleViewModel
import cl.mlopez.recordatorios.ui.formulario.FormularioViewModel
import cl.mlopez.recordatorios.ui.listado.ListadoViewModel

/** Fábrica que entrega a cada ViewModel la interfaz del repositorio (no la implementación). */
object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { ListadoViewModel(ServiceLocator.repositorio, ServiceLocator.simulacion) }
        initializer { FormularioViewModel(ServiceLocator.repositorio) }
        initializer { DetalleViewModel(createSavedStateHandle(), ServiceLocator.repositorio) }
    }
}
