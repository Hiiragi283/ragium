package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.EntityType

data class HTSpawnerMob(val holder: Holder<EntityType<*>>) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTSpawnerMob> = VanillaBiCodecs
            .holder(Registries.ENTITY_TYPE)
            .xmap(::HTSpawnerMob, HTSpawnerMob::holder)
    }

    @Suppress("DEPRECATION")
    constructor(entityType: EntityType<*>) : this(entityType.builtInRegistryHolder())

    val entityType: EntityType<*> get() = holder.value()
}
