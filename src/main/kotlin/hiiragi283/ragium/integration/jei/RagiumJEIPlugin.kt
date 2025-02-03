package hiiragi283.ragium.integration.jei

import com.mojang.logging.LogUtils
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
import hiiragi283.ragium.api.material.HTTypedMaterial
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.get
import hiiragi283.ragium.api.util.collection.HTMultiMap
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.common.recipe.condition.HTDummyCondition
import hiiragi283.ragium.integration.jei.category.HTMachineRecipeCategory
import hiiragi283.ragium.integration.jei.category.HTMaterialInfoCategory
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IJeiHelpers
import mezz.jei.api.recipe.RecipeType
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
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.material.Fluid
import org.slf4j.Logger

@JeiPlugin
class RagiumJEIPlugin : IModPlugin {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()

        @JvmField
        val PLUGIN_ID: ResourceLocation = RagiumAPI.id("default")

        @JvmField
        val MATERIAL_INFO: RecipeType<HTTypedMaterial> =
            RecipeType.create(RagiumAPI.MOD_ID, "material_info", HTTypedMaterial::class.java)

        @JvmStatic
        private val RECIPE_TYPE_MAP: MutableMap<HTMachineKey, RecipeType<RecipeHolder<HTMachineRecipe>>> =
            mutableMapOf()

        @JvmStatic
        fun getRecipeType(machine: HTMachineKey): RecipeType<RecipeHolder<HTMachineRecipe>> =
            RECIPE_TYPE_MAP.computeIfAbsent(machine) { key: HTMachineKey ->
                RecipeType.createRecipeHolderType(RagiumAPI.id(key.name))
            }
    }

    override fun getPluginUid(): ResourceLocation = PLUGIN_ID

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val jeiHelper: IJeiHelpers = registration.jeiHelpers
        val guiHelper: IGuiHelper = jeiHelper.guiHelper
        HTMachineKey.allKeys.forEach { key: HTMachineKey ->
            registration.addRecipeCategories(HTMachineRecipeCategory(key, guiHelper))
        }

        registration.addRecipeCategories(HTMaterialInfoCategory(guiHelper))
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        // Machine
        val level: ClientLevel = Minecraft.getInstance().level ?: return
        val recipeCache: HTMultiMap.Mutable<HTMachineKey, RecipeHolder<HTMachineRecipe>> = mutableMultiMapOf()

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
            registration.addRecipes(getRecipeType(machine), holders.toList())
        }
        // Material Info
        registration.addRecipes(MATERIAL_INFO, RagiumAPI.materialRegistry.typedMaterials)
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        // Machine
        RagiumAPI.machineRegistry.blockMap.forEach { (key: HTMachineKey, content: HTBlockContent) ->
            val stack = ItemStack(content)
            registration.addRecipeCatalysts(getRecipeType(key), stack)
        }

        registration.addRecipeCatalysts(getRecipeType(RagiumMachineKeys.GRINDER), RagiumBlocks.MANUAL_GRINDER)
        registration.addRecipeCatalysts(
            getRecipeType(RagiumMachineKeys.BLAST_FURNACE),
            RagiumBlocks.PRIMITIVE_BLAST_FURNACE,
        )
        // Material
        registration.addRecipeCatalysts(MATERIAL_INFO, Items.IRON_INGOT)
    }
}
