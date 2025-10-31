package hiiragi283.ragium.impl.recipe.result

import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutableOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

internal class HTItemResultImpl(entry: HTKeyOrTagEntry<Item>, amount: Int, components: DataComponentPatch) :
    HTRecipeResultBase<Item, ImmutableItemStack>(entry, amount, components),
    HTItemResult {
    override fun createStack(holder: Holder<Item>, amount: Int, components: DataComponentPatch): ImmutableItemStack =
        ItemStack(holder, amount, components).toImmutableOrThrow()

    override fun copyWithCount(count: Int): HTItemResult = HTItemResultImpl(entry, count, components)
}
