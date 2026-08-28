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

fun main() {
    // El carrito arranca con la politica por defecto: SinDescuento
    val carrito = CarritoDeCompras(cliente = "Juan Diego Gutierrez")

    // Se mezclan los dos tipos: el carrito los guarda a todos como Producto
    carrito.agregarProducto(ProductoFisico("Laptop HP Victus", 2500.0, cantidad = 1, costoEnvio = 35.0))
    carrito.agregarProducto(ProductoFisico("Monitor Teros 24", 350.0, cantidad = 2, costoEnvio = 20.0))
    carrito.agregarProducto(
        ProductoDigital("Licencia Office 365", 180.0, cantidad = 2, descuentoDigital = 0.20, licencia = "Anual")
    )
    carrito.agregarProducto(
        ProductoDigital("Antivirus Kaspersky", 95.0, cantidad = 3, descuentoDigital = 0.25, licencia = "Familiar")
    )

    println(">>> Boleta con la estrategia por defecto (SinDescuento)\n")
    carrito.mostrarResumen()

    // La misma compra, cambiando solo la politica de descuento
    carrito.cambiarEstrategia(DescuentoPorMonto())
    println("\n>>> Misma compra, ahora con DescuentoPorMonto\n")
    carrito.mostrarResumen()

    // ABSTRACCION en accion: se recorre List<EstrategiaDescuento> sin conocer las clases
    println("\n--- Comparacion de estrategias sobre el mismo total ---")
    val total = carrito.calcularTotal()
    val estrategias: List<EstrategiaDescuento> = listOf(SinDescuento(), DescuentoPorMonto())
    for (estrategia in estrategias) {
        val rebaja = estrategia.calcularDescuento(total)
        println(
            String.format(
                Locale.US, "  %-20s descuento S/ %8.2f -> total final S/ %8.2f",
                estrategia::class.simpleName, rebaja, total - rebaja
            )
        )
    }

    // La escala de DescuentoPorMonto probada en sus tres tramos
    println("\n--- Tramos de DescuentoPorMonto ---")
    val porMonto = DescuentoPorMonto()
    for (monto in listOf(1500.0, 3500.0, 6000.0)) {
        println(
            String.format(
                Locale.US, "  Monto S/ %8.2f -> %.0f%% = S/ %8.2f",
                monto, porMonto.porcentajeAplicado(monto) * 100, porMonto.calcularDescuento(monto)
            )
        )
    }

    println("\n--- Calculos por separado ---")
    println(String.format(Locale.US, "  calcularSubtotal()       : S/ %.2f", carrito.calcularSubtotal()))
    println(String.format(Locale.US, "  calcularIGV()            : S/ %.2f", carrito.calcularIGV()))
    println(String.format(Locale.US, "  calcularTotal()          : S/ %.2f", carrito.calcularTotal()))
    println(String.format(Locale.US, "  calcularDescuento()      : S/ %.2f", carrito.calcularDescuento()))
    println(String.format(Locale.US, "  calcularTotalFinal()     : S/ %.2f", carrito.calcularTotalFinal()))
    println("  obtenerProductoMasCaro() : ${carrito.obtenerProductoMasCaro()?.nombre}")

    println("\n--- Validaciones ---")
    probar("Precio negativo (heredado de Producto)") {
        ProductoFisico("Teclado Redragon", -120.0, cantidad = 1, costoEnvio = 10.0)
    }
    probar("Costo de envio negativo (ProductoFisico)") {
        ProductoFisico("Mouse Logitech", 45.5, cantidad = 1, costoEnvio = -5.0)
    }
    probar("Descuento fuera de rango (ProductoDigital)") {
        ProductoDigital("Curso Android", 300.0, cantidad = 1, descuentoDigital = 1.5)
    }
    probar("Agregar un producto con cantidad 0") {
        carrito.agregarProducto(ProductoDigital("Ebook Kotlin", 55.0, cantidad = 0))
    }

    println("\nGracias por su compra, ${carrito.cliente}.")
}

/** Ejecuta un bloque y reporta si la validacion lo rechazo. */
private fun probar(caso: String, bloque: () -> Unit) {
    try {
        bloque()
        println("  [FALLO ] $caso -> se acepto un valor invalido")
    } catch (e: IllegalArgumentException) {
        println("  [OK    ] $caso -> ${e.message}")
    }
}
