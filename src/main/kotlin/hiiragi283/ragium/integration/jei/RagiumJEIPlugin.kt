package hiiragi283.ragium.integration.jei

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.mutableMultiMapOf
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.getOrDefault
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.util.HTTemperatureInfo
import hiiragi283.ragium.api.util.HTTemperatureType
import hiiragi283.ragium.api.util.collection.HTMultiMap
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.integration.jei.category.HTMachineRecipeCategory
import hiiragi283.ragium.integration.jei.category.HTMaterialInfoCategory
import hiiragi283.ragium.integration.jei.category.HTTemperatureInfoCategory
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.helpers.IJeiHelpers
import mezz.jei.api.ingredients.IIngredientType
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.IModIngredientRegistration
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.block.Block
import org.slf4j.Logger
import kotlin.streams.asSequence

@JeiPlugin
class RagiumJEIPlugin : IModPlugin {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()

        @JvmField
        val PLUGIN_ID: ResourceLocation = RagiumAPI.id("default")

        @JvmField
        val MACHINE_TIER_TYPE: IIngredientType<HTMachineTier> =
            IIngredientType<HTMachineTier> { HTMachineTier::class.java }

        @JvmField
        val MATERIAL_INFO: RecipeType<HTMaterialKey> =
            RecipeType.create(RagiumAPI.MOD_ID, "material_info", HTMaterialKey::class.java)

        @JvmField
        val HEATING_INFO: RecipeType<JEITemperatureInfo> =
            RecipeType.create(RagiumAPI.MOD_ID, "heating_info", JEITemperatureInfo::class.java)

        @JvmField
        val COOLING_INFO: RecipeType<JEITemperatureInfo> =
            RecipeType.create(RagiumAPI.MOD_ID, "cooling_info", JEITemperatureInfo::class.java)

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

    override fun registerIngredients(registration: IModIngredientRegistration) {
        registration.register(
            MACHINE_TIER_TYPE,
            HTMachineTier.entries.toList(),
            HTMachineTierHelper,
            HTMaterialTierRenderer,
            HTMachineTier.CODEC,
        )
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val jeiHelper: IJeiHelpers = registration.jeiHelpers
        val guiHelper: IGuiHelper = jeiHelper.guiHelper
        HTMachineKey.allKeys.forEach { key: HTMachineKey ->
            registration.addRecipeCategories(HTMachineRecipeCategory(key, guiHelper))
        }

        registration.addRecipeCategories(
            HTMaterialInfoCategory(guiHelper),
            HTTemperatureInfoCategory(guiHelper, HEATING_INFO, Component.literal("Heating Info"), Items.LAVA_BUCKET),
            HTTemperatureInfoCategory(guiHelper, COOLING_INFO, Component.literal("Cooling Info"), Items.WATER_BUCKET),
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        // Machine
        val level: ClientLevel = Minecraft.getInstance().level ?: return
        val recipeCache: HTMultiMap.Mutable<HTMachineKey, RecipeHolder<HTMachineRecipe>> = mutableMultiMapOf()

        RagiumAPI.machineRegistry.forEachEntries { key: HTMachineKey, _: HTBlockContent?, properties: HTPropertyHolder ->
            properties
                .getOrDefault(HTMachinePropertyKeys.RECIPE_PROXY)
                .getRecipes(level) { holder: RecipeHolder<HTMachineRecipe> ->
                    if (holder.value.machineKey == key) recipeCache.put(key, holder)
                }
        }

        recipeCache.map.forEach { machine: HTMachineKey, holders: Collection<RecipeHolder<HTMachineRecipe>> ->
            registration.addRecipes(getRecipeType(machine), holders.toList())
        }
        // Material Info
        registration.addRecipes(
            MATERIAL_INFO,
            RagiumAPI
                .materialRegistry
                .keys
                .toList(),
        )
        // Heating
        registration.addRecipes(
            HEATING_INFO,
            BuiltInRegistries.BLOCK
                .holders()
                .asSequence()
                .mapNotNull { holder: Holder.Reference<Block> ->
                    val tempInfo: HTTemperatureInfo =
                        holder.getData(RagiumAPI.DataMapTypes.TEMP_TIER) ?: return@mapNotNull null
                    if (tempInfo.type != HTTemperatureType.HEATING) return@mapNotNull null
                    JEITemperatureInfo(tempInfo.tier, holder)
                }.toList(),
        )
        // Cooling
        registration.addRecipes(
            COOLING_INFO,
            BuiltInRegistries.BLOCK
                .holders()
                .asSequence()
                .mapNotNull { holder: Holder.Reference<Block> ->
                    val tempInfo: HTTemperatureInfo =
                        holder.getData(RagiumAPI.DataMapTypes.TEMP_TIER) ?: return@mapNotNull null
                    if (tempInfo.type != HTTemperatureType.COOLING) return@mapNotNull null
                    JEITemperatureInfo(tempInfo.tier, holder)
                }.toList(),
        )
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        // Machine
        RagiumAPI.machineRegistry.blockMap.forEach { (key: HTMachineKey, content: HTBlockContent) ->
            val stack = ItemStack(content)
            registration.addRecipeCatalysts(getRecipeType(key), stack)
        }

        registration.addRecipeCatalysts(getRecipeType(RagiumMachineKeys.GRINDER), RagiumBlocks.MANUAL_GRINDER)
        // Material
        registration.addRecipeCatalysts(MATERIAL_INFO, Items.IRON_INGOT)
        // Heating
        registration.addRecipeCatalysts(HEATING_INFO, Items.LAVA_BUCKET)
        // Cooling
        registration.addRecipeCatalysts(COOLING_INFO, Items.WATER_BUCKET)
    }
}
