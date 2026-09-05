# Laboratorio 03: Jectpack Compose

**Estudiante:** Gutierrez Duran, Juan Diego Gilmer  
**Descripcion:** Esta aplicación es una interfaz desarrollada con Jetpack Compose que simula el formulario de registro de un producto.

![Resultados](Figuras.png)

**Pregunta:** ¿qué pasaría si declaras las variables de los campos SIN remember?

Cuando le quité el remember a las variables y dejé solo el mutableStateOf("") las cajas de texto se congelaron y no me dejaban escribir nada. Cada vez que presionaba una letra en el teclado, la pantalla parpadeaba pero al final el campo quedaba vacío.

Realizando esa prueba concluyo que si solo se usa mutableStateOf la app sí detecta que se presiono una tecla, pero como no se acuerda de lo que se escribio vuelve a poner el campo en blanco. Por ello se necesita de ambos obligatoriamente para que los campos de texto funcionen.