package com.gutierrez.Lab02IA

import java.util.Locale

/**
 * PROGRAMA PRINCIPAL - Laboratorio 02: POO aplicada a un carrito de compras.
 *
 * Integra los cuatro pilares desarrollados en el laboratorio:
 *  - ABSTRACCION    : Producto (clase abstracta) y EstrategiaDescuento (interfaz).
 *  - HERENCIA       : ProductoFisico y ProductoDigital extienden Producto.
 *  - ENCAPSULAMIENTO: CarritoDeCompras esconde su lista y sus calculos financieros.
 *  - POLIMORFISMO   : el carrito llama precioFinal() e imprimirDetalle() sin saber
 *                     que tipo concreto tiene delante.
 *
 * Ejecutable de consola: no usa interfaz grafica.
 */
fun main() {

    // -----------------------------------------------------------------
    // 1. Cliente y creacion de los productos
    // -----------------------------------------------------------------
    val cliente = "Gutierrez Duran Juan Diego"

    titulo("1. CREACION DE PRODUCTOS")

    val laptop = ProductoFisico(
        nombre = "Laptop HP Victus",
        precioBase = 2500.0,
        cantidad = 1,
        costoEnvio = 35.0
    )
    val monitor = ProductoFisico(
        nombre = "Monitor Teros 24",
        precioBase = 350.0,
        cantidad = 2,
        costoEnvio = 20.0
    )
    val office = ProductoDigital(
        nombre = "Licencia Office 365",
        precioBase = 180.0,
        cantidad = 2,
        descuentoDigital = 0.20,
        licencia = "Anual"
    )
    val antivirus = ProductoDigital(
        nombre = "Antivirus Kaspersky",
        precioBase = 95.0,
        cantidad = 3,
        descuentoDigital = 0.25,
        licencia = "Familiar"
    )

    // -----------------------------------------------------------------
    // 2. Carrito del cliente y carga de los productos
    // -----------------------------------------------------------------
    val carrito = CarritoDeCompras(cliente = cliente)

    // La lista es de tipo Producto: acepta indistintamente ambas subclases
    val compra = listOf(laptop, monitor, office, antivirus)
    for (producto in compra) {
        carrito.agregarProducto(producto)
        println(String.format(Locale.US, "  Producto agregado: %-22s x%d", producto.nombre, producto.cantidad))
    }
    println("  Total de productos en el carrito: ${carrito.cantidadProductos} (${carrito.cantidadItems} unidades)")

    // -----------------------------------------------------------------
    // 3. Asignacion de la promocion vigente
    // -----------------------------------------------------------------
    titulo("2. ASIGNACION DE LA ESTRATEGIA DE DESCUENTO")

    println("  Estrategia inicial : ${carrito.estrategiaDescuento.descripcion}")
    carrito.cambiarEstrategia(DescuentoPorMonto())
    println("  Estrategia asignada: ${carrito.estrategiaDescuento.descripcion}")
    println(String.format(Locale.US, "  Total de la compra : S/ %.2f -> corresponde rebajar S/ %.2f",
        carrito.calcularTotal(), carrito.calcularDescuento()))

    // -----------------------------------------------------------------
    // 4. Boleta final
    // -----------------------------------------------------------------
    titulo("3. BOLETA FINAL")

    carrito.mostrarResumen()

    // -----------------------------------------------------------------
    // 5. Manejo de excepciones: el sistema rechaza los datos invalidos
    // -----------------------------------------------------------------
    titulo("4. MANEJO DE EXCEPCIONES (IllegalArgumentException)")

    // Ejemplo explicito con try / catch
    try {
        val invalido = ProductoFisico("Teclado Redragon", -120.0, cantidad = 1, costoEnvio = 10.0)
        println("  [FALLO ] Se creo un producto con precio negativo: $invalido")
    } catch (e: IllegalArgumentException) {
        println("  [OK    ] Precio negativo rechazado -> ${e.message}")
    }

    // El resto de los casos usa el mismo try / catch dentro del helper probarValidacion()
    probarValidacion("Cantidad negativa en el constructor") {
        ProductoDigital("Ebook Kotlin", 55.0, cantidad = -2)
    }
    probarValidacion("Costo de envio negativo") {
        ProductoFisico("Mouse Logitech", 45.5, cantidad = 1, costoEnvio = -5.0)
    }
    probarValidacion("Precio negativo asignado por el setter") {
        laptop.precioBase = -1500.0
    }
    probarValidacion("Cantidad negativa asignada por el setter") {
        monitor.cantidad = -3
    }
    probarValidacion("Costo de envio negativo por el setter") {
        laptop.costoEnvio = -20.0
    }
    probarValidacion("Descuento digital fuera del rango 0.0 - 1.0") {
        ProductoDigital("Curso Android", 300.0, cantidad = 1, descuentoDigital = 1.5)
    }
    probarValidacion("Nombre de producto vacio") {
        ProductoFisico("   ", 100.0, cantidad = 1, costoEnvio = 5.0)
    }
    probarValidacion("Agregar al carrito un producto con cantidad 0") {
        carrito.agregarProducto(ProductoDigital("Plantilla Figma", 30.0, cantidad = 0))
    }
    probarValidacion("Crear un carrito sin cliente") {
        CarritoDeCompras(cliente = "")
    }

    // Ningun intento fallido altero el estado del carrito
    println()
    println("  El carrito sigue intacto tras los intentos invalidos:")
    println(String.format(Locale.US, "    Productos : %d   |   Total final : S/ %.2f",
        carrito.cantidadProductos, carrito.calcularTotalFinal()))

    println()
    println("Gracias por su compra, $cliente.")
}

/** Encabezado de seccion para separar las etapas en la consola. */
private fun titulo(texto: String) {
    println()
    println("*".repeat(92))
    println("  $texto")
    println("*".repeat(92))
}

/**
 * Ejecuta un bloque que deberia fallar y reporta el resultado.
 * Si la validacion no salta, se marca como [FALLO]: seria un hueco del modelo.
 */
private fun probarValidacion(caso: String, bloque: () -> Unit) {
    try {
        bloque()
        println(String.format("  [FALLO ] %-45s -> se acepto un valor invalido", caso))
    } catch (e: IllegalArgumentException) {
        println(String.format("  [OK    ] %-45s -> %s", caso, e.message))
    }
}
