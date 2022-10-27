package cd.infoset.x990sdk

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.vfi.smartpos.deviceservice.aidl.IDeviceService
import com.vfi.smartpos.deviceservice.aidl.IPrinter

class Services {
    var deviceService: IDeviceService? = null
    var printer: IPrinter? = null

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            deviceService = IDeviceService.Stub.asInterface(service)
            try {
                printer = deviceService?.printer
                Log.d("Services", "bind service success")
            }catch (e: Exception) {
                Log.e("Services", e.message, e)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("Services", "${name?.packageName} is disconnected")
            deviceService = null
        }
    }

    fun connectService(context: Context): Boolean {
        val intent = Intent()
        intent.action = "com.vfi.smartpos.device_service"
        intent.`package` = "com.vfi.smartpos.deviceservice"
        return context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }
}