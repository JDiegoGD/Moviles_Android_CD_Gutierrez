package com.gutierrez.lab02carritokotlin

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

fun calcularSubtotal(producto: List<Producto>): Double{
    var subtotal = 0.0
    for (p in producto) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}

fun calcularIGV(subtotal: Double): Double{
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double{
    return subtotal + igv
}

fun mostrarDetalle(productos: List<Producto>){
    println("---------DETALLE DEL CARRITO----------")
    var i = 1
    for (p in productos){
        val importe = p.precio * p.cantidad
        println(String.format("%d. %-20s x%d S/ %8.2f",
            i, p.nombre, p.cantidad ,importe))
        i++
    }
    println("--------------------------------------")
}

fun mensajeDescuento(total: Double): String{
    return when {
        total > 5000 -> "Descuento aplicado: 10% por compra mayor a S/ 5000"
        total > 3000 -> "Descuento aplicado: 5% por compra mayor a S/ 3000"
        else -> "No se aplica descuento."
    }
}

fun calcularDescuento(total: Double): Double{
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}

fun main() {
    println("======================================")
    println("  CARRITO DE COMPRAS - TIENDA TECSUP  ")
    println("======================================")

    val nombreCliente = "Juan Gutierrez"
    val carrito = mutableListOf<Producto>()
    println("Cliente: $nombreCliente")
    println()

    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Teclado Redragon", 120.0, 2))
    carrito.add(Producto("Monitor Teros", 350.0, 2))

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }
    println("\n")
    mostrarDetalle(carrito)

    val subtotalCarrito = calcularSubtotal(carrito)
    val total = calcularTotal(subtotalCarrito, calcularIGV(subtotalCarrito))
    println("Cantidad de productos : ${carrito.size}")
    println("Subtotal              : S/${String.format("%8.2f",subtotalCarrito)}")
    println("IGV (18%)             : S/${String.format("%8.2f",calcularIGV(subtotalCarrito))}")
    println("TOTAL A PAGAR         : S/${String.format("%8.2f",total)}")

    val masCaro = carrito.maxByOrNull { it.precio }
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre}" +
        String.format(" (S/ %.2f)", masCaro.precio))
    }

    println(mensajeDescuento(total))
    println("TOTAL CON DESCUENTO   : S/${String.format("%8.2f",total - calcularDescuento(total))}")

    println("Gracias por su compra, ${nombreCliente}")
}