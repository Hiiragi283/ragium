package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTWoodType
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.toId
import hiiragi283.ragium.api.tag.createTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object RagiumMagitechRecipeProvider : HTRecipeProvider.Integration(RagiumConst.MAGITECH) {
    override fun buildRecipeInternal() {
        HTMagitechWoodType.entries.forEach(::addWoodSawing)
    }

    enum class HTMagitechWoodType(path: String, override val planks: HTItemHolderLike) : HTWoodType {
        CELIFERN("celifern"),
        CHARCOAL_BIRCH("charcoal_birch"),
        ;

        constructor(path: String, planks: String) : this(path, HTItemHolderLike.fromId(RagiumConst.MAGITECH.toId(planks)))

        constructor(name: String) : this("${name}_logs", "${name}_planks")

        override val log: TagKey<Item> = Registries.ITEM.createTagKey(RagiumConst.MAGITECH.toId(path))

        override fun getSerializedName(): String = name.lowercase()
    }
}
