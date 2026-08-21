package hiiragi283.ragium.api.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.OnDatapackSyncEvent

@EventBusSubscriber
data object RagiumRecipeTypes {
    @JvmStatic
    val allTypes: Set<HTRecipeType<*>> field: MutableSet<HTRecipeType<*>> = mutableSetOf()

    @JvmStatic
    private fun <T : Recipe<*>> create(name: String): HTRecipeType<T> = HTRecipeType<T>(RagiumAPI.id(name)).also(allTypes::add)

    @SubscribeEvent
    fun onDatapackSync(event: OnDatapackSyncEvent) {
        event.sendRecipes(allTypes)
    }

    // Mechanical
    @JvmField
    val ASSEMBLING: HTRecipeType<RTAssemblingRecipe> = create(RagiumConstants.ASSEMBLING)

    @JvmField
    val CRUSHING: HTRecipeType<RTCrushingRecipe> = create(RagiumConstants.CRUSHING)

    @JvmField
    val CUTTING: HTRecipeType<RTCuttingRecipe> = create(RagiumConstants.CUTTING)

    // Heat
    @JvmField
    val FREEZING: HTRecipeType<RTFreezingRecipe> = create(RagiumConstants.FREEZING)

    @JvmField
    val MELTING: HTRecipeType<RTMeltingRecipe> = create(RagiumConstants.MELTING)

    @JvmField
    val SMELTING: HTRecipeType<RTSmeltingRecipe> = create(HTConstants.SMELTING)

    // Chemical
    @JvmField
    val ELECTROLYZING: HTRecipeType<RTElectrolyzingRecipe> = create(RagiumConstants.ELECTROLYZING)

    // Bio
    @JvmField
    val BREWING: HTRecipeType<RTBrewingRecipe> = create(RagiumConstants.BREWING)

    // Electronics

    // Arcane
}
