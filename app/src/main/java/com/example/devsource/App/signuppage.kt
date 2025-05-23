package com.example.devsource.App

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devsource.Homepage.AuthState
import com.example.devsource.Homepage.AuthViewModel
import com.example.devsource.R


@Composable
fun SignUpPage(modifier: Modifier=Modifier, navController: NavController, authViewModel: AuthViewModel,useremail:MutableState<String>,username:MutableState<String>,userpassword:MutableState<String>){
    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }
    val authState=authViewModel.authState.observeAsState()
    val context= LocalContext.current
    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("fetchdata")
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message,Toast.LENGTH_LONG).show()
            else -> Unit
        }
    }
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(scrollState)
            .padding(WindowInsets.ime.asPaddingValues()) ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Text(text="Sign Up", fontSize = 34.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painterResource(R.drawable.logo2),
            contentDescription = "logo",
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 35.dp),
            leadingIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = null) }
            ,
            shape = RoundedCornerShape(23.dp),
            singleLine = true,
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 35.dp),
            leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = null) }
            ,
            shape = RoundedCornerShape(23.dp),
            singleLine = true,
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 35.dp),
            leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = null) }
            ,
            shape = RoundedCornerShape(23.dp),
            visualTransformation = PasswordVisualTransformation(),
            value = password,
            singleLine = true,
            onValueChange = { password = it },
            label = { Text(text = "Password") }

        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 35.dp),
            leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = null) },
            shape = RoundedCornerShape(23.dp),
            singleLine = true,
            value = confirmpassword,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { confirmpassword = it },
            label = { Text(text = "Confirm Password") }
        )
        Spacer(modifier = Modifier.height(16.dp))


        Button(onClick = {
            useremail.value=email
            username.value=name
            userpassword.value=confirmpassword
            navController.navigate("otp-page")
        }, enabled=authState.value!=AuthState.Loading && password==confirmpassword){
            Text(text="Create Account",fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(5.dp))
        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text(text="Already have an account!",fontSize = 16.sp)
        }
    }
}