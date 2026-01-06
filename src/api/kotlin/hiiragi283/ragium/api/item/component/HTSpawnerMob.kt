package hiiragi283.ragium.api.item.component

import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.text.HTHasText
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType

@JvmInline
value class HTSpawnerMob(private val value: Holder<EntityType<*>>) :
    HTKeyLike.HolderDelegate<EntityType<*>>,
    HTHolderLike<EntityType<*>, EntityType<*>>,
    HTHasText {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTSpawnerMob> = VanillaBiCodecs
            .holder(Registries.ENTITY_TYPE)
            .xmap(::HTSpawnerMob, HTSpawnerMob::value)
    }

    @Suppress("DEPRECATION")
    constructor(entityType: EntityType<*>) : this(entityType.builtInRegistryHolder())

    fun isOf(holderSet: HolderSet<EntityType<*>>): Boolean = value in holderSet

    override fun getHolder(): Holder<EntityType<*>> = value

    override fun get(): EntityType<*> = value.value()

    override fun getText(): Component = get().description
}
