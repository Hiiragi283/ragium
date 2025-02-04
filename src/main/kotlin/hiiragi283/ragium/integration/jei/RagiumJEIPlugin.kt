package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.mutableMultiMapOf
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.get
import hiiragi283.ragium.api.recipe.HTMachineRecipeBase
import hiiragi283.ragium.api.util.collection.HTMultiMap
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
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
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level
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

        registration.addRecipeCategories(
            HTMultiItemRecipeCategory(
                guiHelper,
                RagiumJEIRecipeTypes.ASSEMBLER,
                RagiumMachineKeys.ASSEMBLER,
                RagiumRecipeSerializers.ASSEMBLER.get(),
            ),
            HTMultiItemRecipeCategory(
                guiHelper,
                RagiumJEIRecipeTypes.BLAST_FURNACE,
                RagiumMachineKeys.BLAST_FURNACE,
                RagiumRecipeSerializers.BLAST_FURNACE.get(),
            ),
            HTSingleItemRecipeCategory(
                guiHelper,
                RagiumJEIRecipeTypes.COMPRESSOR,
                RagiumMachineKeys.COMPRESSOR,
                RagiumRecipeSerializers.COMPRESSOR.get(),
            ),
            HTChemicalRecipeCategory(guiHelper),
            HTExtractorRecipeCategory(guiHelper),
            HTGrinderRecipeCategory(guiHelper),
            HTInfuserRecipeCategory(guiHelper),
            HTMixerRecipeCategory(guiHelper),
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

        register(RagiumJEIRecipeTypes.ASSEMBLER, RagiumRecipeTypes.ASSEMBLER)
        register(RagiumJEIRecipeTypes.BLAST_FURNACE, RagiumRecipeTypes.BLAST_FURNACE)
        register(RagiumJEIRecipeTypes.CHEMICAL, RagiumRecipeTypes.CHEMICAL)
        register(RagiumJEIRecipeTypes.COMPRESSOR, RagiumRecipeTypes.COMPRESSOR)
        register(RagiumJEIRecipeTypes.EXTRACTOR, RagiumRecipeTypes.EXTRACTOR)
        register(RagiumJEIRecipeTypes.GRINDER, RagiumRecipeTypes.GRINDER)
        register(RagiumJEIRecipeTypes.INFUSER, RagiumRecipeTypes.INFUSER)
        register(RagiumJEIRecipeTypes.MIXER, RagiumRecipeTypes.MIXER)
        register(RagiumJEIRecipeTypes.REFINERY, RagiumRecipeTypes.REFINERY)

        // Material Info
        registration.addRecipes(RagiumJEIRecipeTypes.MATERIAL_INFO, RagiumAPI.materialRegistry.typedMaterials)
    }

    private fun registerMachineRecipes(registration: IRecipeRegistration, level: Level) {
        val recipeCache: HTMultiMap.Mutable<HTMachineKey, RecipeHolder<out HTMachineRecipeBase>> = mutableMultiMapOf()
        // Machine
        RagiumAPI.machineRegistry.forEachEntries { key: HTMachineKey, _: HTBlockContent?, properties: HTPropertyHolder ->
            properties[HTMachinePropertyKeys.RECIPE_PROXY]?.getRecipes(level) { holder: RecipeHolder<out HTMachineRecipeBase> ->
                recipeCache.put(key, holder)
            }
        }

        // Grinder

        // PBF Steel
        /*recipeCache.put(
            RagiumMachineKeys.BLAST_FURNACE,
            HTMultiItemRecipeBuilder.blastFurnace()
                .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
                .itemInput(ItemTags.COALS, 4)
                .itemOutput(HTTagPrefix.INGOT, CommonMaterials.STEEL)
                .export(RagiumAPI.id("steel_by_pbf")),
        )*/
        // Generator Fuels
        /*level
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
            }*/
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        // Assembler
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.ASSEMBLER, RagiumMachineKeys.ASSEMBLER.getBlock())
        // Blast Furnace
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.BLAST_FURNACE, RagiumMachineKeys.BLAST_FURNACE.getBlock())
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.BLAST_FURNACE, RagiumBlocks.PRIMITIVE_BLAST_FURNACE)
        // Chemical
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.CHEMICAL, RagiumMachineKeys.CHEMICAL_REACTOR.getBlock())
        // Compressor
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.COMPRESSOR, RagiumMachineKeys.COMPRESSOR.getBlock())
        // Extractor
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.EXTRACTOR, RagiumMachineKeys.EXTRACTOR.getBlock())
        // Grinder
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.GRINDER, RagiumMachineKeys.GRINDER.getBlock())
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.GRINDER, RagiumBlocks.MANUAL_GRINDER)
        // Infuser
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.INFUSER, RagiumMachineKeys.INFUSER.getBlock())
        // Mixer
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.MIXER, RagiumMachineKeys.MIXER.getBlock())
        // Refinery
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.REFINERY, RagiumMachineKeys.REFINERY.getBlock())

        // Material
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.MATERIAL_INFO, Items.IRON_INGOT)
    }
}
