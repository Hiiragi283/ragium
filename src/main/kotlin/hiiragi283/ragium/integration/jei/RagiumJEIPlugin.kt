package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.isSource
import hiiragi283.ragium.api.extension.mutableMultiMapOf
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.recipe.HTMachineRecipe
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.get
import hiiragi283.ragium.api.util.collection.HTMultiMap
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.recipe.condition.HTDummyCondition
import hiiragi283.ragium.integration.jei.category.*
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IJeiHelpers
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.Fluid
import java.util.function.Supplier
import mezz.jei.api.recipe.RecipeType as JEIRecipeType

@JeiPlugin
class RagiumJEIPlugin : IModPlugin {
    companion object {
        @JvmField
        val PLUGIN_ID: ResourceLocation = RagiumAPI.id("default")
    }

    override fun getPluginUid(): ResourceLocation = PLUGIN_ID

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val jeiHelper: IJeiHelpers = registration.jeiHelpers
        val guiHelper: IGuiHelper = jeiHelper.guiHelper

        HTMachineKey.allKeys.forEach { key: HTMachineKey ->
            registration.addRecipeCategories(HTMachineRecipeCategory(key, guiHelper))
        }

        registration.addRecipeCategories(
            HTSingleItemRecipeCategory(
                guiHelper,
                RagiumJEIRecipeTypes.COMPRESSOR,
                RagiumMachineKeys.COMPRESSOR,
                RagiumRecipeSerializers.COMPRESSOR.get(),
            ),
            HTExtractorRecipeCategory(guiHelper),
            HTRefineryRecipeCategory(guiHelper),
            HTMaterialInfoCategory(guiHelper),
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val level: ClientLevel = Minecraft.getInstance().level ?: return
        val recipeManager: RecipeManager = level.recipeManager

        fun <I : RecipeInput, R : Recipe<I>> register(recipeType: JEIRecipeType<R>, recipe: Supplier<RecipeType<R>>) {
            registration.addRecipes(
                recipeType,
                recipeManager.getAllRecipesFor(recipe.get()).map(RecipeHolder<R>::value),
            )
        }

        register(RagiumJEIRecipeTypes.COMPRESSOR, RagiumRecipeTypes.COMPRESSOR)
        register(RagiumJEIRecipeTypes.EXTRACTOR, RagiumRecipeTypes.EXTRACTOR)
        register(RagiumJEIRecipeTypes.REFINERY, RagiumRecipeTypes.REFINERY)

        registerMachineRecipes(registration, level)
        // Material Info
        registration.addRecipes(RagiumJEIRecipeTypes.MATERIAL_INFO, RagiumAPI.materialRegistry.typedMaterials)
    }

    private fun registerMachineRecipes(registration: IRecipeRegistration, level: Level) {
        val recipeCache: HTMultiMap.Mutable<HTMachineKey, RecipeHolder<HTMachineRecipe>> = mutableMultiMapOf()
        // Machine
        RagiumAPI.machineRegistry.forEachEntries { key: HTMachineKey, _: HTBlockContent?, properties: HTPropertyHolder ->
            properties[HTMachinePropertyKeys.RECIPE_PROXY]?.getRecipes(level) { holder: RecipeHolder<HTMachineRecipe> ->
                recipeCache.put(key, holder)
            }
        }
        // PBF Steel
        recipeCache.put(
            RagiumMachineKeys.BLAST_FURNACE,
            HTMachineRecipeBuilder
                .create(RagiumRecipes.BLAST_FURNACE)
                .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
                .itemInput(ItemTags.COALS, 4)
                .condition(HTDummyCondition(Component.literal("Only available on Primitive Blast Furnace")))
                .itemOutput(HTTagPrefix.INGOT, CommonMaterials.STEEL)
                .export(RagiumAPI.id("steel_by_pbf")),
        )
        // Generator Fuels
        level
            .registryAccess()
            .lookupOrThrow(Registries.FLUID)
            .listElements()
            .forEach { holder: Holder.Reference<Fluid> ->
                val fluid: Fluid = holder.value()
                if (!fluid.isSource) return@forEach
                val fuelMap: Map<HTMachineKey, Int> =
                    holder.getData(RagiumAPI.DataMapTypes.MACHINE_FUEL) ?: return@forEach
                fuelMap.forEach { (key: HTMachineKey, amount: Int) ->
                    recipeCache.put(
                        key,
                        HTMachineRecipeBuilder
                            .create(RagiumRecipes.FUEL)
                            .fluidInput(fluid, amount)
                            .export(
                                RagiumAPI
                                    .id(key.name)
                                    .withSuffix("/${holder.idOrThrow.toLanguageKey().replace('.', '/')}"),
                            ),
                    )
                }
            }

        recipeCache.map.forEach { machine: HTMachineKey, holders: Collection<RecipeHolder<HTMachineRecipe>> ->
            registration.addRecipes(RagiumJEIRecipeTypes.getRecipeType(machine), holders.toList())
        }
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        // Compressor
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.COMPRESSOR, RagiumMachineKeys.COMPRESSOR.getBlock())
        // Extractor
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.EXTRACTOR, RagiumMachineKeys.EXTRACTOR.getBlock())
        // Refinery
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.REFINERY, RagiumMachineKeys.REFINERY.getBlock())

        // Machine
        RagiumAPI.machineRegistry.blockMap.forEach { (key: HTMachineKey, content: HTBlockContent) ->
            val stack = ItemStack(content)
            registration.addRecipeCatalysts(RagiumJEIRecipeTypes.getRecipeType(key), stack)
        }

        registration.addRecipeCatalysts(
            RagiumJEIRecipeTypes.getRecipeType(RagiumMachineKeys.GRINDER),
            RagiumBlocks.MANUAL_GRINDER,
        )
        registration.addRecipeCatalysts(
            RagiumJEIRecipeTypes.getRecipeType(RagiumMachineKeys.BLAST_FURNACE),
            RagiumBlocks.PRIMITIVE_BLAST_FURNACE,
        )
        // Material
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.MATERIAL_INFO, Items.IRON_INGOT)
    }
}
