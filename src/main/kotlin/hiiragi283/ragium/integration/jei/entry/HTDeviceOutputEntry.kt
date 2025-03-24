package hiiragi283.ragium.integration.jei.entry

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.integration.jei.addOutput
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

data class HTDeviceOutputEntry(val id: ResourceLocation, val device: Ingredient, private val output: Either<HTItemOutput, HTFluidOutput>) {
    companion object {
        @JvmField
        val CODEC: Codec<HTDeviceOutputEntry> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(HTDeviceOutputEntry::id),
                    Ingredient.CODEC_NONEMPTY.fieldOf("device").forGetter(HTDeviceOutputEntry::device),
                    Codec.either(HTItemOutput.CODEC, HTFluidOutput.CODEC).fieldOf("output").forGetter(HTDeviceOutputEntry::output),
                ).apply(instance, ::HTDeviceOutputEntry)
        }
    }

    constructor(id: ResourceLocation, device: Ingredient, output: HTItemOutput) : this(id, device, Either.left(output))

    constructor(id: ResourceLocation, device: Ingredient, output: HTFluidOutput) : this(id, device, Either.right(output))

    fun addOutput(builder: IRecipeSlotBuilder): IRecipeSlotBuilder {
        output.ifLeft(builder::addOutput).ifRight(builder::addOutput)
        return builder
    }
}
