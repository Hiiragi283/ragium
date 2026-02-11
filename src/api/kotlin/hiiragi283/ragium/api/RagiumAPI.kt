package hiiragi283.ragium.api

import com.mojang.logging.LogUtils
import hiiragi283.core.api.resource.toId
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger

data object RagiumAPI {
    const val MOD_ID = "ragium"

    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @JvmStatic
    fun id(path: String): ResourceLocation = MOD_ID.toId(path)

    @JvmStatic
    fun id(vararg path: String): ResourceLocation = MOD_ID.toId(*path)
}
