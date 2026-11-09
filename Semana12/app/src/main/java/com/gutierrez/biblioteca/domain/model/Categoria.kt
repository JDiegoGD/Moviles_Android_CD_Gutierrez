package com.gutierrez.biblioteca.domain.model

/**
 * Categorias del catalogo de productos.
 *
 * Cada una corresponde a una categoria real de dummyjson.com
 * (GET products/category/{slug}). La UI muestra el emoji y el nombre en espanol, y el
 * repositorio consulta la API con el slug.
 */
enum class Categoria(val nombre: String, val slug: String, val emoji: String) {
    // Tecnologia
    LAPTOPS("Laptops", "laptops", "💻"),
    SMARTPHONES("Smartphones", "smartphones", "📱"),
    TABLETS("Tablets", "tablets", "📲"),
    ACCESORIOS_MOVIL("Accesorios para móvil", "mobile-accessories", "🎧"),

    // Moda
    RELOJES_HOMBRE("Relojes de hombre", "mens-watches", "⌚"),
    RELOJES_MUJER("Relojes de mujer", "womens-watches", "⌚"),
    CAMISAS_HOMBRE("Camisas de hombre", "mens-shirts", "👕"),
    ZAPATOS_HOMBRE("Zapatos de hombre", "mens-shoes", "👟"),
    ZAPATOS_MUJER("Zapatos de mujer", "womens-shoes", "👠"),
    VESTIDOS("Vestidos", "womens-dresses", "👗"),
    BLUSAS("Blusas", "tops", "👚"),
    CARTERAS("Carteras", "womens-bags", "👜"),
    JOYAS("Joyas", "womens-jewellery", "💍"),
    LENTES_SOL("Lentes de sol", "sunglasses", "🕶️"),

    // Belleza
    BELLEZA("Belleza", "beauty", "💄"),
    FRAGANCIAS("Fragancias", "fragrances", "🌸"),
    CUIDADO_PIEL("Cuidado de la piel", "skin-care", "🧴"),

    // Hogar
    MUEBLES("Muebles", "furniture", "🛋️"),
    DECORACION_HOGAR("Decoración del hogar", "home-decoration", "🖼️"),
    COCINA("Accesorios de cocina", "kitchen-accessories", "🍳"),
    ABARROTES("Abarrotes", "groceries", "🛒"),

    // Deportes y vehiculos
    DEPORTES("Accesorios deportivos", "sports-accessories", "⚽"),
    MOTOCICLETAS("Motocicletas", "motorcycle", "🏍️"),
    VEHICULOS("Vehículos", "vehicle", "🚗")
}
