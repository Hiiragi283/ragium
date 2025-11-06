package hiiragi283.ragium.api.data.map.equip

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.data.map.HTEquipAction
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

data class HTMobEffectEquipAction(private val instance: MobEffectInstance) : HTEquipAction {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMobEffectEquipAction> = MobEffectInstance.CODEC
            .fieldOf("effect")
            .xmap(::HTMobEffectEquipAction, HTMobEffectEquipAction::instance)
    }

    constructor(
        effect: Holder<MobEffect>,
        duration: Int,
        amplifier: Int = 0,
        ambient: Boolean = false,
        visible: Boolean = true,
    ) : this(MobEffectInstance(effect, duration, amplifier, ambient, visible))

    override fun type(): MapCodec<out HTEquipAction> = CODEC

    override fun onEquip(player: Player, stackTo: ItemStack) {
        player.addEffect(instance)
    }

    override fun onUnequip(player: Player, stackFrom: ItemStack) {
        player.removeEffect(instance.effect)
    }
}
