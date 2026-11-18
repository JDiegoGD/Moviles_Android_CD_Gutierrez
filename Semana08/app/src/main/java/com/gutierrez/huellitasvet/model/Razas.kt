package com.gutierrez.huellitasvet.model

object Razas {
    fun razasDe(especie: Especie): List<String> = when (especie) {
        Especie.PERRO -> listOf(
            "Mestizo", "Labrador Retriever", "Golden Retriever", "Pastor Alemán",
            "Bulldog Francés", "Poodle", "Chihuahua", "Beagle", "Border Collie",
            "Schnauzer", "Rottweiler", "Shih Tzu", "Husky Siberiano", "Pug",
            "Yorkshire Terrier", "Otra"
        )
        Especie.GATO -> listOf(
            "Mestizo", "Siamés", "Persa", "Angora", "Maine Coon",
            "Bengalí", "British Shorthair", "Ragdoll", "Sphynx", "Otra"
        )
        Especie.OTRO -> listOf(
            "Conejo", "Hámster", "Cobayo", "Loro", "Canario",
            "Tortuga", "Hurón", "Otra"
        )
    }
}
