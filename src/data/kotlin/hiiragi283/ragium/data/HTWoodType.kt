package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.extension.toId
import hiiragi283.ragium.api.material.HTMaterialType
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.stln.magitech.block.BlockInit

enum class HTWoodType(val log: TagKey<Item>, val planks: ItemLike) : HTMaterialType {
    // Vanilla
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

    // Magitech
    CELIFERN(itemTagKey(RagiumConst.MAGITECH.toId("celifern_logs")), BlockInit.CELIFERN_PLANKS),
    CHARCOAL_BIRCH(itemTagKey(RagiumConst.MAGITECH.toId("charcoal_birch_logs")), BlockInit.CHARCOAL_BIRCH_PLANKS),
    ;

    val modId: String = log.location.namespace

    fun getId(path: String): ResourceLocation = modId.toId(path)

    override fun getSerializedName(): String = name.lowercase()
}
