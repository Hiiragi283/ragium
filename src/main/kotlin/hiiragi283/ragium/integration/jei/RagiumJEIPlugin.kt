package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.recipe.HTRecipeConverters
import hiiragi283.ragium.integration.jei.category.*
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IJeiHelpers
import mezz.jei.api.registration.IGuiHandlerRegistration
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
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
                RagiumMachineKeys.ASSEMBLER,
                RagiumJEIRecipeTypes.ASSEMBLER,
                RagiumRecipeSerializers.ASSEMBLER.get(),
            ),
            HTMultiItemRecipeCategory(
                guiHelper,
                RagiumMachineKeys.BLAST_FURNACE,
                RagiumJEIRecipeTypes.BLAST_FURNACE,
                RagiumRecipeSerializers.BLAST_FURNACE.get(),
            ),
            HTSingleItemRecipeCategory(
                guiHelper,
                RagiumMachineKeys.COMPRESSOR,
                RagiumJEIRecipeTypes.COMPRESSOR,
                RagiumRecipeSerializers.COMPRESSOR.get(),
            ),
            HTEnchanterRecipeCategory(guiHelper),
            HTExtractorRecipeCategory(guiHelper),
            HTGrinderRecipeCategory(guiHelper),
            HTGrowthChamberRecipeCategory(guiHelper),
            HTInfuserRecipeCategory(guiHelper),
            HTSingleItemRecipeCategory(
                guiHelper,
                RagiumMachineKeys.LASER_ASSEMBLY,
                RagiumJEIRecipeTypes.LASER_ASSEMBLY,
                RagiumRecipeSerializers.LASER_ASSEMBLY.get(),
            ),
            HTMixerRecipeCategory(guiHelper),
            HTRefineryRecipeCategory(guiHelper),
            HTGeneratorFuelCategory(guiHelper),
            HTStirlingFuelCategory(guiHelper),
            HTMaterialInfoCategory(guiHelper),
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val level: ClientLevel = Minecraft.getInstance().level ?: return
        val recipeManager: RecipeManager = level.recipeManager
        val registry: HTMaterialRegistry = RagiumAPI.getInstance().getMaterialRegistry()

        fun <T : HTMachineRecipeBase> register(recipeType: JEIRecipeType<T>, recipe: Supplier<RecipeType<T>>) {
            registration.addRecipes(
                recipeType,
                recipeManager.getAllRecipesFor(recipe.get()).map(RecipeHolder<T>::value),
            )
        }

        register(RagiumJEIRecipeTypes.ASSEMBLER, RagiumRecipeTypes.ASSEMBLER)
        register(RagiumJEIRecipeTypes.BLAST_FURNACE, RagiumRecipeTypes.BLAST_FURNACE)
        registration.addRecipes(
            RagiumJEIRecipeTypes.COMPRESSOR,
            buildList {
                HTRecipeConverters.compressor(recipeManager, registry, this::add)
            },
        )
        register(RagiumJEIRecipeTypes.ENCHANTER, RagiumRecipeTypes.ENCHANTER)
        register(RagiumJEIRecipeTypes.EXTRACTOR, RagiumRecipeTypes.EXTRACTOR)
        registration.addRecipes(
            RagiumJEIRecipeTypes.GRINDER,
            buildList {
                HTRecipeConverters.grinder(recipeManager, registry, this::add)
            },
        )
        register(RagiumJEIRecipeTypes.GROWTH_CHAMBER, RagiumRecipeTypes.GROWTH_CHAMBER)
        registration.addRecipes(
            RagiumJEIRecipeTypes.INFUSER,
            buildList {
                HTRecipeConverters.infuser(recipeManager, registry, this::add)
            },
        )
        register(RagiumJEIRecipeTypes.LASER_ASSEMBLY, RagiumRecipeTypes.LASER_ASSEMBLY)
        register(RagiumJEIRecipeTypes.MIXER, RagiumRecipeTypes.MIXER)
        register(RagiumJEIRecipeTypes.REFINERY, RagiumRecipeTypes.REFINERY)

        // Generator Fuel
        registration.addRecipes(
            RagiumJEIRecipeTypes.GENERATOR,
            listOf(
                HTGeneratorFuelEntry(RagiumMachineKeys.COMBUSTION_GENERATOR, RagiumFluidTags.NITRO_FUEL, 10),
                HTGeneratorFuelEntry(RagiumMachineKeys.COMBUSTION_GENERATOR, RagiumFluidTags.NON_NITRO_FUEL, 100),
                HTGeneratorFuelEntry(RagiumMachineKeys.THERMAL_GENERATOR, RagiumFluidTags.THERMAL_FUEL, 100),
            ),
        )
        // Stirling
        registration.addRecipes(
            RagiumJEIRecipeTypes.STIRLING,
            level
                .registryAccess()
                .lookupOrThrow(Registries.ITEM)
                .listElements()
                .filter { it.getData(NeoForgeDataMaps.FURNACE_FUELS) != null }
                .map(::HTStirlingFuelEntry)
                .toList(),
        )
        // Material Info
        registration.addRecipes(RagiumJEIRecipeTypes.MATERIAL_INFO, RagiumAPI.getInstance().getMaterialRegistry().typedMaterials)
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        fun register(recipeType: JEIRecipeType<*>, machine: HTMachineKey) {
            registration.addRecipeCatalysts(recipeType, machine.getBlock())
        }

        // Assembler
        register(RagiumJEIRecipeTypes.ASSEMBLER, RagiumMachineKeys.ASSEMBLER)
        // Blast Furnace
        register(RagiumJEIRecipeTypes.BLAST_FURNACE, RagiumMachineKeys.BLAST_FURNACE)
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.BLAST_FURNACE, RagiumBlocks.PRIMITIVE_BLAST_FURNACE)
        // Compressor
        register(RagiumJEIRecipeTypes.COMPRESSOR, RagiumMachineKeys.COMPRESSOR)
        // Enchanter
        register(RagiumJEIRecipeTypes.ENCHANTER, RagiumMachineKeys.ARCANE_ENCHANTER)
        // Extractor
        register(RagiumJEIRecipeTypes.EXTRACTOR, RagiumMachineKeys.EXTRACTOR)
        // Grinder
        register(RagiumJEIRecipeTypes.GRINDER, RagiumMachineKeys.GRINDER)
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.GRINDER, RagiumBlocks.MANUAL_GRINDER)
        // Growth Chamber
        register(RagiumJEIRecipeTypes.GROWTH_CHAMBER, RagiumMachineKeys.GROWTH_CHAMBER)
        // Infuser
        register(RagiumJEIRecipeTypes.INFUSER, RagiumMachineKeys.INFUSER)
        // Laser Assembly
        register(RagiumJEIRecipeTypes.LASER_ASSEMBLY, RagiumMachineKeys.LASER_ASSEMBLY)
        // Mixer
        register(RagiumJEIRecipeTypes.MIXER, RagiumMachineKeys.MIXER)
        // Refinery
        register(RagiumJEIRecipeTypes.REFINERY, RagiumMachineKeys.REFINERY)

        // Generator
        register(RagiumJEIRecipeTypes.GENERATOR, RagiumMachineKeys.COMBUSTION_GENERATOR)
        register(RagiumJEIRecipeTypes.GENERATOR, RagiumMachineKeys.THERMAL_GENERATOR)
        // Stirling
        register(RagiumJEIRecipeTypes.STIRLING, RagiumMachineKeys.STIRLING_GENERATOR)
        // Material
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.MATERIAL_INFO, Items.IRON_INGOT)
    }
}
