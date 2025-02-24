package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.data.recipe.HTMachineRecipeBuilderBase
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeHolder
import net.neoforged.bus.api.Event
import java.util.function.Function

class HTMachineRecipesUpdatedEvent(
    provider: HolderLookup.Provider,
    private val currentType: HTRecipeType<*>,
    private val consumer: (RecipeHolder<out HTMachineRecipeBase>) -> Unit,
) : Event(),
    HolderLookup.Provider by provider {
    fun <T : HTMachineRecipeBase> register(
        recipeType: HTRecipeType<T>,
        recipeId: ResourceLocation,
        function: Function<HolderGetter<Item>, HTMachineRecipeBuilderBase<*, T>?>,
    ) {
        if (currentType == recipeType) {
            function.apply(lookupOrThrow(Registries.ITEM))?.export(recipeId, consumer)
        }
    }
}
