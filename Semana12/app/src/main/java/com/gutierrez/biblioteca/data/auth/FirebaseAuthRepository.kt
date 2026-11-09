package com.gutierrez.biblioteca.data.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.gutierrez.biblioteca.domain.model.Rol
import com.gutierrez.biblioteca.domain.model.Sesion
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.tasks.await

/**
 * Autenticacion con Firebase Auth (correo y contrasena). El rol sale de Firestore:
 * documento usuarios/{uid} con el campo "rol" ("ADMIN" o "USUARIO"). Si el documento
 * no existe, el usuario entra como USUARIO.
 */
class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    @Volatile
    private var sesion: Sesion? = null

    override suspend fun restaurarSesion(): Sesion? {
        val usuario = auth.currentUser ?: return null
        return try {
            armarSesion(usuario).also { sesion = it }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun ingresar(correo: String, contrasena: String): Result<Sesion> =
        try {
            val usuario = auth.signInWithEmailAndPassword(correo.trim(), contrasena).await().user
                ?: throw AuthError("No se pudo iniciar sesión.")
            Result.success(armarSesion(usuario).also { sesion = it })
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            auth.signOut() // no dejar una sesion a medias si fallo la lectura del rol
            Result.failure(traducir(e))
        }

    override fun cerrarSesion() {
        auth.signOut()
        sesion = null
    }

    override fun sesionActual(): Sesion? = sesion

    private suspend fun armarSesion(usuario: FirebaseUser): Sesion {
        val documento = firestore.collection("usuarios").document(usuario.uid).get().await()
        val rol = Rol.entries.firstOrNull { it.name.equals(documento.getString("rol"), ignoreCase = true) }
            ?: Rol.USUARIO
        return Sesion(uid = usuario.uid, correo = usuario.email.orEmpty(), rol = rol)
    }

    /** Convierte las excepciones de Firebase en mensajes para el usuario. */
    private fun traducir(error: Exception): AuthError = when (error) {
        is AuthError -> error
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> AuthError("Correo o contraseña incorrectos.")
        is FirebaseNetworkException -> AuthError("Sin conexión. Revisa tu internet.")
        is FirebaseTooManyRequestsException -> AuthError("Demasiados intentos. Inténtalo más tarde.")
        is FirebaseFirestoreException -> AuthError("No se pudo obtener tu rol. Revisa la base de datos Firestore.")
        else -> AuthError("No se pudo iniciar sesión.")
    }
}
