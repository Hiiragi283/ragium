package hiiragi283.ragium.api

import com.mojang.logging.LogUtils
import hiiragi283.lib.resource.toId
import java.util.ServiceLoader
import net.minecraft.resources.Identifier
import org.slf4j.Logger

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object RagiumAPI {
    /**
     * RagiumのMOD ID
     */
    const val MOD_ID = "ragium"

    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    /**
     * 名前空間が[ragium][MOD_ID]となる[Identifier]を返します。
     */
    @JvmStatic
    fun id(path: String): Identifier = MOD_ID.toId(path)

    /**
     * 名前空間が[ragium][MOD_ID]となる[Identifier]を返します。
     */
    @JvmStatic
    fun id(vararg path: String): Identifier = MOD_ID.toId(*path)

    @JvmStatic
    inline fun <reified T : Any> getService(): T = ServiceLoader.load(T::class.java, RagiumAPI::class.java.classLoader).single()
}
