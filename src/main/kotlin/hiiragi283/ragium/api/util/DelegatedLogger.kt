package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.RagiumAPI
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class DelegatedLogger {
    operator fun provideDelegate(thisRef: Any, property: KProperty<*>): ReadOnlyProperty<Any, Logger> {
        val className: String = thisRef::class.java.name.removeSuffix("\$Companion")
        val logger: Logger = LoggerFactory.getLogger("${RagiumAPI.MOD_NAME}:$className")
        return ReadOnlyProperty { _, _: KProperty<*> -> logger }
    }
}
