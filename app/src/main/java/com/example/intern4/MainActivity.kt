package com.example.intern4

import com.example.intern4.ui.theme.Intern4Theme


import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.intern4.ui.theme.Intern4Theme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Intern4Theme() {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
                    composable("main") { MainScreen(navController) }
                    composable("add_note") { AddNoteScreen(navController, this@MainActivity) }
                    composable("view_notes") { ViewNotesScreen(navController, this@MainActivity) }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    Surface(modifier = Modifier.fillMaxSize().background(Color(0xFFF3F4F6))) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Notes App", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(16.dp))
            Button(
                onClick = { navController.navigate("add_note") },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Add Note")
            }
            Button(
                onClick = { navController.navigate("view_notes") },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("View Notes")
            }
        }
    }
}

@Composable
fun AddNoteScreen(navController: NavController, context: Context) {
    var noteText by remember { mutableStateOf(TextFieldValue("")) }
    Surface(modifier = Modifier.fillMaxSize().background(Color(0xFFE0F7FA))) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text("Add a New Note", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
            BasicTextField(
                value = noteText,
                onValueChange = { noteText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(8.dp)
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp),
                singleLine = false
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    saveNoteToSharedPreferences(context, noteText.text)
                    navController.navigate("main")
                },
                modifier = Modifier.padding(8.dp).fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Note")
            }
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(8.dp).fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
fun ViewNotesScreen(navController: NavController, context: Context) {
    val notes = getNotesFromSharedPreferences(context)
    Surface(modifier = Modifier.fillMaxSize().background(Color(0xFFFFE0B2))) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text("Saved Notes:", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            notes.forEach { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text("- $note", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(16.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 16.dp).fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Back")
            }
        }
    }
}

fun saveNoteToSharedPreferences(context: Context, note: String) {
    val sharedPreferences = context.getSharedPreferences("NotesApp", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val notes = sharedPreferences.getStringSet("notes", mutableSetOf()) ?: mutableSetOf()
    notes.add(note)
    editor.putStringSet("notes", notes)
    editor.apply()
}

fun getNotesFromSharedPreferences(context: Context): List<String> {
    val sharedPreferences = context.getSharedPreferences("NotesApp", Context.MODE_PRIVATE)
    return sharedPreferences.getStringSet("notes", mutableSetOf())?.toList() ?: emptyList()
}
