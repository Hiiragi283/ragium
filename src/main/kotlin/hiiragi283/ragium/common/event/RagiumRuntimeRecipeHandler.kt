package hiiragi283.ragium.common.event

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.material.HTMaterialDefinition
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.getDefaultPrefix
import hiiragi283.core.api.material.getStorageAttribute
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeRecipeHandler {
    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        crushPrefixToDust(event, HCMaterialPrefixes.STORAGE_BLOCK, 1) { it.getStorageAttribute().baseCount }
        crushPrefixToDust(event, HCMaterialPrefixes.STORAGE_BLOCK_RAW, 1) { 12 }

        crushPrefixToDust(event, HCMaterialPrefixes.FUEL, 1)
        crushPrefixToDust(event, HCMaterialPrefixes.GEAR, 1) { 4 }
        crushPrefixToDust(event, HCMaterialPrefixes.GEM, 1)
        crushPrefixToDust(event, HCMaterialPrefixes.INGOT, 1)
        crushPrefixToDust(event, HCMaterialPrefixes.NUGGET, 9)
        crushPrefixToDust(event, HCMaterialPrefixes.PEARL, 1)
        crushPrefixToDust(event, HCMaterialPrefixes.PLATE, 1)
        crushPrefixToDust(event, HCMaterialPrefixes.RAW_MATERIAL, 3) { 4 }
        crushPrefixToDust(event, HCMaterialPrefixes.ROD, 2)
        crushPrefixToDust(event, HCMaterialPrefixes.WIRE, 2)

        cutBlockToPlate(event)
        cutBaseToRod(event)

        pressBaseToPrefix(event, HCMaterialPrefixes.STORAGE_BLOCK, HTMoldType.BLOCK, { it.getStorageAttribute().baseCount }, 1)
        pressBaseToPrefix(event, HCMaterialPrefixes.GEAR, HTMoldType.GEAR, { 4 }, 1)
        pressBaseToPrefix(event, HCMaterialPrefixes.NUGGET, HTMoldType.NUGGET, { 1 }, 9)
        pressBaseToPrefix(event, HCMaterialPrefixes.PLATE, HTMoldType.PLATE, { 1 }, 1)

        meltAndSolidify(event, HCMaterialPrefixes.STORAGE_BLOCK, HTMoldType.BLOCK) {
            HTConst.INGOT_AMOUNT * it.getStorageAttribute().baseCount
        }
        meltAndSolidify(event, HCMaterialPrefixes.DUST, null)
        meltAndSolidify(event, HCMaterialPrefixes.FUEL, HTMoldType.GEM)
        meltAndSolidify(event, HCMaterialPrefixes.GEAR, HTMoldType.GEAR) { HTConst.INGOT_AMOUNT * 4 }
        meltAndSolidify(event, HCMaterialPrefixes.GEM, HTMoldType.GEM)
        meltAndSolidify(event, HCMaterialPrefixes.INGOT, HTMoldType.INGOT)
        meltAndSolidify(event, HCMaterialPrefixes.NUGGET, HTMoldType.NUGGET) { HTConst.INGOT_AMOUNT / 9 }
        meltAndSolidify(event, HCMaterialPrefixes.PEARL, HTMoldType.GEM)
        meltAndSolidify(event, HCMaterialPrefixes.PLATE, HTMoldType.PLATE)
        meltAndSolidify(event, HCMaterialPrefixes.ROD, HTMoldType.ROD) { HTConst.INGOT_AMOUNT / 2 }
    }

    //    Crushing    //

    @JvmStatic
    private fun crushPrefixToDust(
        event: HTRegisterRuntimeRecipeEvent,
        prefix: HTPrefixLike,
        inputCount: Int,
        outputCountGetter: (HTMaterialDefinition) -> Int? = { 1 },
    ) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in event.materialManager.entries) {
            val outputCount: Int = outputCountGetter(definition) ?: continue

            if (!event.isPresentItemTag(prefix, key)) continue
            val dust: Item = event.getFirstItemHolder(HCMaterialPrefixes.DUST, key)?.value() ?: continue

            HTChancedRecipeBuilder
                .crushing(
                    event.itemCreator.fromTagKey(prefix, key, inputCount),
                    event.itemResult.create(dust, outputCount),
                ).saveSuffixed(event.output, "_from_${prefix.asPrefixName()}")
        }
    }

    //    Cutting    //

    @JvmStatic
    private fun cutBlockToPlate(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in event.materialManager.entries) {
            if (!event.isPresentItemTag(HCMaterialPrefixes.STORAGE_BLOCK, key)) continue
            val plate: Item = event.getFirstItemHolder(HCMaterialPrefixes.PLATE, key)?.value() ?: continue

            HTChancedRecipeBuilder
                .cutting(
                    event.itemCreator.fromTagKey(HCMaterialPrefixes.STORAGE_BLOCK, key),
                    event.itemResult.create(plate, definition.getStorageAttribute().baseCount),
                ).saveSuffixed(event.output, "_from_block")
        }
    }

    @JvmStatic
    private fun cutBaseToRod(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in event.materialManager.entries) {
            val basePrefix: HTMaterialPrefix = definition.getDefaultPrefix() ?: continue
            if (basePrefix == HCMaterialPrefixes.ROD) continue
            if (!event.isPresentItemTag(basePrefix, key)) continue

            val rod: Item = event.getFirstItemHolder(HCMaterialPrefixes.ROD, key)?.value() ?: continue

            HTChancedRecipeBuilder
                .cutting(
                    event.itemCreator.fromTagKey(basePrefix, key),
                    event.itemResult.create(rod, 2),
                ).saveSuffixed(event.output, "_from_${basePrefix.name}")
        }
    }

    //    Pressing    //

    @JvmStatic
    private fun pressBaseToPrefix(
        event: HTRegisterRuntimeRecipeEvent,
        prefix: HTPrefixLike,
        moldType: HTMoldType,
        inputCountGetter: (HTMaterialDefinition) -> Int?,
        outputCount: Int,
    ) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in event.materialManager.entries) {
            val basePrefix: HTMaterialPrefix = definition.getDefaultPrefix() ?: continue
            if (basePrefix == prefix) continue
            val inputCount: Int = inputCountGetter(definition) ?: continue

            if (!event.isPresentItemTag(basePrefix, key)) continue
            val result: Item = event.getFirstItemHolder(prefix, key)?.value() ?: continue

            HTSingleRecipeBuilder
                .pressing(
                    event.itemCreator.fromTagKey(basePrefix, key, inputCount),
                    event.itemCreator.fromItem(moldType),
                    event.itemResult.create(result, outputCount),
                ).saveSuffixed(event.output, "_from_${basePrefix.name}")
        }
    }

    //    Fluid    //

    @JvmStatic
    private fun meltAndSolidify(
        event: HTRegisterRuntimeRecipeEvent,
        prefix: HTPrefixLike,
        moldType: HTMoldType?,
        fluidAmountGetter: (HTMaterialDefinition) -> Int? = { HTConst.INGOT_AMOUNT },
    ) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in event.materialManager.entries) {
            val fluidAmount: Int = fluidAmountGetter(definition) ?: continue

            if (!event.isPresentItemTag(prefix, key)) continue
            val molten: Fluid = event.getFirstFluidHolder(HCMaterialPrefixes.MOLTEN, key)?.value() ?: continue
            // Melt
            HTSingleRecipeBuilder
                .melting(
                    event.itemCreator.fromTagKey(prefix, key),
                    event.fluidResult.create(molten, fluidAmount),
                ).saveSuffixed(event.output, "_from_${prefix.asPrefixName()}")
            // Solidify
            if (moldType == null) return
            val item: Item = event.getFirstItemHolder(prefix, key)?.value() ?: continue
            HTSingleRecipeBuilder
                .solidifying(
                    event.fluidCreator.fromTagKey(HCMaterialPrefixes.MOLTEN, key, fluidAmount),
                    event.itemCreator.fromItem(moldType),
                    event.itemResult.create(item),
                ).saveSuffixed(event.output, "_from_molten")
        }
    }
}
