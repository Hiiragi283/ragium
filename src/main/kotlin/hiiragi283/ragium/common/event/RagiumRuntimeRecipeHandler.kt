package hiiragi283.ragium.common.event

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.core.api.data.recipe.result.HTItemResultCreator
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.material.property.getStorageBlock
import hiiragi283.core.api.math.fraction
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.registry.HTWoodDefinition
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import kotlin.streams.asSequence

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeRecipeHandler {
    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        crushPrefixToDust(event, HCMaterialPrefixes.STORAGE_BLOCK, 1) { it.getStorageBlock().baseCount }
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
        cutWoodFromDefinition(event)

        pressBaseToPrefix(event, HCMaterialPrefixes.STORAGE_BLOCK, HTMoldType.BLOCK, { it.getStorageBlock().baseCount }, 1)
        pressBaseToPrefix(event, HCMaterialPrefixes.GEAR, HTMoldType.GEAR, { 4 }, 1)
        pressBaseToPrefix(event, HCMaterialPrefixes.NUGGET, HTMoldType.NUGGET, { 1 }, 9)
        pressBaseToPrefix(event, HCMaterialPrefixes.PLATE, HTMoldType.PLATE, { 1 }, 1)

        meltAndSolidify(event, HCMaterialPrefixes.STORAGE_BLOCK, HTMoldType.BLOCK) {
            HTConst.INGOT_AMOUNT * it.getStorageBlock().baseCount
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
        outputCountGetter: (HTPropertyMap) -> Int? = { 1 },
    ) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            val outputCount: Int = outputCountGetter(propertyMap) ?: continue

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
    private fun cutBlockToPlate(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            if (!event.isPresentTag(HCMaterialPrefixes.STORAGE_BLOCK, key)) continue
            val plate: Item = event.getFirstHolder(HCMaterialPrefixes.PLATE, key)?.value() ?: continue

            HTChancedRecipeBuilder
                .cutting(
                    event.itemCreator.fromTagKey(HCMaterialPrefixes.STORAGE_BLOCK, key),
                    event.itemResult.create(plate, propertyMap.getStorageBlock().baseCount),
                ).saveSuffixed(event.output, "_from_block")
        }
    }

    @JvmStatic
    private fun cutBaseToRod(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            val basePrefix: HTMaterialPrefix = propertyMap.getDefaultPart() ?: continue
            if (basePrefix == HCMaterialPrefixes.ROD) continue
            if (!event.isPresentTag(basePrefix, key)) continue

            val rod: Item = event.getFirstHolder(HCMaterialPrefixes.ROD, key)?.value() ?: continue

            HTChancedRecipeBuilder
                .cutting(
                    event.itemCreator.fromTagKey(basePrefix, key),
                    event.itemResult.create(rod, 2),
                ).saveSuffixed(event.output, "_from_${basePrefix.name}")
        }
    }

    @JvmStatic
    private fun cutWoodFromDefinition(event: HTRegisterRuntimeRecipeEvent) {
        val itemCreator: HTItemIngredientCreator = event.itemCreator
        val itemResult: HTItemResultCreator = event.itemResult
        val output: RecipeOutput = event.output

        event.registryAccess
            .lookupOrThrow(RagiumAPI.WOOD_DEFINITION_KEY)
            .listElements()
            .asSequence()
            .map { it.toLike() }
            .forEach { holder: HTHolderLike.HolderDelegate<HTWoodDefinition, HTWoodDefinition> ->
                val definition: HTWoodDefinition = holder.get()
                val planks: ItemLike = definition[HTWoodDefinition.Variant.PLANKS] ?: return@forEach
                // Log -> 6x Planks
                HTChancedRecipeBuilder
                    .cutting(
                        itemCreator.fromTagKey(definition.logTag),
                        itemResult.create(planks, 6),
                    ).saveSuffixed(output, "_from_log")
                // Boat
                definition[HTWoodDefinition.Variant.BOAT]?.let { boat ->
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(boat),
                            itemResult.create(planks, 5),
                        ).saveSuffixed(output, "_from_boat")
                    // Chest Boat
                    definition[HTWoodDefinition.Variant.CHEST_BOAT]?.let {
                        HTChancedRecipeBuilder
                            .cutting(
                                itemCreator.fromItem(it),
                                itemResult.create(boat),
                            ).addResult(itemResult.create(Items.CHEST))
                            .save(output)
                    }
                }
                // Button
                // Fence
                definition[HTWoodDefinition.Variant.FENCE]?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(it),
                            itemResult.create(planks),
                        ).addResult(itemResult.create(Items.STICK))
                        .saveSuffixed(output, "_from_fence")
                }
                // Fence Gate
                definition[HTWoodDefinition.Variant.FENCE_GATE]?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(it),
                            itemResult.create(planks, 2),
                        ).addResult(itemResult.create(Items.STICK, 4))
                        .saveSuffixed(output, "_from_fence_gate")
                }
                // Pressure Plate
                definition[HTWoodDefinition.Variant.PRESSURE_PLATE]?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(it),
                            itemResult.create(planks, 2),
                        ).saveSuffixed(output, "_from_pressure_plate")
                }
                // Sign
                definition[HTWoodDefinition.Variant.SIGN]?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(it),
                            itemResult.create(planks, 2),
                        ).addResult(itemResult.create(Items.STICK), fraction(1, 3))
                        .saveSuffixed(output, "_from_sign")
                }
                // Hanging Sign
                definition[HTWoodDefinition.Variant.HANGING_SIGN]?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(it),
                            itemResult.create(planks, 4),
                        ).addResult(itemResult.create(Items.CHAIN), fraction(1, 3))
                        .saveSuffixed(output, "_from_hanging_sign")
                }
                // Slab
                definition[HTWoodDefinition.Variant.SLAB]?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(planks),
                            itemResult.create(it, 2),
                        ).save(output)
                }
                // Stairs
                // Door
                definition[HTWoodDefinition.Variant.DOOR]?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(it),
                            itemResult.create(planks, 2),
                        ).saveSuffixed(output, "_from_door")
                }
                // Trapdoor
                definition[HTWoodDefinition.Variant.TRAPDOOR]?.let {
                    HTChancedRecipeBuilder
                        .cutting(
                            itemCreator.fromItem(it),
                            itemResult.create(planks, 3),
                        ).saveSuffixed(output, "_from_trapdoor")
                }
            }
    }

    //    Pressing    //

    @JvmStatic
    private fun pressBaseToPrefix(
        event: HTRegisterRuntimeRecipeEvent,
        prefix: HTPrefixLike,
        moldType: HTMoldType,
        inputCountGetter: (HTPropertyMap) -> Int?,
        outputCount: Int,
    ) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            val basePrefix: HTMaterialPrefix = propertyMap.getDefaultPart() ?: continue
            if (basePrefix == prefix) continue
            val inputCount: Int = inputCountGetter(propertyMap) ?: continue

            if (!event.isPresentTag(basePrefix, key)) continue
            val result: Item = event.getFirstHolder(prefix, key)?.value() ?: continue

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
        fluidAmountGetter: (HTPropertyMap) -> Int = { HTConst.INGOT_AMOUNT },
    ) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            val fluidAmount: Int = fluidAmountGetter(propertyMap)

            if (!event.isPresentTag(prefix, key)) continue
            val molten: HTFluidContent<*, *, *> = propertyMap[HTMaterialPropertyKeys.MOLTEN_FLUID]?.fluid ?: continue
            // Melt
            HTSingleRecipeBuilder
                .melting(
                    event.itemCreator.fromTagKey(prefix, key),
                    event.fluidResult.create(molten, fluidAmount),
                ).saveSuffixed(event.output, "_from_${prefix.asPrefixName()}")
            // Solidify
            if (moldType == null) return
            val item: Item = event.getFirstHolder(prefix, key)?.value() ?: continue
            HTSingleRecipeBuilder
                .solidifying(
                    event.fluidCreator.fromTagKey(molten, fluidAmount),
                    event.itemCreator.fromItem(moldType),
                    event.itemResult.create(item),
                ).saveSuffixed(event.output, "_from_molten")
        }
    }
}
