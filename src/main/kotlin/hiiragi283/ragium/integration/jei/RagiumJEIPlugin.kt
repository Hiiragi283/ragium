package hiiragi283.ragium.integration.jei

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.buildMultiMap
import hiiragi283.ragium.api.extension.machineTier
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.property.get
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.util.collection.HTMultiMap
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.integration.jei.category.HTMachineRecipeCategory
import hiiragi283.ragium.integration.jei.category.HTMaterialInfoCategory
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IJeiHelpers
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
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
        val MATERIAL_INFO: RecipeType<HTMaterialRegistry.Entry> =
            RecipeType.create(RagiumAPI.MOD_ID, "material_info", HTMaterialRegistry.Entry::class.java)

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

    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        val handler: ISubtypeInterpreter<ItemStack> = object : ISubtypeInterpreter<ItemStack> {
            override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any? = ingredient.machineTier

            override fun getLegacyStringSubtypeInfo(ingredient: ItemStack, context: UidContext): String = ingredient.machineTier.text.string
        }

        RagiumAPI.machineRegistry.blocks.forEach { content: HTBlockContent ->
            registration.registerSubtypeInterpreter(content.asItem(), handler)
        }
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val jeiHelper: IJeiHelpers = registration.jeiHelpers
        val guiHelper: IGuiHelper = jeiHelper.guiHelper
        RagiumAPI.machineRegistry.keys.forEach { key: HTMachineKey ->
            registration.addRecipeCategories(HTMachineRecipeCategory(key, guiHelper))
        }

        registration.addRecipeCategories(HTMaterialInfoCategory(guiHelper))
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        // Machine
        val level: ClientLevel = Minecraft.getInstance().level ?: return
        val recipes: List<RecipeHolder<HTMachineRecipe>> =
            level.recipeManager.getAllRecipesFor(RagiumRecipes.MACHINE_TYPE.get())

        val multiMap: HTMultiMap<HTMachineKey, RecipeHolder<HTMachineRecipe>> = buildMultiMap {
            // Process Recipe
            recipes.forEach { holder: RecipeHolder<HTMachineRecipe> -> put(holder.value.machineKey, holder) }
            // Generator Fuel
            RagiumAPI.machineRegistry.entryMap.forEach { (key: HTMachineKey, entry: HTMachineRegistry.Entry) ->
                val fuelData: Set<HTGeneratorFuel> = entry[HTMachinePropertyKeys.GENERATOR_FUEL] ?: return@forEach
                putAll(
                    key,
                    fuelData.map { (fuel: TagKey<Fluid>, amount: Int) ->
                        HTMachineRecipeBuilder
                            .create(key)
                            .fluidInput(fuel, amount)
                            .export(fuel.location)
                    },
                )
            }
        }

        multiMap.map.forEach { machine: HTMachineKey, holders: Collection<RecipeHolder<HTMachineRecipe>> ->
            registration.addRecipes(getRecipeType(machine), holders.toList())
        }
        // Material Info
        registration.addRecipes(
            MATERIAL_INFO,
            RagiumAPI
                .materialRegistry
                .entryMap
                .values
                .toList(),
        )
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        // Machine
        RagiumAPI.machineRegistry.entryMap.forEach { (key: HTMachineKey, _: HTMachineRegistry.Entry) ->
            HTMachineTier.entries.mapNotNull(key::createItemStack).forEach { stack: ItemStack ->
                registration.addRecipeCatalysts(getRecipeType(key), stack)
                if (key == RagiumMachineKeys.MULTI_SMELTER) {
                    registration.addRecipeCatalysts(RecipeTypes.SMELTING, stack)
                }
            }
        }

        registration.addRecipeCatalysts(getRecipeType(RagiumMachineKeys.GRINDER), RagiumBlocks.MANUAL_GRINDER)
        // Material
        registration.addRecipeCatalysts(MATERIAL_INFO, Items.IRON_INGOT)
    }
}
