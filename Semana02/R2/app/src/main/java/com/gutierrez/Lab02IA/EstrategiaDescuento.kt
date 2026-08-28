package com.gutierrez.Lab02IA

import java.util.Locale

/**
 * ABSTRACCION - Contrato de la politica de descuentos.
 *
 * El carrito no sabe COMO se calcula un descuento, solo sabe que existe algo capaz
 * de responder "dado este monto, cuanto se rebaja". Agregar una promocion nueva
 * (por cupon, por fecha, por cliente frecuente) no obliga a tocar CarritoDeCompras:
 * basta con una implementacion mas de esta interfaz.
 */
interface EstrategiaDescuento {

    /** Monto a rebajar sobre el total recibido. Nunca negativo, nunca mayor al monto. */
    fun calcularDescuento(monto: Double): Double

    /** Texto que el carrito imprime en la boleta para justificar la rebaja. */
    val descripcion: String
        get() = "Politica de descuento generica"
}

/**
 * Politica por defecto: el carrito nunca rebaja nada.
 * Evita tener que preguntar "hay descuento?" con un if o un null en el carrito.
 */
class SinDescuento : EstrategiaDescuento {

    override fun calcularDescuento(monto: Double): Double = 0.0

    override val descripcion: String
        get() = "Sin descuento aplicado"
}

/**
 * Politica por volumen de compra:
 *  - mas de S/ 5000 -> 10%
 *  - mas de S/ 3000 -> 5%
 *  - en otro caso   -> 0%
 *
 * Los topes y porcentajes viven aqui, encapsulados: son la regla de ESTA promocion.
 */
class DescuentoPorMonto : EstrategiaDescuento {

    override fun calcularDescuento(monto: Double): Double = when {
        monto > TOPE_ALTO -> monto * PORCENTAJE_ALTO
        monto > TOPE_BAJO -> monto * PORCENTAJE_BAJO
        else -> 0.0
    }

    /** Porcentaje que corresponde a un monto, util para explicar la rebaja. */
    fun porcentajeAplicado(monto: Double): Double = when {
        monto > TOPE_ALTO -> PORCENTAJE_ALTO
        monto > TOPE_BAJO -> PORCENTAJE_BAJO
        else -> 0.0
    }

    override val descripcion: String
        get() = String.format(
            Locale.US,
            "Descuento por monto: %.0f%% sobre S/ %.2f, %.0f%% sobre S/ %.2f",
            PORCENTAJE_BAJO * 100, TOPE_BAJO, PORCENTAJE_ALTO * 100, TOPE_ALTO
        )

    private companion object {
        const val TOPE_BAJO = 3000.0
        const val TOPE_ALTO = 5000.0
        const val PORCENTAJE_BAJO = 0.05
        const val PORCENTAJE_ALTO = 0.10
    }
}
