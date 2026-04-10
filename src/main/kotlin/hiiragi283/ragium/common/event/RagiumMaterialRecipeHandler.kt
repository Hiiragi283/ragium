package hiiragi283.ragium.common.event

import hiiragi283.core.api.component1
import hiiragi283.core.api.component2
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.property.getScaledAmount
import hiiragi283.core.api.material.part.tagPrefix
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTExtraOreResultMap
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.getDefaultFluidAmount
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.data.recipe.blueprint
import hiiragi283.core.common.data.recipe.builder.HTDoubleMultiOutputRecipeBuilder
import hiiragi283.core.common.event.HCRuntimeRecipeHandler
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTagPrefixes
import hiiragi283.ragium.common.data.recipe.HTFreezingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMeltingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.tags.TagKey
import net.minecraft.util.Mth
import net.minecraft.world.item.Item
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumMaterialRecipeHandler : HTRecipeProviderContext.Delegated() {
    override lateinit var delegated: HTRecipeProviderContext

    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        this.delegated = event.context

        for (entry: HTMaterialManager.Entry in materialManager) {
            // Basic
            cutBaseToRod(event, entry)
            cutBlockToPlate(event, entry)

            forgeDustToPellet(event, entry)
            // Heat
            meltPrefixToMolten(event, entry, CommonParts.DUST)
            meltPrefixToMolten(event, entry, CommonParts.GEM)
            meltPrefixToMolten(event, entry, CommonParts.INGOT)
            meltPrefixToMolten(event, entry, CommonParts.PEARL)

            refineDustToPrefix(event, entry, CommonTagPrefixes.GEM)
            refineDustToPrefix(event, entry, CommonTagPrefixes.PEARL)
            // Cool
            freezeMoltenToPrefix(event, entry, CommonParts.DUST)
            freezeMoltenToPrefix(event, entry, CommonParts.INGOT)
            freezeMoltenToPrefix(event, entry, CommonParts.GEM)
            freezeMoltenToPrefix(event, entry, CommonParts.PEARL)

            freezeMoltenToPrefix(event, entry, CommonParts.GEAR)
            freezeMoltenToPrefix(event, entry, CommonParts.PLATE)
            freezeMoltenToPrefix(event, entry, CommonParts.ROD)
            freezeMoltenToPrefix(event, entry, CommonParts.WIRE)
            // Chemical
            washCrushedOre(event, entry)
        }
    }

    @JvmStatic
    private fun getBlueprint(prefix: HTTagPrefix): HTItemIngredient {
        return when (prefix) {
            RagiumTagPrefixes.PELLET -> 8
            else -> return HCRuntimeRecipeHandler.getBlueprint(prefix)
        }.let(inputCreator::blueprint)
    }

    //    Cutting    //

    @JvmStatic
    private fun cutBaseToRod(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        val defaultPart: HTDefaultPart = entry.getDefaultPart() ?: return
        val inputTag: TagKey<Item> = defaultPart.getTag(entry)
        if (!event.isPresentTag(inputTag)) return
        // 加工の前後でタグが一致する場合はパス
        if (inputTag == CommonTagPrefixes.ROD.itemTagKey(entry)) return
        // 完成品を取得
        val rod: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.ROD, entry) ?: return
        // プレフィックスのスケールから個数を算出
        val (inputCount: Int, outputCount: Int) = CommonParts.ROD.getScaledAmount(1, entry)
        // レシピを登録
        RagiumRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(inputTag, inputCount)
            results += resultCreator.create(rod, outputCount)
            time = HCRuntimeRecipeHandler.getTimeFromHardness(entry, time * 3) ?: return
            recipeId suffix "_from_${defaultPart.getSuffix()}"
        }
    }

    @JvmStatic
    private fun cutBlockToPlate(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        if (!event.isPresentTag(CommonTagPrefixes.STORAGE_BLOCK, entry)) return
        // 完成品を取得
        val plate: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.PLATE, entry) ?: return
        // レシピを登録
        RagiumRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.STORAGE_BLOCK, entry)
            results += resultCreator.create(plate, entry.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK).baseCount)
            time = HCRuntimeRecipeHandler.getTimeFromHardness(entry, time * 3) ?: (time * 3)
            recipeId suffix "_from_block"
        }
    }

    //    Forging    //

    @JvmStatic
    private fun forgeDustToPellet(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART).tagPrefix ?: return
        if (!event.isPresentTag(crushedPrefix, entry)) return
        // 完成品を取得
        val pellet: HTItemHolderLike<*> = event.getFirstHolder(RagiumTagPrefixes.PELLET, entry) ?: return
        // レシピを登録
        HTDoubleMultiOutputRecipeBuilder.forging(output) {
            base = inputCreator.create(crushedPrefix, entry, 8)
            addition = getBlueprint(RagiumTagPrefixes.PELLET)
            results += resultCreator.create(pellet)
            recipeId suffix "_from_dust"
        }
    }

    //    Freezing    //

    @JvmStatic
    private fun freezeMoltenToPrefix(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, part: HTPartLike) {
        val prefix: HTTagPrefix = part.tagPrefix ?: return
        // 材料が存在するか判定
        if (!event.isPresentTag(HTFluidPart.MOLTEN, entry)) return
        // レシピを登録
        val resultItem: HTItemHolderLike<*> = event.getFirstHolder(prefix, entry) ?: return
        HTFreezingRecipeBuilder.create(output) {
            itemIngredient = getBlueprint(prefix)
            fluidIngredient = inputCreator.create(HTFluidPart.MOLTEN, entry) {
                part.getScaledAmount(it, entry).toInt()
            }
            result = resultCreator.create(resultItem)
            recipeId suffix "_from_molten"
        }
    }

    //    Melting    //

    @JvmStatic
    private fun meltPrefixToMolten(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, part: HTPartLike) {
        val prefix: HTTagPrefix = part.tagPrefix ?: return
        // 材料が存在するか判定
        if (!event.isPresentTag(prefix, entry)) return
        // 素材のプロパティから液体材料を取得
        val fluidAmount: Int = part.getScaledAmount(entry.getDefaultFluidAmount(), entry).toInt()
        // 完成品を取得
        val molten: HTFluidHolderLike<*> = event.getFirstHolder(HTFluidPart.MOLTEN, entry) ?: return
        // レシピを登録
        HTMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(prefix, entry)
            result = resultCreator.create(molten, fluidAmount)
            recipeId suffix "_from_${part.asPartName()}"
            time = HCRuntimeRecipeHandler.getTimeFromMelting(entry, time) ?: return
        }
    }

    //    Mixing    //

    /*private fun mixFlourToDough(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 素材のプロパティから材料を取得
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        if (!event.isPresentTag(crushedPrefix, entry)) return
        // 完成品を取得
        val dough: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.DOUGH, entry) ?: return
        // レシピを登録
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(crushedPrefix, entry)
            fluidIngredients += inputCreator.water(250)
            time /= 2
            itemResults += resultCreator.create(dough)
        }
    }*/

    //    Refining    //

    @JvmStatic
    private fun refineDustToPrefix(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, prefix: HTTagPrefix) {
        // 材料が存在するか判定
        val crushedPart: HTPartLike = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART)
        val crushedPrefix: HTTagPrefix = crushedPart.tagPrefix ?: return
        if (!event.isPresentTag(crushedPrefix, entry)) return
        // 完成品を取得
        val resultItem: HTItemHolderLike<*> = event.getFirstHolder(prefix, entry) ?: return
        // レシピを登録
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(crushedPrefix, entry)
            ingredient += inputCreator.water(125)
            result += resultCreator.create(resultItem)
            time /= 2
            recipeId suffix "from_${crushedPart.asPartName()}"
        }
    }

    //    Washing    //

    @JvmStatic
    private fun washCrushedOre(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        if (!event.isPresentTag(CommonTagPrefixes.CRUSHED_ORE, entry)) return
        // 完成品を取得
        val dust: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.DUST, entry) ?: return
        // レシピを登録
        // 水 -> 主産物 + 副産物
        RagiumRecipeBuilder.washing(output) {
            // 材料
            ingredient = inputCreator.create(CommonTagPrefixes.CRUSHED_ORE, entry)
            // 主産物
            results += resultCreator.create(dust, CommonParts.CRUSHED_ORE.getScaledAmount(1, entry).toInt())
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(HTExtraOreResultMap.Phase.WASH_CRUSHED)
                ?.let(results::add)

            recipeId suffix "_from_crushed_ore/water"
        }
        // 硫酸 -> 1.5x 主産物
        HTItemOrFluidRecipeBuilder.refining(output) {
            // 材料
            ingredient += inputCreator.create(CommonTagPrefixes.CRUSHED_ORE, entry)
            ingredient += inputCreator.create(RagiumFluids.SULFURIC_ACID, 250)
            // 主産物
            val outputCount: Int = CommonParts.CRUSHED_ORE
                .getScaledAmount(fraction(3, 2), entry)
                .toFloat()
                .let(Mth::ceil)
            result += resultCreator.create(dust, outputCount)

            recipeId suffix "_from_crushed_ore/sulfuric_acid"
        }
        // 水銀 -> 副産物 100%
        /*HTWashingRecipeBuilder.create(output) {
            val (baseResult: HTItemResult, chance: Fraction) = entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(HTExtraOreResultMap.Phase.WASH_CRUSHED)
                ?: return
            if (chance == Fraction.ZERO) return
            val inputAmount: Fraction = 250 * chance.invert()
            // 材料
            itemIngredient = inputCreator.create(CommonTagPrefixes.CRUSHED_ORE, entry)
            fluidIngredient = inputCreator.create(RagiumFluids.MERCURY, inputAmount.toInt())
            // 主産物
            this.result = baseResult

            recipeId suffix "_from_crushed_ore/mercury"
        }*/
    }
}
