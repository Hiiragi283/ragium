package hiiragi283.ragium.api.machine

import com.mojang.serialization.DataResult

interface HTMachineProvider {
    val machineKey: HTMachineKey

    fun getEntry(): HTMachineRegistry.Entry = machineKey.getEntry()

    fun getEntryOrNull(): HTMachineRegistry.Entry? = machineKey.getEntryOrNull()

    fun getEntryData(): DataResult<HTMachineRegistry.Entry> = machineKey.getEntryData()
}
