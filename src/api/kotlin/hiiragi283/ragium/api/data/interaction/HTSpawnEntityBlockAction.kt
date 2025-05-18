package hiiragi283.ragium.api.data.interaction

import com.mojang.serialization.MapCodec
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.item.context.UseOnContext

class HTSpawnEntityBlockAction(val entityType: EntityType<*>) : HTBlockAction {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTSpawnEntityBlockAction> = BuiltInRegistries.ENTITY_TYPE
            .byNameCodec()
            .fieldOf("entity")
            .xmap(::HTSpawnEntityBlockAction, HTSpawnEntityBlockAction::entityType)
    }

    override val codec: MapCodec<out HTBlockAction> = CODEC

    override fun applyAction(context: UseOnContext) {
        val serverLevel: ServerLevel = context.level as? ServerLevel ?: return
        entityType.spawn(serverLevel, context.clickedPos, MobSpawnType.TRIGGERED)
    }
}
