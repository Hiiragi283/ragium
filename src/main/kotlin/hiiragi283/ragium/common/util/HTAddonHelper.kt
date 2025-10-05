package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.HTAddon
import net.neoforged.fml.ModList
import net.neoforged.neoforgespi.language.ModFileScanData
import org.objectweb.asm.Type
import java.lang.reflect.Constructor

internal object HTAddonHelper {
    @JvmStatic
    inline fun <reified T : Any> collectInstances(): List<T> {
        val annotationType: Type = Type.getType(HTAddon::class.java)
        val modList: ModList = ModList.get()
        return buildList {
            for (scanData: ModFileScanData in modList.allScanData) {
                for (data: ModFileScanData.AnnotationData in scanData.annotations) {
                    if (data.annotationType == annotationType) {
                        val modId: String = data.annotationData["value"] as String
                        val priority: Int = data.annotationData["priority"] as? Int ?: 0
                        if (modList.isLoaded(modId)) {
                            add(data.memberName to priority)
                        }
                    }
                }
            }
        }.sortedBy { (_, priority: Int) -> priority }
            .map(Pair<String, Int>::first)
            .mapNotNull { className: String ->
                runCatching {
                    val clazz: Class<*> = Class.forName(className)
                    // Try to load from singleton instance
                    if (clazz.kotlin.objectInstance != null) {
                        return@runCatching clazz.kotlin.objectInstance as? T
                    }
                    // Try to load from constructor with no parameter
                    val asmClazz: Class<out T> = clazz.asSubclass(T::class.java)
                    val constructor: Constructor<out T> = asmClazz.getDeclaredConstructor()
                    val instance: T = constructor.newInstance()
                    instance
                }.onFailure { throwable: Throwable ->
                    RagiumAPI.LOGGER.error("Failed to construct {}", className, throwable)
                }.getOrNull()
            }
    }
}
