package hiiragi283.ragium.api.recipe.result

import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.text.HTTextResult
import hiiragi283.ragium.api.text.RagiumTranslation
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * [ImmutableItemStack]向けの[HTRecipeResult]の実装
 */
class HTItemResult(entry: HTKeyOrTagEntry<Item>, amount: Int, components: DataComponentPatch) :
    HTRecipeResultBase<Item, ImmutableItemStack>(entry, amount, components) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResult> = createCodec(
            Registries.ITEM,
            BiCodec.intRange(1, 99).optionalOrElseField("count", 1),
            ::HTItemResult,
        )
    }

    override fun createStack(holder: Holder<Item>, amount: Int, components: DataComponentPatch): HTTextResult<ImmutableItemStack> =
        when (val stack: ImmutableItemStack? = ItemStack(holder, amount, components).toImmutable()) {
            null -> HTTextResult.failure(RagiumTranslation.EMPTY)
            else -> HTTextResult.success(stack)
        }
}
