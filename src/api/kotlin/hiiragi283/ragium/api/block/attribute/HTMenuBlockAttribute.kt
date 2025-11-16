package hiiragi283.ragium.api.block.attribute

import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import java.util.function.Supplier

/**
 * @see mekanism.common.block.attribute.AttributeGui
 */
@JvmRecord
data class HTMenuBlockAttribute<C>(val type: Supplier<HTDeferredMenuType.WithContext<*, C>>) : HTBlockAttribute {
    fun openMenu(
        player: Player,
        title: Component,
        obj: Any,
        writer: (RegistryFriendlyByteBuf) -> Unit,
    ): InteractionResult = type.get().openMenu(player, title, obj, writer)
}
