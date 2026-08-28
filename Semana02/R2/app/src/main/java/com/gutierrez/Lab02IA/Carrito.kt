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
    println("======= PRUEBA DE HERENCIA: Producto -> Fisico / Digital =======\n")

    // 1) Instanciamos una subclase de cada tipo
    val laptop = ProductoFisico(
        nombre = "Laptop HP Victus",
        precioBase = 2500.0,
        cantidad = 1,
        costoEnvio = 35.0
    )
    val office = ProductoDigital(
        nombre = "Licencia Office 365",
        precioBase = 180.0,
        cantidad = 2,
        descuentoDigital = 0.20,
        licencia = "Anual"
    )

    // 2) Cada uno resuelve precioFinal() e imprimirDetalle() a su manera
    laptop.imprimirDetalle()
    office.imprimirDetalle()

    println()
    println(String.format("Precio final unitario laptop : S/ %.2f", laptop.precioFinal()))
    println(String.format("Precio final unitario office : S/ %.2f", office.precioFinal()))

    // 3) Polimorfismo: la lista es de tipo Producto, no de los tipos concretos
    println("\n--- Recorrido polimorfico sobre List<Producto> ---")
    val catalogo: List<Producto> = listOf(
        laptop,
        office,
        ProductoFisico("Monitor Teros 24", 350.0, cantidad = 2, costoEnvio = 20.0),
        ProductoDigital("Antivirus Kaspersky", 95.0, cantidad = 3, descuentoDigital = 0.25, licencia = "Familiar")
    )
    catalogo.forEach { it.imprimirDetalle() }

    // 4) La boleta del carrito con los mismos objetos
    println()
    val carrito = Carrito(cliente = "Juan Diego Gutierrez")
    catalogo.forEach { carrito.agregar(it) }
    carrito.imprimirBoleta()

    // 5) Las validaciones heredadas y las propias de cada subclase siguen activas
    println("\n--- Prueba de las validaciones ---")
    probar("Precio negativo (heredado de Producto)") {
        ProductoFisico("Teclado Redragon", -120.0, cantidad = 1, costoEnvio = 10.0)
    }
    probar("Cantidad negativa (heredado de Producto)") {
        ProductoDigital("Ebook Kotlin", 55.0, cantidad = -2)
    }
    probar("Costo de envio negativo (ProductoFisico)") {
        ProductoFisico("Mouse Logitech", 45.5, cantidad = 1, costoEnvio = -5.0)
    }
    probar("Descuento fuera de rango (ProductoDigital)") {
        ProductoDigital("Curso Android", 300.0, cantidad = 1, descuentoDigital = 1.5)
    }
    probar("Envio negativo asignado por el setter") {
        laptop.costoEnvio = -1.0
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
