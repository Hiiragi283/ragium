package hiiragi283.ragium.api.item.component

import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.util.Either
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType

@ConsistentCopyVisibility
@JvmRecord
data class HTSpawnerMob private constructor(private val value: Holder<EntityType<*>>) :
    HTHolderLike<EntityType<*>, EntityType<*>>,
    HTHasText {
        companion object {
            @JvmField
            val CODEC: BiCodec<RegistryFriendlyByteBuf, HTSpawnerMob> = VanillaBiCodecs
                .holder(Registries.ENTITY_TYPE)
                .xmap(::of, HTSpawnerMob::value)

            @Suppress("DEPRECATION")
            @JvmStatic
            fun of(entityType: EntityType<*>): HTSpawnerMob = of(entityType.builtInRegistryHolder())

            @JvmStatic
            fun of(holder: Holder<EntityType<*>>): HTSpawnerMob = HTSpawnerMob(holder.delegate)
        }

        override fun unwrap(): Either<ResourceKey<EntityType<*>>, Holder<EntityType<*>>> = Either.Right(value.delegate)

        override fun get(): EntityType<*> = value.value()

        override fun getText(): Text = get().description
    }
