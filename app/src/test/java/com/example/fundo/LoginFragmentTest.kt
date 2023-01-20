package com.example.fundo

import org.junit.Assert.assertEquals
import org.junit.Test

class LoginFragmentTest {

    @Test
    fun `empty username returns false`() {

        val loginFragment = LoginFragment()
        val validate = loginFragment.validateUserLogin("", "omkar1234")
        assertEquals(validate, false)
    }

//    @Test
//    fun `correct username and password returns true`() {
//        val loginFragment = LoginFragment()
//        val validate = loginFragment.validateUserLogin("pd@gmail.com", "omkar1234")
//        assertEquals(validate, true)
//    }
}