package com.gutierrez.LabReto

import kotlin.system.exitProcess

/**
 * Sistema de Estacionamiento de Vehículos
 * Tipos permitidos: Moto, Auto, Camioneta
 */

// Tarifas por hora según tipo de vehículo
enum class TipoVehiculo(val tarifaHora: Double) {
    MOTO(2.0),
    AUTO(4.0),
    CAMIONETA(6.0);

    companion object {
        fun desdeTexto(texto: String): TipoVehiculo {
            return when (texto.trim().lowercase()) {
                "moto" -> MOTO
                "auto" -> AUTO
                "camioneta" -> CAMIONETA
                else -> throw IllegalArgumentException(
                    "Tipo de vehículo no válido: '$texto'. Use Moto, Auto o Camioneta."
                )
            }
        }
    }

    fun etiqueta(): String = when (this) {
        MOTO -> "Moto"
        AUTO -> "Auto"
        CAMIONETA -> "Camioneta"
    }
}

class Vehiculo(
    placa: String,
    tipo: String,
    horas: Int,
    val esClienteFrecuente: Boolean,
    nombreCliente: String
) {
    val placa: String
    val tipo: TipoVehiculo
    val horas: Int
    val nombreCliente: String

    init {
        // Validación de placa
        require(placa.isNotBlank()) { "La placa no puede estar vacía." }

        // Validación y conversión de tipo
        this.tipo = TipoVehiculo.desdeTexto(tipo)

        // Validación de horas: no puede ser menor a 1
        require(horas >= 1) { "Las horas deben ser al menos 1. Valor recibido: $horas" }
        this.horas = horas

        // Validación de nombre de cliente
        require(nombreCliente.isNotBlank()) { "El nombre del cliente no puede estar vacío." }

        this.placa = placa.trim().uppercase()
        this.nombreCliente = nombreCliente.trim()
    }

    /**
     * Calcula el costo total del estacionamiento.
     * Los clientes frecuentes reciben un 15% de descuento.
     */
    fun calcularCosto(): Double {
        val costoBase = horas * tipo.tarifaHora
        return if (esClienteFrecuente) costoBase * 0.85 else costoBase
    }

    override fun toString(): String {
        val frecuente = if (esClienteFrecuente) "Sí" else "No"
        val costo = "%.2f".format(calcularCosto())
        return """
            |-----------------------------------
            |Placa           : $placa
            |Tipo             : ${tipo.etiqueta()}
            |Horas            : $horas
            |Cliente frecuente: $frecuente
            |Cliente          : $nombreCliente
            |Costo total      : S/ $costo
            |-----------------------------------
        """.trimMargin()
    }
}

// ------------------- APLICACIÓN DE TERMINAL -------------------

fun leerEntero(mensaje: String): Int {
    while (true) {
        print(mensaje)
        val entrada = readLine()
        val valor = entrada?.toIntOrNull()
        if (valor != null) return valor
        println("Entrada no válida. Ingrese un número entero.")
    }
}

fun leerTexto(mensaje: String): String {
    while (true) {
        print(mensaje)
        val entrada = readLine()?.trim()
        if (!entrada.isNullOrBlank()) return entrada
        println("Este campo no puede estar vacío.")
    }
}

fun leerBooleano(mensaje: String): Boolean {
    while (true) {
        print("$mensaje (s/n): ")
        when (readLine()?.trim()?.lowercase()) {
            "s", "si", "sí" -> return true
            "n", "no" -> return false
            else -> println("Respuesta no válida. Escriba 's' o 'n'.")
        }
    }
}

fun registrarVehiculo(): Vehiculo? {
    val placa = leerTexto("Placa: ")
    val tipo = leerTexto("Tipo (Moto/Auto/Camioneta): ")
    val horas = leerEntero("Horas estacionado: ")
    val frecuente = leerBooleano("¿Es cliente frecuente?")
    val nombre = leerTexto("Nombre del cliente: ")

    return try {
        Vehiculo(placa, tipo, horas, frecuente, nombre)
    } catch (e: IllegalArgumentException) {
        println("\nError al registrar el vehículo: ${e.message}\n")
        null
    }
}

fun main() {
    val vehiculosRegistrados = mutableListOf<Vehiculo>()

    while (true) {
        println(
            """
            |
            |=== SISTEMA DE ESTACIONAMIENTO ===
            |1. Registrar vehículo
            |2. Listar vehículos registrados
            |3. Salir
            """.trimMargin()
        )
        print("Seleccione una opción: ")

        when (readLine()?.trim()) {
            "1" -> {
                val vehiculo = registrarVehiculo()
                if (vehiculo != null) {
                    vehiculosRegistrados.add(vehiculo)
                    println("\nVehículo registrado correctamente:")
                    println(vehiculo)
                }
            }
            "2" -> {
                if (vehiculosRegistrados.isEmpty()) {
                    println("\nNo hay vehículos registrados todavía.")
                } else {
                    println("\n--- Vehículos registrados (${vehiculosRegistrados.size}) ---")
                    vehiculosRegistrados.forEach { println(it) }
                }
            }
            "3" -> {
                println("Saliendo del sistema...")
                exitProcess(0)
            }
            else -> println("Opción no válida. Intente de nuevo.")
        }
    }
}