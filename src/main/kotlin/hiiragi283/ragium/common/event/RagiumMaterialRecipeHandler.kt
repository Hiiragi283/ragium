package hiiragi283.ragium.common.event

import hiiragi283.core.api.component1
import hiiragi283.core.api.component2
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTExtraOreResultMap
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.getDefaultFluidAmount
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.fluid.CommonFluidTagPrefixes
import hiiragi283.core.api.tag.property.getScaledAmount
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTItemAndItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemToChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemToItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTWashingRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
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
            crushBaseToDust(event, entry)
            crushOreToCrushed(event, entry, CommonTagPrefixes.ORE)
            crushOreToCrushed(event, entry, CommonTagPrefixes.RAW)
            crushCrushedToDust(event, entry)
            crushPrefixToDust(event, entry, CommonTagPrefixes.GEAR)
            crushPrefixToDust(event, entry, CommonTagPrefixes.NUGGET)
            crushPrefixToDust(event, entry, CommonTagPrefixes.PLATE)
            crushPrefixToDust(event, entry, CommonTagPrefixes.ROD)
            crushPrefixToDust(event, entry, CommonTagPrefixes.WIRE)

            cutBaseToRod(event, entry)
            cutBlockToPlate(event, entry)

            pressBaseToGear(event, entry)
            pressBaseToPlate(event, entry)

            wireBaseToWire(event, entry)
            // Heat
            meltPrefixToMolten(event, entry, CommonTagPrefixes.DUST)
            meltPrefixToMolten(event, entry, CommonTagPrefixes.GEM)
            meltPrefixToMolten(event, entry, CommonTagPrefixes.INGOT)
            meltPrefixToMolten(event, entry, CommonTagPrefixes.PEARL)
            // Cool
            freezeMoltenToPrefix(event, entry, CommonTagPrefixes.GEAR, HTMoldType.GEAR)
            freezeMoltenToPrefix(event, entry, CommonTagPrefixes.GEM, HTMoldType.GEM)
            freezeMoltenToPrefix(event, entry, CommonTagPrefixes.INGOT, HTMoldType.INGOT)
            freezeMoltenToPrefix(event, entry, CommonTagPrefixes.NUGGET, HTMoldType.NUGGET)
            freezeMoltenToPrefix(event, entry, CommonTagPrefixes.PEARL, HTMoldType.BALL)
            freezeMoltenToPrefix(event, entry, CommonTagPrefixes.PLATE, HTMoldType.PLATE)
            freezeMoltenToPrefix(event, entry, CommonTagPrefixes.ROD, HTMoldType.ROD)
            // Chemical
            bathDustToPrefix(event, entry, CommonTagPrefixes.GEM)
            bathDustToPrefix(event, entry, CommonTagPrefixes.PEARL)

            washCrushedOre(event, entry)
        }
    }

    @JvmStatic
    private fun getTimeFromHardness(propertyMap: HTPropertyMap, time: Int = 20 * 10): Int? =
        (propertyMap.getOrDefault(HTMaterialPropertyKeys.HARDNESS) * time)?.toInt()

    @JvmStatic
    private fun getTimeFromMelting(propertyMap: HTPropertyMap, time: Int = 20 * 10): Int? =
        (propertyMap.getOrDefault(HTMaterialPropertyKeys.MELTING_POINT) * time)?.toInt()

    //    Bathing    //

    @JvmStatic
    private fun bathDustToPrefix(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, prefix: HTTagPrefix) {
        // 材料が存在するか判定
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        if (!event.isPresentTag(crushedPrefix, entry)) return
        // 完成品を取得
        val resultItem: HTItemHolderLike<*> = event.getFirstHolder(prefix, entry) ?: return
        // レシピを登録
        HTItemOrFluidRecipeBuilder.canning(output) {
            ingredient += inputCreator.create(crushedPrefix, entry)
            ingredient += inputCreator.water(125)
            result += resultCreator.create(resultItem)
            time /= 2
            recipeId suffix "from_${crushedPrefix.name}"
        }
    }

    //    Compressing    //

    //    Crushing    //

    @JvmStatic
    private fun crushPrefixToDust(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, prefix: HTTagPrefix) {
        // 材料が存在するか判定
        if (!event.isPresentTag(prefix, entry)) return
        // 素材のプロパティから完成品を取得
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        val dust: HTItemHolderLike<*> = event.getFirstHolder(crushedPrefix, entry) ?: return
        // プレフィックスのスケールから個数を算出
        val (outputCount: Int, inputCount: Int) = prefix.getScaledAmount(1, entry)
        // レシピを登録
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(prefix, entry, inputCount)
            this.result = resultCreator.create(dust, outputCount)
            time = getTimeFromHardness(entry, time) ?: return
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
        val dust: HTItemHolderLike<*> = event.getFirstHolder(crushedPrefix, entry) ?: return
        // レシピを登録
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(inputTag)
            this.result = resultCreator.create(dust)
            time = getTimeFromHardness(entry, time) ?: return
            recipeId suffix "_from_${defaultPart.getSuffix()}"
        }
    }

    @JvmStatic
    private fun crushOreToCrushed(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, prefix: HTTagPrefix) {
        // 材料が存在するか判定
        if (!event.isPresentTag(prefix, entry)) return
        // 完成品を取得
        val crushedOre: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.CRUSHED_ORE, entry) ?: return
        // レシピを登録
        HTItemToChancedRecipeBuilder.crushing(output) {
            // 材料
            ingredient = inputCreator.create(prefix, entry)
            // 主産物
            this.result = resultCreator.create(crushedOre, prefix.getScaledAmount(2, entry).toInt())
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(HTExtraOreResultMap.Phase.CRUSH_ORE)
                ?.let(extraResult::plusAssign)

            recipeId suffix "_from_${prefix.name}"
        }
    }

    @JvmStatic
    private fun crushCrushedToDust(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        if (!event.isPresentTag(CommonTagPrefixes.CRUSHED_ORE, entry)) return
        // 完成品を取得
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        val dust: ItemLike = event.getFirstHolder(crushedPrefix, entry) ?: return
        // プレフィックスのスケールから個数を算出
        val (outputCount: Int, inputCount: Int) = CommonTagPrefixes.CRUSHED_ORE.getScaledAmount(1, entry)
        // レシピを登録
        HTItemToChancedRecipeBuilder.crushing(output) {
            // 材料
            ingredient = inputCreator.create(CommonTagPrefixes.CRUSHED_ORE, entry, inputCount)
            // 主産物
            this.result = resultCreator.create(dust, outputCount)
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(HTExtraOreResultMap.Phase.CRUSH_CRUSHED)
                ?.let(extraResult::plusAssign)

            recipeId suffix "_from_crushed_ore"
        }
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
        val (inputCount: Int, outputCount: Int) = CommonTagPrefixes.ROD.getScaledAmount(1, entry)
        // レシピを登録
        HTItemToChancedRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(inputTag, inputCount)
            result = resultCreator.create(rod, outputCount)
            time = getTimeFromHardness(entry, time * 3) ?: return
            recipeId suffix "_from_${defaultPart.getSuffix()}"
        }
    }

    @JvmStatic
    private fun cutBlockToPlate(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        if (!event.isPresentTag(CommonTagPrefixes.BLOCK, entry)) return
        // 完成品を取得
        val plate: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.PLATE, entry) ?: return
        // レシピを登録
        HTItemToChancedRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.BLOCK, entry)
            result = resultCreator.create(plate, entry.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK).baseCount)
            time = getTimeFromHardness(entry, time * 3) ?: (time * 3)
            recipeId suffix "_from_block"
        }
    }

    //    Freezing    //

    @JvmStatic
    private fun freezeMoltenToPrefix(
        event: HTRegisterRuntimeRecipeEvent,
        entry: HTMaterialManager.Entry,
        prefix: HTTagPrefix,
        moldType: HTMoldType,
    ) {
        // 材料が存在するか判定
        if (!event.isPresentTag(CommonFluidTagPrefixes.MOLTEN, entry)) return
        // レシピを登録
        val resultItem: HTItemHolderLike<*> = event.getFirstHolder(prefix, entry) ?: return
        HTItemOrFluidRecipeBuilder.freezing(output) {
            ingredient += inputCreator.create(CommonFluidTagPrefixes.MOLTEN, entry) {
                prefix.getScaledAmount(it, entry).toInt()
            }
            ingredient += inputCreator.create(moldType.asItem(), amount = 0)
            result += resultCreator.create(resultItem)
            recipeId suffix "_from_molten"
        }
    }

    //    Melting    //

    @JvmStatic
    private fun meltPrefixToMolten(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, prefix: HTTagPrefix) {
        // 材料が存在するか判定
        if (!event.isPresentTag(prefix, entry)) return
        // 素材のプロパティから液体材料を取得
        val fluidAmount: Int = prefix.getScaledAmount(entry.getDefaultFluidAmount(), entry).toInt()
        // 完成品を取得
        val molten: HTFluidHolderLike<*> = event.getFirstHolder(CommonFluidTagPrefixes.MOLTEN, entry) ?: return
        // レシピを登録
        HTItemOrFluidRecipeBuilder.melting(output) {
            ingredient += inputCreator.create(prefix, entry)
            result += resultCreator.create(molten, fluidAmount)
            recipeId suffix "_from_${prefix.name}"
            time = getTimeFromMelting(entry, time) ?: return
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

    //    Pressing    //

    @JvmStatic
    private fun pressBaseToGear(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 素材のプロパティから材料を取得
        val inputTag: TagKey<Item> = entry.getDefaultPart(entry) ?: return
        if (!event.isPresentTag(inputTag)) return
        // 完成品を取得
        val gear: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.GEAR, entry) ?: return
        // レシピを登録
        HTItemAndItemRecipeBuilder.pressing(output) {
            first = inputCreator.create(inputTag, 4)
            second = inputCreator.create(HTMoldType.GEAR)
            result = resultCreator.create(gear)
            time = getTimeFromHardness(entry, time) ?: return
        }
    }

    @JvmStatic
    private fun pressBaseToPlate(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 基本アイテムがインゴットの素材を除外
        val inputPrefix: HTTagPrefix? = when (entry.getDefaultPart()) {
            HTDefaultPart.Prefixed.FUEL -> null
            HTDefaultPart.Prefixed.GEM -> null
            HTDefaultPart.Prefixed.INGOT -> CommonTagPrefixes.INGOT
            HTDefaultPart.Prefixed.PEARL -> null
            is HTDefaultPart.BuiltIn -> CommonTagPrefixes.DUST
            null -> CommonTagPrefixes.DUST
        }
        if (inputPrefix == null) return
        // 材料が存在するか判定
        if (!event.isPresentTag(inputPrefix, entry)) return
        // 完成品を取得
        val plate: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.PLATE, entry) ?: return
        // レシピを登録
        HTItemAndItemRecipeBuilder.pressing(output) {
            first = inputCreator.create(inputPrefix, entry)
            second = inputCreator.create(HTMoldType.PLATE)
            result = resultCreator.create(plate)
            time = getTimeFromHardness(entry, time) ?: return
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
        HTWashingRecipeBuilder.create(output) {
            // 材料
            itemIngredient = inputCreator.create(CommonTagPrefixes.CRUSHED_ORE, entry)
            fluidIngredient = inputCreator.water(250)
            // 主産物
            this.result = resultCreator.create(dust, CommonTagPrefixes.CRUSHED_ORE.getScaledAmount(1, entry).toInt())
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(HTExtraOreResultMap.Phase.WASH_CRUSHED)
                ?.let(extraResult::plusAssign)

            recipeId suffix "_from_crushed_ore"
        }
    }

    //    Wiring    //

    @JvmStatic
    private fun wireBaseToWire(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        val inputTag: TagKey<Item> = entry.getDefaultPart(entry) ?: return
        if (!event.isPresentTag(inputTag)) return
        // 完成品を取得
        val wire: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.WIRE, entry) ?: return
        // レシピを登録
        HTItemToItemRecipeBuilder.wiring(output) {
            ingredient = inputCreator.create(inputTag)
            result = resultCreator.create(wire, 2)
            time = getTimeFromHardness(entry, time) ?: return
        }
    }
}
