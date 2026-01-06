package hiiragi283.ragium.api

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.createTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object RagiumTags {
    //    Items    //

    object Items {
        @JvmField
        val FOODS_CAN: TagKey<Item> = common("foods", "can")

        @JvmField
        val MOLDS: TagKey<Item> = mod("molds")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(HTConst.COMMON.toId(*path))

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(RagiumAPI.id(*path))
    }
}
