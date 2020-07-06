package com.lhd.view.basewithme.model

data class Account(var id: Int, var name: String) {
    override fun toString(): String {
        return "Account(id=$id, name='$name')"
    }
}