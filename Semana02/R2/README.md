# Desarrollo del Laboratorio N°2 usando POO y IA

### Prompts
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


