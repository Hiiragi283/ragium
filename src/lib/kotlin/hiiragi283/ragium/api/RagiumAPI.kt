package hiiragi283.ragium.api

import hiiragi283.lib.resource.toId
import java.util.ServiceLoader
import net.minecraft.resources.Identifier

data object RagiumAPI {
    const val MOD_ID = "ragium"

    @JvmStatic
    fun id(path: String): Identifier = MOD_ID.toId(path)

    @JvmStatic
    fun id(vararg path: String): Identifier = MOD_ID.toId(*path)

    @JvmStatic
    inline fun <reified T : Any> getService(): T = ServiceLoader.load(T::class.java, RagiumAPI::class.java.classLoader).single()
}
