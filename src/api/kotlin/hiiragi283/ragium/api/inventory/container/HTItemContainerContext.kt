package hiiragi283.ragium.api.inventory.container

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack
import java.util.Optional

@JvmRecord
data class HTItemContainerContext(val hand: Optional<InteractionHand>, val stack: ItemStack) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemContainerContext> = BiCodec.composite(
            VanillaBiCodecs.HAND.toOptional().fieldOf("hand"),
            HTItemContainerContext::hand,
            VanillaBiCodecs.ITEM_STACK.fieldOf("stack"),
            HTItemContainerContext::stack,
            ::HTItemContainerContext,
        )
    }

    constructor(hand: InteractionHand?, stack: ItemStack) : this(Optional.ofNullable(hand), stack)
}
