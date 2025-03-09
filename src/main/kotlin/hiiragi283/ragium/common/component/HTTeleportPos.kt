package hiiragi283.ragium.common.component

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockPosText
import hiiragi283.ragium.api.extension.toCenterVec3
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.common.init.RagiumBlocks
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import net.minecraft.world.level.Level
import net.minecraft.world.level.portal.DimensionTransition
import java.util.function.Consumer

data class HTTeleportPos(val levelKey: ResourceKey<Level>, val pos: BlockPos) : TooltipProvider {
    companion object {
        @JvmField
        val CODEC: Codec<HTTeleportPos> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    ResourceKey.codec(Registries.DIMENSION).fieldOf("level").forGetter(HTTeleportPos::levelKey),
                    BlockPos.CODEC.fieldOf("pos").forGetter(HTTeleportPos::pos),
                ).apply(instance, ::HTTeleportPos)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTTeleportPos> = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION),
            HTTeleportPos::levelKey,
            BlockPos.STREAM_CODEC,
            HTTeleportPos::pos,
            ::HTTeleportPos,
        )
    }

    val targetLevel: ServerLevel? get() = RagiumAPI.getInstance().getCurrentServer()?.getLevel(levelKey)

    fun canTeleportTo(): DataResult<Unit> {
        val targetLevel: ServerLevel =
            this.targetLevel ?: return DataResult.error(RagiumTranslationKeys::TELEPORT_POS_MISSING_LEVEL)
        if (!targetLevel.isLoaded(pos)) {
            return DataResult.error(RagiumTranslationKeys::TELEPORT_POS_MISSING_POS)
        }
        if (!targetLevel.getBlockState(pos.below()).`is`(RagiumBlocks.TELEPORT_ANCHOR)) {
            return DataResult.error(RagiumTranslationKeys::TELEPORT_POS_MISSING_ANCHOR)
        }
        return DataResult.success(Unit)
    }

    fun toTransition(target: Entity): DimensionTransition? {
        val targetLevel: ServerLevel = targetLevel ?: return null
        return DimensionTransition(
            targetLevel,
            pos.toCenterVec3(),
            target.deltaMovement,
            target.yRot,
            target.xRot,
            DimensionTransition.DO_NOTHING,
        )
    }

    override fun addToTooltip(context: Item.TooltipContext, appender: Consumer<Component>, flag: TooltipFlag) {
        // Level
        appender.accept(
            Component
                .translatable(
                    RagiumTranslationKeys.TELEPORT_POS_0,
                    Component.literal(levelKey.location().toString()).withStyle(ChatFormatting.WHITE),
                ).withStyle(ChatFormatting.GRAY),
        )
        // Pos
        appender.accept(
            Component
                .translatable(
                    RagiumTranslationKeys.TELEPORT_POS_1,
                    blockPosText(pos).withStyle(ChatFormatting.WHITE),
                ).withStyle(ChatFormatting.GRAY),
        )
    }
}
