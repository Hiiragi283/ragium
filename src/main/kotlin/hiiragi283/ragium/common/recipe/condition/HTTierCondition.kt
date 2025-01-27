package hiiragi283.ragium.common.recipe.condition

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.level.Level

class HTTierCondition(val minTier: HTMachineTier) : HTMachineRecipeCondition {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTTierCondition> = HTMachineTier.FIELD_CODEC
            .xmap(::HTTierCondition, HTTierCondition::minTier)
    }

    override val codec: MapCodec<out HTMachineRecipeCondition> = CODEC
    override val text: MutableComponent =
        Component.translatable(RagiumTranslationKeys.TIER_CONDITION, minTier.text).withStyle(ChatFormatting.WHITE)

    override fun test(level: Level, pos: BlockPos): Boolean {
        val machineTier: HTMachineTier = level.getMachineEntity(pos)?.machineTier ?: return false
        return machineTier >= minTier
    }
}
