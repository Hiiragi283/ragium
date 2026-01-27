package hiiragi283.ragium.common.event

import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.data.recipe.HTResultCreator
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.getDefaultFluidAmount
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.material.property.getStorageBlock
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.registry.HTWoodDefinition
import hiiragi283.ragium.api.material.property.RagiumMaterialPropertyKeys
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import kotlin.streams.asSequence

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeRecipeHandler {
    private lateinit var output: RecipeOutput
    private lateinit var inputCreator: HTIngredientCreator
    private lateinit var resultCreator: HTResultCreator

    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        output = event.output
        inputCreator = event.inputCreator
        resultCreator = event.resultCreator

        crushPrefixToDust(event, CommonTagPrefixes.BLOCK, 1) { it.getStorageBlock().baseCount }
        crushPrefixToDust(event, CommonTagPrefixes.RAW_BLOCK, 1) { 12 }

        crushBaseToDust(event)
        crushPrefixToDust(event, CommonTagPrefixes.GEAR, 1) { 4 }
        crushPrefixToDust(event, CommonTagPrefixes.NUGGET, 9)
        crushPrefixToDust(event, CommonTagPrefixes.PLATE, 1)
        crushPrefixToDust(event, CommonTagPrefixes.RAW, 3) { 4 }
        crushPrefixToDust(event, CommonTagPrefixes.ROD, 2)
        crushPrefixToDust(event, CommonTagPrefixes.WIRE, 2)

        cutBlockToPlate(event)
        cutBaseToRod(event)
        cutWoodFromDefinition(event)

        pressBaseToPrefix(event, CommonTagPrefixes.BLOCK, HTMoldType.BLOCK, { it.getStorageBlock().baseCount }, 1)
        pressBaseToPrefix(event, CommonTagPrefixes.GEAR, HTMoldType.GEAR, { 4 }, 1)
        pressBaseToPrefix(event, CommonTagPrefixes.NUGGET, HTMoldType.NUGGET, { 1 }, 9)
        pressBaseToPrefix(event, CommonTagPrefixes.PLATE, HTMoldType.PLATE, { 1 }, 1)

        meltBaseToMolten(event)

        mixFlourToDough(event)

        solidifyPrefix(event, CommonTagPrefixes.BLOCK, HTMoldType.BLOCK)
        solidifyPrefix(event, CommonTagPrefixes.GEAR, HTMoldType.GEAR)
        solidifyPrefix(event, CommonTagPrefixes.GEM, HTMoldType.GEM)
        solidifyPrefix(event, CommonTagPrefixes.INGOT, HTMoldType.INGOT)
        solidifyPrefix(event, CommonTagPrefixes.NUGGET, HTMoldType.NUGGET)
        solidifyPrefix(event, CommonTagPrefixes.PEARL, HTMoldType.BALL)
        solidifyPrefix(event, CommonTagPrefixes.PLATE, HTMoldType.PLATE)
        solidifyPrefix(event, CommonTagPrefixes.ROD, HTMoldType.ROD)
    }

    //    Crushing    //

    @JvmStatic
    private fun crushPrefixToDust(
        event: HTRegisterRuntimeRecipeEvent,
        prefix: HTTagPrefix,
        inputCount: Int,
        outputCountGetter: (HTPropertyMap) -> Int? = { 1 },
    ) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            val outputCount: Int = outputCountGetter(propertyMap) ?: continue

            if (!event.isPresentTag(prefix, key)) continue
            val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
            val dust: Item = event.getFirstHolder(crushedPrefix, key)?.value() ?: continue

            HTChancedRecipeBuilder.crushing(output) {
                ingredient = inputCreator.create(prefix, key, inputCount)
                result = resultCreator.create(dust, outputCount)
                recipeId suffix "_from_${prefix.name}"
            }
        }
    }

    @JvmStatic
    private fun crushBaseToDust(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            val defaultPart: HTDefaultPart = propertyMap.getDefaultPart() ?: continue

            val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
            val inputTag: TagKey<Item> = defaultPart.getTag(key)
            if (inputTag == crushedPrefix.itemTagKey(key)) continue

            if (!event.isPresentTag(inputTag)) continue
            val dust: Item = event.getFirstHolder(crushedPrefix, key)?.value() ?: continue
            // Crushing
            HTChancedRecipeBuilder.crushing(output) {
                ingredient = inputCreator.create(inputTag)
                result = resultCreator.create(dust)
                recipeId suffix "_from_${defaultPart.getSuffix()}"
            }
        }
    }

    //    Cutting    //

    @JvmStatic
    private fun cutBlockToPlate(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            if (!propertyMap.getOrDefault(RagiumMaterialPropertyKeys.FORMING_RECIPE_FLAG).mechanical) continue
            if (!event.isPresentTag(CommonTagPrefixes.BLOCK, key)) continue
            val plate: Item = event.getFirstHolder(CommonTagPrefixes.PLATE, key)?.value() ?: continue

            HTChancedRecipeBuilder.cutting(output) {
                ingredient = inputCreator.create(CommonTagPrefixes.BLOCK, key)
                result = resultCreator.create(plate, propertyMap.getStorageBlock().baseCount)
                time *= 3
                recipeId suffix "_from_block"
            }
        }
    }

    @JvmStatic
    private fun cutBaseToRod(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            if (!propertyMap.getOrDefault(RagiumMaterialPropertyKeys.FORMING_RECIPE_FLAG).mechanical) continue
            val inputTag: TagKey<Item> = propertyMap.getDefaultPart(key) ?: continue
            if (inputTag == CommonTagPrefixes.ROD.itemTagKey(key)) continue
            if (!event.isPresentTag(inputTag)) continue

            val rod: Item = event.getFirstHolder(CommonTagPrefixes.ROD, key)?.value() ?: continue

            HTChancedRecipeBuilder.cutting(output) {
                ingredient = inputCreator.create(inputTag)
                result = resultCreator.create(rod, 2)
                recipeId suffix "_from_${inputTag.location().path}"
            }
        }
    }

    @JvmStatic
    private fun cutWoodFromDefinition(event: HTRegisterRuntimeRecipeEvent) {
        event.registryAccess
            .lookupOrThrow(RagiumAPI.WOOD_DEFINITION_KEY)
            .listElements()
            .asSequence()
            .map { it.toLike() }
            .forEach { holder: HTHolderLike.HolderDelegate<HTWoodDefinition, HTWoodDefinition> ->
                val definition: HTWoodDefinition = holder.get()
                val planks: ItemLike = definition[HTWoodDefinition.Variant.PLANKS] ?: return@forEach
                // Log -> 6x Planks
                HTChancedRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(definition.logTag)
                    result = resultCreator.create(planks, 6)
                    recipeId suffix "_from_log"
                }
                // Boat
                definition[HTWoodDefinition.Variant.BOAT]?.let { boat ->
                    HTChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(boat)
                        result = resultCreator.create(planks, 5)
                        recipeId suffix "_from_boat"
                    }
                    // Chest Boat
                    definition[HTWoodDefinition.Variant.CHEST_BOAT]?.let {
                        HTChancedRecipeBuilder.cutting(output) {
                            ingredient = inputCreator.create(it)
                            result = resultCreator.create(boat)
                            chancedResults += resultCreator.create(Items.CHEST)
                        }
                    }
                }
                // Button
                // Fence
                definition[HTWoodDefinition.Variant.FENCE]?.let {
                    HTChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(planks)
                        chancedResults += resultCreator.create(Items.STICK)
                        recipeId suffix "_from_fence"
                    }
                }
                // Fence Gate
                definition[HTWoodDefinition.Variant.FENCE_GATE]?.let {
                    HTChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(planks, 2)
                        chancedResults += resultCreator.create(Items.STICK, 4)
                        recipeId suffix "_from_fence_gate"
                    }
                }
                // Pressure Plate
                definition[HTWoodDefinition.Variant.PRESSURE_PLATE]?.let {
                    HTChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(planks, 2)
                        recipeId suffix "_from_pressure_plate"
                    }
                }
                // Sign
                definition[HTWoodDefinition.Variant.SIGN]?.let {
                    HTChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(planks, 2)
                        chancedResults += resultCreator.create(Items.STICK) to fraction(1, 3)
                        recipeId suffix "_from_sign"
                    }
                }
                // Hanging Sign
                definition[HTWoodDefinition.Variant.HANGING_SIGN]?.let {
                    HTChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(planks, 4)
                        chancedResults += resultCreator.create(Items.CHAIN) to fraction(1, 3)
                        recipeId suffix "_from_hanging_sign"
                    }
                }
                // Slab
                definition[HTWoodDefinition.Variant.SLAB]?.let {
                    HTChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(planks)
                        result = resultCreator.create(it, 2)
                    }
                }
                // Stairs
                // Door
                definition[HTWoodDefinition.Variant.DOOR]?.let {
                    HTChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(planks, 2)
                        recipeId suffix "_from_door"
                    }
                }
                // Trapdoor
                definition[HTWoodDefinition.Variant.TRAPDOOR]?.let {
                    HTChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(planks, 3)
                        recipeId suffix "_from_trapdoor"
                    }
                }
            }
    }

    //    Pressing    //

    @JvmStatic
    private fun pressBaseToPrefix(
        event: HTRegisterRuntimeRecipeEvent,
        prefix: HTTagPrefix,
        moldType: HTMoldType,
        inputCountGetter: (HTPropertyMap) -> Int?,
        outputCount: Int,
    ) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            if (!propertyMap.getOrDefault(RagiumMaterialPropertyKeys.FORMING_RECIPE_FLAG).mechanical) continue
            val inputTag: TagKey<Item> = propertyMap.getDefaultPart(key) ?: continue
            if (inputTag == prefix.itemTagKey(key)) continue
            val inputCount: Int = inputCountGetter(propertyMap) ?: continue

            if (!event.isPresentTag(inputTag)) continue
            val result: Item = event.getFirstHolder(prefix, key)?.value() ?: continue

            HTSingleRecipeBuilder.pressing(output) {
                this.ingredient = inputCreator.create(inputTag, inputCount) to inputCreator.create(moldType)
                this.result = resultCreator.create(result, outputCount)
                this.recipeId suffix "_from_${inputTag.location().path}"
            }
        }
    }

    //    Melting    //

    @JvmStatic
    private fun meltBaseToMolten(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            if (!propertyMap.getOrDefault(RagiumMaterialPropertyKeys.FORMING_RECIPE_FLAG).melting) continue
            val defaultPart: HTDefaultPart = propertyMap.getDefaultPart() ?: continue
            val prefix: HTTagPrefix? = (defaultPart as? HTDefaultPart.Prefixed)?.prefix
            val inputTag: TagKey<Item> = defaultPart.getTag(key)

            var fluidAmount: Int = propertyMap.getDefaultFluidAmount()
            if (prefix != null) {
                fluidAmount = prefix.getOrDefault(HTTagPropertyKeys.ITEM_SCALE)(fluidAmount, propertyMap)
            }

            if (!event.isPresentTag(inputTag)) continue
            val molten: HTFluidContent<*, *, *> = propertyMap[HTMaterialPropertyKeys.MOLTEN_FLUID]?.fluid ?: continue
            // Melt
            HTSingleRecipeBuilder.melting(output) {
                ingredient = inputCreator.create(inputTag)
                result = resultCreator.create(molten, fluidAmount)
                recipeId suffix "_from_${defaultPart.getSuffix()}"
            }
        }
    }

    //    Mixing    //

    @JvmStatic
    private fun mixFlourToDough(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
            if (!event.isPresentTag(crushedPrefix, key)) continue

            val dough: Item = event.getFirstHolder(CommonTagPrefixes.DOUGH, key)?.value() ?: continue
            // Mix
            HTMixingRecipeBuilder.create(output) {
                itemIngredients += inputCreator.create(crushedPrefix, key)
                fluidIngredients += inputCreator.water(250)
                result += resultCreator.create(dough)
            }
        }
    }

    //    Solidifying    //

    @JvmStatic
    private fun solidifyPrefix(event: HTRegisterRuntimeRecipeEvent, prefix: HTTagPrefix, moldType: HTMoldType) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            if (!propertyMap.getOrDefault(RagiumMaterialPropertyKeys.FORMING_RECIPE_FLAG).melting) continue

            val molten: HTFluidContent<*, *, *> = propertyMap[HTMaterialPropertyKeys.MOLTEN_FLUID]?.fluid ?: continue
            val fluidAmount: Int =
                prefix.getOrDefault(HTTagPropertyKeys.ITEM_SCALE)(propertyMap.getDefaultFluidAmount(), propertyMap)

            // Solidify
            val result: Item = event.getFirstHolder(prefix, key)?.value() ?: continue
            HTSingleRecipeBuilder.solidifying(output) {
                this.ingredient = inputCreator.create(molten, fluidAmount) to inputCreator.create(moldType)
                this.result = resultCreator.create(result)
                recipeId suffix "_from_molten"
            }
        }
    }
}
