package com.gutierrez.Lab02IA

import java.util.Locale

/**
 * ENCAPSULAMIENTO + ABSTRACCION
 *
 * Lo que queda encerrado en esta clase:
 *  1. La lista de productos (private): solo se toca a traves de agregarProducto()
 *     y eliminarProducto(), nunca desde afuera.
 *  2. Los calculos financieros: la tasa de IGV es una constante privada. Quien use
 *     el carrito pide calcularTotal(), no reimplementa la formula.
 *
 * Lo que queda delegado a una abstraccion:
 *  3. El descuento. El carrito recibe una EstrategiaDescuento y le pregunta cuanto
 *     rebajar; no conoce topes ni porcentajes. Cambiar la promocion no obliga a
 *     modificar esta clase.
 */
class CarritoDeCompras(
    val cliente: String,
    estrategiaDescuento: EstrategiaDescuento = SinDescuento()
) {

    private val listaProductos = mutableListOf<Producto>()

    /** Politica de descuento vigente. Se lee libremente, pero solo cambia por metodo. */
    var estrategiaDescuento: EstrategiaDescuento = estrategiaDescuento
        private set

    /** Vista inmutable: se puede recorrer, pero no modificar desde afuera. */
    val productos: List<Producto>
        get() = listaProductos.toList()

    /** Cuantos productos distintos hay en el carrito. */
    val cantidadProductos: Int
        get() = listaProductos.size

    /** Suma de las unidades de todos los productos. */
    val cantidadItems: Int
        get() = listaProductos.sumOf { it.cantidad }

    init {
        require(cliente.isNotBlank()) { "El nombre del cliente no puede estar vacio" }
    }

    // ---------------------------------------------------------------
    // Gestion de la lista
    // ---------------------------------------------------------------

    /** Unica puerta de entrada a la lista, y por eso valida antes de insertar. */
    fun agregarProducto(producto: Producto) {
        require(producto.cantidad > 0) {
            "No se puede agregar '${producto.nombre}' con cantidad 0"
        }
        listaProductos.add(producto)
    }

    fun eliminarProducto(codigo: String): Boolean =
        listaProductos.removeAll { it.codigo == codigo }

    fun vaciar() = listaProductos.clear()

    /** Permite intercambiar la promocion en caliente sin recrear el carrito. */
    fun cambiarEstrategia(nueva: EstrategiaDescuento) {
        estrategiaDescuento = nueva
    }

    // ---------------------------------------------------------------
    // Calculos financieros encapsulados
    // ---------------------------------------------------------------

    /**
     * POLIMORFISMO: recorre List<Producto> y llama a precioFinal() sin preguntar
     * si el elemento es ProductoFisico o ProductoDigital.
     */
    fun calcularSubtotal(): Double = listaProductos.sumOf { it.precioFinal() * it.cantidad }

    /** IGV del 18% sobre el subtotal. La tasa es privada: se cambia en un solo lugar. */
    fun calcularIGV(): Double = calcularSubtotal() * IGV

    /** Total gravado, antes de aplicar el descuento. */
    fun calcularTotal(): Double = calcularSubtotal() + calcularIGV()

    /**
     * ABSTRACCION: el carrito no decide el monto, solo delega en la estrategia.
     * Da igual si es SinDescuento, DescuentoPorMonto o una promocion futura.
     */
    fun calcularDescuento(): Double = estrategiaDescuento.calcularDescuento(calcularTotal())

    /** Lo que el cliente termina pagando, ya con la estrategia aplicada. */
    fun calcularTotalFinal(): Double = calcularTotal() - calcularDescuento()

    /** Producto de mayor precio unitario; null si el carrito esta vacio. */
    fun obtenerProductoMasCaro(): Producto? = listaProductos.maxByOrNull { it.precioFinal() }

    // ---------------------------------------------------------------
    // Salida por consola
    // ---------------------------------------------------------------

    /**
     * POLIMORFISMO: cada producto sabe imprimirse a si mismo. El carrito solo
     * recorre la lista; no hay un solo if ni when sobre el tipo concreto.
     */
    fun mostrarDetalle() {
        println(
            String.format(
                Locale.US,
                "%-5s %-22s %-11s %-4s %11s %11s  %s",
                "COD", "PRODUCTO", "TIPO", "CANT", "P.UNIT", "SUBTOTAL", "DETALLE"
            )
        )
        println("-".repeat(ANCHO))
        listaProductos.forEach { it.imprimirDetalle() }
    }

    /** Resumen completo: cliente, productos agregados, detalle, IGV, descuento y total final. */
    fun mostrarResumen() {
        println("=".repeat(ANCHO))
        println(centrar("BOLETA DE VENTA - TIENDA TECSUP"))
        println("=".repeat(ANCHO))
        println(String.format(Locale.US, "%-20s : %s", "Cliente", cliente))
        println(
            String.format(
                Locale.US, "%-20s : %d producto(s), %d unidad(es)",
                "Productos agregados", cantidadProductos, cantidadItems
            )
        )
        println(
            String.format(
                Locale.US, "%-20s : %s", "Promocion", estrategiaDescuento.descripcion
            )
        )
        println("-".repeat(ANCHO))

        if (listaProductos.isEmpty()) {
            println(centrar("El carrito esta vacio"))
            println("=".repeat(ANCHO))
            return
        }

        mostrarDetalle()

        println("-".repeat(ANCHO))
        println(montoDerecha("SUBTOTAL", calcularSubtotal()))
        println(montoDerecha(String.format(Locale.US, "IGV (%.0f%%)", IGV * 100), calcularIGV()))
        println(montoDerecha("TOTAL", calcularTotal()))
        val descuento = calcularDescuento()
        println(montoDerecha("DESCUENTO (${estrategiaDescuento::class.simpleName})", if (descuento > 0.0) -descuento else 0.0))
        println("-".repeat(ANCHO))
        println(montoDerecha("TOTAL FINAL A PAGAR", calcularTotalFinal()))

        obtenerProductoMasCaro()?.let {
            println(
                String.format(
                    Locale.US, "  %-20s : %s (S/ %.2f c/u)",
                    "Producto mas caro", it.nombre, it.precioFinal()
                )
            )
        }
        println("=".repeat(ANCHO))
    }

    // --- Utilitarios de formato ---

    /** Etiqueta a la izquierda y monto con 2 decimales cerrando contra el borde derecho. */
    private fun montoDerecha(etiqueta: String, monto: Double): String =
        String.format(Locale.US, "  %-${ANCHO_ETIQUETA}s S/ %8.2f", etiqueta, monto)

    private fun centrar(texto: String): String =
        " ".repeat(maxOf((ANCHO - texto.length) / 2, 0)) + texto

    private companion object {
        const val IGV = 0.18
        const val ANCHO = 92
        const val ANCHO_ETIQUETA = 76
    }
}
