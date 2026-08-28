package com.gutierrez.Lab02IA

import java.util.Locale

/**
 * ABSTRACCION - Modelo base del carrito.
 *
 * precioFinal() devuelve el precio unitario SIN IGV: cada subclase decide como se
 * arma (recargos, descuentos). El impuesto es una regla del negocio, no del producto,
 * y por eso lo calcula CarritoDeCompras sobre el subtotal.
 *
 * Encapsulamiento:
 *  - nombre     : publico de solo lectura (no se reasigna en la vida del producto).
 *  - precioBase : publico de lectura, con setter validado (no admite negativos).
 *  - cantidad   : publico de lectura, con setter validado (no admite negativos).
 *  - codigo     : publico de lectura, private set (solo la clase lo genera).
 *  - contador   : private, detalle interno que nadie fuera de la clase necesita.
 *  - soles()    : protected, utilitario de formato reservado a las subclases.
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

    /** Precio unitario sin IGV, ya con los recargos o descuentos propios del tipo. */
    abstract fun precioFinal(): Double

    /** Muestra en consola la linea de detalle del producto. */
    abstract fun imprimirDetalle()

    /** Subtotal de la linea: precio final unitario por la cantidad pedida. */
    open fun importeTotal(): Double = precioFinal() * cantidad

    // ---------------------------------------------------------------
    // Utilitarios heredables
    // ---------------------------------------------------------------

    /** Formato monetario unico para todo el sistema. Solo visible para las subclases. */
    protected fun soles(monto: Double): String = String.format(Locale.US, "S/ %8.2f", monto)

    /** Linea comun de detalle; sus anchos coinciden con la cabecera de CarritoDeCompras. */
    protected fun lineaBase(tipo: String): String =
        String.format(
            Locale.US,
            "%-5s %-22s %-11s x%-3d %s %s",
            codigo, nombre.take(22), tipo, cantidad, soles(precioFinal()), soles(importeTotal())
        )

    override fun toString(): String = "$codigo $nombre x$cantidad"

    private companion object {
        var contador = 0
    }
}

/**
 * HERENCIA 1 - Producto fisico (se despacha a domicilio).
 *
 * Aporta el atributo costoEnvio y sobrescribe el contrato de la clase padre:
 *  - precioFinal()     : precio de lista + el costo de envio de la unidad.
 *  - imprimirDetalle() : agrega el costo de envio a la linea de detalle.
 */
class ProductoFisico(
    nombre: String,
    precioBase: Double,
    cantidad: Int = 1,
    costoEnvio: Double
) : Producto(nombre, precioBase, cantidad) {

    /** Atributo propio de la subclase, tambien protegido contra valores negativos. */
    var costoEnvio: Double = costoEnvio
        set(value) {
            require(value >= 0.0) {
                "El costo de envio de '$nombre' no puede ser negativo (recibido: $value)"
            }
            field = value
        }

    init {
        require(costoEnvio >= 0.0) {
            "El costo de envio de '$nombre' no puede ser negativo (recibido: $costoEnvio)"
        }
    }

    /** El producto fisico suma el flete al precio de lista. */
    override fun precioFinal(): Double = precioBase + costoEnvio

    override fun imprimirDetalle() {
        println(
            lineaBase("[Fisico]") +
                String.format(Locale.US, "  (envio S/ %.2f c/u)", costoEnvio)
        )
    }
}

/**
 * HERENCIA 2 - Producto digital (se descarga, no se despacha).
 *
 * Aporta los atributos descuentoDigital y licencia, y sobrescribe:
 *  - precioFinal()     : aplica el descuento digital; nunca paga envio.
 *  - imprimirDetalle() : muestra el tipo de licencia y el descuento aplicado.
 */
class ProductoDigital(
    nombre: String,
    precioBase: Double,
    cantidad: Int = 1,
    descuentoDigital: Double = 0.10,
    val licencia: String = "Personal"
) : Producto(nombre, precioBase, cantidad) {

    /** Descuento expresado en fraccion: 0.10 = 10%. Solo acepta valores entre 0.0 y 1.0. */
    var descuentoDigital: Double = descuentoDigital
        set(value) {
            require(value in 0.0..1.0) {
                "El descuento de '$nombre' debe estar entre 0.0 y 1.0 (recibido: $value)"
            }
            field = value
        }

    init {
        require(descuentoDigital in 0.0..1.0) {
            "El descuento de '$nombre' debe estar entre 0.0 y 1.0 (recibido: $descuentoDigital)"
        }
        require(licencia.isNotBlank()) { "La licencia de '$nombre' no puede estar vacia" }
    }

    /** Monto que el cliente se ahorra por unidad, util para el detalle. */
    val ahorroPorUnidad: Double
        get() = precioBase - precioFinal()

    /** El producto digital rebaja el precio de lista segun su descuento. */
    override fun precioFinal(): Double = precioBase * (1 - descuentoDigital)

    override fun imprimirDetalle() {
        println(
            lineaBase("[Digital]") +
                String.format(
                    Locale.US,
                    "  (licencia %s, -%.0f%% = ahorro S/ %.2f c/u)",
                    licencia, descuentoDigital * 100, ahorroPorUnidad
                )
        )
    }
}
