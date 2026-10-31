package hiiragi283.lib.recipe.display

import com.mojang.serialization.MapCodec
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.crafting.display.DisplayContentsFactory
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.neoforged.neoforge.fluids.crafting.display.ForFluidStacks
import java.util.stream.Stream

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
@JvmRecord
data class HTSizedSlotDisplay(val unsized: SlotDisplay, val size: Int) : SlotDisplay {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTSizedSlotDisplay> = HTCodecs.recordMap { instance ->
            instance.group(
                SlotDisplay.CODEC.fieldOf("unsized").forGetter(HTSizedSlotDisplay::unsized),
                HTCodecs.POSITIVE_INT.optionalFieldOf("size", 1).forGetter(HTSizedSlotDisplay::size),
            ).apply(instance, ::HTSizedSlotDisplay)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSizedSlotDisplay> = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC,
            HTSizedSlotDisplay::unsized,
            ByteBufCodecs.VAR_INT,
            HTSizedSlotDisplay::size,
            ::HTSizedSlotDisplay,
        )

        @JvmField
        val TYPE: SlotDisplay.Type<HTSizedSlotDisplay> = SlotDisplay.Type(CODEC, STREAM_CODEC)
    }

    override fun <T : Any> resolve(context: ContextMap, builder: DisplayContentsFactory<T>): Stream<T> = when (builder) {
        is ForFluidStacks<T> -> unsized.resolve(
            context,
            ForFluidStacks { it.copyWithAmount(size) },
        ).map(builder::forStack)

        is DisplayContentsFactory.ForStacks<T> -> unsized.resolve(
            context,
            DisplayContentsFactory.ForStacks { it.copyWithCount(size) },
        ).map(builder::forStack)

        else -> Stream.empty()
    }

    override fun type(): SlotDisplay.Type<out SlotDisplay> = TYPE
}
