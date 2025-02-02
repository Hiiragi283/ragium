package hiiragi283.ragium.common.recipe.condition

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.getName
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

data class HTSourceCondition(val source: TagKey<Block>, val offset: Direction) : HTMachineRecipeCondition {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTSourceCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    TagKey
                        .codec(Registries.BLOCK)
                        .fieldOf("source")
                        .forGetter(HTSourceCondition::source),
                    Direction.CODEC
                        .optionalFieldOf("target_side", Direction.UP)
                        .forGetter(HTSourceCondition::offset),
                ).apply(instance, ::HTSourceCondition)
        }
    }

    override val codec: MapCodec<out HTMachineRecipeCondition> = CODEC
    override val text: Component =
        Component
            .translatable(RagiumTranslationKeys.SOURCE_CONDITION, source.getName(), offset.name)
            .withStyle(ChatFormatting.GOLD)

    override fun test(level: Level, pos: BlockPos): Boolean = level.getBlockState(pos.relative(offset)).`is`(source)
}
