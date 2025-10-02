package hiiragi283.ragium.integration.emi.type

import hiiragi283.ragium.api.math.HTBoundsProvider
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * @see [mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType]
 */
interface HTRecipeViewerType<RECIPE : Any> :
    HTBoundsProvider,
    HTHasTranslationKey,
    HTHolderLike {
    val iconStack: ItemStack
    val icon: ResourceLocation?
    val workStations: List<ItemLike>

    fun getTitle(): Component = Component.translatable(translationKey)
}
