## PROMPT N°1:

````
Actúa como desarrollador Android especializado en Kotlin, Jetpack Compose y Material 3, con experiencia en arquitectura MVVM + Repository.

Estoy desarrollando CentroTareas para un laboratorio de corrutinas.
El proyecto está en Semana14 y usa el paquete com.gutierrez.centrotareas.
La fase 1 está implementada y ahora trabajo en la rama mejora-ia.

La aplicación tiene tres tarjetas:
1. Tarea con progreso y cancelación.
2. Conteo de números primos con cancelación.
3. Cargas simuladas secuenciales y paralelas, con error y reintento.

Adjunto una imagen como referencia del diseño que quiero implementar.
Rediseñar la aplicación siguiendo el estilo de la imagen, sin cambiar la lógica ni el comportamiento implementado en la fase 1.

1. Revisa PantallaTareas.kt y los archivos de ui/theme antes de editar.
2. Utiliza Jetpack Compose y componentes de Material 3.
3. Aplica estas características visuales:
   - Barra superior índigo oscuro con el título CentroTareas.
   - Fondo claro con un matiz gris azulado.
   - Botón Tócame centrado debajo de la barra superior.
   - Tres tarjetas en el mismo orden que la imagen.
   - Tarjetas claras con bordes índigo finos, esquinas redondeadas
     y sombras discretas.
   - Color principal aproximado #3A47A1.
   - Botones principales rellenos y secundarios con borde.
   - Campos de texto con borde índigo.
   - Resultados sobre fondos lavanda claros.
   - Espaciados consistentes y desplazamiento vertical.
4. Conserva los mensajes, porcentajes, resultados y estados actuales.
5. Mantén la habilitación y deshabilitación de los botones según el estado de cada tarea.
6. Mantén una etiqueta visible “Simular error” junto al interruptor.
7. Distingue los errores mediante los colores semánticos de Material 3.
8. Respeta las barras del sistema y permite usar la interfaz con el teclado abierto y en pantallas pequeñas.
9. Usa recursos y componentes existentes siempre que sea posible.

Restricciones:
- Modifica únicamente archivos de Semana14.
- No cambies el Repository, el ViewModel ni los modelos de estado.
- No implementes todavía mejoras funcionales.
- No uses XML para construir la interfaz.
- No agregues dependencias sin explicar primero su necesidad.
- No muestres “#3A47A1” como texto: es una referencia de color.
- No reproduzcas la barra de estado de iOS que aparece en la imagen.
- No uses emojis como sustitutos de iconos.
- No elimines funciones ni controles existentes.
- No realices commits ni cambies de rama.

1. Resume los cambios visuales que realizarás.
2. Implementa el rediseño.
3. Enumera los archivos modificados y explica brevemente los cambios.
4. Indica cómo verificar que las funciones anteriores siguen trabajando.
5. Distingue las comprobaciones ejecutadas de las pendientes.

````
| Prompt que escribí                                                                                                             | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|--------------------------------------------------------------------------------------------------------------------------------|---|---|---|
| Solicité rediseñar CentroTareas siguiendo la imagen de referencia, usando Material 3 y conservando las funciones de la fase 1. | Actualizó los colores, el tema y la pantalla con tarjetas redondeadas, botones, contador centrado y resultados sobre fondo. | Acepté la propuesta visual sin realizar correcciones adicionales. | El diseño mantiene la organización de las tres tareas y presenta con claridad los controles y resultados.|

## PROMPT N°2
````
Actúa como desarrollador Android especializado en Kotlin, corrutinas,
Flow y arquitectura MVVM + Repository.

Actualmente, la tarea con progreso se simula dentro del ViewModel.
Tiene un Job para cancelarla, un StateFlow de progreso, un estado
corriendo y mensajes como Trabajando..., Terminó y Cancelado.

Mover la simulación de la tarea con progreso al Repository y exponer
su avance mediante Flow, conservando su comportamiento y apariencia.

1. Revisa TareasRepository.kt, TareasRepositoryLocal.kt,
   TareasViewModel.kt y los usos del progreso en PantallaTareas.kt.
2. Agrega a TareasRepository una función:
   fun observarProgreso(): Flow<Float>
3. Implementa observarProgreso() en TareasRepositoryLocal utilizando:
   - flow { ... } para producir los valores.
   - Una emisión inicial de 0f.
   - Un ciclo de 100 pasos con delay(40).
   - Una emisión de i / 100f después de cada espera.
   - flowOn(Dispatchers.Default) para ejecutar la producción del flujo.

4. En el ViewModel, reemplaza la simulación existente por la recolección
   del Flow dentro de viewModelScope.launch. Actualiza _progreso con
   cada valor recibido y elimina el ciclo duplicado del ViewModel.

Conservar:
   - El Job trabajo.
   - La protección contra inicios simultáneos.
   - El reinicio del progreso al comenzar.
   - El mensaje Trabajando... durante la ejecución.
   - El mensaje Terminó solo cuando el flujo finalice normalmente.
   - La función cancelar(), que detiene el Job y muestra Cancelado.
   - El porcentaje alcanzado al cancelar.
   - El restablecimiento de corriendo en finally.

6. Mantén el Repository independiente de la pantalla:
   debe emitir Float, sin conocer Composables ni estados de UI.
7. No captures CancellationException innecesariamente.
   Si la capturas, vuelve a lanzarla.
8. Revisa los logs HILO existentes. No conserves un mensaje que diga
   estar dentro de Dispatchers.Default si ahora se ejecuta en el
   colector del hilo principal. Explica dónde se ejecutan el productor
   del Flow y su recolección.

Restricciones:
- Modifica únicamente archivos de Semana14.
- Conserva el diseño actual y el nombre ficticio que personalicé.
- No cambies el conteo de primos ni las cargas de perfil y pedidos.
- No agregues dependencias si las actuales ya permiten utilizar Flow.
- No uses GlobalScope, runBlocking ni Thread.sleep.
- No crees un CoroutineScope independiente dentro del Repository.
- No realices commits ni cambies de rama.

1. Resume la solución.
2. Implementa los cambios.
3. Enumera los archivos modificados y explica su responsabilidad.
4. Explica cómo se propaga la cancelación del Job hacia el Flow.
5. Indica cómo probar inicio, progreso, cancelación y reinicio.
6. Separa las comprobaciones ejecutadas de las pendientes.

````

| Prompt que escribí                                                                                                                                                             | Qué respondió Gemini                                                                                                                                                 | Qué cambié o descarté                                                                       | Por qué                                                                                                                                                                         |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Solicité mover la tarea con progreso al Repository mediante Flow<Float>, utilizando flowOn(Dispatchers.Default) y conservando el diseño, los estados y la cancelación con Job. | Agregó observarProgreso() al contrato y su implementación con emisiones de 0 a 100 %. Reemplazó el ciclo del ViewModel por collect. | No he realizado correcciones adicionales. La captura muestra la tarea finalizada al 100 %.  | La propuesta separa la generación del progreso de la administración del estado de pantalla. El Repository produce los valores y el ViewModel los recolecta y coordina la tarea. |