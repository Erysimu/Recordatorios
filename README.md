# Recordatorios – Programación Mobile (Actividades 1 y 2)

App Android de recordatorios con Kotlin, Jetpack Compose, Material 3, ViewModel + StateFlow y Navigation Compose.

## Estructura
- `model/` – data classes, sealed class `Categoria`, enums, validaciones y filtros (List/Set/Map).
- `data/` – interfaz `RecordatorioRepository`, implementación en memoria y modo de simulación.
- `ui/components/` – componentes reutilizables (tarjeta, estados, campos del formulario).
- `ui/listado`, `ui/formulario`, `ui/detalle` – pantalla + ViewModel + UiState de cada destino.
- `ui/navigation/` – rutas, `NavHost` y fábrica de ViewModels.

## Simular estados
En el listado, menú ⋮ (arriba a la derecha): Datos normales, Lista vacía, Error al cargar, Error al guardar.
