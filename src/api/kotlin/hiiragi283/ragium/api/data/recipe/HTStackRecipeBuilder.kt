package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.registry.HTItemHolderLike
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe

abstract class HTStackRecipeBuilder<BUILDER : HTStackRecipeBuilder<BUILDER>>(
    prefix: String,
    protected val item: HTItemHolderLike,
    protected val count: Int,
    protected val component: DataComponentPatch,
) : HTRecipeBuilder.Prefixed(prefix) {
    protected abstract fun createRecipe(output: ItemStack): Recipe<*>

    final override fun getPrimalId(): ResourceLocation = item.getId()

    final override fun createRecipe(): Recipe<*> = createRecipe(item.toStack(count, component))
}
