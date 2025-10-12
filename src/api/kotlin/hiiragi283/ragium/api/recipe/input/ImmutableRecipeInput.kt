package hiiragi283.ragium.api.recipe.input

import hiiragi283.ragium.api.storage.item.HTItemStorageStack
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

@JvmInline
value class ImmutableRecipeInput(val input: RecipeInput) : Iterable<HTItemStorageStack> {
    override fun iterator(): Iterator<HTItemStorageStack> = object : Iterator<HTItemStorageStack> {
        private var index = 0

        override fun next(): HTItemStorageStack {
            val stack: ItemStack = input.getItem(index)
            index++
            return HTItemStorageStack.of(stack)
        }

        override fun hasNext(): Boolean = index < input.size()
    }
}
