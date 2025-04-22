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
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.itemLookup
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.tag.HTTagUtil
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.common.recipe.*
import hiiragi283.ragium.integration.emi.recipe.*
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.DyeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
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
        addDeviceRecipes()
        addInfos()

        addCustomRecipe()
    }

    //    Machine    //

    private fun addMachineRecipes() {
        forEachRecipes(RagiumRecipeTypes.CRUSHING.get()) { id: ResourceLocation, recipe: HTMachineRecipe ->
            if (recipe is HTCrushingRecipe) {
                addRecipeSafe(id, recipe, ::HTCrushingEmiRecipe)
            } else if (recipe is HTMaterialCrushingRecipe) {
                val lookup: HolderLookup.RegistryLookup<Item> =
                    RagiumAPI.getInstance().getRegistryAccess()?.itemLookup() ?: return@forEachRecipes
                for (key: HTMaterialKey in RagiumAPI.getInstance().getMaterialRegistry().keys) {
                    val outputItem: Item = HTTagUtil.getFirstItem(lookup, HTTagPrefixes.DUST, key) ?: continue
                    val inputPrefix: HTTagPrefix = recipe.inputPrefix
                    val inputTag: TagKey<Item> = inputPrefix.createItemTag(key)
                    if (lookup.get(inputTag).isEmpty) continue
                    addRecipeSafe(RagiumAPI.id("/crushing/${inputPrefix.name}/${key.name}")) { id: ResourceLocation ->
                        HTCrushingEmiRecipe(
                            id,
                            HTRecipeDefinition(
                                listOf(
                                    SizedIngredient.of(inputTag, recipe.inputCount),
                                ),
                                listOf(),
                                listOf(
                                    HTItemOutput.of(outputItem, recipe.outputCount),
                                ),
                                listOf(),
                            ),
                        )
                    }
                }
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

    //    Device    //

    private fun addDeviceRecipes() {
        // Ash Log
        addRecipeSafe(RagiumAPI.id("/block_info/ash_log")) { id: ResourceLocation ->
            HTBlockInfoEmiRecipe(id, RagiumBlocks.ASH_LOG, RagiumItems.Dusts.ASH)
        }

        // Water Well
        addRecipeSafe(RagiumAPI.id("/block_info/water_well")) { id: ResourceLocation ->
            HTBlockInfoEmiRecipe(id, RagiumBlocks.WATER_COLLECTOR, Fluids.WATER)
        }
        // Lava Well
        addRecipeSafe(RagiumAPI.id("/block_info/lava_well")) { id: ResourceLocation ->
            HTBlockInfoEmiRecipe(id, RagiumBlocks.LAVA_COLLECTOR, Fluids.LAVA)
        }
        // Milk Drain
        addRecipeSafe(RagiumAPI.id("/block_info/milk_drain")) { id: ResourceLocation ->
            HTBlockInfoEmiRecipe(id, RagiumBlocks.MILK_DRAIN, NeoForgeMod.MILK.get())
        }
        // Exp Collector
        addRecipeSafe(RagiumAPI.id("/block_info/exp_collector")) { id: ResourceLocation ->
            HTBlockInfoEmiRecipe(id, RagiumBlocks.EXP_COLLECTOR, RagiumFluidContents.EXPERIENCE.get())
        }
    }

    //    Info    //

    private fun addInfos() {
        addInfo(RagiumBlocks.ASH_LOG, Component.translatable(RagiumTranslationKeys.EMI_ASH_LOG))
        addInfo(RagiumBlocks.QUARTZ_GLASS, Component.translatable(RagiumTranslationKeys.EMI_HARVESTABLE_GLASS))
        addInfo(
            RagiumBlocks.OBSIDIAN_GLASS,
            Component.translatable(RagiumTranslationKeys.EMI_HARVESTABLE_GLASS),
            Component.translatable(RagiumTranslationKeys.EMI_OBSIDIAN_GLASS),
        )
        addInfo(
            RagiumBlocks.SOUL_GLASS,
            Component.translatable(RagiumTranslationKeys.EMI_HARVESTABLE_GLASS),
            Component.translatable(RagiumTranslationKeys.EMI_SOUL_GLASS),
        )

        addInfo(RagiumItems.ITEM_MAGNET, Component.translatable(RagiumTranslationKeys.EMI_ITEM_MAGNET))
        addInfo(RagiumItems.TRADER_CATALOG, Component.translatable(RagiumTranslationKeys.EMI_TRADER_CATALOG))

        addInfo(RagiumItems.AMBROSIA, Component.translatable(RagiumTranslationKeys.EMI_AMBROSIA))
        addInfo(RagiumItems.ICE_CREAM, Component.translatable(RagiumTranslationKeys.EMI_ICE_CREAM))
        addInfo(RagiumItems.WARPED_WART, Component.translatable(RagiumTranslationKeys.EMI_WARPED_WART))
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
                                EmiIngredient.of(DyeColor.entries.map(DyeItem::byColor).map(EmiStack::of)),
                            ),
                            EmiStack.of(EmiPort.setPotion(RagiumItems.ICE_CREAM_SODA.toStack(), holder.value())),
                            id,
                            true,
                        )
                    }
                }
            }
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
    private inline fun addRecipeSafe(recipe: Recipe<*>, factory: () -> EmiRecipe) {
        addRecipeSafe(EmiPort.getId(recipe)) { factory() }
    }

    private inline fun addRecipeSafe(id: ResourceLocation, factory: (ResourceLocation) -> EmiRecipe) {
        runCatching {
            registry.addRecipe(factory(id))
        }.onFailure { throwable: Throwable ->
            LOGGER.warn("Exception thrown when parsing vanilla recipe: $id", throwable)
        }
    }
}
