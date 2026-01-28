package hiiragi283.ragium.common.event

import hiiragi283.core.api.component1
import hiiragi283.core.api.component2
import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.data.recipe.HTResultCreator
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.applyOreMultiplier
import hiiragi283.core.api.material.property.getDefaultFluidAmount
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.material.property.getStorageBlock
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.getScaledAmount
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.registry.HTWoodDefinition
import hiiragi283.ragium.common.data.recipe.HTAlloyingRecipeBuilder
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
import org.apache.commons.lang3.math.Fraction
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

        alloyBaseToPrefix(event, CommonTagPrefixes.GEAR, HTMoldType.GEAR)
        alloyBaseToPrefix(event, CommonTagPrefixes.PLATE, HTMoldType.PLATE)
        alloyBaseToPrefix(event, CommonTagPrefixes.ROD, HTMoldType.ROD)

        crushBaseToDust(event)
        crushOreToDust(event)
        crushPrefixToDust(event, CommonTagPrefixes.RAW_BLOCK)
        crushPrefixToDust(event, CommonTagPrefixes.GEAR)
        crushPrefixToDust(event, CommonTagPrefixes.NUGGET)
        crushPrefixToDust(event, CommonTagPrefixes.PLATE)
        crushPrefixToDust(event, CommonTagPrefixes.RAW)
        crushPrefixToDust(event, CommonTagPrefixes.ROD)
        crushPrefixToDust(event, CommonTagPrefixes.WIRE)

        cutBlockToPlate(event)
        cutBaseToRod(event)
        cutWoodFromDefinition(event)

        pressBaseToPrefix(event, CommonTagPrefixes.GEAR, HTMoldType.GEAR)
        pressBaseToPrefix(event, CommonTagPrefixes.NUGGET, HTMoldType.NUGGET)
        pressBaseToPrefix(event, CommonTagPrefixes.PLATE, HTMoldType.PLATE)

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

    @JvmStatic
    private fun baseOrDust(
        event: HTRegisterRuntimeRecipeEvent,
        key: HTMaterialKey,
        propertyMap: HTPropertyMap,
        amount: Int = 1,
    ): Pair<HTDefaultPart, HTItemIngredient>? {
        val defaultPart: HTDefaultPart = propertyMap.getDefaultPart() ?: return null
        val inputTag: TagKey<Item> = defaultPart.getTag(key)
        val dustTag: TagKey<Item> = CommonTagPrefixes.DUST.itemTagKey(key)
        if (!event.isPresentTag(inputTag) && !event.isPresentTag(dustTag)) return null
        return defaultPart to inputCreator.create(setOf(inputTag, dustTag), amount)
    }

    //    Alloying    //

    @JvmStatic
    private fun alloyBaseToPrefix(event: HTRegisterRuntimeRecipeEvent, prefix: HTTagPrefix, moldType: HTMoldType) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            // 溶融加工が許可されていない場合はパス
            if (!propertyMap.getOrDefault(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG).melting) continue
            // プレフィックスのスケールから個数を算出
            val fraction: Fraction = prefix.getScaledAmount(1, propertyMap)
            val (inputCount: Int, outputCount: Int) = fraction
            // 素材のプロパティから材料を取得
            val (defaultPart: HTDefaultPart, input: HTItemIngredient) = baseOrDust(event, key, propertyMap, inputCount) ?: continue
            // 完成品を取得
            val result: Item = event.getFirstHolder(prefix, key)?.value() ?: continue
            // レシピを登録
            HTAlloyingRecipeBuilder.create(output) {
                this.ingredients += input
                this.ingredients += inputCreator.create(moldType)
                this.result = resultCreator.create(result, outputCount)
                recipeId suffix "_from_${defaultPart.getSuffix()}"
            }
        }
    }

    //    Crushing    //

    @JvmStatic
    private fun crushPrefixToDust(event: HTRegisterRuntimeRecipeEvent, prefix: HTTagPrefix) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            // 材料が存在するか判定
            if (!event.isPresentTag(prefix, key)) continue
            // 素材のプロパティから完成品を取得
            val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
            val dust: Item = event.getFirstHolder(crushedPrefix, key)?.value() ?: continue
            // プレフィックスのスケールから個数を算出
            val fraction: Fraction = prefix.getScaledAmount(1, propertyMap)
            val (outputCount: Int, inputCount: Int) = fraction
            // レシピを登録
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
            // 素材のプロパティから材料を取得
            val defaultPart: HTDefaultPart = propertyMap.getDefaultPart() ?: continue
            val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
            val inputTag: TagKey<Item> = defaultPart.getTag(key)
            if (!event.isPresentTag(inputTag)) continue
            // 加工の前後でタグが一致する場合はパス
            if (inputTag == crushedPrefix.itemTagKey(key)) continue
            // 完成品を取得
            val dust: Item = event.getFirstHolder(crushedPrefix, key)?.value() ?: continue
            // レシピを登録
            HTChancedRecipeBuilder.crushing(output) {
                ingredient = inputCreator.create(inputTag)
                result = resultCreator.create(dust)
                recipeId suffix "_from_${defaultPart.getSuffix()}"
            }
        }
    }

    @JvmStatic
    private fun crushOreToDust(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            // 材料が存在するか判定
            if (!event.isPresentTag(CommonTagPrefixes.ORE, key)) continue
            // 完成品を取得
            val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
            val dust: Item = event.getFirstHolder(crushedPrefix, key)?.value() ?: continue
            // レシピを登録
            HTChancedRecipeBuilder.crushing(output) {
                // 材料
                ingredient = inputCreator.create(CommonTagPrefixes.ORE, key)
                // 主産物
                val dustCount: Int = crushedPrefix
                    .getScaledAmount(2, propertyMap)
                    .let(propertyMap::applyOreMultiplier)
                    .toInt()
                result = resultCreator.create(dust, dustCount)
                // 副産物
                propertyMap
                    .getOrDefault(HTMaterialPropertyKeys.ORE_EXTRA_RESULTS)
                    .map { it.toResult(resultCreator) }
                    .forEach(chancedResults::plusAssign)

                recipeId suffix "_from_ore"
            }
        }
    }

    //    Cutting    //

    @JvmStatic
    private fun cutBlockToPlate(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            // 機械加工が許可されていない場合はパス
            if (!propertyMap.getOrDefault(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG).mechanical) continue
            // 材料が存在するか判定
            if (!event.isPresentTag(CommonTagPrefixes.BLOCK, key)) continue
            // 完成品を取得
            val plate: Item = event.getFirstHolder(CommonTagPrefixes.PLATE, key)?.value() ?: continue
            // レシピを登録
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
            // 機械加工が許可されていない場合はパス
            if (!propertyMap.getOrDefault(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG).mechanical) continue
            // 素材のプロパティから材料を取得
            val inputTag: TagKey<Item> = propertyMap.getDefaultPart(key) ?: continue
            if (!event.isPresentTag(inputTag)) continue
            // 加工の前後でタグが一致する場合はパス
            if (inputTag == CommonTagPrefixes.ROD.itemTagKey(key)) continue
            // 完成品を取得
            val rod: Item = event.getFirstHolder(CommonTagPrefixes.ROD, key)?.value() ?: continue
            // レシピを登録
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
    private fun pressBaseToPrefix(event: HTRegisterRuntimeRecipeEvent, prefix: HTTagPrefix, moldType: HTMoldType) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            // 機械加工が許可されていない場合はパス
            if (!propertyMap.getOrDefault(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG).mechanical) continue
            // 素材のプロパティから材料を取得
            val inputTag: TagKey<Item> = propertyMap.getDefaultPart(key) ?: continue
            if (!event.isPresentTag(inputTag)) continue
            // 加工の前後でタグが一致する場合はパス
            if (inputTag == prefix.itemTagKey(key)) continue
            // 完成品を取得
            val result: Item = event.getFirstHolder(prefix, key)?.value() ?: continue
            // プレフィックスのスケールから個数を算出
            val fraction: Fraction = prefix.getScaledAmount(1, propertyMap)
            val (inputCount: Int, outputCount: Int) = fraction
            // レシピを登録
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
            // 溶融加工が許可されていない場合はパス
            if (!propertyMap.getOrDefault(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG).melting) continue
            // 素材のプロパティから材料を取得
            val (defaultPart: HTDefaultPart, input: HTItemIngredient) = baseOrDust(event, key, propertyMap) ?: continue
            // 素材のプロパティから液体材料を取得
            var fluidAmount: Int = propertyMap.getDefaultFluidAmount()
            val prefix: HTTagPrefix? = (defaultPart as? HTDefaultPart.Prefixed)?.prefix
            if (prefix != null) {
                fluidAmount = prefix.getScaledAmount(fluidAmount, propertyMap).toInt()
            }
            val molten: HTFluidContent<*, *, *> = propertyMap[HTMaterialPropertyKeys.MOLTEN_FLUID]?.fluid ?: continue
            // レシピを登録
            HTSingleRecipeBuilder.melting(output) {
                ingredient = input
                result = resultCreator.create(molten, fluidAmount)
                recipeId suffix "_from_${defaultPart.getSuffix()}"
            }
        }
    }

    //    Mixing    //

    @JvmStatic
    private fun mixFlourToDough(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in event.getAllMaterials()) {
            // 素材のプロパティから材料を取得
            val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
            if (!event.isPresentTag(crushedPrefix, key)) continue
            // 完成品を取得
            val dough: Item = event.getFirstHolder(CommonTagPrefixes.DOUGH, key)?.value() ?: continue
            // レシピを登録
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
            // 溶融加工が許可されていない場合はパス
            if (!propertyMap.getOrDefault(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG).melting) continue
            // 素材のプロパティから材料を取得
            val molten: HTFluidContent<*, *, *> = propertyMap[HTMaterialPropertyKeys.MOLTEN_FLUID]?.fluid ?: continue
            // プレフィックスと素材のプロパティから液体量を算出
            val fluidAmount: Int = prefix.getScaledAmount(propertyMap.getDefaultFluidAmount(), propertyMap).toInt()
            // レシピを登録
            val result: Item = event.getFirstHolder(prefix, key)?.value() ?: continue
            HTSingleRecipeBuilder.solidifying(output) {
                this.ingredient = inputCreator.create(molten, fluidAmount) to inputCreator.create(moldType)
                this.result = resultCreator.create(result)
                recipeId suffix "_from_molten"
            }
        }
    }
}
