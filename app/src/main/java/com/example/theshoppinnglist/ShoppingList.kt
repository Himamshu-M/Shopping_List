package eu.tutorials.theshoppinnglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ShoppingItem(val id: Int, var name: String, var quantity: String, var Edit: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp() {
    var Items by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    var itemEdit by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { itemEdit = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Item")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(Items) { item ->
                if (item.Edit) {
                    ShoppingItemEditor(
                        item = item,
                        onEditComplete = { editedName, editedQuantity ->
                            Items = Items.map { it.copy(Edit = false) }
                            val editedItem = Items.find { it.id == item.id }
                            editedItem?.let {
                                it.name = editedName
                                it.quantity = editedQuantity
                            }
                        }
                    )
                } else {
                    ShoppingListItem(
                        item = item,
                        onEditClick = { Items = Items.map { it.copy(Edit = it.id == item.id) } },
                        onDeleteClick = { Items = Items - item }
                    )
                }
            }
        }
    }

    if (itemEdit) {
        itemQuantity = "1"
        AlertDialog(
            onDismissRequest = { itemEdit = false },
            title = { Text("Add Shopping Item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = {
                            itemQuantity = it

                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if (itemName.isNotBlank()) {
                                val newItem = ShoppingItem(
                                    id = Items.size + 1,
                                    name = itemName,
                                    quantity = itemQuantity
                                )
                                Items = Items + newItem
                                itemEdit = false
                                itemName = ""
                            }
                        }
                    ) {
                        Text("Add", fontSize = 18.sp)
                    }
                    Button(
                        onClick = { itemEdit = false }
                    ) {
                        Text("Cancel", fontSize = 18.sp)
                    }
                }
            }
        )
    }
}

@Composable
fun ShoppingListItem(item: ShoppingItem,
                     onEditClick: () -> Unit,
                     onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            ),

        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name,
            modifier = Modifier.padding(8.dp),
            fontSize = 20.sp
        )
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp), fontSize = 20.sp)
        Row(  modifier = Modifier
            .padding(8.dp)) {
            IconButton(
                onClick = onEditClick,
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(
                onClick = onDeleteClick,
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
fun ShoppingItemEditor(item: ShoppingItem,onEditComplete: (String, String) -> Unit) {
    var newName by remember { mutableStateOf(item.name) }
    var newQuantity by remember { mutableStateOf(item.quantity) }
    var Edit by remember { mutableStateOf(item.Edit) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            BasicTextField(
                value = newName,
                onValueChange = { newName = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                textStyle = TextStyle(fontSize = 20.sp)
            )

            BasicTextField(
                value = newQuantity,
                onValueChange = { newQuantity = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                textStyle = TextStyle(fontSize = 20.sp)
            )
        }
        Button(
            onClick = {
                Edit = false
                onEditComplete(newName, newQuantity)
            }
        ) {
            Text("Save", fontSize = 18.sp)
        }
    }
}

@Preview
@Composable
fun ShoppingListPreview() {
    ShoppingListApp()
}