package com.example.myapplication.screen.Home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.Methods.CarCard
import com.example.myapplication.Models.Car

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: CarViewModel
) {
    var showMessage by remember { mutableStateOf("") }
    val cars by viewModel.cars.collectAsState()
    val scope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }
    var isFiltersVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Поисковая строка
        if (isSearchVisible) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Поиск...") },
                trailingIcon = {
                    IconButton(onClick = { viewModel.searchCars(searchText) }) {
                        Icon(Icons.Default.Search, contentDescription = "Поиск")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        // Верхняя панель
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                Icon(Icons.Default.Search, contentDescription = "Поиск")
            }

            Button(
                onClick = { isFiltersVisible = !isFiltersVisible }
            ) {
                Text("Фильтры")
            }

            Button(
                onClick = { navController.navigate("putForSale") },
                modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
            ) {
                Text("Добавить")
            }

            IconButton(onClick = { navController.navigate("profile") }) {
                Icon(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Профиль",
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Блок фильтров и сортировки
        AnimatedVisibility(
            visible = isFiltersVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Сортировка
                Text(
                    "Сортировка:",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = viewModel.selectedSortOption == "price_asc",
                        onClick = {
                            viewModel.selectedSortOption = "price_asc"
                            viewModel.sortCars(true)
                        },
                        label = { Text("Сначала недорогие") },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    FilterChip(
                        selected = viewModel.selectedSortOption == "price_desc",
                        onClick = {
                            viewModel.selectedSortOption = "price_desc"
                            viewModel.sortCars(false)
                        },
                        label = { Text("Сначала дорогие") }
                    )
                }

                // Фильтр по производителю
                Text(
                    "Производитель:",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )

                LazyRow {
                    items(viewModel.getUniqueManufacturers()) { manufacturer ->
                        FilterChip(
                            selected = viewModel.selectedManufacturer == manufacturer,
                            onClick = {
                                viewModel.selectedManufacturer =
                                    if (viewModel.selectedManufacturer == manufacturer) null
                                    else manufacturer
                                viewModel.filterByManufacturer(viewModel.selectedManufacturer)
                            },
                            label = { Text(manufacturer) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }
        }

        // Список автомобилей
        if (cars.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет доступных автомобилей", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cars, key = { car -> car.id }) { car ->
                    CarCard(
                        car = car,
                        onRentClick = {
                            viewModel.rentCar(car)
                            scope.launch {
                                // Показываем сообщение об успешной аренде
                                showMessage = "Автомобиль ${car.name} арендован"
                                delay(3000)
                                showMessage = ""
                            }
                        },
                        onEditClick = {
                            navController.navigate("editCar/${car.id}")
                        },
                        onDeleteClick = {
                            viewModel.deleteCar(car) {
                                scope.launch {
                                    // Показываем сообщение об удалении
                                    showMessage = "Автомобиль ${car.name} удален"
                                    delay(3000)
                                    showMessage = ""
                                }
                            }
                        }
                    )
                }
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
                modifier = Modifier.weight(1.2f)
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
