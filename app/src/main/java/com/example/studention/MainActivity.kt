package com.example.studention

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Contacts.Intents.UI
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.studention.ui.theme.StudentionTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentionTheme {
               App()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun App() {
    var email = remember { mutableStateOf(TextFieldValue(" ")) }
    var password = remember { mutableStateOf(TextFieldValue(" ")) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    )
    {
        Image(
            painter = painterResource(id = R.drawable.c),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,

            ) {


          /*  Text(
                text = "Hello, ${Texto.value.text}!",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 32.dp),
                color = Color.White,



            )*/
            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Enter your name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp)

            )

            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Ingresa tu Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)

            )

            Spacer(modifier = Modifier.weight(3f))



            Button(
                onClick = {
                   /* email.value = TextFieldValue("Hello, ${Texto.value.text}!")*/
                },
                modifier = Modifier.align(Alignment.End)

            ) {
                Text("Say Hello")
            }

        }
    }
}






