package com.base.baselibrary.viewmodel

@Target(AnnotationTarget.CONSTRUCTOR)
annotation class Auto(val singleton: Boolean = true)