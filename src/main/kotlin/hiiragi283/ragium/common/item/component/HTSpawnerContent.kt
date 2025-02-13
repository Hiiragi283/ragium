package hiiragi283.ragium.common.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.minecraft.ChatFormatting
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import net.neoforged.neoforge.common.Tags
import java.util.function.Consumer

data class HTSpawnerContent(val entityType: EntityType<*>) : TooltipProvider {
    companion object {
        @JvmField
        val CODEC: Codec<HTSpawnerContent> = BuiltInRegistries.ENTITY_TYPE
            .byNameCodec()
            .xmap(::HTSpawnerContent, HTSpawnerContent::entityType)
            .validate { content: HTSpawnerContent ->
                if (content.entityType.`is`(Tags.EntityTypes.BOSSES)) {
                    return@validate DataResult.error { "Entity type: ${content.entityType} is in Boss Tag!" }
                }
                DataResult.success(content)
            }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSpawnerContent> =
            ByteBufCodecs.registry(Registries.ENTITY_TYPE).map(::HTSpawnerContent, HTSpawnerContent::entityType)
    }

    override fun addToTooltip(context: Item.TooltipContext, tooltipAdder: Consumer<Component>, tooltipFlag: TooltipFlag) {
        tooltipAdder.accept(
            entityType.description
                .copy()
                .withStyle(ChatFormatting.BLUE)
                .withStyle(ChatFormatting.ITALIC),
        )
    }
}
