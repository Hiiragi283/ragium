package hiiragi283.ragium.api

import hiiragi283.core.api.resource.toId
import net.minecraft.resources.ResourceLocation

object RagiumAPI {
    const val MOD_ID = "ragium"

    @JvmStatic
    fun id(path: String): ResourceLocation = MOD_ID.toId(path)

    @JvmStatic
    fun id(vararg path: String): ResourceLocation = MOD_ID.toId(*path)
}
