package hiiragi283.ragium.common.material

import hiiragi283.lib.material.HTMaterialAddon
import hiiragi283.lib.material.part.CommonParts
import hiiragi283.lib.material.property.HTDefaultPart
import hiiragi283.lib.material.property.HTMaterialPropertyKeys
import hiiragi283.lib.material.property.HTMaterialTextureSet
import hiiragi283.lib.material.property.setDefaultPart
import hiiragi283.lib.material.property.setName
import hiiragi283.lib.material.property.setTextureSet
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.item.RagiumItems

data object RagiumMaterialAddon : HTMaterialAddon {
    override val priority: Int = 1000

    override fun registerExistingItem(consumer: HTMaterialAddon.ItemConsumer) {
        consumer.accept(CommonParts.FUEL, RagiumMaterialKeys.COAL_COKE, RagiumItems.COAL_COKE)
    }

    override fun modifyMaterial(provider: HTMaterialAddon.MaterialProvider) {
        // Fuels
        provider.builder(RagiumMaterialKeys.COAL_COKE).apply {
            setDefaultPart(HTDefaultPart.BuiltIn(RagiumItems.COAL_COKE))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, RagiumAPI.MOD_ID)

            setName("Coal Coke", "石炭コークス")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 16)
        }
        // Minerals
        provider.builder(RagiumMaterialKeys.BORAX).apply {
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, RagiumAPI.MOD_ID)

            setName("Borax", "ホウ砂")
            setTextureSet("mineral")
        }
        provider.builder(RagiumMaterialKeys.NITER).apply {
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, RagiumAPI.MOD_ID)

            setName("Niter", "硝石")
            setTextureSet("mineral")
        }
        provider.builder(RagiumMaterialKeys.SALT).apply {
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, RagiumAPI.MOD_ID)

            setName("Salt", "食塩")
            setTextureSet("mineral")
        }
        provider.builder(RagiumMaterialKeys.SULFUR).apply {
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, RagiumAPI.MOD_ID)

            setName("Sulfur", "硫黄")
            setTextureSet("mineral")
        }
        provider.builder(RagiumMaterialKeys.RAGINITE).apply {
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, RagiumAPI.MOD_ID)

            setName("Raginite", "ラギナイト")
            setTextureSet("mineral")
        }
        // Gems
        // Metals
        // Alloys
        provider.builder(RagiumMaterialKeys.STEEL).apply {
            setDefaultPart(HTDefaultPart.Ingot)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, RagiumAPI.MOD_ID)

            setName("Steel", "鋼鉄")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
        // Others
    }

    override fun registerMaterialBlock(register: HTMaterialAddon.MaterialEntryRegister) {
        // Fuels
        register.register(RagiumMaterialKeys.COAL_COKE, CommonParts.BLOCK)
        // Minerals
        // Gems
        // Metals
        // Alloys
        register.register(RagiumMaterialKeys.STEEL, CommonParts.BLOCK)
        // Others
    }

    override fun registerMaterialItem(register: HTMaterialAddon.MaterialEntryRegister) {
        // Fuels
        register.registerAll(RagiumMaterialKeys.COAL_COKE, CommonParts.DUST, CommonParts.TINY)
        // Minerals
        register.register(RagiumMaterialKeys.BORAX, CommonParts.DUST)
        register.register(RagiumMaterialKeys.NITER, CommonParts.DUST)
        register.register(RagiumMaterialKeys.SALT, CommonParts.DUST)
        register.register(RagiumMaterialKeys.SULFUR, CommonParts.DUST)
        register.register(RagiumMaterialKeys.RAGINITE, CommonParts.DUST)
        // Gems
        // Metals
        // Alloys
        register.registerAll(RagiumMaterialKeys.STEEL, CommonParts.DUST, CommonParts.INGOT, CommonParts.NUGGET)
        // Others
    }
}
