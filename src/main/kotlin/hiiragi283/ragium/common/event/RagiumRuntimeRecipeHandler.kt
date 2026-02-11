package hiiragi283.ragium.common.event

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.component1
import hiiragi283.core.api.component2
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTExtraOreResultMap
import hiiragi283.core.api.material.property.HTMaterialLevel
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.getDefaultFluidAmount
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.getScaledAmount
import hiiragi283.core.common.material.ColoredMaterials
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTCompressingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTFluidWithItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemToChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTPressingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTWashingRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.setup.RagiumFluids
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodChildKeys
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry
import net.minecraft.tags.ItemTags
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

        bathToColor(ItemTags.BANNERS, ColoredMaterials.BANNER)
        bathToColor(ItemTags.BEDS, ColoredMaterials.BED)
        bathToColor(ItemTags.WOOL_CARPETS, ColoredMaterials.CARPET)
        bathToColor(ItemTags.WOOL, ColoredMaterials.WOOL)

        cutWoodFromDefinition()

        for (entry: HTMaterialManager.Entry in materialManager) {
            arcSmeltingDustToIngot(event, entry)

            bathDustToPrefix(event, entry, CommonTagPrefixes.GEM)
            bathDustToPrefix(event, entry, CommonTagPrefixes.PEARL)

            compressDustToGem(event, entry)

            crushBaseToDust(event, entry)
            
            crushOreToCrushed(event, entry, CommonTagPrefixes.ORE)
            crushOreToCrushed(event, entry, CommonTagPrefixes.RAW)
            crushCrushedToDust(event, entry)
            
            crushPrefixToDust(event, entry, CommonTagPrefixes.GEAR)
            crushPrefixToDust(event, entry, CommonTagPrefixes.NUGGET)
            crushPrefixToDust(event, entry, CommonTagPrefixes.PLATE)
            crushPrefixToDust(event, entry, CommonTagPrefixes.ROD)
            crushPrefixToDust(event, entry, CommonTagPrefixes.WIRE)

            cutBlockToPlate(event, entry)

            meltPrefixToMolten(event, entry, CommonTagPrefixes.DUST)
            meltPrefixToMolten(event, entry, CommonTagPrefixes.GEM)
            meltPrefixToMolten(event, entry, CommonTagPrefixes.INGOT)
            meltPrefixToMolten(event, entry, CommonTagPrefixes.PEARL)

            mixFlourToDough(event, entry)

            pressBaseToPrefix(event, entry, CommonTagPrefixes.GEAR, HTMoldType.GEAR)
            pressBaseToPrefix(event, entry, CommonTagPrefixes.PLATE, HTMoldType.PLATE)
            pressBaseToPrefix(event, entry, CommonTagPrefixes.ROD, HTMoldType.ROD)

            solidifyPrefix(event, entry, CommonTagPrefixes.BLOCK, HTMoldType.BLOCK)
            solidifyPrefix(event, entry, CommonTagPrefixes.GEAR, HTMoldType.GEAR)
            solidifyPrefix(event, entry, CommonTagPrefixes.GEM, HTMoldType.GEM)
            solidifyPrefix(event, entry, CommonTagPrefixes.INGOT, HTMoldType.INGOT)
            solidifyPrefix(event, entry, CommonTagPrefixes.NUGGET, HTMoldType.NUGGET)
            solidifyPrefix(event, entry, CommonTagPrefixes.PEARL, HTMoldType.BALL)
            solidifyPrefix(event, entry, CommonTagPrefixes.PLATE, HTMoldType.PLATE)
            solidifyPrefix(event, entry, CommonTagPrefixes.ROD, HTMoldType.ROD)

            washCrushedOre(event, entry)
        }
    }

    @JvmStatic
    private fun getMolten(material: HTMaterialLike): HTFluidContent? =
        HiiragiCoreAccess.INSTANCE.materialContents.getMoltenFluidMap()[material.asMaterialKey()]

    //    Arc Smelting    //

    @JvmStatic
    private fun arcSmeltingDustToIngot(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 放電に必要なガスを取得
        val meltingLevel: HTMaterialLevel = entry.getOrDefault(HTMaterialPropertyKeys.MELTING_POINT)
        val (inputGas: HTFluidContent, amount: Int) = when (meltingLevel) {
            HTMaterialLevel.NONE -> return
            HTMaterialLevel.LOW -> return
            HTMaterialLevel.MEDIUM -> RagiumFluids.NITROGEN to 250
            HTMaterialLevel.HIGH -> RagiumFluids.NITROGEN to 500
            HTMaterialLevel.HIGHEST -> RagiumFluids.HELIUM to 250
        }
        // 材料が存在するか判定
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        if (!event.isPresentTag(crushedPrefix, entry)) return
        // 素材のプロパティから液体材料を取得
        val fluidAmount: Int = crushedPrefix.getScaledAmount(entry.getDefaultFluidAmount(), entry).toInt()
        // 完成品を取得
        val molten: HTFluidContent = getMolten(entry) ?: return
        // レシピを登録
        HTFluidWithItemRecipeBuilder.arcSmelting(output) {
            itemIngredient = inputCreator.create(crushedPrefix, entry)
            fluidIngredient = inputCreator.create(inputGas, amount)
            result = resultCreator.create(molten, fluidAmount)
            recipeId suffix "_from_${crushedPrefix.name}"
        }
    }

    //    Bathing    //

    @JvmStatic
    private fun bathDustToPrefix(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, prefix: HTTagPrefix) {
        // 材料が存在するか判定
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        if (!event.isPresentTag(crushedPrefix, entry)) return
        // 完成品を取得
        val resultItem: HTItemHolderLike<*> = event.getFirstHolder(prefix, entry) ?: return
        // レシピを登録
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(crushedPrefix, entry)
            fluidIngredient = inputCreator.water(125)
            result = resultCreator.create(resultItem)
            time /= 2
            recipeId suffix "from_${crushedPrefix.name}"
        }
    }

    @JvmStatic
    private fun bathToColor(inputTag: TagKey<Item>, map: Map<HTDefaultColor, HTItemHolderLike<*>>) {
        for ((color: HTDefaultColor, colored: HTItemHolderLike<*>) in map) {
            val dye: HTFluidContent = HCFluids.getDye(color)
            // レシピを登録
            HTFluidWithItemRecipeBuilder.bathing(output) {
                itemIngredient = inputCreator.create(inputTag)
                fluidIngredient = inputCreator.create(dye, 250)
                result = resultCreator.create(colored)
                time /= 2
            }
        }
    }

    //    Compressing    //

    @JvmStatic
    private fun compressDustToGem(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PREFIX)
        if (!event.isPresentTag(crushedPrefix, entry)) return
        // 完成品を取得
        val gem: HTItemHolderLike<*> = event.getFirstHolder(CommonTagPrefixes.GEM, entry) ?: return
        // レシピを登録
        HTCompressingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(crushedPrefix, entry, 4)
            power = 8
            catalyst = inputCreator.create(HTMoldType.GEM)
            result = resultCreator.create(gem, 3)
            recipeId suffix "_from_${crushedPrefix.name}"
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
            time *= 3
            recipeId suffix "_from_block"
        }
    }

    @JvmStatic
    private fun cutWoodFromDefinition() {
        for (type: WoodType in WoodTypeRegistry.INSTANCE) {
            val planks: ItemLike = type.getItemOfThis(VanillaWoodChildKeys.PLANKS) ?: continue
            // Log -> 6x Planks
            type.getItemOfThis(VanillaWoodChildKeys.LOG)?.let {
                HTItemToChancedRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks, 6)
                    recipeId suffix "_from_log"
                }
            }
            // Wood -> 6x Planks
            type.getItemOfThis(VanillaWoodChildKeys.WOOD)?.let {
                HTItemToChancedRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks, 6)
                    recipeId suffix "_from_wood"
                }
            }
            // Boat
            type.getItemOfThis(VanillaWoodChildKeys.BOAT)?.let { boat ->
                HTItemToChancedRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(boat)
                    result = resultCreator.create(planks, 5)
                    recipeId suffix "_from_boat"
                }
                // Chest Boat
                type.getItemOfThis(VanillaWoodChildKeys.CHEST_BOAT)?.let {
                    HTItemToChancedRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(boat)
                        extraResults += HTChancedItemResult.create {
                            result = resultCreator.create(Items.CHEST)
                        }
                    }
                }
            }
            // Button
            // Fence
            type.getItemOfThis(VanillaWoodChildKeys.FENCE)?.let {
                HTItemToChancedRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks)
                    extraResults += HTChancedItemResult.create {
                        result = resultCreator.create(Items.STICK)
                    }
                    recipeId suffix "_from_fence"
                }
            }
            // Fence Gate
            type.getItemOfThis(VanillaWoodChildKeys.FENCE_GATE)?.let {
                HTItemToChancedRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks, 2)
                    extraResults += HTChancedItemResult.create {
                        result = resultCreator.create(Items.STICK, 4)
                    }
                    recipeId suffix "_from_fence_gate"
                }
            }
            // Pressure Plate
            type.getItemOfThis(VanillaWoodChildKeys.PRESSURE_PLATE)?.let {
                HTItemToChancedRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks, 2)
                    recipeId suffix "_from_pressure_plate"
                }
            }
            // Sign
            type.getItemOfThis(VanillaWoodChildKeys.SIGN)?.let {
                HTItemToChancedRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks, 2)
                    extraResults += HTChancedItemResult.create {
                        result = resultCreator.create(Items.STICK)
                        chance = fraction(1, 3)
                    }
                    recipeId suffix "_from_sign"
                }
            }
            // Hanging Sign
            type.getItemOfThis(VanillaWoodChildKeys.HANGING_SIGN)?.let {
                HTItemToChancedRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks, 4)
                    extraResults += HTChancedItemResult.create {
                        result = resultCreator.create(Items.CHAIN)
                        chance = fraction(1, 3)
                    }
                    recipeId suffix "_from_hanging_sign"
                }
            }
            // Slab
            type.getItemOfThis(VanillaWoodChildKeys.SLAB)?.let {
                HTItemToChancedRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(planks)
                    result = resultCreator.create(it, 2)
                }
            }
            // Stairs
            // Door
            type.getItemOfThis(VanillaWoodChildKeys.DOOR)?.let {
                HTItemToChancedRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks, 2)
                    recipeId suffix "_from_door"
                }
            }
            // Trapdoor
            type.getItemOfThis(VanillaWoodChildKeys.TRAPDOOR)?.let {
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
        val hardness: HTMaterialLevel = entry.getOrDefault(HTMaterialPropertyKeys.HARDNESS)
        if (hardness == HTMaterialLevel.NONE) return
        // 素材のプロパティから材料を取得
        val inputTag: TagKey<Item> = entry.getDefaultPart(entry) ?: return
        if (!event.isPresentTag(inputTag)) return
        // 加工の前後でタグが一致する場合はパス
        if (inputTag == prefix.itemTagKey(entry)) return
        // 完成品を取得
        val result: HTItemHolderLike<*> = event.getFirstHolder(prefix, entry) ?: return
        // プレフィックスのスケールから個数を算出
        val (inputCount: Int, outputCount: Int) = prefix.getScaledAmount(1, entry)
        // レシピを登録
        if (hardness == HTMaterialLevel.HIGHEST) {
            HTCompressingRecipeBuilder.create(output) {
                this.ingredient = inputCreator.create(inputTag, inputCount)
                this.power = 8
                this.catalyst = inputCreator.create(moldType)
                this.result = resultCreator.create(result, outputCount)
                this.recipeId suffix "_from_${inputTag.location().path}"
            }
        } else {
            HTPressingRecipeBuilder.pressing(output) {
                this.top = inputCreator.create(inputTag, inputCount)
                this.bottom = inputCreator.create(moldType)
                this.result = resultCreator.create(result, outputCount)
                this.recipeId suffix "_from_${inputTag.location().path}"
            }
        }
    }

    //    Melting    //

    @JvmStatic
    private fun meltPrefixToMolten(event: HTRegisterRuntimeRecipeEvent, entry: HTMaterialManager.Entry, prefix: HTTagPrefix) {
        val meltingLevel: HTMaterialLevel = entry.getOrDefault(HTMaterialPropertyKeys.MELTING_POINT)
        val recipeTime: Int = when (meltingLevel) {
            HTMaterialLevel.NONE -> return
            HTMaterialLevel.LOW -> 20 * 5
            HTMaterialLevel.MEDIUM -> 20 * 10
            HTMaterialLevel.HIGH -> 20 * 20
            HTMaterialLevel.HIGHEST -> return
        }
        // 材料が存在するか判定
        if (!event.isPresentTag(prefix, entry)) return
        // 素材のプロパティから液体材料を取得
        val fluidAmount: Int = prefix.getScaledAmount(entry.getDefaultFluidAmount(), entry).toInt()
        // 完成品を取得
        val molten: HTFluidContent = getMolten(entry) ?: return
        // レシピを登録
        HTSingleRecipeBuilder.melting(output) {
            ingredient = inputCreator.create(prefix, entry)
            result = resultCreator.create(molten, fluidAmount)
            recipeId suffix "_from_${prefix.name}"
            time = recipeTime
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
        val molten: HTFluidContent = getMolten(entry) ?: return
        // プレフィックスと素材のプロパティから液体量を算出
        val fluidAmount: Int = prefix.getScaledAmount(entry.getDefaultFluidAmount(), entry).toInt()
        // レシピを登録
        val resultItem: HTItemHolderLike<*> = event.getFirstHolder(prefix, entry) ?: return
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
