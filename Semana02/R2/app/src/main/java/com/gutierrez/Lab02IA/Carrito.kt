package com.gutierrez.Lab02IA

import java.util.Locale

/**
 * Carrito de compras. Guarda la lista de forma privada y solo expone
 * una vista de solo lectura, para que nadie agregue productos saltandose
 * las validaciones de agregar().
 */
class Carrito(val cliente: String) {

    private val items = mutableListOf<Producto>()

    /** Vista inmutable: se puede recorrer, pero no modificar desde afuera. */
    val productos: List<Producto>
        get() = items.toList()

    val cantidadItems: Int
        get() = items.sumOf { it.cantidad }

    init {
        require(cliente.isNotBlank()) { "El nombre del cliente no puede estar vacio" }
    }

    fun agregar(producto: Producto) {
        require(producto.cantidad > 0) {
            "No se puede agregar '${producto.nombre}' con cantidad 0"
        }
        items.add(producto)
    }

    fun eliminar(codigo: String): Boolean = items.removeAll { it.codigo == codigo }

    fun total(): Double = items.sumOf { it.importeTotal() }

    fun descuentoPorMonto(): Double = when {
        total() > 5000 -> total() * 0.10
        total() > 3000 -> total() * 0.05
        else -> 0.0
    }

    fun mensajeDescuento(): String = when {
        total() > 5000 -> "Descuento aplicado: 10% por compra mayor a S/ 5000"
        total() > 3000 -> "Descuento aplicado: 5% por compra mayor a S/ 3000"
        else -> "No se aplica descuento por monto."
    }

    fun productoMasCaro(): Producto? = items.maxByOrNull { it.precioFinal() }

    /** Aqui se ve el polimorfismo: cada producto sabe como imprimirse. */
    fun imprimirBoleta() {
        println("=".repeat(78))
        println("            BOLETA DE VENTA - TIENDA TECSUP")
        println("  Cliente: $cliente")
        println("=".repeat(78))

        if (items.isEmpty()) {
            println("  El carrito esta vacio.")
            println("=".repeat(78))
            return
        }

        items.forEach { it.imprimirDetalle() }

        val total = total()
        val descuento = descuentoPorMonto()
        println("-".repeat(78))
        println(linea("Items en el carrito", cantidadItems.toDouble(), esMonto = false))
        println(linea("TOTAL (IGV incluido)", total))
        println("  ${mensajeDescuento()}")
        println(linea("Descuento", descuento))
        println(linea("TOTAL A PAGAR", total - descuento))
        productoMasCaro()?.let {
            println(String.format(Locale.US, "  Producto mas caro    : %s (S/ %.2f c/u)", it.nombre, it.precioFinal()))
        }
        println("=".repeat(78))
    }

    private fun linea(etiqueta: String, valor: Double, esMonto: Boolean = true): String =
        if (esMonto) String.format(Locale.US, "  %-20s : S/ %10.2f", etiqueta, valor)
        else String.format(Locale.US, "  %-20s : %13d", etiqueta, valor.toInt())
}

fun main() {
    val carrito = Carrito(cliente = "Juan Diego Gutierrez")

    carrito.agregar(ProductoFisico("Laptop HP", 2500.0, cantidad = 1, pesoKg = 2.4))
    carrito.agregar(ProductoFisico("Monitor Teros", 350.0, cantidad = 2, pesoKg = 4.0))
    carrito.agregar(ProductoDigital("Licencia Office 365", 180.0, cantidad = 1, tamanioMb = 4096))
    carrito.agregar(ProductoDigital("Antivirus Kaspersky", 95.0, cantidad = 2, tamanioMb = 512, descuento = 0.25))
    carrito.agregar(ProductoPerecible("Cafe molido 500g", 42.0, cantidad = 3, diasParaVencer = 2))
    carrito.agregar(ProductoPerecible("Galletas integrales", 8.5, cantidad = 4, diasParaVencer = 30))

    carrito.imprimirBoleta()

    println()
    println("--- Prueba de las validaciones ---")
    probar("Precio negativo en el constructor") {
        ProductoFisico("Teclado Redragon", -120.0, cantidad = 1, pesoKg = 0.9)
    }
    probar("Cantidad negativa en el constructor") {
        ProductoDigital("Ebook Kotlin", 55.0, cantidad = -2, tamanioMb = 12)
    }
    probar("Precio negativo asignado por el setter") {
        val p = ProductoFisico("Mouse Logitech", 45.5, cantidad = 1, pesoKg = 0.2)
        p.precioBase = -10.0
    }
    probar("Cantidad negativa asignada por el setter") {
        val p = ProductoPerecible("Leche Gloria", 4.5, cantidad = 6, diasParaVencer = 10)
        p.cantidad = -1
    }
    probar("Descuento fuera de rango") {
        ProductoDigital("Curso Android", 300.0, cantidad = 1, tamanioMb = 2048, descuento = 1.5)
    }

    println()
    println("Gracias por su compra, ${carrito.cliente}.")
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
