package hiiragi283.ragium.api.recipe.input

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

@JvmInline
value class ImmutableRecipeInput(val input: RecipeInput) : Iterable<ImmutableItemStack> {
    override fun iterator(): Iterator<ImmutableItemStack> = object : Iterator<ImmutableItemStack> {
        private var index = 0

        override fun next(): ImmutableItemStack {
            val stack: ItemStack = input.getItem(index)
            index++
            return stack.toImmutable()
        }

        override fun hasNext(): Boolean = index < input.size()
    }
}
