package com.gutierrez.campusgps.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.gutierrez.campusgps.data.UbicacionRepository
import com.gutierrez.campusgps.model.Posicion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

class UbicacionRepositoryGoogle(context: Context) : UbicacionRepository {
    // Se conserva el contexto de la aplicación, nunca el de una Activity.
    private val appContext = context.applicationContext
    private val cliente by lazy { LocationServices.getFusedLocationProviderClient(appContext) }

    override fun playServicesDisponible(): Boolean {
        return GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(appContext) ==
                ConnectionResult.SUCCESS
    }

    override suspend fun obtenerPosicion(): Result<Posicion> {
        val permisoPreciso = tienePermiso(Manifest.permission.ACCESS_FINE_LOCATION)
        val permisoAproximado = tienePermiso(Manifest.permission.ACCESS_COARSE_LOCATION)

        if (!permisoPreciso && !permisoAproximado) {
            return Result.failure(SecurityException("Falta el permiso de ubicación"))
        }

        val administradorUbicacion = appContext
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!LocationManagerCompat.isLocationEnabled(administradorUbicacion)) {
            return Result.failure(UbicacionNoDisponibleException())
        }

        val cancelacion = CancellationTokenSource()
        return try {
            val prioridad = if (permisoPreciso) {
                Priority.PRIORITY_HIGH_ACCURACY
            } else {
                Priority.PRIORITY_BALANCED_POWER_ACCURACY
            }
            val ubicacion = cliente.getCurrentLocation(prioridad, cancelacion.token).await()
            if (ubicacion == null) {
                Result.failure(UbicacionNoDisponibleException())
            } else {
                Result.success(
                    Posicion(ubicacion.latitude, ubicacion.longitude)
                )
            }
        } catch (e: CancellationException) {
            // Cancelar una corrutina no es un error que deba mostrarse al usuario.
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            cancelacion.cancel()
        }
    }

    private fun tienePermiso(permiso: String): Boolean =
        ContextCompat.checkSelfPermission(appContext, permiso) == PackageManager.PERMISSION_GRANTED

    // TODO Paso 8: agregar playServicesDisponible() también en la interfaz.
}
