package hiiragi283.ragium.api.material

import net.neoforged.bus.api.Event

/**
 * @see net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
 */
class HTMaterialDefinitionEvent(val factory: (HTMaterialKey) -> HTMaterialDefinition.Builder) : Event() {
    inline fun modify(key: HTMaterialKey, builderAction: HTMaterialDefinition.Builder.() -> Unit) {
        factory(key).apply(builderAction)
    }

    inline fun modify(keys: Iterable<HTMaterialKey>, builderAction: HTMaterialDefinition.Builder.(HTMaterialKey) -> Unit) {
        for (key: HTMaterialKey in keys) {
            factory(key).builderAction(key)
        }
    }
}
