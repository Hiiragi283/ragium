package hiiragi283.ragium.api.storage.energy

enum class HTEnergyFilter(val canReceive: Boolean, val canExtract: Boolean) {
    ALWAYS(true, true),
    RECEIVE_ONLY(true, false),
    EXTRACT_ONLY(false, true),
    VIEW_ONLY(false, false),
}
