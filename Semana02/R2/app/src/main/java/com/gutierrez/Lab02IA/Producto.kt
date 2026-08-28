package com.gutierrez.Lab02IA

import java.util.Locale

/**
 * Abstraccion base del modelo de datos del carrito.
 *
 * Encapsulamiento:
 *  - nombre        : publico de solo lectura (no se reasigna durante la vida del producto).
 *  - precioBase    : publico de lectura, con setter validado (no admite negativos).
 *  - cantidad      : publico de lectura, con setter validado (no admite negativos).
 *  - codigo        : publico de lectura, private set (solo la clase lo genera).
 *  - IGV / contador: private, detalle interno que nadie fuera de la clase necesita.
 *  - soles()       : protected, utilitario de formato reservado a las subclases.
 */
abstract class Producto(
    val nombre: String,
    precioBase: Double,
    cantidad: Int = 1
) {

    /** Precio de lista, sin impuestos ni descuentos. */
    var precioBase: Double = precioBase
        set(value) {
            require(value >= 0.0) {
                "El precio de '$nombre' no puede ser negativo (recibido: $value)"
            }
            field = value
        }

    /** Unidades del producto dentro del carrito. */
    var cantidad: Int = cantidad
        set(value) {
            require(value >= 0) {
                "La cantidad de '$nombre' no puede ser negativa (recibido: $value)"
            }
            field = value
        }

    /** Codigo correlativo asignado automaticamente; nadie lo modifica desde afuera. */
    var codigo: String = ""
        private set

    /**
     * Los inicializadores de propiedad NO pasan por el setter personalizado,
     * por eso los valores que llegan del constructor se validan aqui.
     */
    init {
        require(nombre.isNotBlank()) { "El nombre del producto no puede estar vacio" }
        require(precioBase >= 0.0) {
            "El precio de '$nombre' no puede ser negativo (recibido: $precioBase)"
        }
        require(cantidad >= 0) {
            "La cantidad de '$nombre' no puede ser negativa (recibido: $cantidad)"
        }
        contador++
        codigo = String.format("P-%03d", contador)
    }

    // ---------------------------------------------------------------
    // Contrato que cada tipo de producto debe resolver a su manera
    // ---------------------------------------------------------------

    /** Precio unitario ya con impuestos, recargos o descuentos propios del tipo. */
    abstract fun precioFinal(): Double

    /** Muestra en consola la linea de detalle del producto. */
    abstract fun imprimirDetalle()

    /** Importe de la linea: precio final unitario por la cantidad pedida. */
    open fun importeTotal(): Double = precioFinal() * cantidad

    // ---------------------------------------------------------------
    // Utilitarios heredables
    // ---------------------------------------------------------------

    /** Formato monetario unico para todo el sistema. Solo visible para las subclases. */
    protected fun soles(monto: Double): String = String.format(Locale.US, "S/ %8.2f", monto)

    /** Linea comun de detalle; las subclases le agregan su informacion propia. */
    protected fun lineaBase(tipo: String): String =
        String.format(
            Locale.US,
            "%-5s %-22s %-11s x%-3d %s",
            codigo, nombre.take(22), tipo, cantidad, soles(importeTotal())
        )

    override fun toString(): String = "$codigo $nombre x$cantidad"

    private companion object {
        const val IGV = 0.18
        var contador = 0
    }

    /** Expuesto a las subclases sin filtrar la constante privada. */
    protected fun conIgv(monto: Double): Double = monto * (1 + IGV)
}

/**
 * Producto fisico: paga IGV y suma un recargo de despacho segun su peso.
 */
class ProductoFisico(
    nombre: String,
    precioBase: Double,
    cantidad: Int = 1,
    pesoKg: Double
) : Producto(nombre, precioBase, cantidad) {

    var pesoKg: Double = pesoKg
        set(value) {
            require(value >= 0.0) { "El peso de '$nombre' no puede ser negativo" }
            field = value
        }

    init {
        require(pesoKg >= 0.0) { "El peso de '$nombre' no puede ser negativo" }
    }

    private val recargoDespacho: Double
        get() = pesoKg * COSTO_POR_KG

    override fun precioFinal(): Double = conIgv(precioBase) + recargoDespacho

    override fun imprimirDetalle() {
        println(lineaBase("[Fisico]") + String.format(Locale.US, "  (%.1f kg, despacho S/ %.2f)", pesoKg, recargoDespacho))
    }

    private companion object {
        const val COSTO_POR_KG = 3.5
    }
}

/**
 * Producto digital: paga IGV pero no genera despacho y tiene descuento por licencia.
 */
class ProductoDigital(
    nombre: String,
    precioBase: Double,
    cantidad: Int = 1,
    val tamanioMb: Int,
    descuento: Double = 0.10
) : Producto(nombre, precioBase, cantidad) {

    var descuento: Double = descuento
        set(value) {
            require(value in 0.0..1.0) { "El descuento de '$nombre' debe estar entre 0.0 y 1.0" }
            field = value
        }

    init {
        require(tamanioMb >= 0) { "El tamanio de '$nombre' no puede ser negativo" }
        require(descuento in 0.0..1.0) { "El descuento de '$nombre' debe estar entre 0.0 y 1.0" }
    }

    override fun precioFinal(): Double = conIgv(precioBase * (1 - descuento))

    override fun imprimirDetalle() {
        println(lineaBase("[Digital]") + String.format(Locale.US, "  (%d MB, -%.0f%% descarga)", tamanioMb, descuento * 100))
    }
}

/**
 * Producto perecible: mientras mas cerca del vencimiento, mayor el descuento.
 */
class ProductoPerecible(
    nombre: String,
    precioBase: Double,
    cantidad: Int = 1,
    diasParaVencer: Int
) : Producto(nombre, precioBase, cantidad) {

    var diasParaVencer: Int = diasParaVencer
        set(value) {
            require(value >= 0) { "Los dias para vencer de '$nombre' no pueden ser negativos" }
            field = value
        }

    init {
        require(diasParaVencer >= 0) { "Los dias para vencer de '$nombre' no pueden ser negativos" }
    }

    private val porcentajeRebaja: Double
        get() = when {
            diasParaVencer <= 1 -> 0.50
            diasParaVencer <= 3 -> 0.30
            diasParaVencer <= 7 -> 0.15
            else -> 0.0
        }

    override fun precioFinal(): Double = conIgv(precioBase * (1 - porcentajeRebaja))

    override fun imprimirDetalle() {
        val nota = if (porcentajeRebaja > 0)
            String.format(Locale.US, "vence en %d dia(s), -%.0f%%", diasParaVencer, porcentajeRebaja * 100)
        else
            String.format(Locale.US, "vence en %d dia(s)", diasParaVencer)
        println(lineaBase("[Perecible]") + "  ($nota)")
    }
}
