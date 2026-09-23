# Prompts Utilizados
---
## Prompt 1 — Lógica de control de cupos

**Qué le pedí:**
> Un prompt estructurado para que Gemini en modo Agente modificara DatosGym.kt y
> agregara la lógica de control de cupos: descontar cupos al reservar, bloquear
> clases llenas y evitar reservas duplicadas.

**Qué respondió:**
- Creó el mapa observable cupos con mutableStateMapOf, inicializado con los cupos
  disponibles de cada clase.
- Creó las funciones cuposRestantes(claseId), estaLlena(claseId) y
  yaReservada(clase, hora).
- Modificó reservar(clase, hora) para que devuelva Boolean: descuenta un cupo y
  agrega la reserva solo si la clase no está llena y no fue reservada antes en esa hora.
- Cambió los cupos de Boxeo a 1 para poder probar el caso de clase llena.

**Qué tuve que corregir**:
- Nada, funcionó a la primera.

#### ESTRUCTURA DEL PROMPT
###### ROL
Actúa como un desarrollador Android senior experto en Kotlin y Jetpack Compose.

###### CONTEXTO
Estoy desarrollando la app "TECSUP Fit" (reserva de clases de gimnasio) en Kotlin con
Jetpack Compose y Navigation Compose. El paquete base es com.gutierrez.tecsupfit.

En el archivo app/src/main/java/com/gutierrez/tecsupfit/datos/DatosGym.kt tengo:
- data class Clase(id, nombre, dia, horarios, sala, duracion, cuposDisponibles,
  cuposTotales, descripcion)
- enum class EstadoReserva { CONFIRMADA, COMPLETADA }
- data class Reserva(clase, dia, hora, estado)
- object DatosGym con:
    - val clases: List<Clase> con 6 clases de ejemplo
    - val reservas = mutableStateListOf(...)
    - las funciones buscarClase(id), filtrarClases(filtro) y
      reservar(clase: Clase, hora: String) que agrega una Reserva CONFIRMADA

Problema actual: al reservar, los cupos no cambian, se puede reservar una clase llena
y se puede reservar dos veces la misma clase en el mismo horario.

Estoy en la rama "mejora-ia" y quiero agregar un CONTROL DE CUPOS.
Este es el primer paso: solo la lógica de datos.

###### TAREA
Modifica directamente DatosGym.kt y, dentro del object DatosGym:

1. Crea un estado observable con los cupos restantes de cada clase:
   val cupos = mutableStateMapOf<Int, Int>() inicializado con el id y los
   cuposDisponibles de cada clase. Debe declararse DESPUÉS de la lista clases.
2. Crea la función cuposRestantes(claseId: Int): Int que devuelva los cupos
   restantes de esa clase (0 si no existe).
3. Crea la función estaLlena(claseId: Int): Boolean que devuelva true si no quedan
   cupos.
4. Crea la función yaReservada(clase: Clase, hora: String): Boolean que devuelva true
   si ya existe una reserva CONFIRMADA con el mismo nombre de clase y la misma hora.
5. Modifica la función reservar(clase: Clase, hora: String) para que devuelva Boolean:
    - si la clase está llena o ya fue reservada en esa hora, no hace nada y devuelve false
    - si no, descuenta 1 cupo en el mapa cupos, agrega la Reserva CONFIRMADA
      (como ya lo hace) y devuelve true
6. Para poder probar el caso de clase llena, cambia los cuposDisponibles de la clase
   "Boxeo" a 1.

###### REQUISITOS
- Agrega el import androidx.compose.runtime.mutableStateMapOf.
- Agrega comentarios cortos en español que expliquen cada función nueva.

###### RESTRICCIONES
- No elimines ni renombres nada de lo que ya existe (solo cambia el tipo de retorno
  de reservar y los cupos de Boxeo).
- No modifiques ningún otro archivo del proyecto (ni pantallas ni navegación).
- No agregues dependencias nuevas.

###### CRITERIOS DE ACEPTACIÓN
- cuposRestantes(2) devuelve 8 al iniciar la app (Cross Training).
- Después de reservar Cross Training a las 6:00 pm, cuposRestantes(2) devuelve 7.
- Reservar otra vez Cross Training a las 6:00 pm devuelve false y no cambia los cupos.
- Reservar Boxeo una vez devuelve true; la segunda vez (otro horario) devuelve false
  porque estaLlena(5) es true.
- El proyecto compila sin errores.

###### FORMATO DE SALIDA
Aplica los cambios directamente en DatosGym.kt y, al final, dame un resumen breve
en español de qué agregaste y dónde.