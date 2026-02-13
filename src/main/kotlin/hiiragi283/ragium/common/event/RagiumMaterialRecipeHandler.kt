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
import hiiragi283.ragium.common.data.recipe.HTChemicalRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemAndFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemToChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTPressingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
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
            bendIngotToPlate(event, entry)

            compressDustToPlate(event, entry)

            crushBaseToDust(event, entry)
            crushOreToCrushed(event, entry, CommonTagPrefixes.ORE)
            crushOreToCrushed(event, entry, CommonTagPrefixes.RAW)
            crushCrushedToDust(event, entry)
            crushPrefixToDust(event, entry, CommonTagPrefixes.GEAR)
            crushPrefixToDust(event, entry, CommonTagPrefixes.NUGGET)
            crushPrefixToDust(event, entry, CommonTagPrefixes.PLATE)
            crushPrefixToDust(event, entry, CommonTagPrefixes.ROD)
            crushPrefixToDust(event, entry, CommonTagPrefixes.WIRE)

            // cutBlockToPlate(event, entry)

            latheBaseToRod(event, entry)

            pressBaseToGear(event, entry)
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

            mixFlourToDough(event, entry)

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
        HTItemAndFluidRecipeBuilder.canning(output) {
            itemIngredient = inputCreator.create(crushedPrefix, entry)
            fluidIngredient = inputCreator.water(125)
            result = resultCreator.create(resultItem)
            time /= 2
            recipeId suffix "from_${crushedPrefix.name}"
        }
    }

    //    Bending    //

    @JvmStatic
    private fun bendIngotToPlate(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        if (!event.isPresentTag(CommonTagPrefixes.INGOT, entry)) return
        // 完成品を取得
        val plate: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.PLATE, entry) ?: return
        // レシピを登録
        HTSingleRecipeBuilder.bending(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.INGOT, entry)
            result = resultCreator.create(plate)
            time = getTimeFromHardness(entry, time) ?: return
        }
    }

    //    Compressing    //

    @JvmStatic
    private fun compressDustToPlate(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 基本アイテムがインゴットの素材を除外
        val defaultPart: HTDefaultPart? = entry.getDefaultPart()
        if (defaultPart is HTDefaultPart.Prefixed && defaultPart.prefix == CommonTagPrefixes.INGOT) return
        // 材料が存在するか判定
        if (!event.isPresentTag(CommonTagPrefixes.DUST, entry)) return
        // 完成品を取得
        val plate: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.PLATE, entry) ?: return
        // レシピを登録
        HTSingleRecipeBuilder.compressing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, entry)
            result = resultCreator.create(plate)
            time = getTimeFromHardness(entry, time) ?: return
        }
    }

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
            result = resultCreator.create(dust, outputCount)
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
            result = resultCreator.create(dust)
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
            result = resultCreator.create(crushedOre, prefix.getScaledAmount(2, entry).toInt())
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(HTExtraOreResultMap.Phase.CRUSH_ORE)
                ?.let(extraResults::add)

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
        // レシピを登録
        HTItemToChancedRecipeBuilder.crushing(output) {
            // 材料
            ingredient = inputCreator.create(CommonTagPrefixes.CRUSHED_ORE, entry)
            // 主産物
            result = resultCreator.create(dust, CommonTagPrefixes.CRUSHED_ORE.getScaledAmount(1, entry).toInt())
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(HTExtraOreResultMap.Phase.CRUSH_CRUSHED)
                ?.let(extraResults::add)

            recipeId suffix "_from_crushed_ore"
        }
    }

    //    Cutting    //

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
            ingredient += inputCreator.create(moldType, amount = 0)
            result += resultCreator.create(resultItem)
            recipeId suffix "_from_molten"
        }
    }

    //    Lathing    //

    @JvmStatic
    private fun latheBaseToRod(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        val inputTag: TagKey<Item> = entry.getDefaultPart(entry) ?: return
        if (!event.isPresentTag(inputTag)) return
        // 完成品を取得
        val rod: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.ROD, entry) ?: return
        // レシピを登録
        HTSingleRecipeBuilder.lathing(output) {
            ingredient = inputCreator.create(inputTag)
            result = resultCreator.create(rod, 2)
            time = getTimeFromHardness(entry, time) ?: return
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

    @JvmStatic
    private fun mixFlourToDough(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
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
    }

    //    Pressing    //

    @JvmStatic
    private fun pressBaseToGear(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 素材のプロパティから材料を取得
        val inputTag: TagKey<Item> = entry.getDefaultPart(entry) ?: return
        if (!event.isPresentTag(inputTag)) return
        // 完成品を取得
        val gear: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.GEAR, entry) ?: return
        // レシピを登録
        HTPressingRecipeBuilder.pressing(output) {
            top = inputCreator.create(inputTag, 4)
            bottom = inputCreator.create(HTMoldType.GEAR)
            result = resultCreator.create(gear)
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
            result = resultCreator.create(dust, CommonTagPrefixes.CRUSHED_ORE.getScaledAmount(1, entry).toInt())
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(HTExtraOreResultMap.Phase.WASH_CRUSHED)
                ?.let(extraResults::add)

            recipeId suffix "_from_crushed_ore"
        }
    }
}
