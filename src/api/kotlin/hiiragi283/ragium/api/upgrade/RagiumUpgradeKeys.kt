package hiiragi283.ragium.api.upgrade

object RagiumUpgradeKeys {
    // Common
    @JvmField
    val BASE_MULTIPLIER: HTUpgradeKey = HTUpgradeKey.get("base_multiplier")

    @JvmField
    val IS_CREATIVE: HTUpgradeKey = HTUpgradeKey.get("is_creative")

    // Energy
    @JvmField
    val ENERGY_CAPACITY: HTUpgradeKey = HTUpgradeKey.get("energy_capacity")

    @JvmField
    val ENERGY_EFFICIENCY: HTUpgradeKey = HTUpgradeKey.get("energy_efficiency")

    @JvmField
    val ENERGY_GENERATION: HTUpgradeKey = HTUpgradeKey.get("energy_generation")

    // Fluid
    @JvmField
    val FLUID_CAPACITY: HTUpgradeKey = HTUpgradeKey.get("fluid_capacity")

    // Processor
    @JvmField
    val SPEED: HTUpgradeKey = HTUpgradeKey.get("speed")

    @JvmField
    val USE_LUBRICANT: HTUpgradeKey = HTUpgradeKey.get("use_lubricant")

    @JvmField
    val DISABLE_EXTRA: HTUpgradeKey = HTUpgradeKey.get("disable_extra")

    // Device
    @JvmField
    val EXP_COLLECTING: HTUpgradeKey = HTUpgradeKey.get("exp_collecting")

    @JvmField
    val FISHING: HTUpgradeKey = HTUpgradeKey.get("fishing")

    @JvmField
    val MOB_CAPTURE: HTUpgradeKey = HTUpgradeKey.get("mob_capture")
}
