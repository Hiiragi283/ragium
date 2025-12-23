package hiiragi283.ragium.api.upgrade

object HTUpgradeKeys {
    //    Common    //

    @JvmField
    val BASE_MULTIPLIER: HTUpgradeKey = HTUpgradeKey.get("base_multiplier")

    @JvmField
    val IS_CREATIVE: HTUpgradeKey = HTUpgradeKey.get("is_creative")

    //    Generator    //

    @JvmField
    val ENERGY_GENERATION: HTUpgradeKey = HTUpgradeKey.get("energy_generation")

    //    Processor    //

    @JvmField
    val ENERGY_EFFICIENCY: HTUpgradeKey = HTUpgradeKey.get("energy_efficiency")

    @JvmField
    val SPEED: HTUpgradeKey = HTUpgradeKey.get("speed")

    //    Storage    //

    @JvmField
    val ENERGY_CAPACITY: HTUpgradeKey = HTUpgradeKey.get("energy_capacity")

    @JvmField
    val FLUID_CAPACITY: HTUpgradeKey = HTUpgradeKey.get("fluid_capacity")

    @JvmField
    val ITEM_CAPACITY: HTUpgradeKey = HTUpgradeKey.get("item_capacity")
}
