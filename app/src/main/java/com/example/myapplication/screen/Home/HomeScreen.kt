package com.example.myapplication.screen.Home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.Models.Car

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: CarViewModel
) {
    var showMessage by remember { mutableStateOf("") }
    val cars by viewModel.cars.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { navController.navigate("putForSale") }) {
                Text("Выставить в аренду")
            }
            Spacer(modifier = Modifier.padding(6.dp))
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Профиль",
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .clickable { navController.navigate("profile") }
            )
        }

        Spacer(modifier = Modifier.height(11.dp))

        if (cars.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет доступных автомобилей", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(cars, key = { car -> car.id }) { car ->
                    CarItem(
                        car = car,
                        onRentClick = {
                            viewModel.rentCar(car)
                            showMessage = "Автомобиль арендован"
                            scope.launch {
                                delay(3200)
                                showMessage = ""
                            }
                        },
                        onEditClick = {
                            navController.navigate("editCar/${car.id}")
                        },
                        onDeleteClick = {
                            viewModel.deleteCar(car)
                            showMessage = "Автомобиль удален"
                            scope.launch {
                                delay(3200)
                                showMessage = ""
                            }
                        }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = showMessage.isNotEmpty(),
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    showMessage,
                    color = Color.Green,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun CarItem(
    car: Car,
    onRentClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        AsyncImage(
            model = car.imageUrl,
            contentDescription = car.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = car.name,
                fontSize = 18.sp
            )
            Text(
                text = car.price,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Кнопка аренды
        Button(
            onClick = onRentClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Арендовать автомобиль")
        }

        Spacer(modifier = Modifier.height(5.dp))

        // Кнопки управления
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onEditClick,
                modifier = Modifier.weight(1f)
                    .padding(end = 4.dp)
            ) {
                Text("Редактировать")
            }

            Button(
                onClick = onDeleteClick,
                modifier = Modifier.weight(1f)
                    .padding(start = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Удалить")
            }
        }
    }
}
