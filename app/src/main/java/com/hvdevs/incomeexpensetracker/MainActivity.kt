package com.hvdevs.incomeexpensetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hvdevs.incomeexpensetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
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