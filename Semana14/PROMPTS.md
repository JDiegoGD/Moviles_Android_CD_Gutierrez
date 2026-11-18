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