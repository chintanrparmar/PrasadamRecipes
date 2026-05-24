package app.chintan.prasadam.app

import android.app.Application
import app.chintan.prasadam.data.source.DataSeedManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application class annotated with [@HiltAndroidApp] to enable Hilt DI.
 *
 * On creation, [DataSeedManager.seedIfNeeded] is called to asynchronously
 * import [recipes.json] into Room on first install.
 */
@HiltAndroidApp
class PrasadamApp : Application() {

    @Inject
    lateinit var dataSeedManager: DataSeedManager

    override fun onCreate() {
        super.onCreate()
        dataSeedManager.seedIfNeeded()
    }
}
