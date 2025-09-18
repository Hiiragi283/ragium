package hiiragi283.ragium.impl

import hiiragi283.ragium.api.data.recipe.HTWoodType
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

enum class HTVanillaWoodType(override val log: TagKey<Item>, override val planks: ItemLike) : HTWoodType {
    OAK(ItemTags.OAK_LOGS, Items.OAK_PLANKS),
    SPRUCE(ItemTags.SPRUCE_LOGS, Items.SPRUCE_PLANKS),
    BIRCH(ItemTags.BIRCH_LOGS, Items.BIRCH_PLANKS),
    JUNGLE(ItemTags.JUNGLE_LOGS, Items.JUNGLE_PLANKS),
    ACACIA(ItemTags.ACACIA_LOGS, Items.ACACIA_PLANKS),
    CHERRY(ItemTags.CHERRY_LOGS, Items.CHERRY_PLANKS),
    DARK_OAK(ItemTags.DARK_OAK_LOGS, Items.DARK_OAK_PLANKS),
    MANGROVE(ItemTags.MANGROVE_LOGS, Items.MANGROVE_PLANKS),
    BAMBOO(ItemTags.BAMBOO_BLOCKS, Items.BAMBOO_PLANKS),
    CRIMSON(ItemTags.CRIMSON_STEMS, Items.CRIMSON_PLANKS),
    WARPED(ItemTags.WARPED_STEMS, Items.WARPED_PLANKS),
    ;

    override fun getSerializedName(): String = name.lowercase()
}
