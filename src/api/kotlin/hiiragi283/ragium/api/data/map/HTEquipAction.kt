package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.util.function.Function

interface HTEquipAction {
    companion object {
        @JvmField
        val CODEC: Codec<HTEquipAction> = RagiumAPI.EQUIP_ACTION_TYPE_REGISTRY
            .byNameCodec()
            .dispatch(HTEquipAction::type, Function.identity())
    }

    fun type(): MapCodec<out HTEquipAction>

    fun onEquip(player: Player, stackTo: ItemStack)

    fun onUnequip(player: Player, stackFrom: ItemStack)
}
