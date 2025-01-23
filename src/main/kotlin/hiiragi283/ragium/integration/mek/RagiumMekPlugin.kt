package hiiragi283.ragium.integration.mek

import hiiragi283.ragium.api.RagiumIMC
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTRegisterMaterialEvent
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.collection.HTWrappedTable
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import mekanism.api.MekanismAPI
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
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = MekanismAPI.MEKANISM_MODID, bus = EventBusSubscriber.Bus.MOD)
object RagiumMekPlugin {
    @JvmField
    val OSMIUM: HTMaterialKey = HTMaterialKey.of("osmium")

    @JvmField
    val REFINED_GLOWSTONE: HTMaterialKey = HTMaterialKey.of("refined_glowstone")

    @JvmField
    val REFINED_OBSIDIAN: HTMaterialKey = HTMaterialKey.of("refined_obsidian")

    @SubscribeEvent
    fun registerMaterial(event: HTRegisterMaterialEvent) {
        // Register Material
        event.register(REFINED_GLOWSTONE, HTMaterialType.ALLOY)
        event.register(REFINED_OBSIDIAN, HTMaterialType.ALLOY)

        event.register(OSMIUM, HTMaterialType.METAL)
        // Bind Item
        RagiumIMC.sendMaterialItemIMC(
            HTTagPrefix.STORAGE_BLOCK,
            RagiumMaterialKeys.BRONZE,
            MekanismBlocks.BRONZE_BLOCK::asItem,
        )
        RagiumIMC.sendMaterialItemIMC(
            HTTagPrefix.STORAGE_BLOCK,
            RagiumMaterialKeys.STEEL,
            MekanismBlocks.STEEL_BLOCK::asItem,
        )
        RagiumIMC.sendMaterialItemIMC(
            HTTagPrefix.STORAGE_BLOCK,
            REFINED_GLOWSTONE,
            MekanismBlocks.REFINED_GLOWSTONE_BLOCK::asItem,
        )
        RagiumIMC.sendMaterialItemIMC(
            HTTagPrefix.STORAGE_BLOCK,
            REFINED_OBSIDIAN,
            MekanismBlocks.REFINED_OBSIDIAN_BLOCK::asItem,
        )
        RagiumIMC.sendMaterialItemIMC(
            HTTagPrefix.STORAGE_BLOCK,
            RagiumMaterialKeys.FLUORITE,
            MekanismBlocks.FLUORITE_BLOCK::asItem,
        )

        MekanismBlocks.PROCESSED_RESOURCE_BLOCKS.forEach { (resource: IResource, obj: BlockRegistryObject<*, *>) ->
            RagiumIMC.sendMaterialItemIMC(
                HTTagPrefix.STORAGE_BLOCK,
                HTMaterialKey.of(resource.registrySuffix),
                obj::asItem,
            )
        }

        HTWrappedTable(
            MekanismItems.PROCESSED_RESOURCES,
        ).forEach { (type: ResourceType, resource: PrimaryResource, obj: ItemRegistryObject<Item>) ->
            val prefix: HTTagPrefix = HTTagPrefix.fromName(type.baseTagPath) ?: return@forEach
            RagiumIMC.sendMaterialItemIMC(prefix, HTMaterialKey.of(resource.registrySuffix), obj)
        }

        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.DUST, RagiumMaterialKeys.BRONZE, MekanismItems.BRONZE_DUST)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.DUST, RagiumMaterialKeys.COAL, MekanismItems.COAL_DUST)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.DUST, RagiumMaterialKeys.DIAMOND, MekanismItems.DIAMOND_DUST)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.DUST, RagiumMaterialKeys.EMERALD, MekanismItems.EMERALD_DUST)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.DUST, RagiumMaterialKeys.LAPIS, MekanismItems.LAPIS_LAZULI_DUST)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.DUST, RagiumMaterialKeys.NETHERITE, MekanismItems.NETHERITE_DUST)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.DUST, RagiumMaterialKeys.QUARTZ, MekanismItems.QUARTZ_DUST)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.DUST, RagiumMaterialKeys.SALT, MekanismItems.SALT)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.DUST, RagiumMaterialKeys.STEEL, MekanismItems.STEEL_DUST)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.DUST, RagiumMaterialKeys.SULFUR, MekanismItems.SULFUR_DUST)

        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.GEM, RagiumMaterialKeys.FLUORITE, MekanismItems.FLUORITE_GEM)

        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.INGOT, RagiumMaterialKeys.BRONZE, MekanismItems.BRONZE_INGOT)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.INGOT, RagiumMaterialKeys.STEEL, MekanismItems.STEEL_INGOT)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.INGOT, REFINED_GLOWSTONE, MekanismItems.REFINED_GLOWSTONE_INGOT)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.INGOT, REFINED_OBSIDIAN, MekanismItems.REFINED_OBSIDIAN_INGOT)

        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.NUGGET, RagiumMaterialKeys.BRONZE, MekanismItems.BRONZE_NUGGET)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.NUGGET, RagiumMaterialKeys.STEEL, MekanismItems.STEEL_NUGGET)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.NUGGET, REFINED_GLOWSTONE, MekanismItems.REFINED_GLOWSTONE_NUGGET)
        RagiumIMC.sendMaterialItemIMC(HTTagPrefix.NUGGET, REFINED_OBSIDIAN, MekanismItems.REFINED_OBSIDIAN_NUGGET)

        MekanismBlocks.ORES.forEach { (type: OreType, ore: OreBlockType) ->
            RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, HTMaterialKey.of(type.serializedName), ore.stone::asItem)
            RagiumIMC.sendMaterialItemIMC(HTTagPrefix.ORE, HTMaterialKey.of(type.serializedName), ore.deepslate::asItem)
        }
    }
}
