# Desarrollo del Laboratorio N°2 usando POO y IA

### Prompt N°1
* Actua como un desarrollador Senior en Kotlin. Diseña la abstracción delmodelo de datos para un carrito de compras ejecutable en la consola. Crea una
clase abstracta Producto que encapsule las propiedades nombre (String), precioBase(Double), usa modificadores adecuadamente (protected/private). Agrega metodos abstractos para calcular el precioFinal() y imprimirDetalle().
Asegurate de incluir validaciones en el setter o en un bloque init para que el precio y la cantidad no sean negativos. No uses interfaz grafica.

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
* Sobrescribir los metodos necesarios para cada tipo de producto tenga su propia forma de  
calcular su precio final e imprimir sus detalles en consola. Genera un método main()     
básico para probar instanciar ambos tipos y ver la salida en terminal.

|                                |         ProductoFisico          | ProductoDigital                                      |
|--------------------------------|:-------------------------------:|------------------------------------------------------|
| Atributo propio                |       costoEnvio: Double        | descuentoDigital: Double + licencia: String          |
| precioFinal()                  | conIgv(precioBase) + costoEnvio | conIgv(precioBase * (1 - descuentoDigital))          |
| imprimirDetalle()              |   agregar el envío por unidad   | agregar licencia, % y ahorro por unidad              |

* Cálculo de impuestos diferenciado: ProductoFisico aplica IGV al precio base y luego suma el envío (no imponible); ProductoDigital aplica el descuento primero y calcula el IGV sobre el monto resultante.
* Validación heredada y propia: Cada subclase valida sus atributos específicos (costo de envío o porcentaje de descuento) tanto en el bloque init como en los setters.
* Demostración de Polimorfismo: En main(), se itera sobre una lista heterogénea List<Producto>, ejecutando el comportamiento específico de cada subclase al imprimir detalles y calcular el precio final.
* Simplificación del modelo: Se eliminó la clase ProductoPerecible para mantener la estructura enfocada en solo dos subclases (ProductoFisico y ProductoDigital).