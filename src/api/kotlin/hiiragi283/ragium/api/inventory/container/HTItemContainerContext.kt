package hiiragi283.ragium.api.inventory.container

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.InteractionHand
import java.util.Optional

@JvmRecord
data class HTItemContainerContext(val hand: Optional<InteractionHand>, val stack: ImmutableItemStack) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemContainerContext> = BiCodec.composite(
            VanillaBiCodecs.HAND.optionalFieldOf("hand").forGetter(HTItemContainerContext::hand),
            ImmutableItemStack.CODEC.fieldOf("stack").forGetter(HTItemContainerContext::stack),
            ::HTItemContainerContext,
        )
    }

    constructor(hand: InteractionHand?, stack: ImmutableItemStack) : this(Optional.ofNullable(hand), stack)
}
