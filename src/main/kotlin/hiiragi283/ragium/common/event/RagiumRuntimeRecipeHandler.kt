package hiiragi283.ragium.common.event

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.fraction
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.common.material.ColoredMaterials
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTChemicalRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
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

        cutWoodFromDefinition()
        cutBedToPlanks()

        mixToColor(ItemTags.BANNERS, ColoredMaterials.BANNER)
        mixToColor(ItemTags.BEDS, ColoredMaterials.BED)
        mixToColor(ItemTags.WOOL_CARPETS, ColoredMaterials.CARPET)
        mixToColor(ItemTags.WOOL, ColoredMaterials.WOOL)
    }

    //    Cutting    //

    @JvmStatic
    private fun cutWoodFromDefinition() {
        for (type: WoodType in WoodTypeRegistry.INSTANCE) {
            val planks: ItemLike = type.getItemOfThis(VanillaWoodChildKeys.PLANKS) ?: continue
            // Stripped Log -> 6x Planks
            type.getItemOfThis(VanillaWoodChildKeys.STRIPPED_LOG)?.let { strippedLog: Item ->
                RagiumRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(strippedLog)
                    result = resultCreator.create(planks, 6)
                    recipeId suffix "_from_log"
                }
                // Log -> Stripped Log
                type.getItemOfThis(VanillaWoodChildKeys.LOG)?.let {
                    RagiumRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(strippedLog)
                        recipeId suffix "_from_log"
                    }
                }
            }
            // Stripped Wood -> 6x Planks
            type.getItemOfThis(VanillaWoodChildKeys.STRIPPED_WOOD)?.let { strippedWood: Item ->
                RagiumRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(strippedWood)
                    result = resultCreator.create(planks, 6)
                    recipeId suffix "_from_wood"
                }
                // Wood -> Stripped Wood
                type.getItemOfThis(VanillaWoodChildKeys.WOOD)?.let {
                    RagiumRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(strippedWood)
                        recipeId suffix "_from_wood"
                    }
                }
            }
            // Boat
            type.getItemOfThis(VanillaWoodChildKeys.BOAT)?.let { boat: Item ->
                RagiumRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(boat)
                    result = resultCreator.create(planks, 5)
                    recipeId suffix "_from_boat"
                }
                // Chest Boat
                type.getItemOfThis(VanillaWoodChildKeys.CHEST_BOAT)?.let {
                    RagiumRecipeBuilder.cutting(output) {
                        ingredient = inputCreator.create(it)
                        result = resultCreator.create(boat)
                        extraResult += resultCreator.create(Items.CHEST)
                    }
                }
            }
            // Button
            // Fence
            type.getItemOfThis(VanillaWoodChildKeys.FENCE)?.let {
                RagiumRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks)
                    extraResult += resultCreator.create(Items.STICK)
                    recipeId suffix "_from_fence"
                }
            }
            // Fence Gate
            type.getItemOfThis(VanillaWoodChildKeys.FENCE_GATE)?.let {
                RagiumRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks, 2)
                    extraResult += resultCreator.create(Items.STICK, 4)
                    recipeId suffix "_from_fence_gate"
                }
            }
            // Pressure Plate
            type.getItemOfThis(VanillaWoodChildKeys.PRESSURE_PLATE)?.let {
                RagiumRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks, 2)
                    recipeId suffix "_from_pressure_plate"
                }
            }
            // Sign
            type.getItemOfThis(VanillaWoodChildKeys.SIGN)?.let {
                RagiumRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks, 2)
                    extraResult += resultCreator.create(Items.STICK) to fraction(1, 3)
                    recipeId suffix "_from_sign"
                }
            }
            // Hanging Sign
            type.getItemOfThis(VanillaWoodChildKeys.HANGING_SIGN)?.let {
                RagiumRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks, 4)
                    extraResult += resultCreator.create(Items.CHAIN) to fraction(1, 3)
                    recipeId suffix "_from_hanging_sign"
                }
            }
            // Slab
            type.getItemOfThis(VanillaWoodChildKeys.SLAB)?.let {
                RagiumRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(planks)
                    result = resultCreator.create(it, 2)
                }
            }
            // Stairs
            // Door
            type.getItemOfThis(VanillaWoodChildKeys.DOOR)?.let {
                RagiumRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks, 2)
                    recipeId suffix "_from_door"
                }
            }
            // Trapdoor
            type.getItemOfThis(VanillaWoodChildKeys.TRAPDOOR)?.let {
                RagiumRecipeBuilder.cutting(output) {
                    ingredient = inputCreator.create(it)
                    result = resultCreator.create(planks, 3)
                    recipeId suffix "_from_trapdoor"
                }
            }
        }
    }

    @JvmStatic
    private fun cutBedToPlanks() {
        for ((color: HTDefaultColor, bed: HTSimpleDeferredItem) in ColoredMaterials.BED) {
            val wool: HTSimpleDeferredItem = ColoredMaterials.WOOL[color] ?: continue
            RagiumRecipeBuilder.cutting(output) {
                ingredient = inputCreator.create(bed)
                result = resultCreator.create(wool, 3)
                extraResult += resultCreator.create(Items.OAK_PLANKS, 3)
                recipeId suffix "_from_bed"
            }
        }
    }

    //    Mixing    //

    @JvmStatic
    private fun mixToColor(inputTag: TagKey<Item>, map: Map<HTDefaultColor, HTItemHolderLike<*>>) {
        for ((color: HTDefaultColor, colored: HTItemHolderLike<*>) in map) {
            val dye: HTFluidContent = HCFluids.getDye(color)
            // レシピを登録
            HTChemicalRecipeBuilder.mixing(output) {
                itemIngredients += inputCreator.create(inputTag)
                fluidIngredients += inputCreator.create(dye, 250)
                itemResults += resultCreator.create(colored)
                time /= 2
            }
        }
    }
}
