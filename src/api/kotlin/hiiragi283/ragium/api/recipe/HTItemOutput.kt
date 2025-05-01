package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

@ConsistentCopyVisibility
data class HTItemOutput private constructor(private val holder: Holder<Item>, val count: Int, val components: DataComponentPatch) :
    Supplier<ItemStack> {
        companion object {
            @JvmField
            val CODEC: Codec<HTItemOutput> = ItemStack.CODEC.comapFlatMap(HTItemOutput::fromStack, HTItemOutput::get)

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemOutput> =
                ItemStack.STREAM_CODEC.map(HTItemOutput::of, HTItemOutput::get)

            @JvmStatic
            fun of(item: ItemLike, count: Int): HTItemOutput = of(ItemStack(item, count))

            @JvmStatic
            fun of(stack: ItemStack): HTItemOutput = fromStack(stack).orThrow

            @JvmStatic
            fun fromStack(stack: ItemStack): DataResult<HTItemOutput> {
                if (stack.isEmpty) {
                    return DataResult.error { "Empty ItemStack is not allowed for HTItemOutput!" }
                }
                return DataResult.success(HTItemOutput(stack.itemHolder, stack.count, stack.componentsPatch))
            }
        }

        val id: ResourceLocation = holder.idOrThrow

        override fun get(): ItemStack = ItemStack(holder, count, components)
    }
