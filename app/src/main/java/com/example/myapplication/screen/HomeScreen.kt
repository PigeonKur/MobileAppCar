package com.example.myapplication.screen

import android.util.EventLogTags.Description
import android.widget.ImageButton
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
    fun HomeScreen(navController: NavController) {
    var showMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { navController.navigate("putForSale") }) {
                Text(text = "Выставить на продажу")
            }
            Spacer(modifier = Modifier.padding(6.dp))

            Image(painter = painterResource(id = R.drawable.profile), contentDescription = "Фото профиля",
                modifier = Modifier.size(45.dp).clip(CircleShape).clickable{navController.navigate("profile")})
        }

        Spacer(modifier = Modifier.height(11.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(R.drawable.car1),
                contentDescription = "Car_1",
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Автомобиль 1", color = Color.Black, fontSize = 18.sp)
            Text(text = "Цена 1", color = Color.Black, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            showMessage = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(3200)
                showMessage = false
            }
        }) {
            Text(text = "Арендовать автомобиль")
        }

        if (showMessage) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Автомобиль арендован", color = Color.Green)
        }
    }
}
