package com.lab3.data

/**
 * ItemsData - singleton class (only one instance) can be the example of shared data source
 * You can get the data from this ItemsData object from any place in the code.
 */
object ItemsData {
    // Static list with the items of Item
    val itemsList: List<Item> = listOf(
        Item(1, "Title 1", "Description 1"),
        Item(2, "Title 2", "Description 2"),
        Item(3, "Title 3", "Description 3"),
    )
}

// Item class
class Item(val id: Int, val title: String, val description: String)