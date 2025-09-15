package hiiragi283.ragium.api.inventory.container

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack
import java.util.Optional

@JvmRecord
data class HTItemContainerContext(val hand: Optional<InteractionHand>, val stack: ItemStack) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemContainerContext> = BiCodec.composite(
            BiCodecs.HAND.toOptional().fieldOf("hand"),
            HTItemContainerContext::hand,
            BiCodecs.itemStack(true).fieldOf("stack"),
            HTItemContainerContext::stack,
            ::HTItemContainerContext,
        )
    }

    constructor(hand: InteractionHand?, stack: ItemStack) : this(Optional.ofNullable(hand), stack)
}
