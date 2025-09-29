package hiiragi283.ragium.impl.recipe.result

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.filterNot
import hiiragi283.ragium.api.extension.wrapDataResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

internal class HTItemResultImpl(entry: HTKeyOrTagEntry<Item>, amount: Int, components: DataComponentPatch) :
    HTRecipeResultBase<Item, ItemStack>(entry, amount, components),
    HTItemResult {
    override fun createStack(holder: Holder<Item>, amount: Int, components: DataComponentPatch): DataResult<ItemStack> =
        ItemStack(holder, amount, components)
            .wrapDataResult()
            .filterNot(ItemStack::isEmpty, "Empty item stack is not valid for recipe result")
}
