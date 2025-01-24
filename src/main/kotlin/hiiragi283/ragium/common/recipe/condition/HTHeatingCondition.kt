package hiiragi283.ragium.common.recipe.condition

import com.mojang.logging.LogUtils
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.level.Level
import org.slf4j.Logger

data class HTHeatingCondition(val minTier: HTMachineTier, val maxTier: HTMachineTier = HTMachineTier.ULTIMATE) : HTMachineRecipeCondition {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()

        @JvmField
        val CODEC: MapCodec<HTHeatingCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMachineTier.CODEC.fieldOf("min_tier").forGetter(HTHeatingCondition::minTier),
                    HTMachineTier.CODEC
                        .optionalFieldOf("max_tier", HTMachineTier.ULTIMATE)
                        .forGetter(HTHeatingCondition::maxTier),
                ).apply(instance, ::HTHeatingCondition)
        }
    }

    override val codec: MapCodec<out HTMachineRecipeCondition> = CODEC
    override val text: MutableComponent = Component.literal("Require heating from $minTier to $maxTier tier")

    override fun test(level: Level, pos: BlockPos): Boolean {
        val currentTier: HTMachineTier =
            level.getCapability(RagiumAPI.BlockCapabilities.HEATING_TIER, pos.below(), Direction.UP) ?: return false
        LOGGER.debug("Found Heating Tier: {} at {}", currentTier, pos)
        return currentTier >= minTier && currentTier <= maxTier
    }
}
