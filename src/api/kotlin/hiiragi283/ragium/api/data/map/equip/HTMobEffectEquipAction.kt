package hiiragi283.ragium.api.data.map.equip

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.data.map.HTEquipAction
import hiiragi283.ragium.api.item.alchemy.HTMobEffectInstance
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

data class HTMobEffectEquipAction(private val instance: HTMobEffectInstance) : HTEquipAction {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMobEffectEquipAction> = HTMobEffectInstance.CODEC
            .codec
            .fieldOf("effect")
            .xmap(::HTMobEffectEquipAction, HTMobEffectEquipAction::instance)
    }

    constructor(
        effect: Holder<MobEffect>,
        duration: Int,
        amplifier: Int = 0,
        ambient: Boolean = false,
        visible: Boolean = true,
    ) : this(HTMobEffectInstance(effect, duration, amplifier, ambient, visible))

    override fun type(): MapCodec<out HTEquipAction> = CODEC

    override fun onEquip(player: Player, stackTo: ItemStack) {
        player.addEffect(instance.toMutable())
    }

    override fun onUnequip(player: Player, stackFrom: ItemStack) {
        player.removeEffect(instance.effect)
    }
}
