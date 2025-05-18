package hiiragi283.ragium.integration.emi

import com.mojang.logging.LogUtils
import com.mojang.serialization.DataResult
import dev.emi.emi.EmiPort
import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiCraftingRecipe
import dev.emi.emi.api.recipe.EmiInfoRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.recipe.special.EmiSmithingTrimRecipe
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.api.data.interaction.HTBlockAction
import hiiragi283.ragium.api.data.interaction.HTBlockInteraction
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.extension.idOrNull
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTDefinitionRecipe
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import hiiragi283.ragium.common.recipe.HTInfusingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.recipe.custom.HTBucketExtractingRecipe
import hiiragi283.ragium.common.recipe.custom.HTBucketFillingRecipe
import hiiragi283.ragium.common.recipe.custom.HTEternalTicketRecipe
import hiiragi283.ragium.common.recipe.custom.HTIceCreamSodaRecipe
import hiiragi283.ragium.integration.emi.recipe.HTCrushingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTExtractingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTInfusingEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTMachineEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTRefiningEmiRecipe
import hiiragi283.ragium.integration.emi.recipe.HTSolidifyingEmiRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import org.slf4j.Logger

@EmiEntrypoint
class RagiumEmiPlugin : EmiPlugin {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    private lateinit var registry: EmiRegistry
    private lateinit var recipeManager: RecipeManager

    override fun register(registry: EmiRegistry) {
        // Category, Workstation
        RagiumEmiCategories.register(registry)
        // Recipe
        this.registry = registry
        recipeManager = registry.recipeManager

        addMachineRecipes()
        addInfos()

        addCustomRecipe()
    }

    //    Machine    //

    private fun addMachineRecipes() {
        forEachRecipes(RagiumRecipeTypes.CRUSHING.get()) { id: ResourceLocation, recipe: HTMachineRecipe ->
            if (recipe is HTCrushingRecipe) {
                addRecipeSafe(id, recipe, ::HTCrushingEmiRecipe)
            }
        }

        forEachRecipes(RagiumRecipeTypes.EXTRACTING.get()) { id: ResourceLocation, recipe: HTMachineRecipe ->
            when (recipe) {
                is HTExtractingRecipe -> addRecipeSafe(id, recipe, ::HTExtractingEmiRecipe)

                is HTBucketExtractingRecipe -> EmiPort.getFluidRegistry().holders().forEach(::addBucketExtracting)
            }
        }

        forEachRecipes(RagiumRecipeTypes.INFUSING.get()) { id: ResourceLocation, recipe: HTMachineRecipe ->
            when (recipe) {
                is HTInfusingRecipe -> addRecipeSafe(id, recipe, ::HTInfusingEmiRecipe)

                is HTBucketFillingRecipe -> EmiPort.getFluidRegistry().holders().forEach(::addBucketFilling)
            }
        }

        forEachRecipes(RagiumRecipeTypes.REFINING.get()) { id: ResourceLocation, recipe: HTMachineRecipe ->
            if (recipe is HTRefiningRecipe) {
                addRecipeSafe(id, recipe, ::HTRefiningEmiRecipe)
            }
        }

        forEachRecipes(RagiumRecipeTypes.SOLIDIFYING.get()) { id: ResourceLocation, recipe: HTMachineRecipe ->
            if (recipe is HTSolidifyingRecipe) {
                addRecipeSafe(id, recipe, ::HTSolidifyingEmiRecipe)
            }
        }
    }

    private fun addBucketExtracting(holder: Holder.Reference<Fluid>) {
        // 液体源でない場合はスキップ
        val fluid: Fluid = holder.value()
        if (!fluid.isSource(fluid.defaultFluidState())) return
        // バケツが存在しない場合はスキップ
        val bucket: ItemStack = FluidUtil.getFilledBucket(FluidStack(fluid, 1))
        if (bucket.isEmpty) return
        addRecipeSafe(holder.idOrThrow.withPrefix("/extracting/bucket/")) { id: ResourceLocation ->
            HTExtractingEmiRecipe(
                id,
                HTRecipeDefinition(
                    listOf(
                        SizedIngredient.of(bucket.item, 1),
                    ),
                    listOf(),
                    listOf(
                        HTItemOutput.of(bucket.craftingRemainingItem),
                    ),
                    listOf(
                        HTFluidOutput.of(fluid, 1000),
                    ),
                ),
            )
        }
    }

    private fun addBucketFilling(holder: Holder.Reference<Fluid>) {
        // 液体源でない場合はスキップ
        val fluid: Fluid = holder.value()
        if (!fluid.isSource(fluid.defaultFluidState())) return
        // バケツが存在しない場合はスキップ
        val bucket: ItemStack = FluidUtil.getFilledBucket(FluidStack(fluid, 1))
        if (bucket.isEmpty) return
        addRecipeSafe(holder.idOrThrow.withPrefix("/infusing/bucket/")) { id: ResourceLocation ->
            HTInfusingEmiRecipe(
                id,
                HTRecipeDefinition(
                    listOf(
                        SizedIngredient.of(bucket.craftingRemainingItem.item, 1),
                    ),
                    listOf(
                        SizedFluidIngredient.of(fluid, 1000),
                    ),
                    listOf(
                        HTItemOutput.of(bucket),
                    ),
                    listOf(),
                ),
            )
        }
    }

    //    Info    //

    private fun addInfos() {
        addInfo(RagiumBlocks.ASH_LOG, Component.translatable(RagiumTranslationKeys.EMI_ASH_LOG))
        addInfo(RagiumBlocks.QUARTZ_GLASS, Component.translatable(RagiumTranslationKeys.EMI_HARVESTABLE_GLASS))
        addInfo(RagiumItems.AMBROSIA, Component.translatable(RagiumTranslationKeys.EMI_AMBROSIA))
        addInfo(RagiumItems.ICE_CREAM, Component.translatable(RagiumTranslationKeys.EMI_ICE_CREAM))
        addInfo(RagiumItems.ITEM_MAGNET, Component.translatable(RagiumTranslationKeys.EMI_ITEM_MAGNET))

        addInfo(
            RagiumBlocks.OBSIDIAN_GLASS,
            Component.translatable(RagiumTranslationKeys.EMI_HARVESTABLE_GLASS),
            Component.translatable(RagiumTranslationKeys.EMI_OBSIDIAN_GLASS),
        )
        addInfo(RagiumItems.RAGI_CHERRY, Component.translatable(RagiumTranslationKeys.EMI_RAGI_CHERRY))
        addInfo(
            RagiumBlocks.SOUL_GLASS,
            Component.translatable(RagiumTranslationKeys.EMI_HARVESTABLE_GLASS),
            Component.translatable(RagiumTranslationKeys.EMI_SOUL_GLASS),
        )
        addInfo(RagiumItems.TRADER_CATALOG, Component.translatable(RagiumTranslationKeys.EMI_TRADER_CATALOG))
        addInfo(RagiumItems.WARPED_WART, Component.translatable(RagiumTranslationKeys.EMI_WARPED_WART))

        // World Interaction
        addRecipeSafe(RagiumAPI.id("/world/block_info/ash_log")) { id: ResourceLocation ->
            EmiWorldInteractionRecipe
                .builder()
                .id(id)
                .rightInput(EmiStack.of(RagiumBlocks.ASH_LOG), false)
                .leftInput(EmiStack.EMPTY)
                .output(EmiStack.of(RagiumItems.ASH_DUST))
                .build()
        }

        addRecipeSafe(RagiumAPI.id("/world/block_info/water_well")) { id: ResourceLocation ->
            EmiWorldInteractionRecipe
                .builder()
                .id(id)
                .rightInput(EmiStack.of(RagiumBlocks.WATER_COLLECTOR), false)
                .leftInput(EmiStack.EMPTY)
                .output(EmiStack.of(Fluids.WATER))
                .build()
        }
        // Lava Well
        addRecipeSafe(RagiumAPI.id("/world/block_info/lava_well")) { id: ResourceLocation ->
            EmiWorldInteractionRecipe
                .builder()
                .id(id)
                .rightInput(EmiStack.of(RagiumBlocks.LAVA_COLLECTOR), false)
                .leftInput(EmiStack.EMPTY)
                .output(EmiStack.of(Fluids.LAVA))
                .build()
        }
        // Milk Drain
        addRecipeSafe(RagiumAPI.id("/world/block_info/milk_drain")) { id: ResourceLocation ->
            EmiWorldInteractionRecipe
                .builder()
                .id(id)
                .rightInput(EmiStack.of(RagiumBlocks.MILK_DRAIN), false)
                .leftInput(EmiStack.of(Items.COW_SPAWN_EGG))
                .output(EmiStack.of(NeoForgeMod.MILK.get()))
                .build()
        }
        // Exp Collector
        addRecipeSafe(RagiumAPI.id("/world/block_info/exp_collector")) { id: ResourceLocation ->
            EmiWorldInteractionRecipe
                .builder()
                .id(id)
                .rightInput(EmiStack.of(RagiumBlocks.EXP_COLLECTOR), false)
                .leftInput(EmiStack.of(Items.COW_SPAWN_EGG))
                .output(EmiStack.of(RagiumFluidContents.EXPERIENCE.get()))
                .build()
        }

        for (holder: Holder.Reference<Block> in BuiltInRegistries.BLOCK.holders()) {
            val id: ResourceLocation = holder.idOrNull ?: continue
            val interaction: HTBlockInteraction = holder.getData(RagiumDataMaps.BLOCK_INTERACTION) ?: continue
            val firstStack: ItemStack = interaction.actions
                .filterIsInstance<HTBlockAction.ItemPreview>()
                .firstOrNull()
                ?.getPreviewStack() ?: continue
            addRecipeSafe(id.withPrefix("/world/interaction/")) { id1: ResourceLocation ->
                EmiWorldInteractionRecipe
                    .builder()
                    .id(id1)
                    .leftInput(EmiStack.of(holder.value()))
                    .rightInput(EmiIngredient.of(interaction.ingredient), false)
                    .output(EmiStack.of(firstStack))
                    .build()
            }
        }
    }

    private fun addInfo(icon: ItemLike, vararg texts: Component) {
        addInfo(EmiStack.of(icon), *texts)
    }

    private fun addInfo(icon: EmiStack, vararg texts: Component) {
        addRecipeSafe(icon.id.withPrefix("/")) { id: ResourceLocation ->
            EmiInfoRecipe(
                listOf(icon),
                listOf(*texts),
                id,
            )
        }
    }

    //    Custom    //

    private fun addCustomRecipe() {
        // Crafting
        forEachRecipes(RecipeType.CRAFTING) { _: ResourceLocation, recipe: CraftingRecipe? ->
            if (recipe is HTIceCreamSodaRecipe) {
                EmiPort.getPotionRegistry().holders().forEach { holder: Holder.Reference<Potion> ->
                    addRecipeSafe(
                        holder.key().location().withPrefix("/shapeless/ice_cream_soda/"),
                    ) { id: ResourceLocation ->
                        EmiCraftingRecipe(
                            listOf(
                                EmiStack.of(RagiumItems.ICE_CREAM),
                                EmiIngredient.of(RagiumItemTags.FOODS_CHERRY),
                                EmiStack.of(createPotionStack(holder)),
                                EmiIngredient.of(Tags.Items.DYES_GREEN),
                            ),
                            EmiStack.of(RagiumAPI.getInstance().createSoda(holder)),
                            id,
                            true,
                        )
                    }
                }
            }
        }
        // Smithing
        addRecipeSafe(HTEternalTicketRecipe) { recipe: HTEternalTicketRecipe ->
            EmiSmithingTrimRecipe(
                EmiStack.of(RagiumItems.ETERNAL_TICKET),
                EmiIngredient.of(
                    EmiPort
                        .getItemRegistry()
                        .holders()
                        .filter { holder: Holder.Reference<Item> -> holder.value().defaultInstance.isDamageableItem }
                        .map { holder: Holder.Reference<Item> -> EmiStack.of(holder.value()) }
                        .toList(),
                ),
                EmiStack.EMPTY,
                EmiStack.EMPTY,
                recipe,
            )
        }
    }

    //    Extension    //

    private inline fun <I : RecipeInput, R : Recipe<I>> forEachRecipes(recipeType: RecipeType<R>, action: (ResourceLocation, R) -> Unit) {
        for (holder: RecipeHolder<R> in recipeManager.getAllRecipesFor(recipeType)) {
            val id: ResourceLocation = holder.id
            val recipe: R = holder.value
            action(id, recipe)
        }
    }

    private fun <R : HTDefinitionRecipe<*>> addRecipeSafe(
        id: ResourceLocation,
        recipe: R,
        factory: (ResourceLocation, HTRecipeDefinition) -> HTMachineEmiRecipe,
    ) {
        recipe
            .getDefinition()
            .ifSuccess { definition: HTRecipeDefinition ->
                addRecipeSafe(id) { factory(id, definition) }
            }.ifError { error: DataResult.Error<HTRecipeDefinition> ->
                LOGGER.warn("Error when parsing vanilla recipe: $id, {}", error.message())
            }
    }

    /**
     * @see dev.emi.emi.VanillaPlugin.addRecipeSafe
     */
    private inline fun <T : Recipe<*>> addRecipeSafe(recipe: T, factory: (T) -> EmiRecipe) {
        addRecipeSafe(EmiPort.getId(recipe)) { factory(recipe) }
    }

    private inline fun addRecipeSafe(id: ResourceLocation, factory: (ResourceLocation) -> EmiRecipe) {
        runCatching {
            registry.addRecipe(factory(id))
        }.onFailure { throwable: Throwable ->
            LOGGER.warn("Exception thrown when parsing vanilla recipe: $id", throwable)
        }
    }
}
