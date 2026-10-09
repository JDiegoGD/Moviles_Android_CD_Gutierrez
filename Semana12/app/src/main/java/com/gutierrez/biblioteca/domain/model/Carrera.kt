package com.gutierrez.biblioteca.domain.model

/**
 * Carreras de Tecsup usadas para categorizar los libros.
 *
 * dummyjson.com solo tiene categorias de productos, por eso cada carrera se
 * asocia a un slug real de la API (products/category/{slug}). La UI muestra
 * el nombre de la carrera y el repositorio consulta con el slug.
 */
enum class Carrera(val nombre: String, val slug: String) {
    // Tecnologia Digital y Computacion
    DISENO_DESARROLLO_SOFTWARE("Diseño y Desarrollo de Software", "laptops"),
    REDES_COMUNICACIONES("Administración de Redes y Comunicaciones", "mobile-accessories"),
    BIG_DATA("Big Data y Ciencia de Datos", "tablets"),
    CIBERSEGURIDAD("Ciberseguridad y Auditoría Informática", "smartphones"),
    SIMULADORES_VIDEOJUEGOS("Diseño y Desarrollo de Simuladores y Videojuegos", "sports-accessories"),
    MODELADO_ANIMACION("Modelado y Animación Digital", "home-decoration"),

    // Electricidad y Electronica
    MECATRONICA("Mecatrónica Industrial", "motorcycle"),
    ELECTRICIDAD_INDUSTRIAL("Electricidad Industrial", "kitchen-accessories"),
    ELECTRONICA_AUTOMATIZACION("Electrónica y Automatización Industrial", "mens-watches"),
    MECANICA_ELECTRICA("Tecnología Mecánica Eléctrica", "mens-shoes"),

    // Mecanica y Aviacion
    MAQUINARIA_PESADA("Mantenimiento de Maquinaria Pesada", "vehicle"),
    MAQUINARIA_INDUSTRIAL("Mantenimiento de Maquinaria Industrial", "furniture"),
    AVIONICA("Aviónica y Mecánica Aeronáutica", "womens-watches"),

    // Mineria, Quimica y Procesos
    OPERACIONES_MINERAS("Operaciones Mineras", "womens-jewellery"),
    PLANTAS_MINERALES("Procesamiento de Minerales", "fragrances"),
    PROCESOS_QUIMICOS("Procesos Químicos y Metalúrgicos", "skin-care"),
    TOPOGRAFIA("Topografía y Geomática", "sunglasses"),

    // Gestion, Produccion y Negocios
    TECNOLOGIA_PRODUCCION("Tecnología de la Producción", "groceries"),
    NEGOCIOS_DIGITALES("Negocios Digitales", "womens-bags"),
    DISENO_INDUSTRIAL("Diseño Industrial", "mens-shirts"),
    LOGISTICA("Logística Digital Integrada", "tops"),
    MARKETING_DIGITAL("Marketing Digital Analítico", "beauty"),
    PRODUCCION_GESTION("Producción y Gestión Industrial", "womens-shoes")
}
