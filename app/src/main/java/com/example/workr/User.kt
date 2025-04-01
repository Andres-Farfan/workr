package com.example.workr

class User(
    val name: String,
    val id: String,
    val age: Int,
    val color: String
) {
    override fun toString(): String {
        return "User info:\n\nName: ${name}\nId: ${id}\nAge: ${age}\nColor: ${color}"
    }
}