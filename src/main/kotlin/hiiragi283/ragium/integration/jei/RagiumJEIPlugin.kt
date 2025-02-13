package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTSoap
import hiiragi283.ragium.api.data.RagiumDataMaps
import hiiragi283.ragium.api.extension.getAllRecipes
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.recipe.HTRecipeConverters
import hiiragi283.ragium.integration.jei.category.*
import hiiragi283.ragium.integration.jei.entry.HTGeneratorFuelEntry
import hiiragi283.ragium.integration.jei.entry.HTSoapEntry
import hiiragi283.ragium.integration.jei.entry.HTStirlingFuelEntry
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
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
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
                HTMachineType.ASSEMBLER,
                RagiumJEIRecipeTypes.ASSEMBLER,
                RagiumRecipeSerializers.ASSEMBLER.get(),
            ),
            HTMultiItemRecipeCategory(
                guiHelper,
                HTMachineType.BLAST_FURNACE,
                RagiumJEIRecipeTypes.BLAST_FURNACE,
                RagiumRecipeSerializers.BLAST_FURNACE.get(),
            ),
            HTBreweryRecipeCategory(guiHelper),
            HTSingleItemRecipeCategory(
                guiHelper,
                HTMachineType.COMPRESSOR,
                RagiumJEIRecipeTypes.COMPRESSOR,
                RagiumRecipeSerializers.COMPRESSOR.get(),
            ),
            HTEnchanterRecipeCategory(guiHelper),
            HTExtractorRecipeCategory(guiHelper),
            HTSingleItemRecipeCategory(
                guiHelper,
                HTMachineType.GRINDER,
                RagiumJEIRecipeTypes.GRINDER,
                RagiumRecipeSerializers.GRINDER.get(),
            ),
            HTGrowthChamberRecipeCategory(guiHelper),
            HTInfuserRecipeCategory(guiHelper),
            HTSingleItemRecipeCategory(
                guiHelper,
                HTMachineType.LASER_ASSEMBLY,
                RagiumJEIRecipeTypes.LASER_ASSEMBLY,
                RagiumRecipeSerializers.LASER_ASSEMBLY.get(),
            ),
            HTMixerRecipeCategory(guiHelper),
            HTRefineryRecipeCategory(guiHelper),
            HTGeneratorFuelCategory(guiHelper),
            HTStirlingFuelCategory(guiHelper),
            HTMaterialInfoCategory(guiHelper),
            HTSoapCategory(guiHelper),
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val level: ClientLevel = Minecraft.getInstance().level ?: return
        val recipeManager: RecipeManager = level.recipeManager

        fun <T : HTMachineRecipeBase> register(recipeType: JEIRecipeType<T>, recipe: Supplier<RecipeType<T>>) {
            registration.addRecipes(
                recipeType,
                recipeManager.getAllRecipes(recipe.get()),
            )
        }

        register(RagiumJEIRecipeTypes.ASSEMBLER, RagiumRecipeTypes.ASSEMBLER)
        register(RagiumJEIRecipeTypes.BLAST_FURNACE, RagiumRecipeTypes.BLAST_FURNACE)
        registration.addRecipes(
            RagiumJEIRecipeTypes.COMPRESSOR,
            buildList {
                HTRecipeConverters.compressor(this::add)
            },
        )
        register(RagiumJEIRecipeTypes.BREWERY, RagiumRecipeTypes.BREWERY)
        register(RagiumJEIRecipeTypes.ENCHANTER, RagiumRecipeTypes.ENCHANTER)
        register(RagiumJEIRecipeTypes.EXTRACTOR, RagiumRecipeTypes.EXTRACTOR)
        registration.addRecipes(
            RagiumJEIRecipeTypes.GRINDER,
            buildList {
                HTRecipeConverters.grinder(this::add)
            },
        )
        register(RagiumJEIRecipeTypes.GROWTH_CHAMBER, RagiumRecipeTypes.GROWTH_CHAMBER)
        registration.addRecipes(
            RagiumJEIRecipeTypes.INFUSER,
            buildList {
                HTRecipeConverters.infuser(this::add)
            },
        )
        register(RagiumJEIRecipeTypes.LASER_ASSEMBLY, RagiumRecipeTypes.LASER_ASSEMBLY)
        register(RagiumJEIRecipeTypes.MIXER, RagiumRecipeTypes.MIXER)
        register(RagiumJEIRecipeTypes.REFINERY, RagiumRecipeTypes.REFINERY)

        // Generator Fuel
        registration.addRecipes(
            RagiumJEIRecipeTypes.GENERATOR,
            listOf(
                HTGeneratorFuelEntry(HTMachineType.COMBUSTION_GENERATOR, RagiumFluidTags.NITRO_FUEL, 10),
                HTGeneratorFuelEntry(HTMachineType.COMBUSTION_GENERATOR, RagiumFluidTags.NON_NITRO_FUEL, 100),
                HTGeneratorFuelEntry(HTMachineType.THERMAL_GENERATOR, RagiumFluidTags.THERMAL_FUEL, 100),
            ),
        )
        // Stirling
        registration.addRecipes(
            RagiumJEIRecipeTypes.STIRLING,
            BuiltInRegistries.ITEM
                .getDataMap(NeoForgeDataMaps.FURNACE_FUELS)
                .mapNotNull { (key: ResourceKey<Item>, fuelTime: FurnaceFuel) ->
                    val holder: Holder.Reference<Item> = BuiltInRegistries.ITEM.getHolderOrThrow(key)
                    HTStirlingFuelEntry(holder, fuelTime.burnTime)
                }.sorted(),
        )
        // Material Info
        registration.addRecipes(RagiumJEIRecipeTypes.MATERIAL_INFO, RagiumAPI.getInstance().getMaterialRegistry().typedMaterials)
        // Soap
        registration.addRecipes(
            RagiumJEIRecipeTypes.SOAP,
            BuiltInRegistries.BLOCK
                .getDataMap(RagiumDataMaps.SOAP)
                .mapNotNull { (key: ResourceKey<Block>, soap: HTSoap) ->
                    val holder: Holder.Reference<Block> = BuiltInRegistries.BLOCK.getHolderOrThrow(key)
                    if (holder.value() == soap.block) return@mapNotNull null
                    HTSoapEntry(holder, soap)
                }.sorted(),
        )
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        // Assembler
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.ASSEMBLER, HTMachineType.ASSEMBLER)
        // Blast Furnace
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.BLAST_FURNACE, HTMachineType.BLAST_FURNACE)
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.BLAST_FURNACE, RagiumBlocks.PRIMITIVE_BLAST_FURNACE)
        // Brewery
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.BREWERY, HTMachineType.ALCHEMICAL_BREWERY)
        // Compressor
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.COMPRESSOR, HTMachineType.COMPRESSOR)
        // Enchanter
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.ENCHANTER, HTMachineType.ARCANE_ENCHANTER)
        // Extractor
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.EXTRACTOR, HTMachineType.EXTRACTOR)
        // Grinder
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.GRINDER, HTMachineType.GRINDER)
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.GRINDER, RagiumBlocks.MANUAL_GRINDER)
        // Growth Chamber
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.GROWTH_CHAMBER, HTMachineType.GROWTH_CHAMBER)
        // Infuser
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.INFUSER, HTMachineType.INFUSER)
        // Laser Assembly
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.LASER_ASSEMBLY, HTMachineType.LASER_ASSEMBLY)
        // Mixer
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.MIXER, HTMachineType.MIXER)
        // Refinery
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.REFINERY, HTMachineType.REFINERY)

        // Generator
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.GENERATOR, HTMachineType.COMBUSTION_GENERATOR)
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.GENERATOR, HTMachineType.THERMAL_GENERATOR)
        // Stirling
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.STIRLING, HTMachineType.STIRLING_GENERATOR)
        // Material
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.MATERIAL_INFO, Items.IRON_INGOT)
        // Soap
        registration.addRecipeCatalysts(RagiumJEIRecipeTypes.SOAP, RagiumItems.SOAP)
    }
}
