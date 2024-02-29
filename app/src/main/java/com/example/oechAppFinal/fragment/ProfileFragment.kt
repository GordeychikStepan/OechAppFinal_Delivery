package com.example.oechAppFinal.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oechAppFinal.R
import com.example.oechAppFinal.model.MenuItem
import com.example.oechAppFinal.model.MenuItemAdapter
import com.example.oechAppFinal.activity.NotificationActivity
import com.example.oechAppFinal.model.Constances
import com.example.oechAppFinal.activity.SendPackageActivity
import com.example.oechAppFinal.activity.SplashScreen
import com.example.oechAppFinal.databinding.ActivityMainBinding
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), MenuItemAdapter.Listener {

    private lateinit var adapter: MenuItemAdapter
    private lateinit var recyclerView: RecyclerView
    private var context: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val switchMode = view.findViewById<Switch>(R.id.switchMode)
        val fullName = view.findViewById<TextView>(R.id.tvProfileFullname)
        val balance = view.findViewById<TextView>(R.id.tvCurrentBalance)
        val switchBal = view.findViewById<ImageView>(R.id.ivVis)

        val listDark = listOf(
            MenuItem(
                "Edit Profile",
                "Name, phone no, address, email...",
                R.drawable.ic_editprofile
            ),
            MenuItem(
                "Statements & Reports",
                "Download transaction details, orders, deliveries",
                R.drawable.ic_certificate
            ),
            MenuItem(
                "Notification Settings",
                "mute, unmute, set location & tracking setting",
                R.drawable.ic_notification
            ),
            MenuItem(
                "Card & Bank account settings",
                "change cards, delete card details",
                R.drawable.ic_cards
            ),
            MenuItem(
                "Referrals",
                "check no of friends and earn",
                R.drawable.ic_referrals
            ),
            MenuItem(
                "About Us",
                "know more about us, terms and conditions",
                R.drawable.ic_aboutus
            ),
            MenuItem(
                "Log Out",
                "",
                R.drawable.ic_logout
            )
        )

        var listLight = listOf(
            MenuItem(
                "Edit Profile",
                "Name, phone no, address, email...",
                R.drawable.ic_editprofile
            ),
            MenuItem(
                "Statements & Reports",
                "Download transaction details, orders, deliveries",
                R.drawable.ic_certificate
            ),
            MenuItem(
                "Notification Settings",
                "mute, unmute, set location & tracking setting",
                R.drawable.ic_notification
            ),
            MenuItem(
                "Card & Bank account settings",
                "change cards, delete card details",
                R.drawable.ic_cards
            ),
            MenuItem(
                "Referrals",
                "check no of friends and earn",
                R.drawable.ic_referrals
            ),
            MenuItem(
                "About Us",
                "know more about us, terms and conditions",
                R.drawable.ic_aboutus
            ),
            MenuItem(
                "Log Out",
                "",
                R.drawable.ic_logout
            )
        )

        switchMode.isChecked = getSavedThemeState()
        switchMode.setOnCheckedChangeListener { _, isChecked ->
            saveThemeState(isChecked)
            requireActivity().recreate()
        }

        switchBal.setOnClickListener {
            val profBalance = view.findViewById<TextView>(R.id.tvProfileBalance)
            if (profBalance.visibility != INVISIBLE) {
                profBalance.text = "*****"
            } else {
                profBalance.text = getString(R.string.examplebalance)
            }
        }

        recyclerView = view.findViewById(R.id.recyclerViewProfileMenu)
        adapter = MenuItemAdapter(this)

        if (getSavedThemeState()) {
            adapter.dark = true
            adapter.addAll(listDark)
            val theme = resources.newTheme()
            switchMode.setTextColor(resources.getColor(R.color.white, theme))
            fullName.setTextColor(resources.getColor(R.color.white, theme))
            balance.setTextColor(resources.getColor(R.color.white, theme))
            switchBal.imageTintList = resources.getColorStateList(R.color.white, theme)
            view.setBackgroundColor(getResources().getColor(R.color.blue_primary_shade1))

        } else {
            adapter.dark = false
            adapter.addAll(listLight)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        return view
    }

    override fun onClick(id: Int) {
        when (id) {
            1 -> {
                startActivity(
                    Intent(
                        this@ProfileFragment.requireContext(),
                        SendPackageActivity::class.java
                    )
                )
            }

            2 -> {
                startActivity(
                    Intent(
                        this@ProfileFragment.requireContext(),
                        NotificationActivity::class.java
                    )
                )
            }

            6 -> {
                val supabase = createSupabaseClient(
                    supabaseUrl = "https://cvrihxuwwzuqfxulrjne.supabase.co",
                    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN2cmloeHV3d3p1cWZ4dWxyam5lIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDkxNDM2MDEsImV4cCI6MjAyNDcxOTYwMX0.tBvqGRNPAmzW3BLfI-6KRUxPREzEsbgatzROVBcLDJY"
                ) {
                    install(Auth)
                }
                lifecycleScope.launch(Dispatchers.IO) {
                    supabase.auth.signOut()
                }
                val intent = Intent(
                    this@ProfileFragment.requireContext(),
                    SplashScreen::class.java
                )
                startActivity(intent)
                activity?.finish()
            }

            else -> {
                Toast.makeText(
                    this@ProfileFragment.requireContext(),
                    "coming soon...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }

    private fun saveThemeState(isDarkTheme: Boolean) {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(Constances.KEY, isDarkTheme)
        editor.apply()
    }
}