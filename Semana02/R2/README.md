# Desarrollo del Laboratorio N°2 usando POO y IA

### Prompt N°1
* Actua como un desarrollador Senior en Kotlin. Diseña la abstracción delmodelo de datos para un carrito de compras ejecutable en la consola. Crea una clase abstracta Producto que encapsule las propiedades nombre (String), precioBase(Double), usa modificadores adecuadamente (protected/private). Agrega metodos abstractos para calcular el precioFinal() y imprimirDetalle(). Asegurate de incluir validaciones en el setter o en un bloque init para que el precio y la cantidad no sean negativos. No uses interfaz grafica.

###### Resultado:
```
app/
└── src/
    └── main/
        └── java/
            └── com.gutierrez.Lab02IA/
                ├── Carrito.kt
                └── Producto.kt
```


| Miembro                        |       Modificador        | Porque                                                         |
|--------------------------------|:------------------------:|----------------------------------------------------------------|
| nombre                         |       val público        | No se reasigna en la vida del producto                         |
| precioBase, cantidad           |  var con setter valido   | Cambian, pero solo a valores legales (evita valores negativos) |
| codigo                         |       private set        | Número secuencial y consecutivo que solo la clase genera       |
| IGV, contador                  | private companion object | Detalle interno que nadie fuera de la clase necesita           |                                               |
| soles(), lineaBase(), conIgv() |        protected         | Funcionales reservados a las subclases                         |


* Doble validación en Producto: El bloque init valida los datos recibidos al construir el objeto y los setters personalizados protegen las reasignaciones posteriores contra valores negativos.
* Contrato abstracto: precioFinal() e imprimirDetalle() son abstractos para forzar su implementación en las subclases; importeTotal() es open con la lógica genérica (precioFinal() * cantidad).
* Especializaciones por tipo:
  * ProductoFisico: suma el IGV y un recargo por peso.
  * ProductoDigital: aplica IGV y descuento de licencia.
  * ProductoPerecible: incluye IGV y una rebaja según los días para su vencimiento.
* Encapsulamiento en el Carrito: Encapsula la lista de productos como privada y la expone solo como lectura para controlar las inserciones mediante agregar().
* Polimorfismo: El método imprimirBoleta() recorre la lista genérica e invoca imprimirDetalle() de forma polimórfica sin importar el tipo exacto de producto.
* Pruebas de validación: El método main() ejecuta pruebas para verificar que el sistema rechace valores inválidos (precios, cantidades o descuentos negativos) lanzando IllegalArgumentException.

### Prompt N°2
* Usando la clase abstracta Producto, aplica Herencia para crear dos subclases en Kotlin:
  * ProductoFisico: añade un atributo costoEnvio (Double).
  * ProductoDigital: añade un atributo descuentoDigital (Double) o licencia.              
* Sobrescribir los metodos necesarios para cada tipo de producto tenga su propia forma de calcular su precio final e imprimir sus detalles en consola. Genera un método main() básico para probar instanciar ambos tipos y ver la salida en terminal.

|                                |         ProductoFisico          | ProductoDigital                                      |
|--------------------------------|:-------------------------------:|------------------------------------------------------|
| Atributo propio                |       costoEnvio: Double        | descuentoDigital: Double + licencia: String          |
| precioFinal()                  | conIgv(precioBase) + costoEnvio | conIgv(precioBase * (1 - descuentoDigital))          |
| imprimirDetalle()              |   agregar el envío por unidad   | agregar licencia, % y ahorro por unidad              |

###### Resultado: 
* Cálculo de impuestos diferenciado: ProductoFisico aplica IGV al precio base y luego suma el envío (no imponible); ProductoDigital aplica el descuento primero y calcula el IGV sobre el monto resultante.
* Validación heredada y propia: Cada subclase valida sus atributos específicos (costo de envío o porcentaje de descuento) tanto en el bloque init como en los setters.
* Demostración de Polimorfismo: En main(), se itera sobre una lista heterogénea List<Producto>, ejecutando el comportamiento específico de cada subclase al imprimir detalles y calcular el precio final.
* Simplificación del modelo: Se eliminó la clase ProductoPerecible para mantener la estructura enfocada en solo dos subclases (ProductoFisico y ProductoDigital).

### Prompt N°3
* Crea una clase CarritoDeCompras que aplique Encapsulamiento encapsulando la lista de productos(private val listaProductos = mutableListOf<Producto>()). Implementa métodos para agregarProducto(producto: Producto) y mostraDetalle(). Aplica Polimorfismo al iterar sobre la lista de tipo Producto y llamar a imprimirDetalle() y precioFinal() sin importar si es un ProductoFisico o ProductoDigital. Imprime el detalle en la terminal aliniando las columnas con String.format.

###### Resultados
* Encapsulamiento estricto: La colección listaProductos es privada y solo se expone externamente como una copia inmutable (toList()). El ingreso de elementos queda restringido al método agregarProducto().
* Polimorfismo puro: El método mostrarDetalle() itera sobre los productos ejecutando imprimirDetalle() y precioFinal() dinámicamente según la subclase, sin requerir condicionales (if/when).
* Formato y alineación: Se estructura la salida en consola mediante String.format, integrando la columna P.UNIT y alineando los totales con los montos de la tabla.
* Propiedades derivadas: Expone métricas de consulta (cantidadProductos, cantidadItems, calcularTotal()) sin dar acceso a la estructura interna.

### Prompt N°4
Completa la clase CarritoDeCompras encapsulando los calculos financieros:
1. CalcularSubtotal()
2. calcularIGV() (18% sobre el subtotal)
3. calcularTotal()
4. obtenerProductoMasCaro() (usando maxByNull)      

Actualizar el método mostrarResumen() para imprimir en consola exactamente el formato requerido (Cliente, Productos agregados, Lista formateada con 2 decimales, IGV, Descuentos y Total final).

###### Resultado:
| Método                 |                     Fórmula                     |
|------------------------|:-----------------------------------------------:|
| calcularSubtotal()     |    sumOf { it.precioFianl() * it.cantidad }     |
| calcularIGV()          |         calcularSubtotal() * IGV (18%)          |
| calcularTotal()        |       5% sobre S/ 3000, 10% sobre S/ 5000       |
| calcularDescuento()    |                total - descuento                |                                               
| obtenerProductoMasCaro | listaProductos.maxByOrNull { it.precioFinal() } | 

* Encapsulamiento de constantes: Las tasas de impuestos y límites de descuento se definen como constantes privadas dentro de un companion object, centralizando las reglas financieras.
* Mensajes consistentes: El método mensajeDescuento() es privado y deriva su texto directamente de las constantes internas para garantizar coherencia con los cobros. 
* Reporte integrado: mostrarResumen() estructura la salida completa solicitada (cliente, productos, detalle de la tabla vía mostrarDetalle(), subtotal, IGV, descuentos, total a pagar y producto más caro).
* Lógica de cálculo: El descuento se evalúa sobre el total final con IGV y la búsqueda del producto de mayor valor utiliza maxByOrNull.

### Prompt N°5
* Aplica el principio de Abstraccion para la logica de descuentos del carrito de compras en Kotlin.
1. Crea una interfaz llamada EstrategiaDescuento con un metodo calcularDescuento(monto: Double): Double.
2. Implementa dos clases concretas que usen esta interfaz:
  - SinDescuento: devuelve 0.0.
  - DescuentoPorMonto: aplica un 5% si el monto supera S/ 3000 y un 10% si supera S/ 5000 usando la estructura when.
3. Modificar la clase CarritoDeCompras para que acepte una EstrategiaDescuento (por defecto SinDescuento) y actualiza la lógica para aplicar esta estrtegia sobre el total final. No utilices interfaz gráfica, solo terminal.

###### Resultado:

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

| Archivo                |                             Contiene                             |
|------------------------|:----------------------------------------------------------------:|
| Producto.kt            | Abstraccion Producto + herencia ProductoFisico / ProductoDigital |
| EstrategiaDescuento.kt |           Interfaz + SinDescuento + DescuentoPorMonto            |
| CarritoDeCompras.kt    |   Encapsulamiento, calculos financieros, polimorfismo y main()   |

* Interfaz EstrategiaDescuento: Define el contrato calcularDescuento(monto: Double) y la propiedad descripcion para desacoplar el texto informativo de la boleta.
* Implementaciones concretas: SinDescuento (devuelve 0) y DescuentoPorMonto (calcula 5% o 10% con when), manteniendo sus reglas y constantes encapsuladas.
* Desacoplamiento en CarritoDeCompras: Elimina constantes de descuento y condicionales locales; delega el cálculo a la estrategia inyectada (EstrategiaDescuento).
* Flexibilidad y dinamismo: Utiliza var con private set y el método cambiarEstrategia() para intercambiar promociones en tiempo de ejecución.
* Validación en main(): Demuestra la variabilidad ejecutando la misma compra con distintas estrategias y probando los tramos de descuento.

### Prompt N°6
* Escribe la funcion main() en un archivo ejecutable de Kotlin que integre todas las clases desarrolladas hasta el momento.
  1. Intancia un cliente (Gutierrez Duran Juan Diego) y crea objetos de tipo ProductoFisico y ProductoDigital.
  2. Crea un CarritoDeCompras para le cliente y agrega los productos utilizando agregarProducto().
  3. Asigna la estrategia DescuentoPorMonto al carrito para aplicar la promoción adecuada según el total de la compra.
  4. Ejecuta mostrarResumen() para imprimir la boleta final formateada en consola con 2 decimales, columnas aliniadas, subtotal, IGV, descuento aplicado y el producto más caro.
  5. Agrega un bloque final que demuestre el manejo de exepciones probando instanciar productos con valores no validos (precios, cantidades o envios negativos) para verificar que el sistema responda de forma segura lanzando IllegalAtgumentException. No uses interfaz grafica.

###### Resultado:
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

| Archivo                |                           Rol                            |
|------------------------|:--------------------------------------------------------:|
| Producto.kt            |    Clase abstracta + ProductoFisico / ProductoDigital    |
| EstrategiaDescuento.kt |       Interfaz + SinDescuento + DescuentoPorMonto        |
| CarritoDeCompras.kt    | Encapsulamiento, calculos financieros, salida formateada |
| Main.kt                |            Punto de entrada que integra todo             |


##### Resultado Final (terminal)

![resultado](/imagenes/resultado1.png)
![resultado](/imagenes/resultado2.png)