package hiiragi283.ragium.api.item.component

import com.mojang.serialization.Codec
import hiiragi283.core.api.registry.getKeyOrThrow
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.Text
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType

@JvmInline
value class HTSpawnerMob private constructor(private val value: Holder<EntityType<*>>) :
    SupplierWithId<EntityType<*>>,
    HTHasText {
    companion object {
        @JvmField
        val CODEC: Codec<HTSpawnerMob> = HTCodecs.holder(Registries.ENTITY_TYPE).xmap(::of, HTSpawnerMob::value)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSpawnerMob> =
            HTStreamCodecs.holder(Registries.ENTITY_TYPE).map(::of, HTSpawnerMob::value)

        @Suppress("DEPRECATION")
        @JvmStatic
        fun of(entityType: EntityType<*>): HTSpawnerMob = of(entityType.builtInRegistryHolder())

        @JvmStatic
        fun of(holder: Holder<EntityType<*>>): HTSpawnerMob = HTSpawnerMob(holder.delegate)
    }

    override fun get(): EntityType<*> = value.value()

    override fun getId(): ResourceLocation = value.getKeyOrThrow().location()

    override fun getText(): Text = get().description
}
