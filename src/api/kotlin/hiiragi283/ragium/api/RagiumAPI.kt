package hiiragi283.ragium.api

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.registry.toId
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger
import java.util.ServiceLoader

/**
 * @see [mekanism.api.MekanismAPI]
 */
object RagiumAPI {
    /**
     * Ragiumで使用する名前空間
     */
    const val MOD_ID = "ragium"

    /**
     * Ragiumの表示名
     */
    const val MOD_NAME = "Ragium"

    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    /**
     * 名前空間が`ragium`となる[ResourceLocation]を返します。
     */
    @JvmStatic
    fun id(path: String): ResourceLocation = MOD_ID.toId(path)

    /**
     * 名前空間が`ragium`となる[ResourceLocation]を返します。
     */
    @JvmStatic
    fun id(prefix: String, suffix: String): ResourceLocation = id("$prefix/$suffix")

    /**
     * 名前空間が`ragium`となる[ResourceLocation]を返します。
     */
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
