package hiiragi283.ragium.common.event

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.fraction
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.asSequence
import hiiragi283.core.common.material.ColoredMaterials
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTChemicalRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemAndFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemToChancedRecipeBuilder
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodChildKeys
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.Tags

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeRecipeHandler : HTRecipeProviderContext.Delegated() {
    override lateinit var delegated: HTRecipeProviderContext

    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        this.delegated = event.context

        canFluidToBucket()

        cutWoodFromDefinition()

        mixToColor(ItemTags.BANNERS, ColoredMaterials.BANNER)
        mixToColor(ItemTags.BEDS, ColoredMaterials.BED)
        mixToColor(ItemTags.WOOL_CARPETS, ColoredMaterials.CARPET)
        mixToColor(ItemTags.WOOL, ColoredMaterials.WOOL)
    }

    @JvmStatic
    private fun fluidSequence(): Sequence<Fluid> = provider
        .lookupOrThrow(Registries.FLUID)
        .asSequence()
        .map(HTHolderLike.HolderDelegate<Fluid, Fluid>::get)
        .filter { fluid: Fluid -> fluid.isSource(fluid.defaultFluidState()) }

    //    Canning    //

    @JvmStatic
    private fun canFluidToBucket() {
        fluidSequence().forEach { fluid: Fluid ->
            val bucket: Item = fluid.bucket
            if (bucket == Items.AIR) return@forEach
            // レシピを登録
            HTItemAndFluidRecipeBuilder.canning(output) {
                itemIngredient = inputCreator.create(Tags.Items.BUCKETS_EMPTY)
                fluidIngredient = inputCreator.create(fluid, HTConst.DEFAULT_FLUID_AMOUNT)
                result = resultCreator.create(bucket)
                time = 20
            }
        }
    }

    //    Cutting    //

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
