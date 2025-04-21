package hiiragi283.ragium.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPropertyKeys
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.property.HTEmptyPropertyMap
import hiiragi283.ragium.api.property.HTMutablePropertyMap
import hiiragi283.ragium.api.property.HTPropertyKey
import hiiragi283.ragium.api.property.HTPropertyMap
import org.slf4j.Logger

internal object HTMaterialRegistryImpl : HTMaterialRegistry {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmStatic
    private val propertyMap: MutableMap<HTMaterialKey, HTMutablePropertyMap> = mutableMapOf()

    //    HTMaterialRegistry    //

    override lateinit var keys: Set<HTMaterialKey>

    override fun getPropertyMap(key: HTMaterialKey): HTPropertyMap = propertyMap[key] ?: HTEmptyPropertyMap

    //    Init    //

    @JvmStatic
    fun initRegistry() {
        registerMaterials()
        setupProperties()
        LOGGER.info("Loaded material registry!")
    }

    @JvmStatic
    private fun registerMaterials() {
        LOGGER.info("Invoke material events...")
        val keyCache: MutableSet<HTMaterialKey> = mutableSetOf()

        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onMaterialRegister { key: HTMaterialKey, type: HTMaterialType ->
                check(keyCache.add(key)) { "Duplicated material registration: ${key.name}" }
                getOrCreatePropertyMap(key)[HTMaterialPropertyKeys.MATERIAL_TYPE] = type
            }
        }

        this.keys = keyCache
        LOGGER.info("Registered new materials!")
    }

    @JvmStatic
    private fun setupProperties() {
        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onMaterialSetup(::getOrCreatePropertyMap)
        }
        LOGGER.info("Registered material properties!")
    }

    private fun getOrCreatePropertyMap(key: HTMaterialKey): HTMutablePropertyMap = propertyMap.computeIfAbsent(key) { PropertyMap() }

    private class PropertyMap : HTMutablePropertyMap {
        private val map: MutableMap<HTPropertyKey<*>, Any> = mutableMapOf()

        override fun <T : Any> set(key: HTPropertyKey<T>, value: T): T? = key.castAs(map.put(key, value))

        override fun <T : Any> get(key: HTPropertyKey<T>): T? = key.castAs(map[key])
    }
}
