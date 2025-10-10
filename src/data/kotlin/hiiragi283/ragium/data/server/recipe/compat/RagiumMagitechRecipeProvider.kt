package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTWoodType
import hiiragi283.ragium.api.registry.toId
import hiiragi283.ragium.api.tag.createTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.stln.magitech.block.BlockInit

object RagiumMagitechRecipeProvider : HTRecipeProvider.Integration(RagiumConst.MAGITECH) {
    override fun buildRecipeInternal() {
        HTMagitechWoodType.entries.forEach(::addWoodSawing)
    }

    enum class HTMagitechWoodType(path: String, override val planks: ItemLike) : HTWoodType {
        CELIFERN("celifern_logs", BlockInit.CELIFERN_PLANKS),
        CHARCOAL_BIRCH("charcoal_birch_logs", BlockInit.CHARCOAL_BIRCH_PLANKS),
        ;

        override val log: TagKey<Item> = Registries.ITEM.createTagKey(RagiumConst.MAGITECH.toId(path))

        override fun materialName(): String = name.lowercase()
    }
}
