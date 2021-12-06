package com.example.filmschecker.adapter

object TestSingleton {

    private var singleton: TestSingleton?= null

    fun getInstance(): TestSingleton {
        if(singleton == null) {
            singleton = TestSingleton
        }
        return singleton as TestSingleton
    }
}