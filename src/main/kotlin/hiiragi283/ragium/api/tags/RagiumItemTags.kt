package hiiragi283.ragium.api.tags

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object RagiumItemTags {
    @JvmField
    val ALKALI: TagKey<Item> = create("alkali")

    /**
     * Act as fluid filter for [hiiragi283.ragium.common.block.transfer.HTExporterBlockEntityBase]
     */
    @JvmField
    val FLUID_EXPORTER_FILTERS: TagKey<Item> = create(RagiumAPI.MOD_ID, "exporter_filters/fluid")

    /**
     * Act as item filter for [hiiragi283.ragium.common.block.transfer.HTExporterBlockEntityBase]
     */
    @JvmField
    val ITEM_EXPORTER_FILTERS: TagKey<Item> = create(RagiumAPI.MOD_ID, "exporter_filters/item")

    @JvmField
    val PROTEIN_FOODS: TagKey<Item> = create("foods/protein")

    @JvmField
    val SILICON: TagKey<Item> = create("silicon")

    @JvmField
    val SILICON_PLATES: TagKey<Item> = create("plates/silicon")

    @JvmField
    val REFINED_SILICON_PLATES: TagKey<Item> = create("plates/refined_silicon")

    /**
     * Upgrades [hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase] tier to [hiiragi283.ragium.api.machine.HTMachineTier.ADVANCED]
     */
    @JvmField
    val ADVANCED_UPGRADES: TagKey<Item> = create(RagiumAPI.MOD_ID, "upgrades/advanced")

    /**
     * Upgrades [hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase] tier to [hiiragi283.ragium.api.machine.HTMachineTier.BASIC]
     */
    @JvmField
    val BASIC_UPGRADES: TagKey<Item> = create(RagiumAPI.MOD_ID, "upgrades/basic")

    @JvmStatic
    fun create(namespace: String, path: String): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.of(namespace, path))

    @JvmStatic
    fun create(path: String): TagKey<Item> = create(TagUtil.C_TAG_NAMESPACE, path)
}
