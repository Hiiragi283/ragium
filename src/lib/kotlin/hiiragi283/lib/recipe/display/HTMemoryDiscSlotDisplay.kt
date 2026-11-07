package hiiragi283.lib.recipe.display

import com.mojang.serialization.MapCodec
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.ragium.api.data.HTMemoryDiscData
import hiiragi283.ragium.api.data.RagiumDataComponents
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.display.DisplayContentsFactory
import net.minecraft.world.item.crafting.display.SlotDisplay
import java.util.stream.Stream

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
@JvmRecord
data class HTMemoryDiscSlotDisplay(val base: SlotDisplay, val data: SlotDisplay) : SlotDisplay {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMemoryDiscSlotDisplay> = HTCodecs.recordMap { instance ->
            instance.group(
                SlotDisplay.CODEC.fieldOf("base").forGetter(HTMemoryDiscSlotDisplay::base),
                SlotDisplay.CODEC.fieldOf("data").forGetter(HTMemoryDiscSlotDisplay::data),
            ).apply(instance, ::HTMemoryDiscSlotDisplay)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMemoryDiscSlotDisplay> = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC,
            HTMemoryDiscSlotDisplay::base,
            SlotDisplay.STREAM_CODEC,
            HTMemoryDiscSlotDisplay::data,
            ::HTMemoryDiscSlotDisplay,
        )

        @JvmField
        val TYPE: SlotDisplay.Type<HTMemoryDiscSlotDisplay> = SlotDisplay.Type(CODEC, STREAM_CODEC)
    }

    override fun <T : Any> resolve(context: ContextMap, builder: DisplayContentsFactory<T>): Stream<T> =
        if (builder is DisplayContentsFactory.ForStacks<T>) {
            base.resolve(context, SlotDisplay.ItemStackContentsFactory.INSTANCE).flatMap { stack: ItemStack ->
                data.resolve(context, SlotDisplay.ItemStackContentsFactory.INSTANCE)
                    .map(HTMemoryDiscData::from)
                    .map { data: HTMemoryDiscData? ->
                        val result: ItemStack = stack.copy()
                        result[RagiumDataComponents.MEMORY_DISC_DATA] = data
                        result
                    }
            }.map(builder::forStack)
        } else {
            Stream.empty()
        }

    override fun type(): SlotDisplay.Type<out SlotDisplay> = TYPE
}
