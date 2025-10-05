package hiiragi283.ragium.api

import hiiragi283.ragium.api.extension.toId
import net.minecraft.resources.ResourceLocation
import java.util.ServiceLoader

/**
 * @see [mekanism.api.MekanismAPI]
 */
object RagiumAPI {
    const val MOD_ID = "ragium"
    const val MOD_NAME = "Ragium"

    /**
     * 名前空間が`ragium`となる[ResourceLocation]を返します。
     */
    @JvmStatic
    fun id(path: String): ResourceLocation = MOD_ID.toId(path)

    @JvmStatic
    fun id(prefix: String, suffix: String): ResourceLocation = id("$prefix/$suffix")

    @JvmStatic
    fun wrapId(other: ResourceLocation): ResourceLocation = when (other.namespace) {
        MOD_ID -> other
        else -> id(other.path)
    }

    /**
     * @see [mekanism.api.MekanismAPI.getService]
     */
    @Suppress("UnstableApiUsage")
    @JvmStatic
    inline fun <reified SERVICE : Any> getService(): SERVICE =
        ServiceLoader.load(SERVICE::class.java, RagiumAPI::class.java.classLoader).first()
}
