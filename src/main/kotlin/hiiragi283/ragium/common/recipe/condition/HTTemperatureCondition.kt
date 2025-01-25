package hiiragi283.ragium.common.recipe.condition

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getBlockData
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.api.util.HTTemperatureInfo
import hiiragi283.ragium.api.util.HTTemperatureType
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.level.Level

@ConsistentCopyVisibility
data class HTTemperatureCondition private constructor(
    val temperatureType: HTTemperatureType,
    val minTier: HTMachineTier,
    val maxTier: HTMachineTier,
    val targetSide: Direction,
) : HTMachineRecipeCondition {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTTemperatureCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTTemperatureType.CODEC
                        .fieldOf("temperature_type")
                        .forGetter(HTTemperatureCondition::temperatureType),
                    HTMachineTier.CODEC.fieldOf("min_tier").forGetter(HTTemperatureCondition::minTier),
                    HTMachineTier.CODEC
                        .optionalFieldOf("max_tier", HTMachineTier.ULTIMATE)
                        .forGetter(HTTemperatureCondition::maxTier),
                    Direction.CODEC
                        .optionalFieldOf("target_side", Direction.UP)
                        .forGetter(HTTemperatureCondition::targetSide),
                ).apply(instance, ::HTTemperatureCondition)
        }

        @JvmStatic
        fun heating(
            minTier: HTMachineTier,
            maxTier: HTMachineTier = HTMachineTier.ULTIMATE,
            targetSide: Direction = Direction.UP,
        ): HTTemperatureCondition = HTTemperatureCondition(HTTemperatureType.HEATING, minTier, maxTier, targetSide)

        @JvmStatic
        fun cooling(
            minTier: HTMachineTier,
            maxTier: HTMachineTier = HTMachineTier.ULTIMATE,
            targetSide: Direction = Direction.UP,
        ): HTTemperatureCondition = HTTemperatureCondition(HTTemperatureType.COOLING, minTier, maxTier, targetSide)
    }

    override val codec: MapCodec<out HTMachineRecipeCondition> = CODEC
    override val text: MutableComponent =
        Component
            .translatable(RagiumTranslationKeys.HEATING_CONDITION, minTier.text, maxTier.text)
            .withStyle(ChatFormatting.GOLD)

    override fun test(level: Level, pos: BlockPos): Boolean {
        val currentInfo: HTTemperatureInfo =
            level
                .getBlockState(pos.relative(targetSide.opposite))
                .getBlockData(RagiumAPI.DataMapTypes.TEMP_TIER)
                ?: return false
        val (type: HTTemperatureType, currentTier: HTMachineTier) = currentInfo
        if (type != temperatureType) return false
        return currentTier >= minTier && currentTier <= maxTier
    }
}
