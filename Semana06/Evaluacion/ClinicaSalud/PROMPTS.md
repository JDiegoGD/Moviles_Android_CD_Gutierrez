# Prompts Utilizados
---
### Prompt N°1.
###### ROL
Actúa como un desarrollador Android senior experto en Kotlin y Jetpack Compose.

###### CONTEXTO
Estoy desarrollando la app "Clínica Salud+" (reserva de citas médicas) en Kotlin con
Jetpack Compose y Navigation Compose. El paquete base es com.gutierrez.clinicasalud.

En el archivo app/src/main/java/com/gutierrez/clinicasalud/datos/DatosClinica.kt tengo:
- data class Medico(id, nombre, especialidad, calificacion, resenas, experiencia, descripcion)
- object DatosClinica con:
    - val especialidades = listOf("Todos", "Cardiología", "Pediatría", "Dermatología")
    - val medicos: List<Medico> con 5 médicos de ejemplo
    - las funciones buscarMedico(id) y agendarCita(...)

Estoy trabajando en la rama "mejora-ia" y quiero agregar un BUSCADOR DE MÉDICOS.
Este es el primer paso: solo la lógica de datos.

###### TAREA
Modifica directamente el archivo DatosClinica.kt y agrega dentro del object DatosClinica:

1. Una función privada normalizar(texto: String): String que:
    - quite las tildes usando java.text.Normalizer con Normalizer.Form.NFD
      y elimine las marcas diacríticas con el regex "\\p{Mn}+"
    - convierta el texto a minúsculas
    - elimine los espacios al inicio y al final

2. Una función pública buscarMedicos(texto: String, especialidad: String): List<Medico>
   que devuelva los médicos que cumplan AMBAS condiciones:
    - coinciden con la especialidad (si especialidad es "Todos", no se filtra por especialidad)
    - el texto normalizado está contenido en el nombre o en la especialidad normalizados
      del médico (si el texto está vacío, no se filtra por texto)

###### REQUISITOS
- Agrega el import java.text.Normalizer al inicio del archivo.
- Ubica las dos funciones después de la función agendarCita.
- Agrega comentarios cortos en español que expliquen cada función.

###### RESTRICCIONES
- No modifiques, renombres ni elimines nada de lo que ya existe en el archivo.
- No modifiques ningún otro archivo del proyecto (ni pantallas ni navegación).
- No agregues dependencias nuevas.

###### CRITERIOS DE ACEPTACIÓN
- buscarMedicos("rosa diaz", "Todos") devuelve a "Dra. Rosa Díaz".
- buscarMedicos("", "Pediatría") devuelve solo los médicos de Pediatría.
- buscarMedicos("cardio", "Todos") devuelve los médicos de Cardiología.
- buscarMedicos("", "Todos") devuelve todos los médicos.
- El proyecto compila sin errores.

###### FORMATO DE SALIDA
Aplica los cambios directamente en DatosClinica.kt y, al final, dame un resumen breve
en español de qué agregaste y dónde.