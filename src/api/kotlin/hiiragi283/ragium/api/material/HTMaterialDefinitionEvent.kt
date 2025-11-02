package hiiragi283.ragium.api.material

import net.neoforged.bus.api.Event

/**
 * @see net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
 */
class HTMaterialDefinitionEvent(val factory: (HTMaterialKey) -> HTMaterialDefinition.Builder) : Event() {
    inline fun modify(key: HTMaterialKey, builderAction: HTMaterialDefinition.Builder.() -> Unit) {
        factory(key).apply(builderAction)
    }

    inline fun <T : HTMaterialLike> modify(keys: Iterable<T>, builderAction: HTMaterialDefinition.Builder.(T) -> Unit) {
        for (material: T in keys) {
            val key: HTMaterialKey = material.asMaterialKey()
            factory(key).builderAction(material)
        }
    }
}
