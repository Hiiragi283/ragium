package hiiragi283.ragium.common.capability.energy

import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.energy.ComponentEnergyStorage

class HTItemEnergyStorage(stack: ItemStack, capacity: Int) :
    ComponentEnergyStorage(stack, RagiumComponentTypes.ENERGY_CONTENT.get(), capacity)
