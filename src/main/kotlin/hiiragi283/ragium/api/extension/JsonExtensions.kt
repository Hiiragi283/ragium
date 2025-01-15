package hiiragi283.ragium.api.extension

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.resources.ResourceLocation

//    JsonArray    //

fun buildJsonArray(builderAction: JsonArray.() -> Unit): JsonArray = JsonArray().apply(builderAction)

//    JsonObject    //

fun buildJsonObj(builderAction: JsonObject.() -> Unit): JsonObject = JsonObject().apply(builderAction)

fun JsonObject.addProperty(name: String, value: ResourceLocation) {
    addProperty(name, value.toString())
}
