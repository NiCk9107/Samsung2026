package com.example.opharma.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Check

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit = {}
) {
    var isEditing by remember { mutableStateOf(false) }

    var firstName by remember { mutableStateOf("Никита") }
    var lastName by remember { mutableStateOf("Гаврилов") }
    var phone by remember { mutableStateOf("+7 (999) 123-45-67") }
    var age by remember { mutableStateOf("30") }
    var allergies by remember { mutableStateOf("Пенициллин, Арахис") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (isEditing) {
                            // TODO: Сохранить на бэкенд
                        }
                        isEditing = !isEditing
                    }) {
                        Icon(
                            imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = if (isEditing) "Сохранить" else "Редактировать"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Аватар
            Card(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${firstName.firstOrNull() ?: ""}${lastName.firstOrNull() ?: ""}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            ProfileField("Имя", firstName, isEditing, { firstName = it }, Icons.Default.Person)
            ProfileField("Фамилия", lastName, isEditing, { lastName = it })
            ProfileField("Телефон", phone, isEditing, { phone = it }, Icons.Default.Phone)
            ProfileField("Возраст", age, isEditing, { age = it }, Icons.Default.Person)
            ProfileField("Аллергии", allergies, isEditing, { allergies = it }, maxLines = 3)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Выйти")
            }
        }
    }
}
@Composable
private fun ProfileField(
    label: String,
    value: String,
    isEditable: Boolean,
    onValueChange: (String) -> Unit,
    icon: ImageVector? = null,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = icon?.let { { Icon(it, contentDescription = label) } },
        enabled = isEditable,
        readOnly = !isEditable,
        maxLines = maxLines,
        singleLine = maxLines == 1
    )
}