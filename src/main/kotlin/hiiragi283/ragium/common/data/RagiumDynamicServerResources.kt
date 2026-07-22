package hiiragi283.ragium.common.data

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.color.VanillaColoredCollections
import hiiragi283.core.api.component1
import hiiragi283.core.api.component2
import hiiragi283.core.api.data.pack.HTDynamicDataRegister
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialContents
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
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.core.api.registry.forEachData
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.SimpleSupplierWithKey
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.data.HCDynamicRecipeProvider
import hiiragi283.ragium.common.data.recipe.HTFreezingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.material.part.RagiumParts
import hiiragi283.ragium.setup.RagiumFluids
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodChildKeys
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.TagKey
import net.minecraft.util.Mth
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable

internal data object RagiumDynamicServerResources : HTRecipeProviderContext.Delegated() {
    @JvmStatic
    fun initialize() {
        delegate = HTDynamicDataRegister

        redox()

        cutWoodFromDefinition()
        cutBedToPlanks()
        waxing()

        // Material
        for (entry: HTMaterialManager.Entry in materialManager) {
            // Basic
            compressDustToPellet(entry)
            compressIngotToPlate(entry)

            cutBaseToRod(entry)
            cutBlockToPlate(entry)
            // Heat
            meltPrefixToMolten(entry, CommonParts.DUST)
            meltPrefixToMolten(entry, CommonParts.GEM)
            meltPrefixToMolten(entry, CommonParts.INGOT)
            meltPrefixToMolten(entry, CommonParts.PEARL)

            implodeDustToPrefix(entry, CommonParts.GEM)
            implodeDustToPrefix(entry, CommonParts.PEARL)
            // Cool
            freezeMoltenToPrefix(entry, CommonParts.DUST)
            freezeMoltenToPrefix(entry, CommonParts.INGOT)
            freezeMoltenToPrefix(entry, CommonParts.GEM)
            freezeMoltenToPrefix(entry, CommonParts.PEARL)

            freezeMoltenToPrefix(entry, CommonParts.GEAR)
            freezeMoltenToPrefix(entry, CommonParts.PLATE)
            freezeMoltenToPrefix(entry, CommonParts.ROD)
            freezeMoltenToPrefix(entry, CommonParts.WIRE)
            // Chemical
            washCrushedOre(entry)
        }
    }

    //    Bathing    //

    @JvmStatic
    private fun redox() {
        BuiltInRegistries.BLOCK.asLookup().forEachData(NeoForgeDataMaps.OXIDIZABLES) { holder: Holder<Block>, oxidizable: Oxidizable ->
            val before: SimpleSupplierWithKey<Block> = holder.toLike()
            val after: SimpleSupplierWithKey<Block> = oxidizable.nextOxidationStage().toLike()
            // レシピを登録
            // Oxidization
            RagiumRecipeBuilder.bathing {
                itemIngredient { +before.get() }
                fluidIngredient {
                    +RagiumFluids.OXYGEN
                    amount = 250
                }
                result { +oxidizable.nextOxidationStage() }
                recipeId suffix "_from_${before.path}"
            }.save(exporter)
            // Reduction
            RagiumRecipeBuilder.bathing {
                itemIngredient { +oxidizable.nextOxidationStage() }
                fluidIngredient {
                    +RagiumFluids.HYDROGEN
                    amount = 250
                }
                result { +before.get() }
                recipeId suffix "_from_${after.path}"
            }.save(exporter)
        }
    }

    //    Compressing    //

    @JvmStatic
    private fun compressDustToPellet(entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        val crushedPrefix: HTTagPrefix = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART).tagPrefix ?: return
        // レシピを登録
        RagiumRecipeBuilder.compressing {
            ingredient {
                +tag(crushedPrefix, entry)
                count = 8
            }
            result { +HTItemResult.MaterialPart(RagiumParts.PELLET, entry) }
            recipeId suffix "_from_dust"
        }.save(exporter)
    }

    @JvmStatic
    private fun compressIngotToPlate(entry: HTMaterialManager.Entry) {
        // レシピを登録
        RagiumRecipeBuilder.compressing {
            ingredient { +tag(CommonTagPrefixes.INGOT, entry) }
            result { +HTItemResult.MaterialPart(CommonParts.PLATE, entry) }
            recipeId suffix "_from_ingot"
        }.save(exporter)
    }

    //    Cutting    //

    @JvmStatic
    private fun cutBaseToRod(entry: HTMaterialManager.Entry) {
        // 材料が存在するか判定
        val defaultPart: HTDefaultPart = entry.getDefaultPart() ?: return
        val inputTag: TagKey<Item> = defaultPart.getTag(entry)
        // 加工の前後でタグが一致する場合はパス
        if (inputTag == CommonTagPrefixes.ROD.itemTagKey(entry)) return
        // プレフィックスのスケールから個数を算出
        val (inputCount: Int, outputCount: Int) = CommonParts.ROD.getScaledAmount(1, entry)
        // レシピを登録
        RagiumRecipeBuilder.cutting {
            ingredient {
                +inputTag
                count = inputCount
            }
            result {
                +HTItemResult.MaterialPart(CommonParts.ROD, entry)
                count = outputCount
            }
            time = HCDynamicRecipeProvider.getTimeFromHardness(entry, time * 3) ?: return
            recipeId suffix "_from_${defaultPart.getSuffix()}"
        }.save(exporter)
    }

    @JvmStatic
    private fun cutBlockToPlate(entry: HTMaterialManager.Entry) {
        // レシピを登録
        RagiumRecipeBuilder.cutting {
            ingredient { +tag(CommonTagPrefixes.STORAGE_BLOCK, entry) }
            result {
                +HTItemResult.MaterialPart(CommonParts.PLATE, entry)
                count = entry.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK).baseCount
            }
            time = HCDynamicRecipeProvider.getTimeFromHardness(entry, time * 3) ?: (time * 3)
            recipeId suffix "_from_block"
        }.save(exporter)
    }

    @JvmStatic
    private fun cutWoodFromDefinition() {
        for (type: WoodType in WoodTypeRegistry.INSTANCE) {
            val planks: ItemLike = type.getItemOfThis(VanillaWoodChildKeys.PLANKS) ?: continue
            // Stripped Log -> 6x Planks
            type.getItemOfThis(VanillaWoodChildKeys.STRIPPED_LOG)?.let { strippedLog: Item ->
                RagiumRecipeBuilder.cutting {
                    ingredient { +strippedLog }
                    result {
                        +planks
                        count = 6
                    }
                    recipeId suffix "_from_log"
                }.save(exporter)
                // Log -> Stripped Log
                type.getItemOfThis(VanillaWoodChildKeys.LOG)?.let {
                    RagiumRecipeBuilder.cutting {
                        ingredient { +it }
                        result { +strippedLog }
                        recipeId suffix "_from_log"
                    }.save(exporter)
                }
            }
            // Stripped Wood -> 6x Planks
            type.getItemOfThis(VanillaWoodChildKeys.STRIPPED_WOOD)?.let { strippedWood: Item ->
                RagiumRecipeBuilder.cutting {
                    ingredient { +strippedWood }
                    result {
                        +planks
                        count = 6
                    }
                    recipeId suffix "_from_wood"
                }.save(exporter)
                // Wood -> Stripped Wood
                type.getItemOfThis(VanillaWoodChildKeys.WOOD)?.let {
                    RagiumRecipeBuilder.cutting {
                        ingredient { +it }
                        result { +strippedWood }
                        recipeId suffix "_from_wood"
                    }.save(exporter)
                }
            }
            // Boat
            type.getItemOfThis(VanillaWoodChildKeys.BOAT)?.let { boat: Item ->
                RagiumRecipeBuilder.cutting {
                    ingredient { +boat }
                    result {
                        +planks
                        count = 5
                    }
                    recipeId suffix "_from_boat"
                }.save(exporter)
                // Chest Boat
                type.getItemOfThis(VanillaWoodChildKeys.CHEST_BOAT)?.let {
                    RagiumRecipeBuilder.cutting {
                        ingredient { +it }
                        result { +boat }
                        result { +Items.CHEST }
                    }.save(exporter)
                }
            }
            // Button
            // Fence
            type.getItemOfThis(VanillaWoodChildKeys.FENCE)?.let {
                RagiumRecipeBuilder.cutting {
                    ingredient { +it }
                    result { +planks }
                    result { +Items.STICK }
                    recipeId suffix "_from_fence"
                }.save(exporter)
            }
            // Fence Gate
            type.getItemOfThis(VanillaWoodChildKeys.FENCE_GATE)?.let {
                RagiumRecipeBuilder.cutting {
                    ingredient { +it }
                    result {
                        +planks
                        count = 2
                    }
                    result {
                        +Items.STICK
                        count = 4
                    }
                    recipeId suffix "_from_fence_gate"
                }.save(exporter)
            }
            // Pressure Plate
            type.getItemOfThis(VanillaWoodChildKeys.PRESSURE_PLATE)?.let {
                RagiumRecipeBuilder.cutting {
                    ingredient { +it }
                    result {
                        +planks
                        count = 2
                    }
                    recipeId suffix "_from_pressure_plate"
                }.save(exporter)
            }
            // Sign
            type.getItemOfThis(VanillaWoodChildKeys.SIGN)?.let {
                RagiumRecipeBuilder.cutting {
                    ingredient { +it }
                    result {
                        +planks
                        count = 2
                    }
                    result {
                        +Items.STICK
                        chance = fraction(1, 3)
                    }
                    recipeId suffix "_from_sign"
                }.save(exporter)
            }
            // Hanging Sign
            type.getItemOfThis(VanillaWoodChildKeys.HANGING_SIGN)?.let {
                RagiumRecipeBuilder.cutting {
                    ingredient { +it }
                    result {
                        +planks
                        count = 4
                    }
                    result {
                        +Items.CHAIN
                        chance = fraction(1, 3)
                    }
                    recipeId suffix "_from_hanging_sign"
                }.save(exporter)
            }
            // Slab
            type.getItemOfThis(VanillaWoodChildKeys.SLAB)?.let {
                RagiumRecipeBuilder.cutting {
                    ingredient { +planks }
                    result {
                        +it
                        count = 2
                    }
                }.save(exporter)
            }
            // Stairs
            // Door
            type.getItemOfThis(VanillaWoodChildKeys.DOOR)?.let {
                RagiumRecipeBuilder.cutting {
                    ingredient { +it }
                    result {
                        +planks
                        count = 2
                    }
                    recipeId suffix "_from_door"
                }.save(exporter)
            }
            // Trapdoor
            type.getItemOfThis(VanillaWoodChildKeys.TRAPDOOR)?.let {
                RagiumRecipeBuilder.cutting {
                    ingredient { +it }
                    result {
                        +planks
                        count = 3
                    }
                    recipeId suffix "_from_trapdoor"
                }.save(exporter)
            }
        }
    }

    @JvmStatic
    private fun cutBedToPlanks() {
        for ((color: HTDefaultColor, bed: HTSimpleDeferredBlockAndItem) in VanillaColoredCollections.BED.asSequenceWithColor()) {
            val wool: HTSimpleDeferredBlockAndItem = VanillaColoredCollections.WOOL[color]
            RagiumRecipeBuilder.cutting {
                ingredient { +bed }
                result {
                    +wool
                    count = 3
                }
                result {
                    +Items.OAK_PLANKS
                    count = 3
                }
                recipeId suffix "_from_bed"
            }.save(exporter)
        }
    }

    @JvmStatic
    private fun waxing() {
        BuiltInRegistries.BLOCK.asLookup().forEachData(NeoForgeDataMaps.WAXABLES) { holder: Holder<Block>, waxable: Waxable ->
            val before: SimpleSupplierWithKey<Block> = holder.toLike()
            val after: SimpleSupplierWithKey<Block> = waxable.waxed().toLike()
            // レシピを登録
            // Waxing

            // Dis-waxing
            RagiumRecipeBuilder.cutting {
                ingredient { +after.get() }
                result { +before.get() }
                recipeId suffix "_from_${after.path}"
            }.save(exporter)
        }
    }

    //    Freezing    //

    @JvmStatic
    private fun freezeMoltenToPrefix(entry: HTMaterialManager.Entry, part: HTPartLike) {
        val prefix: HTTagPrefix = part.tagPrefix ?: return
        // レシピを登録
        HTFreezingRecipeBuilder.create {
            ingredient {
                +HTFluidPart.MOLTEN.createTagKey(entry)
                amount = part.getScaledAmount(entry.getDefaultFluidAmount(), entry).toInt()
            }
            catalyst = HCDynamicRecipeProvider.getBlueprint(prefix)
            result { +HTItemResult.MaterialPart(part, entry) }
            recipeId suffix "_from_molten"
        }.save(exporter)
    }

    //    Imploding    //

    @JvmStatic
    private fun implodeDustToPrefix(entry: HTMaterialManager.Entry, part: HTPartLike) {
        // 材料が存在するか判定
        val crushedPart: HTPartLike = entry.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART)
        val crushedPrefix: HTTagPrefix = crushedPart.tagPrefix ?: return
        // レシピを登録
        RagiumRecipeBuilder.imploding {
            ingredient { +tag(crushedPrefix, entry) }
            result { +HTItemResult.MaterialPart(part, entry) }
            recipeId suffix "from_${crushedPart.asPartName()}"
        }.save(exporter)
    }

    //    Melting    //

    @JvmStatic
    private fun meltPrefixToMolten(entry: HTMaterialManager.Entry, part: HTPartLike) {
        val prefix: HTTagPrefix = part.tagPrefix ?: return
        // 素材のプロパティから液体材料を取得
        val fluidAmount: Int = part.getScaledAmount(entry.getDefaultFluidAmount(), entry).toInt()
        // 完成品を取得
        val molten: HTMaterialContents.FluidEntry = HiiragiCoreAccess.INSTANCE.registeredFluids[HTFluidPart.MOLTEN, entry]
            ?: return
        // レシピを登録
        RagiumRecipeBuilder.melting {
            ingredient { +tag(prefix, entry) }
            result {
                +molten
                amount = fluidAmount
            }
            recipeId suffix "_from_${part.asPartName()}"
            time = HCDynamicRecipeProvider.getTimeFromMelting(entry, time) ?: return
        }.save(exporter)
    }

    //    Washing    //

    @JvmStatic
    private fun washCrushedOre(entry: HTMaterialManager.Entry) {
        // レシピを登録
        // 水 -> 主産物 + 副産物
        RagiumRecipeBuilder.washing {
            // 材料
            ingredient { +tag(CommonTagPrefixes.CRUSHED_ORE, entry) }
            // 主産物
            result { +HTItemResult.MaterialPart(CommonParts.DUST, entry, CommonParts.CRUSHED_ORE.getScaledAmount(1, entry).toInt()) }
            // 副産物
            entry[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(HTExtraOreResultMap.Phase.WASH_CRUSHED)
                ?.let { +it }

            recipeId suffix "_from_crushed_ore/water"
        }
        // 硫酸 -> 1.5x 主産物
        RagiumRecipeBuilder.bathing {
            // 材料
            itemIngredient { +tag(CommonTagPrefixes.CRUSHED_ORE, entry) }
            fluidIngredient {
                +RagiumFluids.SULFURIC_ACID
                amount = 250
            }
            // 主産物
            val outputCount: Int = CommonParts.CRUSHED_ORE
                .getScaledAmount(fraction(3, 2), entry)
                .toFloat()
                .let(Mth::ceil)
            result { +HTItemResult.MaterialPart(CommonParts.DUST, entry, outputCount) }

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
