package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTMaterialType
import org.slf4j.Logger

internal object HTMaterialRegistryImpl : HTMaterialRegistry {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmStatic
    private lateinit var typeMap: Map<HTMaterialKey, HTMaterialType>

    //    HTMaterialRegistry    //

    override val keys: Set<HTMaterialKey> get() = typeMap.keys

    override fun getType(key: HTMaterialKey): HTMaterialType = typeMap[key] ?: error("Unknown material key: $key")

    //    Init    //

    @JvmStatic
    fun initRegistry() {
        registerMaterials()
        // modifyProperties()
        LOGGER.info("Loaded material registry!")
    }

    @JvmStatic
    private fun registerMaterials() {
        LOGGER.info("Invoke material events...")
        val typeCache: MutableMap<HTMaterialKey, HTMaterialType> = mutableMapOf()

        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onMaterialRegister { key: HTMaterialKey, type: HTMaterialType ->
                check(typeCache.put(key, type) == null) { "Duplicated material registration: ${key.name}" }
            }
        }

        this.typeMap = typeCache
        LOGGER.info("Registered new materials!")
    }
}
