package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.text.HTHasText
import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

class HTDeferredEntityType<ENTITY : Entity> :
    HTDeferredHolder<EntityType<*>, EntityType<ENTITY>>,
    HTHasTranslationKey,
    HTHasText {
    constructor(key: ResourceKey<EntityType<*>>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.ENTITY_TYPE, id)

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Component = get().description
}
