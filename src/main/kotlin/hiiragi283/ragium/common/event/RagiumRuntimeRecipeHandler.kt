package hiiragi283.ragium.common.event

import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.material.HTMaterialDefinition
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.getStorageAttribute
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import net.minecraft.world.item.Item
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeRecipeHandler {
    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        prefixToDust(event, HCMaterialPrefixes.FUEL, 1, 1)
        prefixToDust(event, HCMaterialPrefixes.GEAR, 1, 4)
        prefixToDust(event, HCMaterialPrefixes.GEM, 1, 1)
        prefixToDust(event, HCMaterialPrefixes.INGOT, 1, 1)
        prefixToDust(event, HCMaterialPrefixes.NUGGET, 9, 1)
        prefixToDust(event, HCMaterialPrefixes.PEARL, 1, 1)
        prefixToDust(event, HCMaterialPrefixes.PLATE, 1, 1)
        prefixToDust(event, HCMaterialPrefixes.RAW_MATERIAL, 3, 4)
        prefixToDust(event, HCMaterialPrefixes.ROD, 2, 1)
        prefixToDust(event, HCMaterialPrefixes.WIRE, 2, 1)

        blockToPlate(event)
        ingotToRod(event)

        ingotToPrefix(event, HCMaterialPrefixes.STORAGE_BLOCK, HTMoldType.BLOCK, { it.getStorageAttribute().baseCount }, 1)
        ingotToPrefix(event, HCMaterialPrefixes.GEAR, HTMoldType.GEAR, { 4 }, 1)
        ingotToPrefix(event, HCMaterialPrefixes.NUGGET, HTMoldType.NUGGET, { 1 }, 9)
        ingotToPrefix(event, HCMaterialPrefixes.PLATE, HTMoldType.PLATE, { 1 }, 1)
        ingotToPrefix(event, HCMaterialPrefixes.ROD, HTMoldType.ROD, { 1 }, 2)
    }

    //    Crushing    //

    @JvmStatic
    private fun prefixToDust(
        event: HTRegisterRuntimeRecipeEvent,
        prefix: HTPrefixLike,
        inputCount: Int,
        outputCount: Int,
    ) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in event.materialManager.entries) {
            if (!event.isPresentTag(prefix, key)) continue
            val dust: Item = event.getFirstHolder(HCMaterialPrefixes.DUST, key)?.value() ?: continue

            HTChancedRecipeBuilder
                .crushing(
                    event.itemCreator.fromTagKey(prefix, key, inputCount),
                    event.itemResult.create(dust, outputCount),
                ).saveSuffixed(event.output, "_from_${prefix.asPrefixName()}")
        }
    }

    //    Cutting    //

    @JvmStatic
    private fun blockToPlate(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in event.materialManager.entries) {
            if (!event.isPresentTag(HCMaterialPrefixes.STORAGE_BLOCK, key)) continue
            val plate: Item = event.getFirstHolder(HCMaterialPrefixes.PLATE, key)?.value() ?: continue

            HTChancedRecipeBuilder
                .cutting(
                    event.itemCreator.fromTagKey(HCMaterialPrefixes.STORAGE_BLOCK, key),
                    event.itemResult.create(plate, definition.getStorageAttribute().baseCount),
                ).saveSuffixed(event.output, "_from_block")
        }
    }

    @JvmStatic
    private fun ingotToRod(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in event.materialManager.entries) {
            if (!event.isPresentTag(HCMaterialPrefixes.INGOT, key)) continue
            val rod: Item = event.getFirstHolder(HCMaterialPrefixes.ROD, key)?.value() ?: continue

            HTChancedRecipeBuilder
                .cutting(
                    event.itemCreator.fromTagKey(HCMaterialPrefixes.INGOT, key),
                    event.itemResult.create(rod, 2),
                ).saveSuffixed(event.output, "_from_ingot")
        }
    }

    //    Pressing    //

    @JvmStatic
    private fun ingotToPrefix(
        event: HTRegisterRuntimeRecipeEvent,
        prefix: HTPrefixLike,
        moldType: HTMoldType,
        inputCountGetter: (HTMaterialDefinition) -> Int?,
        outputCount: Int,
    ) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in event.materialManager.entries) {
            val inputCount: Int = inputCountGetter(definition) ?: continue

            if (!event.isPresentTag(HCMaterialPrefixes.INGOT, key)) continue
            val result: Item = event.getFirstHolder(prefix, key)?.value() ?: continue

            HTSingleRecipeBuilder
                .pressing(
                    event.itemCreator.fromTagKey(HCMaterialPrefixes.INGOT, key, inputCount),
                    event.itemCreator.fromItem(moldType),
                    event.itemResult.create(result, outputCount),
                ).saveSuffixed(event.output, "_from_ingot")
        }
    }
}
