package hiiragi283.ragium.integration.emi

import com.mojang.logging.LogUtils
import com.mojang.serialization.DataResult
import dev.emi.emi.EmiPort
import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiInfoRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.registry.HTMachineRecipeType
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.integration.emi.recipe.*
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
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

        addMachineRecipe(RagiumRecipes.CENTRIFUGING, ::HTCentrifugingEmiRecipe)
        addMachineRecipe(RagiumRecipes.CRUSHING, HTItemProcessEmiRecipe.create(RagiumEmiCategories.CRUSHING))
        addMachineRecipe(RagiumRecipes.EXTRACTING, HTItemProcessEmiRecipe.create(RagiumEmiCategories.EXTRACTING))
        addMachineRecipe(RagiumRecipes.INFUSING, ::HTInfusingEmiRecipe)
        addMachineRecipe(RagiumRecipes.REFINING, HTFluidProcessEmiRecipe.create(RagiumEmiCategories.REFINING))

        addDeviceRecipes()
        addInfos()
    }

    private fun addMachineRecipe(recipeType: HTMachineRecipeType, factory: (ResourceLocation, HTRecipeDefinition) -> HTMachineEmiRecipe) {
        // 動的レシピを登録する
        recipeType.reloadCache(recipeManager)
        // レシピを登録する
        for (holder: RecipeHolder<HTMachineRecipe> in recipeType.getAllRecipes()) {
            val id: ResourceLocation = holder.id
            val recipe: HTMachineRecipe = holder.value
            recipe
                .getDefinition()
                .ifSuccess { definition: HTRecipeDefinition ->
                    addRecipeSafe(id) { factory(id, definition) }
                }.ifError { error: DataResult.Error<HTRecipeDefinition> ->
                    LOGGER.warn("Error when parsing vanilla recipe: $id, {}", error.message())
                }
        }
    }

    private fun addDeviceRecipes() {
        // Water Well
        addRecipeSafe(RagiumAPI.id("/device/water_well")) { id: ResourceLocation ->
            HTDeviceEmiRecipe(id, RagiumBlocks.WATER_COLLECTOR, EmiStack.of(Fluids.WATER))
        }
        // Lava Well
        addRecipeSafe(RagiumAPI.id("/device/lava_well")) { id: ResourceLocation ->
            HTDeviceEmiRecipe(id, RagiumBlocks.LAVA_COLLECTOR, EmiStack.of(Fluids.LAVA))
        }
        // Milk Drain
        addRecipeSafe(RagiumAPI.id("/device/milk_drain")) { id: ResourceLocation ->
            HTDeviceEmiRecipe(id, RagiumBlocks.MILK_DRAIN, EmiStack.of(NeoForgeMod.MILK.get()))
        }
    }

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
        registry.addRecipe(
            EmiInfoRecipe(
                listOf(icon),
                listOf(*texts),
                icon.id.withPrefix("/"),
            ),
        )
    }

    /**
     * @see dev.emi.emi.VanillaPlugin.addRecipeSafe
     */
    private inline fun addRecipeSafe(supplier: () -> EmiRecipe) {
        runCatching {
            registry.addRecipe(supplier())
        }.onFailure { throwable: Throwable ->
            LOGGER.warn("Exception thrown when parsing EMI recipe (no ID available)", throwable)
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
