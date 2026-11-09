package com.gutierrez.biblioteca.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.gutierrez.biblioteca.MainActivity

/**
 * Implementacion de Notificador con NotificationCompat: crea el canal, comprueba el
 * permiso (Android 13+) y muestra una notificacion local que abre la app al tocarla.
 */
class NotificadorAndroid(
    private val context: Context
) : Notificador {

    init {
        crearCanal()
    }

    @SuppressLint("MissingPermission") // se comprueba en tienePermiso()
    override fun mostrar(titulo: String, mensaje: String) {
        if (!tienePermiso()) return // sin permiso no se avisa, pero la app sigue funcionando

        val abrirApp = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificacion = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setStyle(NotificationCompat.BigTextStyle().bigText(mensaje))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(abrirApp)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICACION_ID, notificacion)
    }

    private fun tienePermiso(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED

    /** Desde Android 8 toda notificacion necesita un canal (minSdk de la app es 26). */
    private fun crearCanal() {
        val canal = NotificationChannel(
            CANAL_ID,
            "Sincronización",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Avisos cuando se sincronizan los productos del catálogo"
        }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
    }

    private companion object {
        const val CANAL_ID = "sincronizacion"
        const val NOTIFICACION_ID = 1
    }
}
