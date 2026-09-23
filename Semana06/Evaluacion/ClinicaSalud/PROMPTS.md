# Prompts Utilizados
---
## Prompt 1 — Elegir la mejora y crear la lógica de búsqueda

**Qué le pedí:**
> "Ahora parte 2" (adjunté el enunciado de la Fase 2: crear la rama mejora-ia a partir
> de main y agregar una mejora funcional relevante con ayuda de IA).

**Qué respondió:**
- Primero propuso "cancelar una cita con un AlertDialog de confirmación", que es el
  ejemplo del enunciado.
- Después de mi corrección, me dio 4 opciones (buscador de médicos, horarios ocupados,
  motivo de consulta y médicos favoritos). Elegí el buscador de médicos.
- Generó la función buscarMedicos(texto, especialidad) en DatosClinica, que filtra
  los médicos combinando el texto escrito con el chip de especialidad seleccionado.
- Generó la función normalizar() con Normalizer para ignorar tildes y mayúsculas
  (por ejemplo, "rosa diaz" encuentra a "Dra. Rosa Díaz").

**Qué tuve que corregir**:
- Le indiqué "debe ser una nueva mejora", porque no quería copiar el ejemplo del
  enunciado.
- Al pegar el código, Normalizer aparecía en rojo (*Unresolved reference*) porque
  faltaba el import. Lo corregí agregando import java.text.Normalizer al inicio de
  DatosClinica.kt.

#### ESTRUCTURA DEL PROMPT
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

---

## Prompt 2 — Barra de búsqueda en la pantalla de Inicio

**Qué le pedí:**
> Un prompt estructurado para que Gemini en modo Agente modificara directamente
> HomeScreen.kt y agregara una barra de búsqueda que use la función buscarMedicos()
> creada en el paso anterior.

**Qué respondió:**
- Agregó el estado textoBusqueda con remember { mutableStateOf("") }.
- Agregó un OutlinedTextField arriba de los chips de especialidad, con el ícono de
  lupa (Icons.Filled.Search) y el placeholder "Buscar médico o especialidad".
- Agregó un botón X (Icons.Filled.Close) que limpia el texto y que solo aparece
  cuando hay algo escrito.
- Reemplazó el filtro de la lista por DatosClinica.buscarMedicos(textoBusqueda,
  especialidadSeleccionada), para que la búsqueda funcione junto con los chips.

#### ESTRUCTURA DEL PROMPT
###### ROL
Actúa como un desarrollador Android senior experto en Kotlin, Jetpack Compose y Material 3.

###### CONTEXTO
Estoy desarrollando la app "Clínica Salud+" (reserva de citas médicas) en Kotlin con
Jetpack Compose. El paquete base es com.gutierrez.clinicasalud. Estoy en la rama
"mejora-ia" agregando un BUSCADOR DE MÉDICOS.

En el paso anterior agregué en datos/DatosClinica.kt la función:
- DatosClinica.buscarMedicos(texto: String, especialidad: String): List<Medico>
  que filtra los médicos por texto (nombre o especialidad, sin importar tildes ni
  mayúsculas) y por especialidad ("Todos" = sin filtro).

El archivo app/src/main/java/com/gutierrez/clinicasalud/screens/HomeScreen.kt tiene:
- HomeScreen(navController: NavController, onMenuClick: () -> Unit)
- Un Scaffold con TopAppBar morada ("Clínica Salud+", "Hola, Juan" e ícono de menú)
- Un estado: var especialidadSeleccionada by remember { mutableStateOf("Todos") }
- Una variable medicosFiltrados que hoy filtra DatosClinica.medicos solo por especialidad
- Un LazyRow con FilterChip por especialidad
- Un Text "Médicos disponibles"
- Un LazyColumn que muestra medicosFiltrados con MedicoCard y navega al detalle
  con Screen.Detail.createRoute(medico.id)

###### TAREA
Modifica directamente HomeScreen.kt para agregar una barra de búsqueda:

1. Agrega un estado: var textoBusqueda by remember { mutableStateOf("") }
2. Agrega un OutlinedTextField ARRIBA del LazyRow de especialidades, dentro del
   Column que ya recibe el padding del Scaffold, con:
    - placeholder: "Buscar médico o especialidad"
    - leadingIcon: Icons.Filled.Search
    - trailingIcon: un IconButton con Icons.Filled.Close que limpie el texto
      (textoBusqueda = ""), visible SOLO cuando el texto no esté vacío
    - singleLine = true
    - shape = RoundedCornerShape(12.dp)
    - modifier: fillMaxWidth() con padding horizontal de 16.dp y superior de 12.dp
3. Reemplaza el cálculo de medicosFiltrados por:
   DatosClinica.buscarMedicos(textoBusqueda, especialidadSeleccionada)

###### REQUISITOS
- Agrega los imports necesarios (Icons.Filled.Search, Icons.Filled.Close,
  RoundedCornerShape, OutlinedTextField).
- Mantén los colores, la TopAppBar, los chips, las tarjetas y la navegación tal como están.
- Agrega comentarios cortos en español en las partes nuevas.

###### RESTRICCIONES
- No modifiques ningún otro archivo del proyecto.
- No elimines la función MedicoCard ni cambies su diseño.
- Todavía NO agregues mensaje de "sin resultados", contador de resultados ni manejo
  del teclado (eso será otro paso).
- No agregues dependencias nuevas.

###### CRITERIOS DE ACEPTACIÓN
- Al escribir "ana" aparece solo "Dra. Ana Torres".
- Al escribir "rosa diaz" (sin tilde) aparece "Dra. Rosa Díaz".
- Al escribir "pedia" aparecen los médicos de Pediatría.
- Con el chip "Cardiología" seleccionado y el texto "carlos", aparece solo "Dr. Carlos Ríos".
- La X aparece solo cuando hay texto y, al tocarla, se limpia la búsqueda.
- Tocar un médico sigue navegando a su perfil.
- El proyecto compila sin errores.

###### FORMATO DE SALIDA
Aplica los cambios directamente en HomeScreen.kt y, al final, dame un resumen breve
en español de qué agregaste y dónde.

---

## Prompt 3 — Mensaje sin resultados, contador y manejo del teclado

**Qué le pedí:**
> Un prompt estructurado para que Gemini en modo Agente terminara el buscador en
> HomeScreen.kt: mostrar un mensaje cuando no haya resultados, un contador de médicos
> encontrados y cerrar el teclado al presionar el botón de búsqueda.

**Qué respondió:**
- Agregó al lado de "Médicos disponibles" un contador con la cantidad de médicos
  encontrados (por ejemplo, "3 encontrados").
- Cuando la búsqueda no tiene resultados, en lugar de la lista vacía muestra un ícono
  de lupa, el mensaje "No se encontraron médicos", el texto "Intenta con otro nombre o
  especialidad" y un botón "Limpiar filtros" que reinicia el texto y el chip a "Todos".
- Configuró el OutlinedTextField con ImeAction.Search para que el teclado muestre el
  botón de búsqueda y, al presionarlo, se cierre con LocalFocusManager.clearFocus().

**Qué tuve que corregir**:
- Nada, funcionó a la primera.

#### ESTRUCTURA DEL PROMPT
###### ROL
Actúa como un desarrollador Android senior experto en Kotlin, Jetpack Compose y Material 3,
con buen criterio de experiencia de usuario (UX).

###### CONTEXTO
Estoy desarrollando la app "Clínica Salud+" (reserva de citas médicas) en Kotlin con
Jetpack Compose. El paquete base es com.gutierrez.clinicasalud. Estoy en la rama
"mejora-ia" terminando un BUSCADOR DE MÉDICOS.

En los pasos anteriores ya hice lo siguiente:
- En datos/DatosClinica.kt: la función buscarMedicos(texto, especialidad), que filtra
  por texto (sin tildes ni mayúsculas) y por especialidad.
- En screens/HomeScreen.kt:
    - los estados textoBusqueda y especialidadSeleccionada
    - un OutlinedTextField con ícono de lupa y botón X para limpiar
    - un LazyRow con FilterChip de especialidades
    - un Text "Médicos disponibles"
    - un LazyColumn que muestra
      medicosFiltrados = DatosClinica.buscarMedicos(textoBusqueda, especialidadSeleccionada)

Problema actual: si la búsqueda no encuentra nada, la pantalla queda vacía sin
explicación, no se sabe cuántos resultados hay y el teclado no se cierra al buscar.

###### TAREA
Modifica directamente HomeScreen.kt para:

1. Contador de resultados: convierte el Text "Médicos disponibles" en un Row que tenga
   a la izquierda "Médicos disponibles" (igual que ahora) y a la derecha el texto
   "${medicosFiltrados.size} encontrados" con estilo bodySmall y color onSurfaceVariant.

2. Estado sin resultados: si medicosFiltrados está vacío, en lugar del LazyColumn
   muestra un Column centrado (fillMaxSize, padding 32.dp) con:
    - Icons.Filled.Search de 64.dp en color onSurfaceVariant
    - el texto "No se encontraron médicos" en titleMedium y negrita
    - el texto "Intenta con otro nombre o especialidad" en bodyMedium y onSurfaceVariant
    - un TextButton "Limpiar filtros" que ponga textoBusqueda = "" y
      especialidadSeleccionada = "Todos"

3. Manejo del teclado en el OutlinedTextField:
    - keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    - keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
    - obtén focusManager con LocalFocusManager.current

###### REQUISITOS
- Agrega los imports necesarios (KeyboardOptions, KeyboardActions, ImeAction,
  LocalFocusManager, TextButton).
- Mantén la barra de búsqueda, los chips, las tarjetas, los colores y la navegación
  tal como están.
- Agrega comentarios cortos en español en las partes nuevas.

###### RESTRICCIONES
- No modifiques ningún otro archivo del proyecto.
- No cambies la lógica de buscarMedicos ni el diseño de MedicoCard.
- No agregues dependencias nuevas.

###### CRITERIOS DE ACEPTACIÓN
- Sin filtros, el contador muestra "5 encontrados".
- Con el chip "Pediatría", el contador muestra "2 encontrados".
- Al escribir "xyz" aparece el mensaje "No se encontraron médicos" en lugar de la lista.
- El botón "Limpiar filtros" borra el texto, vuelve el chip a "Todos" y se muestran los
  5 médicos.
- El teclado muestra el botón de búsqueda (lupa) y, al tocarlo, el teclado se cierra.
- Tocar un médico sigue navegando a su perfil.
- El proyecto compila sin errores.

###### FORMATO DE SALIDA
Aplica los cambios directamente en HomeScreen.kt y, al final, dame un resumen breve
en español de qué agregaste y dónde.