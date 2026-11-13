package cl.mlopez.recordatorios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import cl.mlopez.recordatorios.ui.navigation.AppNavHost
import cl.mlopez.recordatorios.ui.theme.RecordatoriosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecordatoriosTheme {
                // El NavController se crea en el nivel superior y solo lo usa el NavHost.
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}
