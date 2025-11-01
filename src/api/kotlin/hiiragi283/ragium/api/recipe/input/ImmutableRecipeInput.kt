package hiiragi283.ragium.api.recipe.input

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.world.item.crafting.RecipeInput

class ImmutableRecipeInput(private val input: RecipeInput) : AbstractList<ImmutableItemStack?>() {
    override val size: Int
        get() = input.size()

    override fun get(index: Int): ImmutableItemStack? = input.getItem(index).toImmutable()
}
