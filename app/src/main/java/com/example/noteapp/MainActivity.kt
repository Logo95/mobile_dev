package com.example.noteapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noteapp.ui.theme.NoteAppTheme
import org.json.JSONArray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NoteAppTheme {
                NoteAppScreen(this)
            }
        }
    }
}

@Composable
fun NoteAppScreen(context: Context) {
    // Список заметок
    var notes by remember {
        mutableStateOf(loadNotes(context))
    }

    // Текущая вводимая заметка
    var currentNote by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Поле ввода новой заметки
        BasicTextField(
            value = currentNote,
            onValueChange = { currentNote = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(56.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary) // Добавляем border
                .padding(8.dp),
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    if (currentNote.isEmpty()) Text("Введите заметку...")
                    innerTextField()
                }
            }
        )

        // Кнопка "Сохранить"
        Button(
            onClick = {
                if (currentNote.isNotBlank()) {
                    notes = notes + currentNote
                    saveNotes(context, notes)
                    currentNote = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }

        // Список заметок
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes.size) { index ->
                Text(
                    text = notes[index],
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer) // Добавляем background
                        .padding(8.dp)
                )
            }
        }
    }
}

// Функция загрузки заметок из SharedPreferences
fun loadNotes(context: Context): List<String> {
    val sharedPreferences = context.getSharedPreferences("NoteApp", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("notes", "[]")
    val jsonArray = JSONArray(json)
    val notes = mutableListOf<String>()
    for (i in 0 until jsonArray.length()) {
        notes.add(jsonArray.getString(i))
    }
    return notes
}

// Функция сохранения заметок в SharedPreferences
fun saveNotes(context: Context, notes: List<String>) {
    val sharedPreferences = context.getSharedPreferences("NoteApp", Context.MODE_PRIVATE)
    val jsonArray = JSONArray(notes)
    sharedPreferences.edit()
        .putString("notes", jsonArray.toString())
        .apply()
}

@Preview(showBackground = true)
@Composable
fun NoteAppPreview() {
    NoteAppTheme {
        NoteAppPreviewScreen(listOf("Пример заметки 1", "Пример заметки 2"))
    }
}

@Composable
fun NoteAppPreviewScreen(notes: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Поле для ввода
        BasicTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(56.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary)
                .padding(8.dp),
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    if ("".isEmpty()) Text("Введите заметку...")
                    innerTextField()
                }
            }
        )

        // Кнопка "Сохранить"
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }

        // Список фиктивных заметок
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes.size) { index ->
                Text(
                    text = notes[index],
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(8.dp)
                )
            }
        }
    }
}
