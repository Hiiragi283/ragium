package hiiragi283.ragium.common.component

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.extension.putId
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import net.minecraft.world.level.Level
import net.minecraft.world.level.SpawnData
import net.minecraft.world.level.block.Blocks
import java.util.*
import java.util.function.Consumer

data class HTSpawnerData(val entityType: EntityType<*>) : TooltipProvider {
    companion object {
        @JvmField
        val CODEC: Codec<HTSpawnerData> =
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().xmap(::HTSpawnerData, HTSpawnerData::entityType)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSpawnerData> =
            ByteBufCodecs.registry(Registries.ENTITY_TYPE).map(::HTSpawnerData, HTSpawnerData::entityType)
    }

    fun toSpawnerNbt(): CompoundTag {
        val id: ResourceLocation = EntityType.getKey(entityType)
        val root = CompoundTag()
        SpawnData.CODEC
            .encodeStart(
                NbtOps.INSTANCE,
                SpawnData(
                    buildNbt {
                        putId("id", id)
                    },
                    Optional.empty(),
                    Optional.empty(),
                ),
            ).ifSuccess { root.put("SpawnData", it) }
        return root
    }

    fun placeSpawner(level: Level, pos: BlockPos) {
        val entityData: CompoundTag = this.toSpawnerNbt()
        level.setBlockAndUpdate(pos, Blocks.SPAWNER.defaultBlockState())
        level.getBlockEntity(pos)?.loadCustomOnly(entityData, level.registryAccess())
    }

    override fun addToTooltip(context: Item.TooltipContext, appender: Consumer<Component>, flag: TooltipFlag) {
        appender.accept(
            Component
                .translatable(
                    RagiumTranslationKeys.ENTITY_DATA,
                    entityType.description.copy().withStyle(ChatFormatting.WHITE),
                ).withStyle(ChatFormatting.GRAY),
        )
    }
}
