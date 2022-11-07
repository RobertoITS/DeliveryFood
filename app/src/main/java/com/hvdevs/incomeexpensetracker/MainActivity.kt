package com.hvdevs.incomeexpensetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.hvdevs.incomeexpensetracker.databinding.ActivityMainBinding

//Diseño: https://www.figma.com/file/OhYx60ta2pqs5BUDFhbCOW/%5BFREEBIES%5D-Income-%26-Expense-Tracker-(Community)?node-id=1%3A520

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        //Creamos el bottom menu, con otro dentro para simular el detalle:
        binding.bottomNav.background = null
        binding.bottomNav.menu.getItem(2).isEnabled = false

        //Agregamos el primer fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment, HomeFrag()).commit()

        //Esto es un test, una prueba de las notificaciones
        val badgeDrawable = binding.bottomNav.getOrCreateBadge(R.id.wallet)
        badgeDrawable.isVisible = true
        badgeDrawable.number = 8

        //La seleccion de items
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, HomeFrag())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.statistics -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, StatisticsFrag())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.wallet -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, WalletFrag())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.acoount -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, AccountFrag())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
    }
}