package cl.mlopez.recordatorios.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cl.mlopez.recordatorios.ui.detalle.DetalleRoute
import cl.mlopez.recordatorios.ui.detalle.DetalleViewModel
import cl.mlopez.recordatorios.ui.formulario.FormularioRoute
import cl.mlopez.recordatorios.ui.formulario.FormularioViewModel
import cl.mlopez.recordatorios.ui.listado.ListadoRoute
import cl.mlopez.recordatorios.ui.listado.ListadoViewModel

/**
 * Grafo de navegación. El NavController solo se usa aquí: las pantallas reciben lambdas
 * (onCrearRecordatorio, onRecordatorioClick, onGuardado, onCancelar, onVolver).
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.LISTADO,
        modifier = modifier,
    ) {
        composable(Rutas.LISTADO) { entrada ->
            val viewModel: ListadoViewModel = viewModel(factory = AppViewModelProvider.Factory)

            // Si el formulario guardó un recordatorio, se recarga la lista al volver.
            val recargar by entrada.savedStateHandle
                .getStateFlow(Rutas.RESULTADO_CREADO, false)
                .collectAsStateWithLifecycle()
            LaunchedEffect(recargar) {
                if (recargar) {
                    viewModel.cargarRecordatorios()
                    entrada.savedStateHandle[Rutas.RESULTADO_CREADO] = false
                }
            }

            ListadoRoute(
                viewModel = viewModel,
                onCrearRecordatorio = {
                    navController.navigate(Rutas.CREAR) { launchSingleTop = true }
                },
                onRecordatorioClick = { id ->
                    navController.navigate(Rutas.detalle(id)) { launchSingleTop = true }
                },
            )
        }

        composable(Rutas.CREAR) {
            val viewModel: FormularioViewModel = viewModel(factory = AppViewModelProvider.Factory)
            FormularioRoute(
                viewModel = viewModel,
                onGuardado = {
                    navController.previousBackStackEntry?.savedStateHandle?.set(Rutas.RESULTADO_CREADO, true)
                    navController.popBackStack()
                },
                onCancelar = { navController.popBackStack() },
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument(Rutas.ARG_ID) { type = NavType.IntType }),
        ) {
            val viewModel: DetalleViewModel = viewModel(factory = AppViewModelProvider.Factory)
            DetalleRoute(
                viewModel = viewModel,
                onVolver = { navController.popBackStack() },
            )
        }
    }
}
