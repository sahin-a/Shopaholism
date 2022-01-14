package com.sar.shopaholism.presentation.presenter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain

abstract class BasePresenterTest {
    @ExperimentalCoroutinesApi
    private val testMainDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    open fun setup() {
        Dispatchers.setMain(testMainDispatcher)
    }
}