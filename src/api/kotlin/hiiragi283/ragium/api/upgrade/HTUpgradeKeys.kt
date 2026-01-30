package hiiragi283.ragium.api.upgrade

import hiiragi283.ragium.api.RagiumAPI

object HTUpgradeKeys {
    //    Common    //

    @JvmField
    val BASE_MULTIPLIER: HTUpgradeKey = HTUpgradeKey.get(RagiumAPI.id("base_multiplier"))

    @JvmField
    val IS_CREATIVE: HTUpgradeKey = HTUpgradeKey.get(RagiumAPI.id("is_creative"))

    //    Generator    //

    @JvmField
    val ENERGY_GENERATION: HTUpgradeKey = HTUpgradeKey.get(RagiumAPI.id("energy_generation"))

    //    Processor    //

    @JvmField
    val ENERGY_EFFICIENCY: HTUpgradeKey = HTUpgradeKey.get(RagiumAPI.id("energy_efficiency"))

    @JvmField
    val SPEED: HTUpgradeKey = HTUpgradeKey.get(RagiumAPI.id("speed"))

    //    Storage    //

    @JvmField
    val ENERGY_CAPACITY: HTUpgradeKey = HTUpgradeKey.get(RagiumAPI.id("energy_capacity"))

    @JvmField
    val FLUID_CAPACITY: HTUpgradeKey = HTUpgradeKey.get(RagiumAPI.id("fluid_capacity"))

    @JvmField
    val ITEM_CAPACITY: HTUpgradeKey = HTUpgradeKey.get(RagiumAPI.id("item_capacity"))
}
