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

    println("Cantidad de productos : ${carrito.size}")
    println("Subtotal              : S/${String.format("%8.2f",calcularSubtotal(carrito))}")
    println("IGV (18%)             : S/${String.format("%8.2f",calcularIGV(calcularSubtotal(carrito)))}")
    println("TOTAL A PAGAR         : S/${String.format("%8.2f",calcularTotal(calcularSubtotal(carrito), calcularIGV(calcularSubtotal(carrito))))}")


}