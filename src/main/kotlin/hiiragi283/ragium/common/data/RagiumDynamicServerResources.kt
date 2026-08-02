package hiiragi283.ragium.common.data

import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.color.VanillaColoredCollections
import hiiragi283.core.api.component1
import hiiragi283.core.api.component2
import hiiragi283.core.api.data.pack.HTDynamicDataRegister
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterial
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartKey
import hiiragi283.core.api.material.part.property.getScaledAmount
import hiiragi283.core.api.material.part.property.tagPrefix
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTExtraOreResultMap
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.core.api.registry.forEachData
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.SimpleSupplierWithKey
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.data.HCDynamicRecipeProvider
import hiiragi283.ragium.api.material.property.RagiumMaterialPropertyKeys
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.material.part.RagiumParts
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.TagKey
import net.minecraft.util.Mth
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable

internal data object RagiumDynamicServerResources : HTRecipeProviderContext.Delegated() {
    @JvmStatic
    fun initialize() {
        delegate = HTDynamicDataRegister

        redox()

        // cutWoodFromDefinition()
        cutBedToPlanks()
        waxing()

        // Material
        for (material: HTMaterial in materialManager) {
            // Basic
            compressDustToPellet(material)
            compressIngotToPlate(material)

            cutBaseToRod(material)
            cutBlockToPlate(material)
            // Heat
            meltPrefixToMolten(material, CommonParts.DUST)
            meltPrefixToMolten(material, CommonParts.GEM)
            meltPrefixToMolten(material, CommonParts.INGOT)
            meltPrefixToMolten(material, CommonParts.PEARL)

            implodeDustToPrefix(material, CommonParts.GEM)
            implodeDustToPrefix(material, CommonParts.PEARL)
            // Cool
            freezeMoltenToPrefix(material, CommonParts.DUST)
            freezeMoltenToPrefix(material, CommonParts.INGOT)
            freezeMoltenToPrefix(material, CommonParts.GEM)
            freezeMoltenToPrefix(material, CommonParts.PEARL)

            freezeMoltenToPrefix(material, CommonParts.GEAR)
            freezeMoltenToPrefix(material, CommonParts.PLATE)
            freezeMoltenToPrefix(material, CommonParts.ROD)
            freezeMoltenToPrefix(material, CommonParts.WIRE)
            // Chemical
            washCrushedOre(material)
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
    private fun compressDustToPellet(material: HTMaterial) {
        val key: HTMaterialKey = material.key
        // 材料が存在するか判定
        val crushedPrefix: HTTagPrefix = material.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART).let(partManager::get)?.tagPrefix ?: return
        // レシピを登録
        RagiumRecipeBuilder.compressing {
            ingredient {
                +tag(crushedPrefix, key)
                count = 8
            }
            result { +HTItemResult.MaterialPart(RagiumParts.PELLET, key) }
            recipeId suffix "_from_dust"
        }.save(exporter)
    }

    @JvmStatic
    private fun compressIngotToPlate(material: HTMaterial) {
        val key: HTMaterialKey = material.key
        // レシピを登録
        RagiumRecipeBuilder.compressing {
            ingredient { +tag(CommonTagPrefixes.INGOT, key) }
            result { +HTItemResult.MaterialPart(CommonParts.PLATE, key) }
            recipeId suffix "_from_ingot"
        }.save(exporter)
    }

    //    Cutting    //

    @JvmStatic
    private fun cutBaseToRod(material: HTMaterial) {
        val key: HTMaterialKey = material.key
        // 材料が存在するか判定
        val defaultPart: HTDefaultPart = material.getDefaultPart() ?: return
        val inputTag: TagKey<Item> = defaultPart.getTag(key)
        // 加工の前後でタグが一致する場合はパス
        if (inputTag == CommonTagPrefixes.ROD.itemTagKey(key)) return
        // プレフィックスのスケールから個数を算出
        val (inputCount: Int, outputCount: Int) = partManager.get(CommonParts.ROD)?.getScaledAmount(1, material) ?: return
        // レシピを登録
        RagiumRecipeBuilder.cutting {
            ingredient {
                +inputTag
                count = inputCount
            }
            result {
                +HTItemResult.MaterialPart(CommonParts.ROD, key)
                count = outputCount
            }
            time = HCDynamicRecipeProvider.getTimeFromHardness(material, time * 3) ?: return
            recipeId suffix "_from_${defaultPart.getSuffix()}"
        }.save(exporter)
    }

    @JvmStatic
    private fun cutBlockToPlate(material: HTMaterial) {
        val key: HTMaterialKey = material.key
        // レシピを登録
        RagiumRecipeBuilder.cutting {
            ingredient { +tag(CommonTagPrefixes.STORAGE_BLOCK, key) }
            result {
                +HTItemResult.MaterialPart(CommonParts.PLATE, key)
                count = material.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK).baseCount
            }
            time = HCDynamicRecipeProvider.getTimeFromHardness(material, time * 3) ?: (time * 3)
            recipeId suffix "_from_block"
        }.save(exporter)
    }

    /*private fun cutWoodFromDefinition() {
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
    }*/

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

    private fun freezeMoltenToPrefix(material: HTMaterial, partKey: HTPartKey) {
        val part: HTPart = partManager[partKey] ?: return
        val prefix: HTTagPrefix = part.tagPrefix ?: return
        // 液体材料を取得
        val molten: HTFluidContent = material[RagiumMaterialPropertyKeys.MOLTEN_FLUID] ?: return
        // レシピを登録
        RagiumRecipeBuilder.freezing {
            fluidIngredient {
                +molten
                amount = part.getScaledAmount(material.getOrDefault(RagiumMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT), material).toInt()
            }
            itemIngredient { +HCDynamicRecipeProvider.getBlueprint(prefix) }
            result { +HTItemResult.MaterialPart(part, material.key) }
            recipeId suffix "_from_molten"
        }.save(exporter)
    }

    //    Imploding    //

    @JvmStatic
    private fun implodeDustToPrefix(material: HTMaterial, partKey: HTPartKey) {
        val part: HTPart = partManager[partKey] ?: return
        val key: HTMaterialKey = material.key
        // 材料が存在するか判定
        val crushedPart: HTPartKey = material.getOrDefault(HTMaterialPropertyKeys.CRUSHED_PART)
        val crushedPrefix: HTTagPrefix = partManager[crushedPart]?.tagPrefix ?: return
        // レシピを登録
        RagiumRecipeBuilder.imploding {
            ingredient { +tag(crushedPrefix, key) }
            result { +HTItemResult.MaterialPart(part, key) }
            recipeId suffix "from_${crushedPart.name}"
        }.save(exporter)
    }

    //    Melting    //

    private fun meltPrefixToMolten(material: HTMaterial, partKey: HTPartKey) {
        val part: HTPart = partManager[partKey] ?: return
        val prefix: HTTagPrefix = part.tagPrefix ?: return
        // 素材のプロパティから液体材料を取得
        val fluidAmount: Int = part.getScaledAmount(material.getOrDefault(RagiumMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT), material).toInt()
        // 完成品を取得
        val molten: HTFluidContent = material[RagiumMaterialPropertyKeys.MOLTEN_FLUID] ?: return
        // レシピを登録
        RagiumRecipeBuilder.melting {
            ingredient { +tag(prefix, material.key) }
            result {
                +molten
                amount = fluidAmount
            }
            recipeId suffix "_from_${partKey.name}"
            time = HCDynamicRecipeProvider.getTimeFromMelting(material, time) ?: return
        }.save(exporter)
    }

    //    Washing    //

    @JvmStatic
    private fun washCrushedOre(material: HTMaterial) {
        val key: HTMaterialKey = material.key
        // レシピを登録
        // 水 -> 主産物 + 副産物
        RagiumRecipeBuilder.washing {
            // 材料
            ingredient { +tag(CommonTagPrefixes.CRUSHED_ORE, key) }
            // 主産物
            result { +HTItemResult.MaterialPart(CommonParts.DUST, key, partManager.getOrThrow(CommonParts.CRUSHED_ORE).getScaledAmount(1, material).toInt()) }
            // 副産物
            material[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(HTExtraOreResultMap.Phase.WASH_CRUSHED)
                ?.let { +it }

            recipeId suffix "_from_crushed_ore/water"
        }
        // 硫酸 -> 1.5x 主産物
        RagiumRecipeBuilder.bathing {
            // 材料
            itemIngredient { +tag(CommonTagPrefixes.CRUSHED_ORE, key) }
            fluidIngredient {
                +RagiumFluids.SULFURIC_ACID
                amount = 250
            }
            // 主産物
            val outputCount: Int = partManager
                .getOrThrow(CommonParts.CRUSHED_ORE)
                .getScaledAmount(fraction(3, 2), material)
                .toFloat()
                .let(Mth::ceil)
            result { +HTItemResult.MaterialPart(CommonParts.DUST, key, outputCount) }

            recipeId suffix "_from_crushed_ore/sulfuric_acid"
        }
        // 水銀 -> 副産物 100%
        /*HTWashingRecipeBuilder.create(output) {
            val (baseResult: HTItemResult, chance: Fraction) = material[HTMaterialPropertyKeys.EXTRA_ORE_RESULTS]
                ?.getResult(HTExtraOreResultMap.Phase.WASH_CRUSHED)
                ?: return
            if (chance == Fraction.ZERO) return
            val inputAmount: Fraction = 250 * chance.invert()
            // 材料
            itemIngredient = inputCreator.create(CommonTagPrefixes.CRUSHED_ORE, material)
            fluidIngredient = inputCreator.create(RagiumFluids.MERCURY, inputAmount.toInt())
            // 主産物
            this.result = baseResult

            recipeId suffix "_from_crushed_ore/mercury"
        }*/
    }
}
