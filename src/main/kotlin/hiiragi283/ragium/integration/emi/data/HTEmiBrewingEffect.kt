package hiiragi283.ragium.integration.emi.data

import hiiragi283.ragium.api.data.map.HTBrewingEffect
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import net.minecraft.resources.ResourceLocation

@JvmRecord
data class HTEmiBrewingEffect(val input: HTItemHolderLike, val effect: HTBrewingEffect) : HTHolderLike {
    override fun getId(): ResourceLocation = input.getId().withPrefix("/brewing/effect/")
}
