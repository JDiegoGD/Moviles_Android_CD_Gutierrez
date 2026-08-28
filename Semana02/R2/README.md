# Desarrollo del Laboratorio N°2 usando POO y IA
## Estudiante: Gutierrez Duran Juan Diego

### Prompt N°1
* Actua como un desarrollador Senior en Kotlin. Diseña la abstracción delmodelo de datos para un carrito de compras ejecutable en la consola. Crea una clase abstracta Producto que encapsule las propiedades nombre (String), precioBase(Double), usa modificadores adecuadamente (protected/private). Agrega metodos abstractos para calcular el precioFinal() y imprimirDetalle(). Asegurate de incluir validaciones en el setter o en un bloque init para que el precio y la cantidad no sean negativos. No uses interfaz grafica.

##### Resultado:
###### Estructura del Proyecto
```
app/
└── src/
    └── main/
        └── java/
            └── com.gutierrez.Lab02IA/
                ├── Carrito.kt
                └── Producto.kt
```

###### Resumen
| Miembro                    |       Modificador        | Porque                                                         |
|----------------------------|:------------------------:|----------------------------------------------------------------|
| nombre                     |       val público        | No se reasigna en la vida del producto                         |
| precioBase, cantidad       |  var con setter valido   | Cambian, pero solo a valores legales (evita valores negativos) |
| codigo                     |       private set        | Número secuencial y consecutivo que solo la clase genera       |
| IGV, contador              | private companion object | Detalle interno que nadie fuera de la clase necesita           |                                               |
| soles(), lineaBase(), conIgv() |        protected         | Funcionales reservados a las subclases                         |


###### Puntos Clave del Resultado
* Doble Validación: Filtra valores negativos en init y en los setters personalizados.
* Contrato Abstracto: precioFinal() e imprimirDetalle() obligan su implementación en cada subclase.
* Especialización: Subclases para producto físico (envío), digital (licencia) y perecible (vencimiento).
* Colección Segura: Lista de productos privada en Carrito, expuesta solo como copia de lectura.
* Manejo de Excepciones: Validación estricta que lanza IllegalArgumentException ante datos inválidos.


### Prompt N°2
* Usando la clase abstracta Producto, aplica Herencia para crear dos subclases en Kotlin:
  * ProductoFisico: añade un atributo costoEnvio (Double).
  * ProductoDigital: añade un atributo descuentoDigital (Double) o licencia.              
* Sobrescribir los metodos necesarios para cada tipo de producto tenga su propia forma de calcular su precio final e imprimir sus detalles en consola. Genera un método main() básico para probar instanciar ambos tipos y ver la salida en terminal.

###### Resumen
|                                |         ProductoFisico          | ProductoDigital                                      |
|--------------------------------|:-------------------------------:|------------------------------------------------------|
| Atributo propio                |       costoEnvio: Double        | descuentoDigital: Double + licencia: String          |
| precioFinal()                  | conIgv(precioBase) + costoEnvio | conIgv(precioBase * (1 - descuentoDigital))          |
| imprimirDetalle()              |   agregar el envío por unidad   | agregar licencia, % y ahorro por unidad              |

###### Puntos Clave del Resultado
* Impuestos Diferenciados: El flete del producto físico es exento de IGV; el producto digital aplica el impuesto sobre el neto con descuento.
* Simplificación: Se retira ProductoPerecible para enfocar la arquitectura en dos subclases representativas.
* Polimorfismo: Iteración limpia en main() sobre List<Producto> ejecutando la lógica propia de cada variante.

### Prompt N°3
* Crea una clase CarritoDeCompras que aplique Encapsulamiento encapsulando la lista de productos(private val listaProductos = mutableListOf<Producto>()). Implementa métodos para agregarProducto(producto: Producto) y mostraDetalle(). Aplica Polimorfismo al iterar sobre la lista de tipo Producto y llamar a imprimirDetalle() y precioFinal() sin importar si es un ProductoFisico o ProductoDigital. Imprime el detalle en la terminal aliniando las columnas con String.format.

###### Puntos Clave del Resultado
* Encapsulamiento Estricto: Exposición inmutable mediante toList() y mutación restringida a agregarProducto().
* Formato Consola: Tabulación uniforme de montos y columnas mediante String.format.
* Cero Condicionales: Ejecución polimórfica sin estructuras if o when para identificar subclases.

### Prompt N°4
Completa la clase CarritoDeCompras encapsulando los calculos financieros:
1. CalcularSubtotal()
2. calcularIGV() (18% sobre el subtotal)
3. calcularTotal()
4. obtenerProductoMasCaro() (usando maxByNull)      

Actualizar el método mostrarResumen() para imprimir en consola exactamente el formato requerido (Cliente, Productos agregados, Lista formateada con 2 decimales, IGV, Descuentos y Total final).

###### Resumen
| Método                 |                     Fórmula                     |
|------------------------|:-----------------------------------------------:|
| calcularSubtotal()     |    sumOf { it.precioFianl() * it.cantidad }     |
| calcularIGV()          |         calcularSubtotal() * IGV (18%)          |
| calcularTotal()        |       5% sobre S/ 3000, 10% sobre S/ 5000       |
| calcularDescuento()    |                total - descuento                |                                               
| obtenerProductoMasCaro | listaProductos.maxByOrNull { it.precioFinal() } | 

* Constantes Centralizadas: Las tasas impositivas y reglas de descuento se aíslan en el companion object.
* Salida Estructurada: Boleta integrada con datos del cliente, tabla alineada, desglose impositivo y producto de mayor valor.

### Prompt N°5
* Aplica el principio de Abstraccion para la logica de descuentos del carrito de compras en Kotlin.
1. Crea una interfaz llamada EstrategiaDescuento con un metodo calcularDescuento(monto: Double): Double.
2. Implementa dos clases concretas que usen esta interfaz:
  - SinDescuento: devuelve 0.0.
  - DescuentoPorMonto: aplica un 5% si el monto supera S/ 3000 y un 10% si supera S/ 5000 usando la estructura when.
3. Modificar la clase CarritoDeCompras para que acepte una EstrategiaDescuento (por defecto SinDescuento) y actualiza la lógica para aplicar esta estrtegia sobre el total final. No utilices interfaz gráfica, solo terminal.

###### Estructura del Proyecto
```
app/
└── src/
    └── main/
        └── java/
            └── com.gutierrez.Lab02IA/
                ├── EstrategiaDescuento.kt
                ├── CarritoDeCompras.kt
                └── Producto.kt
```
###### Resumen
| Archivo                |                             Contiene                             |
|------------------------|:----------------------------------------------------------------:|
| Producto.kt            | Abstraccion Producto + herencia ProductoFisico / ProductoDigital |
| EstrategiaDescuento.kt |           Interfaz + SinDescuento + DescuentoPorMonto            |
| CarritoDeCompras.kt    |   Encapsulamiento, calculos financieros, polimorfismo y main()   |

* Patrón Strategy: Desacopla la lógica comercial del carrito mediante la interfaz EstrategiaDescuento.
* Cambio en Tiempo de Ejecución: cambiarEstrategia() permite intercambiar promociones dinámicamente.

### Prompt N°6
* Escribe la funcion main() en un archivo ejecutable de Kotlin que integre todas las clases desarrolladas hasta el momento.
  1. Intancia un cliente (Gutierrez Duran Juan Diego) y crea objetos de tipo ProductoFisico y ProductoDigital.
  2. Crea un CarritoDeCompras para le cliente y agrega los productos utilizando agregarProducto().
  3. Asigna la estrategia DescuentoPorMonto al carrito para aplicar la promoción adecuada según el total de la compra.
  4. Ejecuta mostrarResumen() para imprimir la boleta final formateada en consola con 2 decimales, columnas aliniadas, subtotal, IGV, descuento aplicado y el producto más caro.
  5. Agrega un bloque final que demuestre el manejo de exepciones probando instanciar productos con valores no validos (precios, cantidades o envios negativos) para verificar que el sistema responda de forma segura lanzando IllegalAtgumentException. No uses interfaz grafica.

###### Estructura del Proyecto
```
app/
└── src/
    └── main/
        └── java/
            └── com.gutierrez.Lab02IA/
                ├── EstrategiaDescuento.kt
                ├── CarritoDeCompras.kt
                ├── Main.kt
                └── Producto.kt
```
###### Resumen
| Archivo                |                           Rol                            |
|------------------------|:--------------------------------------------------------:|
| Producto.kt            |    Clase abstracta + ProductoFisico / ProductoDigital    |
| EstrategiaDescuento.kt |       Interfaz + SinDescuento + DescuentoPorMonto        |
| CarritoDeCompras.kt    | Encapsulamiento, calculos financieros, salida formateada |
| Main.kt                |            Punto de entrada que integra todo             |

##### Resultado Final (terminal)
![resultado](/imagenes/resultado1.png)
![resultado](/imagenes/resultado2.png)