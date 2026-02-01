package hiiragi283.ragium.common.event

import hiiragi283.core.api.component1
import hiiragi283.core.api.component2
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.getDefaultFluidAmount
import hiiragi283.core.api.material.property.getDefaultPart
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
import hiiragi283.ragium.common.data.recipe.HTFluidWithItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemToChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeRecipeHandler : HTRecipeProviderContext.Delegated() {
    override lateinit var delegated: HTRecipeProviderContext

    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        this.delegated = event.context

        cutWoodFromDefinition()

        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in materialManager) {
            bathDustToPrefix(event, key, propertyMap, CommonTagPrefixes.GEM)
            bathDustToPrefix(event, key, propertyMap, CommonTagPrefixes.PEARL)

            crushBaseToDust(event, key, propertyMap)
            crushOreToDust(event, key, propertyMap)
            crushPrefixToDust(event, key, propertyMap, CommonTagPrefixes.RAW_BLOCK)
            crushPrefixToDust(event, key, propertyMap, CommonTagPrefixes.GEAR)
            crushPrefixToDust(event, key, propertyMap, CommonTagPrefixes.NUGGET)
            crushPrefixToDust(event, key, propertyMap, CommonTagPrefixes.PLATE)
            crushPrefixToDust(event, key, propertyMap, CommonTagPrefixes.RAW)
            crushPrefixToDust(event, key, propertyMap, CommonTagPrefixes.ROD)
            crushPrefixToDust(event, key, propertyMap, CommonTagPrefixes.WIRE)

            cutBlockToPlate(event, key, propertyMap)

            bathFlourToDough(event, key, propertyMap)

            if (propertyMap.getOrDefault(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG).mechanical) {
                pressBaseToPrefix(event, key, propertyMap, CommonTagPrefixes.GEAR, HTMoldType.GEAR)
                pressBaseToPrefix(event, key, propertyMap, CommonTagPrefixes.PLATE, HTMoldType.PLATE)
                pressBaseToPrefix(event, key, propertyMap, CommonTagPrefixes.ROD, HTMoldType.ROD)
            }

            if (propertyMap.getOrDefault(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG).melting) {
                meltBaseToMolten(event, key, propertyMap)

                solidifyPrefix(event, key, propertyMap, CommonTagPrefixes.BLOCK, HTMoldType.BLOCK)
                solidifyPrefix(event, key, propertyMap, CommonTagPrefixes.GEAR, HTMoldType.GEAR)
                solidifyPrefix(event, key, propertyMap, CommonTagPrefixes.GEM, HTMoldType.GEM)
                solidifyPrefix(event, key, propertyMap, CommonTagPrefixes.INGOT, HTMoldType.INGOT)
                solidifyPrefix(event, key, propertyMap, CommonTagPrefixes.NUGGET, HTMoldType.NUGGET)
                solidifyPrefix(event, key, propertyMap, CommonTagPrefixes.PEARL, HTMoldType.BALL)
                solidifyPrefix(event, key, propertyMap, CommonTagPrefixes.PLATE, HTMoldType.PLATE)
                solidifyPrefix(event, key, propertyMap, CommonTagPrefixes.ROD, HTMoldType.ROD)
            }
        }
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

    //    Bathing    //

    @JvmStatic
    private fun bathDustToPrefix(
        event: HTRegisterRuntimeRecipeEvent,
        key: HTMaterialKey,
        propertyMap: HTPropertyMap,
        prefix: HTTagPrefix,
    ) {
        // 材料が存在するか判定
        val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        if (!event.isPresentTag(crushedPrefix, key)) return
        // 完成品を取得
        val resultItem: Item = event.getFirstHolder(prefix, key)?.value() ?: return
        // レシピを登録
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(crushedPrefix, key)
            fluidIngredient = inputCreator.water(125)
            result = resultCreator.create(resultItem)
            recipeId suffix "from_${crushedPrefix.name}"
        }
    }

    @JvmStatic
    private fun bathFlourToDough(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, propertyMap: HTPropertyMap) {
        // 素材のプロパティから材料を取得
        val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        if (!event.isPresentTag(crushedPrefix, key)) return
        // 完成品を取得
        val dough: Item = event.getFirstHolder(CommonTagPrefixes.DOUGH, key)?.value() ?: return
        // レシピを登録
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(crushedPrefix, key)
            fluidIngredient = inputCreator.water(250)
            result = resultCreator.create(dough)
        }
    }

    //    Crushing    //

    @JvmStatic
    private fun crushPrefixToDust(
        event: HTRegisterRuntimeRecipeEvent,
        key: HTMaterialKey,
        propertyMap: HTPropertyMap,
        prefix: HTTagPrefix,
    ) {
        // 材料が存在するか判定
        if (!event.isPresentTag(prefix, key)) return
        // 素材のプロパティから完成品を取得
        val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        val dust: Item = event.getFirstHolder(crushedPrefix, key)?.value() ?: return
        // プレフィックスのスケールから個数を算出
        val (outputCount: Int, inputCount: Int) = prefix.getScaledAmount(1, propertyMap)
        // レシピを登録
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(prefix, key, inputCount)
            result = resultCreator.create(dust, outputCount)
            recipeId suffix "_from_${prefix.name}"
        }
    }

    @JvmStatic
    private fun crushBaseToDust(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, propertyMap: HTPropertyMap) {
        // 素材のプロパティから材料を取得
        val defaultPart: HTDefaultPart = propertyMap.getDefaultPart() ?: return
        val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        val inputTag: TagKey<Item> = defaultPart.getTag(key)
        if (!event.isPresentTag(inputTag)) return
        // 加工の前後でタグが一致する場合はパス
        if (inputTag == crushedPrefix.itemTagKey(key)) return
        // 完成品を取得
        val dust: Item = event.getFirstHolder(crushedPrefix, key)?.value() ?: return
        // レシピを登録
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(inputTag)
            result = resultCreator.create(dust)
            recipeId suffix "_from_${defaultPart.getSuffix()}"
        }
    }

    @JvmStatic
    private fun crushOreToDust(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, propertyMap: HTPropertyMap) {
        // 材料が存在するか判定
        if (!event.isPresentTag(CommonTagPrefixes.ORE, key)) return
        // 完成品を取得
        val crushedPrefix: HTTagPrefix = propertyMap.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        val dust: Item = event.getFirstHolder(crushedPrefix, key)?.value() ?: return
        // レシピを登録
        HTItemToChancedRecipeBuilder.crushing(output) {
            // 材料
            ingredient = inputCreator.create(CommonTagPrefixes.ORE, key)
            // 主産物
            result = resultCreator.create(dust, CommonTagPrefixes.ORE.getScaledAmount(1, propertyMap).toInt())
            // 副産物
            propertyMap
                .getOrDefault(HTMaterialPropertyKeys.ORE_EXTRA_RESULTS)
                .map { it.toResult(resultCreator) }
                .forEach(chancedResults::plusAssign)

            recipeId suffix "_from_ore"
        }
    }

    //    Cutting    //

    @JvmStatic
    private fun cutBlockToPlate(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, propertyMap: HTPropertyMap) {
        // 材料が存在するか判定
        if (!event.isPresentTag(CommonTagPrefixes.BLOCK, key)) return
        // 完成品を取得
        val plate: Item = event.getFirstHolder(CommonTagPrefixes.PLATE, key)?.value() ?: return
        // レシピを登録
        HTItemToChancedRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.BLOCK, key)
            result = resultCreator.create(plate, propertyMap.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK).baseCount)
            time *= 3
            recipeId suffix "_from_block"
        }
    }

    @JvmStatic
    private fun cutWoodFromDefinition() {
        provider
            .lookupOrThrow(RagiumAPI.WOOD_DEFINITION_KEY)
            .listElements()
            .map { it.toLike() }
            .forEach { holder: HTHolderLike.HolderDelegate<HTWoodDefinition, HTWoodDefinition> ->
                val definition: HTWoodDefinition = holder.get()
                val planks: ItemLike = definition[HTWoodDefinition.Variant.PLANKS] ?: return@forEach
                // Log -> 6x Planks
                HTItemToChancedRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(definition.logTag)
                    result = resultCreator.create(planks, 6)
                    recipeId suffix "_from_log"
                }
                // Boat
                definition[HTWoodDefinition.Variant.BOAT]?.let { boat ->
                    HTItemToChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(boat)
                        result = resultCreator.create(planks, 5)
                        recipeId suffix "_from_boat"
                    }
                    // Chest Boat
                    definition[HTWoodDefinition.Variant.CHEST_BOAT]?.let {
                        HTItemToChancedRecipeBuilder.cutting(output) {
                            ingredient = inputCreator.create(it)
                            result = resultCreator.create(boat)
                            chancedResults += resultCreator.create(Items.CHEST)
                        }
                    }
                }
                // Button
                // Fence
                definition[HTWoodDefinition.Variant.FENCE]?.let {
                    HTItemToChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(planks)
                        chancedResults += resultCreator.create(Items.STICK)
                        recipeId suffix "_from_fence"
                    }
                }
                // Fence Gate
                definition[HTWoodDefinition.Variant.FENCE_GATE]?.let {
                    HTItemToChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(planks, 2)
                        chancedResults += resultCreator.create(Items.STICK, 4)
                        recipeId suffix "_from_fence_gate"
                    }
                }
                // Pressure Plate
                definition[HTWoodDefinition.Variant.PRESSURE_PLATE]?.let {
                    HTItemToChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(planks, 2)
                        recipeId suffix "_from_pressure_plate"
                    }
                }
                // Sign
                definition[HTWoodDefinition.Variant.SIGN]?.let {
                    HTItemToChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(planks, 2)
                        chancedResults += resultCreator.create(Items.STICK) to fraction(1, 3)
                        recipeId suffix "_from_sign"
                    }
                }
                // Hanging Sign
                definition[HTWoodDefinition.Variant.HANGING_SIGN]?.let {
                    HTItemToChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(planks, 4)
                        chancedResults += resultCreator.create(Items.CHAIN) to fraction(1, 3)
                        recipeId suffix "_from_hanging_sign"
                    }
                }
                // Slab
                definition[HTWoodDefinition.Variant.SLAB]?.let {
                    HTItemToChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(planks)
                        result = resultCreator.create(it, 2)
                    }
                }
                // Stairs
                // Door
                definition[HTWoodDefinition.Variant.DOOR]?.let {
                    HTItemToChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(planks, 2)
                        recipeId suffix "_from_door"
                    }
                }
                // Trapdoor
                definition[HTWoodDefinition.Variant.TRAPDOOR]?.let {
                    HTItemToChancedRecipeBuilder.cutting(output) {
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
        key: HTMaterialKey,
        propertyMap: HTPropertyMap,
        prefix: HTTagPrefix,
        moldType: HTMoldType,
    ) {
        // 素材のプロパティから材料を取得
        val inputTag: TagKey<Item> = propertyMap.getDefaultPart(key) ?: return
        if (!event.isPresentTag(inputTag)) return
        // 加工の前後でタグが一致する場合はパス
        if (inputTag == prefix.itemTagKey(key)) return
        // 完成品を取得
        val result: Item = event.getFirstHolder(prefix, key)?.value() ?: return
        // プレフィックスのスケールから個数を算出
        val (inputCount: Int, outputCount: Int) = prefix.getScaledAmount(1, propertyMap)
        // レシピを登録
        HTSingleRecipeBuilder.pressing(output) {
            this.ingredient = inputCreator.create(inputTag, inputCount) to inputCreator.create(moldType)
            this.result = resultCreator.create(result, outputCount)
            this.recipeId suffix "_from_${inputTag.location().path}"
        }
    }

    //    Melting    //

    @JvmStatic
    private fun meltBaseToMolten(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, propertyMap: HTPropertyMap) {
        // 素材のプロパティから材料を取得
        val (defaultPart: HTDefaultPart, input: HTItemIngredient) = baseOrDust(event, key, propertyMap) ?: return
        // 素材のプロパティから液体材料を取得
        var fluidAmount: Int = propertyMap.getDefaultFluidAmount()
        val prefix: HTTagPrefix? = (defaultPart as? HTDefaultPart.Prefixed)?.prefix
        if (prefix != null) {
            fluidAmount = prefix.getScaledAmount(fluidAmount, propertyMap).toInt()
        }
        val molten: HTFluidContent<*, *, *> = propertyMap[HTMaterialPropertyKeys.MOLTEN_FLUID]?.fluid ?: return
        // レシピを登録
        HTSingleRecipeBuilder.melting(output) {
            ingredient = input
            result = resultCreator.create(molten, fluidAmount)
            recipeId suffix "_from_${defaultPart.getSuffix()}"
        }
    }

    //    Solidifying    //

    @JvmStatic
    private fun solidifyPrefix(
        event: HTRegisterRuntimeRecipeEvent,
        key: HTMaterialKey,
        propertyMap: HTPropertyMap,
        prefix: HTTagPrefix,
        moldType: HTMoldType,
    ) {
        // 素材のプロパティから材料を取得
        val molten: HTFluidContent<*, *, *> = propertyMap[HTMaterialPropertyKeys.MOLTEN_FLUID]?.fluid ?: return
        // プレフィックスと素材のプロパティから液体量を算出
        val fluidAmount: Int = prefix.getScaledAmount(propertyMap.getDefaultFluidAmount(), propertyMap).toInt()
        // レシピを登録
        val resultItem: Item = event.getFirstHolder(prefix, key)?.value() ?: return
        HTFluidWithItemRecipeBuilder.solidifying(output) {
            fluidIngredient = inputCreator.create(molten, fluidAmount)
            itemIngredient = inputCreator.create(moldType)
            result = resultCreator.create(resultItem)
            recipeId suffix "_from_molten"
        }
    }
}
