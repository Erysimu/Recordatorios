package cl.mlopez.recordatorios

import cl.mlopez.recordatorios.model.Categoria
import cl.mlopez.recordatorios.model.Prioridad
import cl.mlopez.recordatorios.model.ValidadorRecordatorio
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ValidadorRecordatorioTest {
    @Test fun tituloConEspaciosNoCuentaComoValido() =
        assertNotNull(ValidadorRecordatorio.validarTitulo("   abc   "))

    @Test fun tituloDeCuatroCaracteresEsValido() =
        assertNull(ValidadorRecordatorio.validarTitulo(" abcd "))

    @Test fun descripcionDe201CaracteresEsInvalida() =
        assertNotNull(ValidadorRecordatorio.validarDescripcion("a".repeat(201)))

    @Test fun categoriaYPrioridadObligatorias() {
        assertNotNull(ValidadorRecordatorio.validarCategoria(null))
        assertNotNull(ValidadorRecordatorio.validarPrioridad(null))
        assertNull(ValidadorRecordatorio.validarCategoria(Categoria.Salud))
        assertNull(ValidadorRecordatorio.validarPrioridad(Prioridad.ALTA))
    }
}
