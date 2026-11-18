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

## PROMPT N°3
```
Actúa como desarrollador Android especializado en Kotlin, Flow, corrutinas y arquitectura MVVM + Repository.

La aplicación ya tiene:
- Diseño Material 3 con paleta índigo y resultados lavanda.
- Tarea con progreso producido desde el Repository mediante Flow.
- Conteo de primos en Dispatchers.Default con cancelación cooperativa.
- Cargas secuenciales y paralelas con manejo de errores y reintento.

Objetivo:
Agregar progreso real al cálculo de primos, conservando el resultado,
la medición de tiempo, la cancelación y el diseño existente.

1. Revisa el Repository, el ViewModel, los modelos y la tarjeta de primos antes de modificar sus contratos.
2. Adapta contarPrimos para devolver un Flow con el avance del cálculo.
   Crea un modelo de datos independiente de la UI que incluya:
   - Progreso como Float entre 0f y 1f.
   - Cantidad acumulada de primos.
3. Mantén una sola implementación del algoritmo. Actualiza todos los
   consumidores afectados por el cambio de contrato.
4. Produce el flujo con flow y flowOn(Dispatchers.Default).
   Conserva ensureActive() dentro del recorrido y la comprobación de
   divisores hasta la raíz cuadrada, evitando desbordamientos.
5. Calcula el avance según los candidatos revisados desde 2 hasta N:
   candidatos revisados / total de candidatos.
   Contempla correctamente N = 2.
   No utilices la cantidad de primos como porcentaje de avance.
6. Emite un estado inicial con progreso 0f.
   Limita las emisiones intermedias a cambios de porcentaje entero
   y garantiza una emisión final con progreso 1f y el conteo completo.
   No agregues delay ni otras esperas artificiales.
7. En el ViewModel:
   - Recolecta el Flow mediante viewModelScope.
   - Expón el progreso como StateFlow de solo lectura.
   - Conserva el Job independiente del cálculo.
   - Evita iniciar dos cálculos simultáneos.
   - Reinicia el avance al comenzar.
   - Publica el resultado final únicamente al completar normalmente
     la recolección.
   - Conserva la medición de tiempo.
   - Al cancelar, muestra el estado Cancelado y relanza
     CancellationException si la capturas.
   - Conserva el porcentaje alcanzado al cancelar.

8. En la tarjeta de primos:
   - Muestra LinearProgressIndicator y el porcentaje durante el cálculo.
   - Conserva el avance visible al cancelar.
   - Mantén el campo numérico, su validación, Contar y Cancelar.
   - Conserva el resultado final con cantidad y milisegundos.
   - Mantén los colores, bordes, espaciados y estilo actuales.

Restricciones:
- Modifica únicamente archivos de Semana14.
- No cambies la tarea de progreso ya implementada ni las cargas.
- Conserva el nombre ficticio personalizado.
- El Repository no debe depender de PrimosUi ni de Composables.
- No uses GlobalScope, runBlocking ni Thread.sleep.
- No agregues dependencias innecesarias.
- No realices commits ni cambies de rama.

1. Resume la solución.
2. Implementa los cambios.
3. Enumera los archivos modificados.
4. Explica cómo calcular el porcentaje y limitar las emisiones.
5. Explica cómo se conserva la cancelación cooperativa.
6. Indica qué pruebas ejecutaste y cuáles quedan pendientes.

Resultados de preferencia:
N = 2 → 1 primo.
N = 100 → 25 primos.
N = 1000 → 168 primos.
N = 10000 → 1229 primos.
N = 500000 → 41538 primos.
```
| Prompt que escribí                                                                                                                                         | Qué respondió Gemini                                                                                                                                                                             | Qué cambié o descarté                                                                                                         | Por qué                                                                                                                                                                                                 |
|------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Solicité agregar progreso real al conteo de primos mediante Flow, calculado según los candidatos revisados, conservando la cancelación y el diseño actual. | Incorporó PrimosProgreso, adaptó el Repository para emitir avances y actualizó el ViewModel y la pantalla. Limitó las emisiones a cambios de porcentaje entero. | No he realizado correcciones adicionales. La propuesta guarda el porcentaje en los estados de primos.| El estado reúne la información necesaria para dibujar la pantalla. Limitar las emisiones reduce actualizaciones innecesarias y conservar el porcentaje permite identificar dónde se canceló el cálculo. |

## PROMPT N°4
```
Actúa como desarrollador Android especializado en Kotlin, StateFlow,
Jetpack Compose y arquitectura MVVM + Repository.

Trabajo en CentroTareas, dentro de Semana14, con el paquete
com.gutierrez.centrotareas y en la rama mejora-ia.

La aplicación ya tiene:
- Diseño Material 3 con paleta índigo y resultados lavanda.
- Progreso de una tarea producido desde el Repository con Flow.
- Conteo de primos con progreso y cancelación.
- Cargas de perfil y pedidos, secuenciales y paralelas.
- Simulación de errores y reintento.

Objetivo:
Acumular los tiempos de las cargas exitosas y mostrarlos en una tabla
para comparar los modos Una por una y A la vez.

Instrucciones:
1. Revisa los modelos, TareasViewModel y la tarjeta Dos cargas
   antes de implementar los cambios.
2. Crea un modelo sencillo para una medición:
   - Modo de carga.
   - Tiempo transcurrido en milisegundos.
   Utiliza nombres coherentes con el proyecto.
3. En el ViewModel, guarda las mediciones en un MutableStateFlow
   privado y expón un StateFlow<List<...>> de solo lectura.
   Actualiza la lista de forma inmutable.
4. Registra exactamente una medición cuando una carga termina
   correctamente, tanto en cargarUnaPorUna() como en cargarALaVez().
   Un reintento exitoso también debe registrar una medición.
5. No registres mediciones al iniciar, al fallar ni al cancelar.
   No agregues registros desde un Composable ni como consecuencia
   de una recomposición.
6. Conserva las mediciones durante la rotación mediante el ViewModel.
   No agregues persistencia: pueden perderse al eliminarse el ViewModel
   o terminar el proceso.
7. Dentro de la tarjeta Dos cargas, debajo del estado actual,
   muestra una sección “Comparación de tiempos” con:
   - Número de ejecución.
   - Modo.
   - Tiempo en ms.
   Las columnas deben mantener una alineación consistente.
8. Si no existen mediciones, muestra:
   “Realiza una carga para comparar los tiempos”.
9. Agrega un botón “Limpiar comparación” y una función del ViewModel
   que vacíe únicamente las mediciones.
   Deshabilita el botón cuando la lista esté vacía o haya una carga activa.
   No reinicies el resultado actual, el interruptor ni las otras tareas.
10. Conserva el desplazamiento vertical de la pantalla.
    No anides una LazyColumn sin altura limitada dentro del verticalScroll.
    Mantén la tabla legible en pantallas pequeñas y con fuentes grandes.
11. Conserva:
    - Los modos secuencial y paralelo.
    - La protección contra ejecuciones duplicadas.
    - Simular error y Reintentar.
    - La propagación de CancellationException.
    - El nombre ficticio personalizado.
    - El diseño índigo y lavanda existente.

Restricciones:
- Modifica únicamente archivos de Semana14.
- No alteres la tarea con progreso ni el cálculo de primos.
- No agregues DataStore, base de datos ni librerías de gráficos.
- No cambies los tiempos simulados del Repository.
- No uses GlobalScope, runBlocking ni Thread.sleep.
- No realices commits ni cambies de rama.

1. Resume la solución.
2. Implementa los cambios.
3. Enumera los archivos modificados.
4. Explica dónde se registra cada medición y cómo evitas duplicados.
5. Explica qué ocurre con la tabla al rotar o cerrar la aplicación.
6. Indica qué comprobaciones ejecutaste y cuáles quedan pendientes.

Criterios de Aceptación:
- Dos cargas secuenciales y dos paralelas generan cuatro filas.
- Las secuenciales se aproximan a 5000 ms y las paralelas a 3000 ms.
- Una carga fallida no agrega filas.
- Un reintento exitoso agrega exactamente una fila.
- Rotar conserva las mediciones.
- Limpiar comparación vacía la tabla sin borrar el resultado actual.
- La pantalla sigue respondiendo durante las cargas.
```
| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Solicité acumular los tiempos de las cargas exitosas y mostrarlos en una tabla, conservando las funciones y el diseño existentes. | Creó MedicionCarga, agregó una lista observable en el ViewModel y una tabla con número, modo y tiempo. Incorporó el botón Limpiar comparación y reportó una compilación exitosa. | No he realizado correcciones adicionales. En el emulador observé tres mediciones con tiempos cercanos a tres segundos en paralelo y cinco segundos en secuencia. | La tabla permite comparar ejecuciones anteriores. Registrar las mediciones en el ViewModel evita que una recomposición de la pantalla agregue filas. |

## PROMPT N°5
```
Actúa como desarrollador Android especializado en Kotlin, corrutinas,
manejo de cancelación y arquitectura MVVM + Repository.

Trabajo en CentroTareas, dentro de Semana14, con el paquete
com.gutierrez.centrotareas y en la rama mejora-ia.

La aplicación ya tiene:
- Diseño Material 3 con paleta índigo y resultados lavanda.
- Tarea con progreso desde el Repository mediante Flow.
- Conteo de primos con progreso y cancelación.
- Cargas secuenciales y paralelas.
- Simulación de errores y reintento manual.
- Tabla de tiempos que registra únicamente cargas exitosas.

Las cargas simuladas duran aproximadamente cinco segundos en modo
secuencial y tres segundos en paralelo.

Objetivo:
Implementar la mejora “Tiempo máximo”: si una carga supera dos segundos,
cancelarla y mostrar “Tardó demasiado”, sin bloquear la pantalla.


1. Revisa el ViewModel, los modelos de estado y la tarjeta Dos cargas
   antes de modificar el código.
2. Agrega en el ViewModel un MutableStateFlow<Boolean> privado,
   inicialmente false, para activar el límite de tiempo.
   Expón su versión de solo lectura y una función para cambiarlo.
3. Agrega a la tarjeta Dos cargas un Switch con la etiqueta visible
   “Limitar a 2 segundos”.
   Conserva el interruptor Simular error como un control independiente.
   Deshabilita ambos interruptores durante una carga.
4. Captura la configuración de los interruptores al iniciar cada operación,
   para que se mantenga estable durante su ejecución.
5. Cuando el límite esté activado, utiliza withTimeout(2000L) alrededor
   de la operación completa:
   - En modo secuencial, debe incluir perfil y pedidos.
   - En modo paralelo, debe incluir coroutineScope, ambos async y await.
   No asignes dos segundos independientes a cada llamada.
6. Cuando el límite esté desactivado, conserva las cargas normales.
   Evita duplicar innecesariamente la lógica de obtención de datos.
   Mantén la orquestación en el ViewModel y las simulaciones en el Repository.
7. Distingue el timeout de esta operación de una cancelación externa:
   - Si vence su límite, publica CargaUi.Error("Tardó demasiado").
   - Si el Job padre fue cancelado, propaga la cancelación.
   - No conviertas otras CancellationException en errores de pantalla.
   Si capturas TimeoutCancellationException, comprueba que el contexto
   exterior siga activo antes de transformarla en un estado de error.
   Ordena las capturas específicas antes de las generales.
8. Al vencer el límite, ninguna carga hija debe continuar trabajando.
   Conserva la concurrencia estructurada y no crees scopes independientes.
9. Publica Exito y registra una medición en la tabla únicamente después
   de que la operación completa haya terminado correctamente.
   Un timeout no debe generar una fila ni mostrar un resultado parcial.
10. Conserva Reintentar: debe volver a ejecutar A la vez con la configuración
    actual de los interruptores.
    Si el límite sigue activado, volverá a vencer.
    Si se desactiva, la carga podrá finalizar normalmente.
11. Mantén los botones de carga deshabilitados mientras el estado sea
    Cargando y permite iniciar otra operación después del timeout.
12. Conserva el estilo visual actual. Presenta “Tardó demasiado”
    en el contenedor de error existente con el botón Reintentar.

Restricciones
- Modifica únicamente archivos de Semana14.
- No cambies los tiempos simulados del Repository.
- No modifiques los flujos de progreso ni el cálculo de primos.
- Conserva el nombre ficticio personalizado.
- No agregues todavía reintentos automáticos ni DataStore.
- No uses GlobalScope, runBlocking ni Thread.sleep.
- No agregues dependencias innecesarias.
- No realices commits ni cambies de rama.

Formato de Respuesta:
1. Resume la solución.
2. Implementa los cambios.
3. Enumera los archivos modificados.
4. Explica qué incluye el límite de dos segundos.
5. Explica cómo distingues timeout y cancelación externa.
6. Separa las comprobaciones ejecutadas de las pendientes.

Criterios de Aceptación:
- Límite desactivado: Una por una tarda aproximadamente cinco segundos
  y A la vez aproximadamente tres; ambas registran su medición.
- Límite activado: cualquiera de los modos muestra “Tardó demasiado”
  después de aproximadamente dos segundos.
- El timeout no añade filas a la tabla.
- Tócame sigue respondiendo durante la espera.
- Desactivar el límite y pulsar Reintentar permite obtener el resultado.
- Con límite desactivado y Simular error activado, sigue apareciendo
  el error “Sin conexión”.

```

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Solicité agregar un límite opcional de dos segundos con withTimeout a las cargas completas, conservando el reintento y la cancelación externa. | Agregó el estado limitarTiempo, un interruptor y el límite para ambos modos. Indicó que distingue el timeout de la cancelación externa y registra mediciones solo cuando hay éxito. Reportó una compilación exitosa. | No he realizado correcciones adicionales. En el emulador observé el mensaje «Tardó demasiado», con Reintentar disponible y sin mediciones en la tabla. | El límite permite detener las cargas que exceden el plazo. Mantenerlo opcional permite seguir comparando las ejecuciones normales de tres y cinco segundos. |