package hiiragi283.ragium.api.material.prefix

import net.neoforged.bus.api.Event

class HTRegisterPrefixEvent(private val prefixMap: MutableMap<String, HTMaterialPrefix>) : Event() {
    fun register(prefix: HTPrefixLike) {
        val name: String = prefix.asPrefixName()
        check(prefixMap.put(name, prefix.asMaterialPrefix()) == null) {
            "Duplicated material prefix registration: $name, $prefix"
        }
    }
}
