package hiiragi283.ragium.common.event

import hiiragi283.core.api.component1
import hiiragi283.core.api.component2
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.getDefaultFluidAmount
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.getScaledAmount
import hiiragi283.core.api.times
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.registry.HTWoodDefinition
import hiiragi283.ragium.common.data.recipe.HTFluidWithItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemToChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTWashingRecipeBuilder
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

        for (entry: HTMaterialManager.Entry in materialManager) {
            crushBaseToDust(event, entry)
            crushOreToCrushed(event, entry, CommonTagPrefixes.ORE)
            crushOreToCrushed(event, entry, CommonTagPrefixes.RAW)
            crushPrefixToDust(event, entry, CommonTagPrefixes.GEAR)
            crushPrefixToDust(event, entry, CommonTagPrefixes.NUGGET)
            crushPrefixToDust(event, entry, CommonTagPrefixes.PLATE)
            crushPrefixToDust(event, entry, CommonTagPrefixes.ROD)
            crushPrefixToDust(event, entry, CommonTagPrefixes.WIRE)

            cutBlockToPlate(event, entry)

            mixDustToPrefix(event, entry, CommonTagPrefixes.GEM)
            mixDustToPrefix(event, entry, CommonTagPrefixes.PEARL)
            mixFlourToDough(event, entry)

            washCrushedOre(event, entry)

            if (HTMaterialPropertyKeys.DISABLE_MECHANICAL !in entry) {
                pressBaseToPrefix(event, entry, CommonTagPrefixes.GEAR, HTMoldType.GEAR)
                pressBaseToPrefix(event, entry, CommonTagPrefixes.PLATE, HTMoldType.PLATE)
                pressBaseToPrefix(event, entry, CommonTagPrefixes.ROD, HTMoldType.ROD)
            }

            if (HTMaterialPropertyKeys.DISABLE_MELTING !in entry) {
                meltBaseToMolten(event, entry)

                solidifyPrefix(event, entry, CommonTagPrefixes.BLOCK, HTMoldType.BLOCK)
                solidifyPrefix(event, entry, CommonTagPrefixes.GEAR, HTMoldType.GEAR)
                solidifyPrefix(event, entry, CommonTagPrefixes.GEM, HTMoldType.GEM)
                solidifyPrefix(event, entry, CommonTagPrefixes.INGOT, HTMoldType.INGOT)
                solidifyPrefix(event, entry, CommonTagPrefixes.NUGGET, HTMoldType.NUGGET)
                solidifyPrefix(event, entry, CommonTagPrefixes.PEARL, HTMoldType.BALL)
                solidifyPrefix(event, entry, CommonTagPrefixes.PLATE, HTMoldType.PLATE)
                solidifyPrefix(event, entry, CommonTagPrefixes.ROD, HTMoldType.ROD)
            }
        }
    }

    @JvmStatic
    private fun baseOrDust(
        event: HTRegisterRuntimeRecipeEvent,
        entry: HTMaterialManager.Entry,
        amount: Int = 1,
    ): Pair<HTDefaultPart, HTItemIngredient>? {
        val defaultPart: HTDefaultPart = entry.getDefaultPart() ?: return null
        val inputTag: TagKey<Item> = defaultPart.getTag(entry)
        val dustTag: TagKey<Item> = CommonTagPrefixes.DUST.itemTagKey(entry)
        if (!event.isPresentTag(inputTag) && !event.isPresentTag(dustTag)) return null
        return defaultPart to inputCreator.create(setOf(inputTag, dustTag), amount)
    }

    //    Crushing    //

    @JvmStatic
    private fun crushPrefixToDust(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, prefix: HTTagPrefix) {
        // 材料が存在するか判定
        if (!event.isPresentTag(prefix, entry)) return
        // 素材のプロパティから完成品を取得
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        val dust: ItemLike = event.getFirstHolder(crushedPrefix, entry) ?: return
        // プレフィックスのスケールから個数を算出
        val (outputCount: Int, inputCount: Int) = prefix.getScaledAmount(1, entry)
        // レシピを登録
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(prefix, entry, inputCount)
            result = resultCreator.create(dust, outputCount)
            recipeId suffix "_from_${prefix.name}"
        }
    }

    @JvmStatic
    private fun crushBaseToDust(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 素材のプロパティから材料を取得
        val defaultPart: HTDefaultPart = entry.getDefaultPart() ?: return
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        val inputTag: TagKey<Item> = defaultPart.getTag(entry)
        if (!event.isPresentTag(inputTag)) return
        // 加工の前後でタグが一致する場合はパス
        if (inputTag == crushedPrefix.itemTagKey(entry)) return
        // 完成品を取得
        val dust: ItemLike = event.getFirstHolder(crushedPrefix, entry) ?: return
        // レシピを登録
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(inputTag)
            result = resultCreator.create(dust)
            recipeId suffix "_from_${defaultPart.getSuffix()}"
        }
    }

    @JvmStatic
    private fun crushOreToCrushed(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, prefix: HTTagPrefix) {
        // 材料が存在するか判定
        if (!event.isPresentTag(prefix, entry)) return
        // 完成品を取得
        val crushedOre: ItemLike = event.getFirstHolder(CommonTagPrefixes.CRUSHED_ORE, entry) ?: return
        // レシピを登録
        HTItemToChancedRecipeBuilder.crushing(output) {
            // 材料
            ingredient = inputCreator.create(prefix, entry)
            // 主産物
            result = resultCreator.create(crushedOre, prefix.getScaledAmount(2, entry).toInt())
            // 副産物
            entry
                .getOrDefault(HTMaterialPropertyKeys.ORE_EXTRA_RESULTS)
                .map { it.toResult(resultCreator) }
                .forEach(chancedResults::plusAssign)

            recipeId suffix "_from_${prefix.name}"
        }
    }

    //    Cutting    //

    @JvmStatic
    private fun cutBlockToPlate(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        if (!event.isPresentTag(CommonTagPrefixes.BLOCK, entry)) return
        // 完成品を取得
        val plate: ItemLike = event.getFirstHolder(CommonTagPrefixes.PLATE, entry) ?: return
        // レシピを登録
        HTItemToChancedRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.BLOCK, entry)
            result = resultCreator.create(plate, entry.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK).baseCount)
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
        entry: HTMaterialManager.Entry,
        prefix: HTTagPrefix,
        moldType: HTMoldType,
    ) {
        // 素材のプロパティから材料を取得
        val inputTag: TagKey<Item> = entry.getDefaultPart(entry) ?: return
        if (!event.isPresentTag(inputTag)) return
        // 加工の前後でタグが一致する場合はパス
        if (inputTag == prefix.itemTagKey(entry)) return
        // 完成品を取得
        val result: ItemLike = event.getFirstHolder(prefix, entry) ?: return
        // プレフィックスのスケールから個数を算出
        val (inputCount: Int, outputCount: Int) = prefix.getScaledAmount(1, entry)
        // レシピを登録
        HTSingleRecipeBuilder.pressing(output) {
            this.ingredient = inputCreator.create(inputTag, inputCount) to inputCreator.create(moldType)
            this.result = resultCreator.create(result, outputCount)
            this.recipeId suffix "_from_${inputTag.location().path}"
        }
    }

    //    Melting    //

    @JvmStatic
    private fun meltBaseToMolten(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 素材のプロパティから材料を取得
        val (defaultPart: HTDefaultPart, input: HTItemIngredient) = baseOrDust(event, entry) ?: return
        // 素材のプロパティから液体材料を取得
        var fluidAmount: Int = entry.getDefaultFluidAmount()
        val prefix: HTTagPrefix? = (defaultPart as? HTDefaultPart.Prefixed)?.prefix
        if (prefix != null) {
            fluidAmount = prefix.getScaledAmount(fluidAmount, entry).toInt()
        }
        val molten: HTFluidContent<*, *, *> = entry[HTMaterialPropertyKeys.MOLTEN_FLUID]?.fluid ?: return
        // レシピを登録
        HTSingleRecipeBuilder.melting(output) {
            ingredient = input
            result = resultCreator.create(molten, fluidAmount)
            recipeId suffix "_from_${defaultPart.getSuffix()}"
        }
    }

    //    Mixing    //

    @JvmStatic
    private fun mixDustToPrefix(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, prefix: HTTagPrefix) {
        // 材料が存在するか判定
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        if (!event.isPresentTag(crushedPrefix, entry)) return
        // 完成品を取得
        val resultItem: ItemLike = event.getFirstHolder(prefix, entry) ?: return
        // レシピを登録
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(crushedPrefix, entry)
            fluidIngredients += inputCreator.water(125)
            result += resultCreator.create(resultItem)
            time /= 2
            recipeId suffix "from_${crushedPrefix.name}"
        }
    }

    @JvmStatic
    private fun mixFlourToDough(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 素材のプロパティから材料を取得
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        if (!event.isPresentTag(crushedPrefix, entry)) return
        // 完成品を取得
        val dough: ItemLike = event.getFirstHolder(CommonTagPrefixes.DOUGH, entry) ?: return
        // レシピを登録
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(crushedPrefix, entry)
            fluidIngredients += inputCreator.water(250)
            time /= 2
            result += resultCreator.create(dough)
        }
    }

    //    Solidifying    //

    @JvmStatic
    private fun solidifyPrefix(
        event: HTRegisterRuntimeRecipeEvent,
        entry: HTMaterialManager.Entry,
        prefix: HTTagPrefix,
        moldType: HTMoldType,
    ) {
        // 素材のプロパティから材料を取得
        val molten: HTFluidContent<*, *, *> = entry[HTMaterialPropertyKeys.MOLTEN_FLUID]?.fluid ?: return
        // プレフィックスと素材のプロパティから液体量を算出
        val fluidAmount: Int = prefix.getScaledAmount(entry.getDefaultFluidAmount(), entry).toInt()
        // レシピを登録
        val resultItem: ItemLike = event.getFirstHolder(prefix, entry) ?: return
        HTFluidWithItemRecipeBuilder.solidifying(output) {
            fluidIngredient = inputCreator.create(molten, fluidAmount)
            itemIngredient = inputCreator.create(moldType)
            result = resultCreator.create(resultItem)
            recipeId suffix "_from_molten"
        }
    }

    //    Washing    //

    @JvmStatic
    private fun washCrushedOre(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        if (!event.isPresentTag(CommonTagPrefixes.CRUSHED_ORE, entry)) return
        // 完成品を取得
        val dust: ItemLike = event.getFirstHolder(CommonTagPrefixes.DUST, entry) ?: return
        // レシピを登録
        HTWashingRecipeBuilder.create(output) {
            // 材料
            itemIngredient = inputCreator.create(CommonTagPrefixes.CRUSHED_ORE, entry)
            fluidIngredient = inputCreator.water(250)
            // 主産物
            result = resultCreator.create(dust, CommonTagPrefixes.CRUSHED_ORE.getScaledAmount(1, entry).toInt())
            // 副産物
            entry
                .getOrDefault(HTMaterialPropertyKeys.ORE_EXTRA_RESULTS)
                .map { it.toResult(resultCreator) }
                .map { it.copy(chance = it.chance * 2) }
                .forEach(chancedResults::plusAssign)

            recipeId suffix "_from_crushed_ore"
        }
    }
}
