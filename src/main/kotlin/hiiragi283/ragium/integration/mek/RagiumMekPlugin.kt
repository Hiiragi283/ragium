package hiiragi283.ragium.integration.mek

import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.TriConsumer
import hiiragi283.ragium.api.util.collection.HTWrappedTable
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import mekanism.common.registration.impl.BlockRegistryObject
import mekanism.common.registration.impl.ItemRegistryObject
import mekanism.common.registries.MekanismBlocks
import mekanism.common.registries.MekanismItems
import mekanism.common.resource.IResource
import mekanism.common.resource.PrimaryResource
import mekanism.common.resource.ResourceType
import mekanism.common.resource.ore.OreBlockType
import mekanism.common.resource.ore.OreType
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

object RagiumMekPlugin : RagiumPlugin {
    override val priority: Int = 0

    @JvmField
    val OSMIUM: HTMaterialKey = HTMaterialKey.of("osmium")

    @JvmField
    val REFINED_GLOWSTONE: HTMaterialKey = HTMaterialKey.of("refined_glowstone")

    @JvmField
    val REFINED_OBSIDIAN: HTMaterialKey = HTMaterialKey.of("refined_obsidian")

    override fun registerMaterial(helper: RagiumPlugin.MaterialHelper) {
        helper.register(REFINED_GLOWSTONE, HTMaterialType.ALLOY)
        helper.register(REFINED_OBSIDIAN, HTMaterialType.ALLOY)

        helper.register(OSMIUM, HTMaterialType.METAL)
    }

    override fun bindMaterialToItem(consumer: TriConsumer<HTTagPrefix, HTMaterialKey, Supplier<out ItemLike>>) {
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.BRONZE, MekanismBlocks.BRONZE_BLOCK::asItem)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.STEEL, MekanismBlocks.STEEL_BLOCK::asItem)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, REFINED_GLOWSTONE, MekanismBlocks.REFINED_GLOWSTONE_BLOCK::asItem)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, REFINED_OBSIDIAN, MekanismBlocks.REFINED_OBSIDIAN_BLOCK::asItem)
        consumer.accept(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.FLUORITE, MekanismBlocks.FLUORITE_BLOCK::asItem)

        MekanismBlocks.PROCESSED_RESOURCE_BLOCKS.forEach { (resource: IResource, obj: BlockRegistryObject<*, *>) ->
            consumer.accept(HTTagPrefix.STORAGE_BLOCK, HTMaterialKey.of(resource.registrySuffix), obj::asItem)
        }

        HTWrappedTable(
            MekanismItems.PROCESSED_RESOURCES,
        ).forEach { (type: ResourceType, resource: PrimaryResource, obj: ItemRegistryObject<Item>) ->
            val prefix: HTTagPrefix = HTTagPrefix.fromName(type.baseTagPath) ?: return@forEach
            consumer.accept(prefix, HTMaterialKey.of(resource.registrySuffix), obj)
        }

        consumer.accept(HTTagPrefix.DUST, RagiumMaterialKeys.BRONZE, MekanismItems.BRONZE_DUST)
        consumer.accept(HTTagPrefix.DUST, RagiumMaterialKeys.COAL, MekanismItems.COAL_DUST)
        consumer.accept(HTTagPrefix.DUST, RagiumMaterialKeys.DIAMOND, MekanismItems.DIAMOND_DUST)
        consumer.accept(HTTagPrefix.DUST, RagiumMaterialKeys.EMERALD, MekanismItems.EMERALD_DUST)
        consumer.accept(HTTagPrefix.DUST, RagiumMaterialKeys.LAPIS, MekanismItems.LAPIS_LAZULI_DUST)
        consumer.accept(HTTagPrefix.DUST, RagiumMaterialKeys.NETHERITE, MekanismItems.NETHERITE_DUST)
        consumer.accept(HTTagPrefix.DUST, RagiumMaterialKeys.QUARTZ, MekanismItems.QUARTZ_DUST)
        consumer.accept(HTTagPrefix.DUST, RagiumMaterialKeys.SALT, MekanismItems.SALT)
        consumer.accept(HTTagPrefix.DUST, RagiumMaterialKeys.STEEL, MekanismItems.STEEL_DUST)
        consumer.accept(HTTagPrefix.DUST, RagiumMaterialKeys.SULFUR, MekanismItems.SULFUR_DUST)

        consumer.accept(HTTagPrefix.GEM, RagiumMaterialKeys.FLUORITE, MekanismItems.FLUORITE_GEM)

        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.BRONZE, MekanismItems.BRONZE_INGOT)
        consumer.accept(HTTagPrefix.INGOT, RagiumMaterialKeys.STEEL, MekanismItems.STEEL_INGOT)
        consumer.accept(HTTagPrefix.INGOT, REFINED_GLOWSTONE, MekanismItems.REFINED_GLOWSTONE_INGOT)
        consumer.accept(HTTagPrefix.INGOT, REFINED_OBSIDIAN, MekanismItems.REFINED_OBSIDIAN_INGOT)

        consumer.accept(HTTagPrefix.NUGGET, RagiumMaterialKeys.BRONZE, MekanismItems.BRONZE_NUGGET)
        consumer.accept(HTTagPrefix.NUGGET, RagiumMaterialKeys.STEEL, MekanismItems.STEEL_NUGGET)
        consumer.accept(HTTagPrefix.NUGGET, REFINED_GLOWSTONE, MekanismItems.REFINED_GLOWSTONE_NUGGET)
        consumer.accept(HTTagPrefix.NUGGET, REFINED_OBSIDIAN, MekanismItems.REFINED_OBSIDIAN_NUGGET)

        MekanismBlocks.ORES.forEach { (type: OreType, ore: OreBlockType) ->
            consumer.accept(HTTagPrefix.ORE, HTMaterialKey.of(type.serializedName), ore.stone::asItem)
            consumer.accept(HTTagPrefix.ORE, HTMaterialKey.of(type.serializedName), ore.deepslate::asItem)
        }
    }
}
