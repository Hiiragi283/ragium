package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.storage.item.ImmutableItemStack
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe

abstract class HTStackRecipeBuilder<BUILDER : HTStackRecipeBuilder<BUILDER>>(prefix: String, protected val stack: ImmutableItemStack) :
    HTRecipeBuilder.Prefixed(prefix) {
    protected abstract fun createRecipe(output: ItemStack): Recipe<*>

    final override fun getPrimalId(): ResourceLocation = stack.getId()

    final override fun createRecipe(): Recipe<*> = createRecipe(stack.stack)
}
