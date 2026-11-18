package com.gutierrez.centrotareas.model.data

import com.gutierrez.centrotareas.model.PrimosProgreso
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TareasRepositoryLocal : TareasRepository {
    override suspend fun cargarPerfil(): String {
        delay(2000)
        return "Gutierrez Juan (ficticia)"
    }

    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3000)
        if (simularError) throw IOException("Sin conexión")
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }

    override fun contarPrimos(hasta: Int): Flow<PrimosProgreso> = flow {
        emit(PrimosProgreso(0f, 0))
        if (hasta < 2) {
            emit(PrimosProgreso(1f, 0))
            return@flow
        }

        val total = (hasta - 1).toFloat()
        var cantidad = 0
        var ultimoPorcentajeEnt = -1

        for (n in 2..hasta) {
            currentCoroutineContext().ensureActive()
            if (esPrimo(n)) {
                cantidad++
            }
            val revisados = (n - 1).toFloat()
            val progreso = (revisados / total).coerceIn(0f, 1f)
            val porcentajeEnt = (progreso * 100).toInt()

            if (porcentajeEnt != ultimoPorcentajeEnt || n == hasta) {
                ultimoPorcentajeEnt = porcentajeEnt
                emit(PrimosProgreso(progreso, cantidad))
            }
        }
        emit(PrimosProgreso(1f, cantidad))
    }.flowOn(Dispatchers.Default)

    override fun observarProgreso(): Flow<Float> = flow {
        emit(0f)
        for (i in 1..100) {
            delay(40)
            emit(i / 100f)
        }
    }.flowOn(Dispatchers.Default)

    private fun esPrimo(n: Int): Boolean {
        if (n < 2) return false
        var divisor = 2
        while (divisor.toLong() * divisor <= n) {
            if (n % divisor == 0) return false
            divisor++
        }
        return true
    }
}
