package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTCrushingRecipeBuilder
import hiiragi283.ragium.api.event.HTRecipesUpdatedEvent
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.RagiumRecipes
import net.minecraft.core.HolderGetter
import net.minecraft.world.item.Item
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeRecipes {
    @SubscribeEvent
    fun onMachineRecipesUpdated(event: HTRecipesUpdatedEvent) {
        crushing(event)
    }

    @JvmStatic
    private fun crushing(event: HTRecipesUpdatedEvent) {
        val registry: HTMaterialRegistry = RagiumAPI.getInstance().getMaterialRegistry()
        for (key: HTMaterialKey in registry.keys) {
            val name: String = key.name
            val type: HTMaterialType = registry.getType(key)
            val mainPrefix: HTTagPrefix? = type.getMainPrefix()
            val resultPrefix: HTTagPrefix? = type.getOreResultPrefix()
            // Ore
            if (resultPrefix != null) {
                event.register(
                    RagiumRecipes.CRUSHING,
                    RagiumAPI.id("runtime_${name}_dust_from_ore"),
                ) { lookup: HolderGetter<Item> ->
                    val result: Item = event.getFirstItem(resultPrefix, key) ?: return@register null
                    HTCrushingRecipeBuilder(result, 2)
                        .setIngredient(HTTagPrefix.ORE, key)
                        .createRecipe()
                }
            }
            val dust: Item = event.getFirstItem(HTTagPrefix.DUST, key) ?: continue
            // Gem/Ingot
            if (mainPrefix != null) {
                event.register(
                    RagiumRecipes.CRUSHING,
                    RagiumAPI.id("runtime_${name}_dust_from_main"),
                ) { lookup: HolderGetter<Item> ->
                    HTCrushingRecipeBuilder(dust)
                        .setIngredient(mainPrefix, key)
                        .createRecipe()
                }
            }
            // Gear
            event.register(
                RagiumRecipes.CRUSHING,
                RagiumAPI.id("runtime_${name}_dust_from_gear"),
            ) { lookup: HolderGetter<Item> ->
                HTCrushingRecipeBuilder(dust, 4)
                    .setIngredient(HTTagPrefix.GEAR, key)
                    .createRecipe()
            }
            // Plate
            event.register(
                RagiumRecipes.CRUSHING,
                RagiumAPI.id("runtime_${name}_dust_from_plate"),
            ) { lookup: HolderGetter<Item> ->
                HTCrushingRecipeBuilder(dust)
                    .setIngredient(HTTagPrefix.PLATE, key)
                    .createRecipe()
            }
        }
    }
}
