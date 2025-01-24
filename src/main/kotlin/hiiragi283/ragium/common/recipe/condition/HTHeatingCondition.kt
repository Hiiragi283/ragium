package hiiragi283.ragium.common.recipe.condition

import com.mojang.logging.LogUtils
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getBlockData
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.level.Level
import org.slf4j.Logger

data class HTHeatingCondition(
    val minTier: HTMachineTier,
    val maxTier: HTMachineTier = HTMachineTier.ULTIMATE,
    val targetSide: Direction = Direction.UP,
) : HTMachineRecipeCondition {
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
                    Direction.CODEC
                        .optionalFieldOf("target_side", Direction.UP)
                        .forGetter(HTHeatingCondition::targetSide),
                ).apply(instance, ::HTHeatingCondition)
        }
    }

    override val codec: MapCodec<out HTMachineRecipeCondition> = CODEC
    override val text: MutableComponent =
        Component
            .translatable(RagiumTranslationKeys.HEATING_CONDITION, minTier.text, maxTier.text)
            .withStyle(ChatFormatting.GOLD)

    override fun test(level: Level, pos: BlockPos): Boolean {
        val currentTier: HTMachineTier =
            level
                .getBlockState(pos.relative(targetSide.opposite))
                .getBlockData(RagiumAPI.DataMapTypes.HEATING_TIER)
                ?: return false
        LOGGER.debug("Found Heating Tier: {} at {}", currentTier, pos)
        return currentTier >= minTier && currentTier <= maxTier
    }
}
