package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.RagiumAPI
import net.neoforged.fml.ModList
import net.neoforged.neoforgespi.language.ModFileScanData
import org.objectweb.asm.Type
import java.lang.reflect.Constructor

internal object HTAddonHelper {
    @JvmStatic
    inline fun <reified T : Any> collectInstances(): List<T> {
        val annotationType: Type = Type.getType(HTAddon::class.java)
        return ModList
            .get()
            .allScanData
            .flatMap(ModFileScanData::getAnnotations)
            .filter { it.annotationType == annotationType }
            .map(ModFileScanData.AnnotationData::memberName)
            .mapNotNull { className: String ->
                runCatching {
                    val clazz: Class<*> = Class.forName(className)
                    val asmClazz: Class<out T> = clazz.asSubclass(T::class.java)
                    // Try to load from singleton instance
                    asmClazz.kotlin.objectInstance ?: run {
                        // Try to load from constructor with no parameter
                        val constructor: Constructor<out T> = asmClazz.getDeclaredConstructor()
                        constructor.newInstance()
                    }
                }.onFailure { throwable: Throwable ->
                    RagiumAPI.LOGGER.error("Failed to construct {}", className, throwable)
                }.getOrNull()
            }
    }

    annotation class HTAddon
}
