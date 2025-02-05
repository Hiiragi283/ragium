package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.machine.HTMachineKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid

data class HTGeneratorFuelEntry(val machine: HTMachineKey, val fuelTag: TagKey<Fluid>, val amount: Int)
