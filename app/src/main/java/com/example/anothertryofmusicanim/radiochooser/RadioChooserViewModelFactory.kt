import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.anothertryofmusicanim.radiochooser.RadioChooserViewModel
import kotlinx.serialization.ImplicitReflectionSerializer

class RadioChooserViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @ImplicitReflectionSerializer
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RadioChooserViewModelFactory::class.java)) {
            return RadioChooserViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}