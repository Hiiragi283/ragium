package hiiragi283.ragium.common.event

import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.material.HTAbstractMaterial
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.material.RagiumMaterial
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

        ingotToPrefix(event, HCMaterialPrefixes.STORAGE_BLOCK, HTMoldType.BLOCK, 9, 1)
        ingotToPrefix(event, HCMaterialPrefixes.GEAR, HTMoldType.GEAR, 4, 1)
        ingotToPrefix(event, HCMaterialPrefixes.NUGGET, HTMoldType.NUGGET, 1, 9)
        ingotToPrefix(event, HCMaterialPrefixes.PLATE, HTMoldType.PLATE, 1, 1)
        ingotToPrefix(event, HCMaterialPrefixes.ROD, HTMoldType.ROD, 1, 2)
    }

    @JvmStatic
    private fun getAllMaterials(): Set<HTAbstractMaterial> = HCMaterial.entries.plus(RagiumMaterial.entries)

    //    Crushing    //

    @JvmStatic
    private fun prefixToDust(
        event: HTRegisterRuntimeRecipeEvent,
        prefix: HTPrefixLike,
        inputCount: Int,
        outputCount: Int,
    ) {
        for (material: HTAbstractMaterial in getAllMaterials()) {
            if (!event.isPresentTag(prefix, material)) continue
            val dust: Item = event.getFirstHolder(HCMaterialPrefixes.DUST, material)?.value() ?: continue

            HTChancedRecipeBuilder
                .crushing(
                    event.itemCreator.fromTagKey(prefix, material, inputCount),
                    event.itemResult.create(dust, outputCount),
                ).saveSuffixed(event.output, "_from_${prefix.asPrefixName()}")
        }
    }

    //    Cutting    //

    @JvmStatic
    private fun blockToPlate(event: HTRegisterRuntimeRecipeEvent) {
        for (material: HTAbstractMaterial in getAllMaterials()) {
            if (!event.isPresentTag(HCMaterialPrefixes.STORAGE_BLOCK, material)) continue
            val plate: Item = event.getFirstHolder(HCMaterialPrefixes.PLATE, material)?.value() ?: continue

            HTChancedRecipeBuilder
                .cutting(
                    event.itemCreator.fromTagKey(HCMaterialPrefixes.STORAGE_BLOCK, material),
                    event.itemResult.create(plate, 9),
                ).saveSuffixed(event.output, "_from_block")
        }
    }

    @JvmStatic
    private fun ingotToRod(event: HTRegisterRuntimeRecipeEvent) {
        for (material: HTAbstractMaterial in getAllMaterials()) {
            if (!event.isPresentTag(HCMaterialPrefixes.INGOT, material)) continue
            val rod: Item = event.getFirstHolder(HCMaterialPrefixes.ROD, material)?.value() ?: continue

            HTChancedRecipeBuilder
                .cutting(
                    event.itemCreator.fromTagKey(HCMaterialPrefixes.INGOT, material),
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
        inputCount: Int,
        outputCount: Int,
    ) {
        for (material: HTAbstractMaterial in getAllMaterials()) {
            if (!event.isPresentTag(HCMaterialPrefixes.INGOT, material)) continue
            val result: Item = event.getFirstHolder(prefix, material)?.value() ?: continue

            HTSingleRecipeBuilder
                .pressing(
                    event.itemCreator.fromTagKey(HCMaterialPrefixes.INGOT, material, inputCount),
                    event.itemCreator.fromItem(moldType),
                    event.itemResult.create(result, outputCount),
                ).saveSuffixed(event.output, "_from_ingot")
        }
    }
}
