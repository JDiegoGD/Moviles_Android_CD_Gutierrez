## PROMPT N°1
```
Actúa como desarrollador Android especializado en Kotlin, Jetpack Compose,
StateFlow y arquitectura MVVM + Repository.

Estoy trabajando en CampusGPS, dentro de Semana13, paquete
com.gutierrez.campusgps y rama mejora-ia.

La fase 1 está terminada. Actualmente:
- LugaresRepository proporciona los lugares del campus.
- MapaViewModel obtiene la ubicación, calcula distancias y ordena lugares.
- La pantalla muestra un mapa y una lista de lugares.
- Se puede seleccionar un lugar y se identifica el más cercano.
- Existen estados de búsqueda y errores relacionados con la ubicación.

Objetivo
Permitir buscar lugares por nombre y filtrarlos por categoría, sin perder
las distancias, la selección ni las funciones de ubicación existentes.

Pasos:
1. Revisa los modelos, MapaViewModel, PantallaMapa, ListaLugares y
   MapaUbicacion antes de modificar el código.

2. En el ViewModel agrega:
   - Estado privado para el texto de búsqueda.
   - Estado privado para la categoría seleccionada.
   - Exposición de ambos estados mediante StateFlow de solo lectura.
   - Funciones para actualizar la consulta y seleccionar una categoría.

3. Obtén las categorías desde los lugares existentes, sin mantener una
   segunda lista fija que pueda quedar desactualizada.
   Incluye la opción “Todas”.

4. Expón una lista filtrada mediante combine y stateIn:
   - Filtra por nombre sin distinguir mayúsculas y minúsculas.
   - Ignora espacios al inicio y al final de la consulta.
   - Permite buscar sin distinguir tildes, por ejemplo:
     “cafeteria” debe encontrar “Cafetería”.
   - Aplica conjuntamente la consulta y la categoría.
   - Conserva el orden por distancia cuando esta se conozca.
   - Conserva todos los datos originales sin modificar la lista fuente.

5. Mantén la lógica de filtrado fuera de los Composables.
   Si necesitas normalizar texto, utiliza una función pura y reutilizable.

6. En la pantalla agrega:
   - Un OutlinedTextField con la etiqueta “Buscar lugar”.
   - Una acción para borrar la consulta.
   - FilterChip de Material 3 para las categorías.
   - Desplazamiento horizontal de categorías si no caben.
   - Un mensaje “No se encontraron lugares” cuando no haya coincidencias.

7. Utiliza la misma lista filtrada para la lista y los marcadores de lugares.
   No ocultes el marcador de la ubicación del usuario.
   Si el lugar seleccionado deja de ser visible, limpia su selección.
   La indicación de lugar más cercano debe corresponder a los lugares
   visibles y no mostrar un lugar oculto por los filtros.

8. La búsqueda y las categorías deben funcionar aunque todavía no se haya
   obtenido la ubicación. En ese caso, conserva las distancias desconocidas
   sin inventar valores ni pedir permisos para filtrar.

9. Conserva el texto de búsqueda y la categoría al rotar la pantalla
   mediante el ViewModel. No agregues persistencia para esta mejora.

10. Mantén el diseño y la navegación existentes.
    No reinicies la cámara del mapa ni vuelvas a solicitar ubicación
    por cada cambio en el campo de búsqueda.

Restricciones:
- Modifica únicamente archivos de Semana13.
- No modifiques CentroTareas ni proyectos de otras semanas.
- El ViewModel no debe recibir Context, Activity, NavController
  ni referencias a Composables.
- Mantén el acceso a servicios de ubicación dentro del Repository.
- No cambies las coordenadas ni los datos originales de los lugares.
- No implementes todavía favoritos, persistencia o timeout.
- No uses GlobalScope, runBlocking ni Thread.sleep.
- No leas, muestres ni modifiques claves de API o archivos que las contienen.
- No agregues dependencias innecesarias.
- No realices commits ni cambies de rama.

Formato de respuesta:
1. Resume la solución.
2. Implementa los cambios.
3. Enumera los archivos modificados y su responsabilidad.
4. Explica cómo combinas búsqueda, categoría y distancias.
5. Explica cómo mantienes coherentes lista, marcadores y selección.
6. Separa las comprobaciones ejecutadas de las pendientes.

Criterios de aceptacion:
- Buscar “biblioteca” encuentra Biblioteca.
- Buscar “cafeteria” encuentra Cafetería.
- Seleccionar una categoría muestra únicamente sus lugares.
- Consulta y categoría funcionan simultáneamente.
- Borrar la consulta no borra la categoría seleccionada.
- Una consulta sin coincidencias muestra el mensaje de lista vacía.
- Filtrar funciona sin permiso de ubicación.
- Al obtener ubicación, los resultados conservan el orden por distancia.
- Rotar conserva la consulta y la categoría.
```
| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Solicité buscar lugares por nombre sin distinguir mayúsculas ni tildes y filtrar por categoría desde el ViewModel, manteniendo coherentes lista, marcadores y selección. | Agregó la normalización de texto, estados combinados con combine y stateIn, un buscador y categorías con FilterChip. Reportó una compilación exitosa. | No he realizado correcciones adicionales. En el dispositivo observé el filtro Servicios con dos estacionamientos ordenados por distancia y el más cercano identificado. | Centralizar los filtros en el ViewModel permite reutilizar la misma lista para la pantalla y el mapa, conservando las distancias sin modificar los datos originales. |

---
## PROMPT N°3
```
Actúa como desarrollador Android especializado en Kotlin, DataStore,
StateFlow, Jetpack Compose y arquitectura MVVM + Repository.

Trabajo en CampusGPS, dentro de Semana13, paquete
com.gutierrez.campusgps y rama mejora-ia.

La aplicación ya obtiene ubicación, muestra lugares y distancias,
permite seleccionar un lugar y buscar por nombre y categoría.
La búsqueda y los filtros están coordinados desde MapaViewModel.

Objetivo:
Permitir marcar y desmarcar lugares favoritos, guardar esa selección
con Preferences DataStore y recuperarla al volver a abrir la aplicación.

Pasos:
1. Revisa la estructura actual y la creación del ViewModel antes de editar.

2. Crea una interfaz FavoritosRepository que exponga:
   - Un Flow con el conjunto de IDs favoritos.
   - Una función suspendida para alternar un favorito.

3. Implementa la persistencia con Preferences DataStore:
   - Guarda únicamente IDs estables de lugares.
   - No guardes coordenadas del usuario ni distancias calculadas.
   - Usa una sola instancia de DataStore por archivo.
   - Alterna los IDs mediante una actualización atómica con edit.
   - Mantén Context únicamente en la implementación de datos,
     utilizando applicationContext.
   - Maneja IOException de lectura sin ocultar otros errores.
   - No ignores errores de escritura ni CancellationException.

4. Inyecta FavoritosRepository mediante la Factory existente.
   El ViewModel debe depender de la interfaz, sin recibir Context.

5. En MapaViewModel:
   - Expón los favoritos como StateFlow de solo lectura.
   - Agrega una función para alternar un favorito con viewModelScope.
   - Agrega el estado “Solo favoritos”, inicialmente desactivado.
   - Combina favoritos, búsqueda y categoría en los resultados visibles.
   - Conserva el orden por distancia.
   - Si falla una escritura, muestra un mensaje específico sin reemplazar
     el estado de ubicación ni fingir que se guardó correctamente.

6. En cada elemento de la lista agrega un IconToggleButton de estrella:
   - Rellena si es favorito y contorno si no lo es.
   - Incluye contentDescription descriptiva.
   - Pulsarlo no debe seleccionar accidentalmente el lugar ni mover
     la cámara del mapa.
   - Utiliza iconos disponibles; justifica cualquier dependencia nueva.

7. Agrega un FilterChip “Solo favoritos”, visualmente independiente
   de las categorías. Debe combinarse con el buscador y la categoría
   seleccionada, sin borrar ninguno de esos filtros.

8. Mantén coherentes la lista y los marcadores de lugares:
   - Ambos deben recibir los mismos resultados filtrados.
   - Conserva el marcador de ubicación del usuario.
   - Actualiza el lugar más cercano según los resultados visibles.
   - Limpia la selección si el lugar seleccionado deja de ser visible.

9. Muestra un mensaje adecuado cuando no haya favoritos o cuando
   los filtros no produzcan coincidencias.

10. Conserva el diseño Material 3 y las funciones existentes.
    La lectura y escritura de favoritos no deben solicitar ubicación,
    permisos nuevos ni conexión a internet.

Restricciones:
- Modifica únicamente archivos de Semana13.
- No cambies los IDs, coordenadas ni datos originales de los lugares.
- No coloques lógica de persistencia o filtrado en Composables.
- No uses GlobalScope, runBlocking ni Thread.sleep.
- No agregues permisos, servicios de ubicación en segundo plano ni Firebase.
- No leas, muestres ni modifiques claves de API.
- Agrega solo la dependencia de DataStore necesaria, siguiendo el catálogo
  y las convenciones existentes del proyecto.
- No implementes todavía timeout o cancelación de ubicación.
- No realices commits ni cambies de rama.

Formato de Respuesta:
1. Resume la solución.
2. Implementa los cambios.
3. Enumera los archivos modificados y su responsabilidad.
4. Explica cómo se guardan los favoritos y se inyecta el Repository.
5. Explica cómo se combinan los filtros y se manejan los errores.
6. Separa las comprobaciones ejecutadas de las pendientes.

Criterios de Aceptación:
- Marcar un lugar cambia su estrella.
- Desmarcarlo elimina su condición de favorito.
- Al cerrar y volver a abrir la aplicación se recuperan los favoritos.
- Solo favoritos funciona junto con consulta y categoría.
- Quitar un favorito con ese filtro activo actualiza lista y marcadores.
- Rotar no pierde los favoritos.
- Se pueden gestionar favoritos sin permiso de ubicación.
- Pulsar una estrella no mueve la cámara ni cambia la selección.
```
| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué                                                                                                                                                                                       |
|---|---|---|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Solicité guardar los lugares favoritos con Preferences DataStore y agregar estrellas y un filtro Solo favoritos, conservando la búsqueda, las categorías y las distancias. | Creó el Repository de favoritos, lo inyectó mediante la Factory y combinó sus datos con los filtros del ViewModel. Agregó estrellas y mensajes de error, y reportó una compilación exitosa. | No he realizado correcciones adicionales. En el dispositivo observé Cafetería marcada y Comedor sin marcar. Queda pendiente comprobar la persistencia y los filtros combinados. | Guardar IDs estables permite recuperar los favoritos sin almacenar la ubicación del usuario. La interfaz del Repository mantiene la persistencia separada del ViewModel y de los Composables. |