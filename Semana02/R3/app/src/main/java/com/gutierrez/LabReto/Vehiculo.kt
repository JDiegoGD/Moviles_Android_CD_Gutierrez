import kotlin.system.exitProcess

/**
 * Sistema de Estacionamiento de Vehículos
 * Tipos permitidos: Moto, Auto, Camioneta, Trailer
 */

// Tarifas por hora según tipo de vehículo
enum class TipoVehiculo(val tarifaHora: Double) {
    MOTO(2.0),
    AUTO(4.0),
    CAMIONETA(10.0),

    TRAILER(20.0);

    companion object {
        fun desdeTexto(texto: String): TipoVehiculo {
            return when (texto.trim().lowercase()) {
                "moto" -> MOTO
                "auto" -> AUTO
                "camioneta" -> CAMIONETA
                "trailer" -> TRAILER
                else -> throw IllegalArgumentException(
                    "Tipo de vehículo no válido: '$texto'. Use Moto, Auto, Trailer o Camioneta."
                )
            }
        }
    }

    fun etiqueta(): String = when (this) {
        MOTO -> "Moto"
        AUTO -> "Auto"
        CAMIONETA -> "Camioneta"
        TRAILER -> "Trailer"
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
     * Reglas de recargo por hora, sobre la tarifa base del tipo de vehículo:
     *  - Horas 1-2   : 0%  de recargo
     *  - Horas 3-5   : 20% de recargo
     *  - Hora 6 en adelante: 50% de recargo
     */
    private fun recargoPorHora(hora: Int): Int = when {
        hora <= 2 -> 0
        hora in 3..5 -> 20
        else -> 50
    }

    /**
     * Desglosa el cobro hora por hora, aplicando el recargo correspondiente
     * a la tarifa base según el tramo en el que cae cada hora.
     */
    fun detalleHoras(): List<DetalleHora> {
        val detalle = mutableListOf<DetalleHora>()
        for (hora in 1..horas) {
            val recargo = recargoPorHora(hora)
            val montoHora = tipo.tarifaHora * (1 + recargo / 100.0)
            detalle.add(DetalleHora(hora, tipo.tarifaHora, recargo, montoHora))
        }
        return detalle
    }

    /** Suma de todas las horas antes de aplicar el descuento por cliente frecuente. */
    fun calcularSubtotal(): Double = detalleHoras().sumOf { it.montoHora }

    /**
     * Calcula el monto total a pagar: suma el costo de cada hora (con su recargo)
     * y, si es_cliente_frecuente es true, aplica un 10% de descuento sobre ese total.
     */
    fun calcularTotal(): Double {
        val subtotal = calcularSubtotal()
        return if (esClienteFrecuente) subtotal * 0.90 else subtotal
    }

    /** Imprime el desglose del cobro hora por hora, y el total final con descuento si aplica. */
    fun imprimirDetalle() {
        println("Desglose por hora (tarifa base ${tipo.etiqueta()}: S/ %.2f)".format(tipo.tarifaHora))
        for (d in detalleHoras()) {
            println(
                "  Hora %2d -> recargo %3d%% -> S/ %.2f"
                    .format(d.hora, d.recargoPorcentaje, d.montoHora)
            )
        }
        val subtotal = calcularSubtotal()
        println("  Subtotal            : S/ %.2f".format(subtotal))
        if (esClienteFrecuente) {
            val descuento = subtotal * 0.10
            println("  Descuento frecuente (10%%): -S/ %.2f".format(descuento))
        }
        println("  TOTAL A PAGAR       : S/ %.2f".format(calcularTotal()))
    }

    /**
     * Genera e imprime en consola la boleta (comprobante) del estacionamiento:
     * datos del cliente/vehículo, tabla de tarifa por hora y resumen de pago.
     */
    fun generarBoleta() {
        val ancho = 54
        val linea = "=".repeat(ancho)
        val separador = "-".repeat(ancho)

        println(linea)
        println(centrar("BOLETA DE ESTACIONAMIENTO", ancho))
        println(linea)

        // --- Datos del cliente y vehículo ---
        println("Cliente        : $nombreCliente")
        println("Placa          : $placa")
        println("Tipo de vehículo: ${tipo.etiqueta()}")
        println(separador)

        // --- Tabla de tarifa por hora ---
        println("%-6s %-14s %-11s %-12s".format("Hora", "Tarifa Base", "%Recargo", "Importe"))
        println(separador)
        for (d in detalleHoras()) {
            println(
                "%-6d %-14s %-11s %-12s".format(
                    d.hora,
                    "S/ %.2f".format(d.tarifaBase),
                    "${d.recargoPorcentaje}%",
                    "S/ %.2f".format(d.montoHora)
                )
            )
        }
        println(separador)

        // --- Resumen de pago ---
        val subtotal = calcularSubtotal()
        val descuento = if (esClienteFrecuente) subtotal * 0.10 else 0.0
        println("%-30s S/ %8.2f".format("Subtotal:", subtotal))
        println("%-30s S/ %8.2f".format("Descuento cliente frecuente:", descuento))
        println("%-30s S/ %8.2f".format("MONTO TOTAL A PAGAR:", calcularTotal()))
        println(linea)
    }

    private fun centrar(texto: String, ancho: Int): String {
        if (texto.length >= ancho) return texto
        val espacios = ancho - texto.length
        val izquierda = espacios / 2
        val derecha = espacios - izquierda
        return " ".repeat(izquierda) + texto + " ".repeat(derecha)
    }

    override fun toString(): String {
        val frecuente = if (esClienteFrecuente) "Sí" else "No"
        val total = "%.2f".format(calcularTotal())
        return """
            |-----------------------------------
            |Placa           : $placa
            |Tipo             : ${tipo.etiqueta()}
            |Horas            : $horas
            |Cliente frecuente: $frecuente
            |Cliente          : $nombreCliente
            |Costo total      : S/ $total
            |-----------------------------------
        """.trimMargin()
    }
}

/** Representa el cobro correspondiente a una hora individual de estacionamiento. */
data class DetalleHora(
    val hora: Int,
    val tarifaBase: Double,
    val recargoPorcentaje: Int,
    val montoHora: Double
)

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
            |3. Ver boleta de un vehículo (por placa)
            |4. Salir
            """.trimMargin()
        )
        print("Seleccione una opción: ")

        when (readLine()?.trim()) {
            "1" -> {
                val vehiculo = registrarVehiculo()
                if (vehiculo != null) {
                    vehiculosRegistrados.add(vehiculo)
                    println("\nVehículo registrado correctamente:\n")
                    vehiculo.generarBoleta()
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
                val placaBuscada = leerTexto("Ingrese la placa a consultar: ").trim().uppercase()
                val encontrado = vehiculosRegistrados.find { it.placa == placaBuscada }
                if (encontrado == null) {
                    println("\nNo se encontró ningún vehículo con esa placa.")
                } else {
                    println()
                    encontrado.generarBoleta()
                }
            }
            "4" -> {
                println("Saliendo del sistema...")
                exitProcess(0)
            }
            else -> println("Opción no válida. Intente de nuevo.")
        }
    }
}