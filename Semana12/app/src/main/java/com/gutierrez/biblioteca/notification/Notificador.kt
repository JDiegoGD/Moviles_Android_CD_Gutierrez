package com.gutierrez.biblioteca.notification

/**
 * Contrato para avisar al usuario con una notificacion local.
 *
 * Los ViewModels y repositorios solo conocen esta interface; la implementacion
 * real (NotificadorAndroid, con NotificationCompat) se agrega en H15. Asi se
 * puede usar un Notificador falso en las pruebas.
 */
interface Notificador {
    fun mostrar(titulo: String, mensaje: String)
}
