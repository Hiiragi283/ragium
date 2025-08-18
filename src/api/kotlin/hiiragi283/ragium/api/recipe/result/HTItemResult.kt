package hiiragi283.ragium.api.recipe.result

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.tag.HTKeyOrTagEntry
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class HTItemResult(entry: HTKeyOrTagEntry<Item>, amount: Int, components: DataComponentPatch) :
    HTRecipeResultBase<Item, ItemStack>(
        entry,
        amount,
        components,
    ) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResult> = createCodec(
            Registries.ITEM,
            BiCodec.intRange(1, 99).optionalOrElseField("count", 1),
            ::HTItemResult,
        )
    }

    override fun createStack(holder: Holder<Item>, amount: Int, components: DataComponentPatch): DataResult<ItemStack> {
        val stack = ItemStack(holder, amount, components)
        return when {
            stack.isEmpty -> DataResult.error { "Empty Item Stack is not valid for recipe result!" }
            else -> DataResult.success(stack)
        }
    }

    override val emptyStack: ItemStack get() = ItemStack.EMPTY
}
