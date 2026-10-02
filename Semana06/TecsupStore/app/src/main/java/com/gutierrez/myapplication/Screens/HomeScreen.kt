package com.gutierrez.myapplication.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.Circle
import com.gutierrez.myapplication.navigation.Screen
import kotlinx.coroutines.launch

data class Product(
    val id: Int,
    val name: String,
    val precio: Double
)

data class Enlace(
    val id: Int,
    val name: String,
    val ruta : Screen
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedDrawerItem by remember { mutableStateOf(1) }

    val purplePrimary = Color(0xFF9549C4)
    val purpleBackground = Color(0xFFF3E5F5)
    val dividerColor = Color(0xFFE0E0E0)

    val listaProductos = remember {
        listOf(
            Product(1, "Audifonos Gamer", 89.00),
            Product(2, "SmartWatch", 199.00),
            Product(3, "Funda Celular", 25.00),
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 0.dp),
                modifier = Modifier.width(320.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(color = purpleBackground, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "JD",
                            color = purplePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Juan Gutierrez",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "juan.gutierrez.d@tecsup.edu.pe",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    color = dividerColor,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(8.dp))

                val menuItems = listOf(
                    Enlace(1, "Inicio", Screen.Home),
                    Enlace(2, "Mis pedidos", Screen.Pedidos),
                    Enlace(3, "Favoritos", Screen.Favoritos),
                    Enlace(4, "Perfil", Screen.Perfil),
                    Enlace(5, "Cerrar sesion", Screen.Close),
                )


                menuItems.forEach { enlace ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = enlace.name,
                                fontWeight = if (selectedDrawerItem == enlace.id) FontWeight.Bold else FontWeight.Normal, // 3. Editado aquí: antes tenías 'id'
                                fontSize = 16.sp
                            )
                        },
                        selected = selectedDrawerItem == enlace.id,
                        onClick = {
                            selectedDrawerItem = enlace.id
                            scope.launch { drawerState.close() }

                            navController.navigate(enlace.ruta.route)
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Adjust,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = purpleBackground,
                            selectedIconColor = purplePrimary,
                            selectedTextColor = purplePrimary,
                            unselectedContainerColor = Color.Transparent,
                            unselectedIconColor = Color.Black,
                            unselectedTextColor = Color.Black
                        ),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .height(56.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menú",
                                tint = Color.White
                            )
                        }
                    },
                    title = {
                        Column {
                            Text(
                                text = "Tecsup Store",
                                color = Color.White,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Mas vendidos",
                                color = Color.White,
                                fontSize = 15.sp,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = purplePrimary
                    )
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(listaProductos) { producto ->
                    CardProduct(
                        name = producto.name,
                        precio = producto.precio,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun CardProduct(name: String, precio: Double, navController: NavController) {
    var expandedCard by remember { mutableStateOf(false) }

    val purplePrimary = Color(0xFF6A1B9A)
    val purpleBackground = Color(0xFFF3E5F5)
    val cardBackground = Color(0xFFFAFAFC)
    val strokeColor = Color(0xFF4A148C)

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = cardBackground),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(strokeColor),
            width = 1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(color = purpleBackground, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Icono de producto",
                    tint = purplePrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "S/ ${String.format("%.2f", precio)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = purplePrimary
                )
            }
            Box {
                IconButton(onClick = { expandedCard = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones de producto"
                    )
                }
                DropdownMenu(
                    expanded = expandedCard,
                    onDismissRequest = { expandedCard = false }
                ) {
                    DropdownMenuItem(
                        onClick = { expandedCard = false },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Favoritos"
                            )
                        },
                        text = { Text("Favoritos") }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 2.dp),
                        color = Color.Black,
                        thickness = 1.dp
                    )
                    DropdownMenuItem(
                        onClick = {
                            expandedCard = false
                            navController.navigate(Screen.Home.route)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Compartir"
                            )
                        },
                        text = { Text("Compartir") }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 2.dp),
                        color = Color.Black,
                        thickness = 1.dp
                    )
                    DropdownMenuItem(
                        onClick = {
                            expandedCard = false
                            navController.navigate(Screen.Home.route)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ReportProblem,
                                contentDescription = "Reportar"
                            )
                        },
                        text = { Text("Home") }
                    )
                }
            }
        }
    }
}
