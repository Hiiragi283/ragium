package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.registry.HTDeferredRegister

class HTDeferredMaterialPrefixRegister : HTDeferredRegister<HTMaterialPrefix>(RagiumAPI.MATERIAL_PREFIX_KEY, RagiumConst.COMMON) {
    fun register(name: String, commonTagPath: String = "c:${name}s", tagPath: String = "$commonTagPath/%s"): HTMaterialPrefix {
        val prefix = HTMaterialPrefix(commonTagPath, tagPath)
        register(name) { _ -> prefix }
        return prefix
    }
}
