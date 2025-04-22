package com.example.myapplication.screen.PutForSale

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.R
import com.example.myapplication.screen.Home.CarViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.Methods.CarViewModelFactory
import com.example.myapplication.supabase

@Composable
fun PutForSale(navController: NavController) {
    val context = LocalContext.current
    val factory = CarViewModelFactory(supabase)
    val carViewModel: CarViewModel = viewModel(factory = factory)


    var manufacturer by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            uri: Uri? -> imageUri = uri
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = imageUri?.let { rememberAsyncImagePainter(it) }
                    ?: painterResource(R.drawable.add_image),
                contentDescription = "Selected Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(30.dp)
                    .clickable { launcher.launch("image/*") }
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
        TextField(value = manufacturer, onValueChange = { manufacturer = it }, label = { Text("Производитель") })

        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = model, onValueChange = { model = it }, label = { Text("Модель") })

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Цена") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))
        val context = LocalContext.current
        val viewModel: AddViewModel = viewModel()

        Button(onClick = {
            viewModel.addCar(
                manufacturer = manufacturer,
                model = model,
                price = price,
                imageUri = imageUri,
                context = context,
                onSuccess = {
                    navController.navigate("home")
                },
                onError = { exception ->
                    println("Error adding car: $exception")
                }
            )
        }) {
            Text("Выставить в аренду")
        }
        carViewModel.loadCars()

    }
}
