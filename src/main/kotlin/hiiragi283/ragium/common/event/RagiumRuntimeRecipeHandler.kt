package hiiragi283.ragium.common.event

import com.google.common.collect.ImmutableMultimap
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.api.fraction
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.asFluidSequence
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.common.material.ColoredMaterials
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.util.HCPotionFluidHelper
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTChemicalRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemToChancedRecipeBuilder
import hiiragi283.ragium.mixin.PotionBrewingAccessor
import hiiragi283.ragium.mixin.PotionBrewingMixAccessor
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodChildKeys
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.brewing.BrewingRecipe

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeRecipeHandler : HTRecipeProviderContext.Delegated() {
    override lateinit var delegated: HTRecipeProviderContext

    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        this.delegated = event.context

        canFluidToBucket()

        cutWoodFromDefinition()

        mixBrewing()
        mixToColor(ItemTags.BANNERS, ColoredMaterials.BANNER)
        mixToColor(ItemTags.BEDS, ColoredMaterials.BED)
        mixToColor(ItemTags.WOOL_CARPETS, ColoredMaterials.CARPET)
        mixToColor(ItemTags.WOOL, ColoredMaterials.WOOL)
    }

    @JvmStatic
    private fun fluidSequence(): Sequence<HTFluidHolderLike<*>> = provider
        .lookupOrThrow(Registries.FLUID)
        .asFluidSequence()
        .filter { holder: HTFluidHolderLike<*> ->
            val fluid: Fluid = holder.asFluid()
            fluid.isSource(fluid.defaultFluidState())
        }

    //    Canning    //

    @JvmStatic
    private fun canFluidToBucket() {
        fluidSequence().forEach { holder: HTFluidHolderLike<*> ->
            val bucket: Item = holder.getBucket()
            if (bucket == Items.AIR) return@forEach
            // レシピを登録
            HTItemOrFluidRecipeBuilder.canning(output) {
                ingredient += inputCreator.create(Tags.Items.BUCKETS_EMPTY)
                ingredient += inputCreator.create(holder.asFluid(), HTConst.DEFAULT_FLUID_AMOUNT)
                result += resultCreator.create(bucket)
                time = 20
            }
            HTItemOrFluidRecipeBuilder.canning(output) {
                ingredient += inputCreator.create(bucket)
                result += resultCreator.create(Items.BUCKET)
                result += resultCreator.create(holder, HTConst.DEFAULT_FLUID_AMOUNT)
                time = 20
                recipeId replace holder.getId().withPrefix("bucket_from_")
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
    private fun mixBrewing() {
        // 醸造レシピを集める
        val potionBrewing: PotionBrewing = HiiragiCoreAPI.getActiveServer()?.potionBrewing() ?: return
        val builder: ImmutableMultimap.Builder<Holder<Potion>, Pair<Holder<Potion>, Ingredient>> = ImmutableMultimap.builder()
        // Vanilla
        for (accessor: PotionBrewingMixAccessor<Potion> in (potionBrewing as PotionBrewingAccessor).potionMixes) {
            val potionFrom: Holder<Potion> = accessor.from
            val potionTo: Holder<Potion> = accessor.to
            if (potionTo.value().effects.isEmpty()) continue
            builder.put(potionTo, potionFrom to accessor.ingredient)
        }
        // Modded
        for (recipe: BrewingRecipe in potionBrewing.recipes.filterIsInstance<BrewingRecipe>()) {
            val potionFrom: Holder<Potion> = getPotion(recipe.input.items[0])
            val potionTo: Holder<Potion> = getPotion(recipe.output)
            if (potionTo.value().effects.isEmpty()) continue
            builder.put(potionTo, potionFrom to recipe.ingredient)
        }
        // 醸造レシピを登録していく
        val multimap: ImmutableMultimap<Holder<Potion>, Pair<Holder<Potion>, Ingredient>> = builder.build()
        for (potionTo: Holder<Potion> in multimap.keySet()) {
            multimap[potionTo].forEachIndexed { index: Int, (potionFrom: Holder<Potion>, ingredient: Ingredient) ->
                val resultContents: HTPotionContents = HTPotionContents.of(potionTo, HTBottleType.DEFAULT) ?: return@forEachIndexed
                HTChemicalRecipeBuilder.mixing(output) {
                    itemIngredients += inputCreator.create(ingredient)
                    fluidIngredients += inputCreator.create(HTPotionFluidIngredient(HolderSet.direct(potionFrom), HTBottleType.DEFAULT))
                    fluidResults += resultCreator.create(HCPotionFluidHelper.createFluid(resultContents))
                    recipeId replace potionTo.toLike().getId().withSuffix("_$index")
                }
            }
        }
    }

    @JvmStatic
    private fun getPotion(stack: ItemStack): Holder<Potion> = HTPotionHelper.getPotion(stack).potion.orElse(Potions.WATER)

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
