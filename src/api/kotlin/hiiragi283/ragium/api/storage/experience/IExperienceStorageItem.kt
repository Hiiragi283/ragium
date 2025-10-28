package hiiragi283.ragium.api.storage.experience

import net.minecraft.world.item.ItemStack

interface IExperienceStorageItem : IExperienceStorage {
    fun getContainer(): ItemStack
}
