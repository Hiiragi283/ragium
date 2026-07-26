package hiiragi283.ragium.api.item.component

import com.mojang.serialization.Codec
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.SimpleSupplierWithKey
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.Text
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType

@JvmInline
value class HTSpawnerMob private constructor(private val delegate: SimpleSupplierWithKey<EntityType<*>>) :
    SimpleSupplierWithKey<EntityType<*>>,
    HTHasText {
    companion object {
        @JvmField
        val CODEC: Codec<HTSpawnerMob> = HTCodecs.resourceKey(Registries.ENTITY_TYPE).xmap(::of, HTSpawnerMob::getKey)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTSpawnerMob> = HTStreamCodecs.resourceKey(Registries.ENTITY_TYPE).map(::of, HTSpawnerMob::getKey)

        @JvmStatic
        fun of(entityType: EntityType<*>): HTSpawnerMob = HTSpawnerMob(entityType.toLike())

        @JvmStatic
        fun of(key: ResourceKey<EntityType<*>>): HTSpawnerMob = HTSpawnerMob(HTDeferredHolder(key))

        @JvmStatic
        fun of(holder: Holder<EntityType<*>>): HTSpawnerMob = HTSpawnerMob(holder.toLike())
    }

    override fun getKey(): ResourceKey<EntityType<*>> = delegate.getKey()

    override fun get(): EntityType<*> = delegate.get()

    override fun getText(): Text = get().description
}
